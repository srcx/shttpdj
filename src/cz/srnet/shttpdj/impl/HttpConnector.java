/*
 * $Id: HttpConnector.java,v 1.8 2004/05/30 13:22:10 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj.impl;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import cz.srnet.shttpdj.GenericException;
import cz.srnet.shttpdj.IConnector;
import cz.srnet.shttpdj.IIOModule;
import cz.srnet.shttpdj.IResponse;

/**
 * Implementation of {@link cz.srnet.shttpdj.IConnector}.
 * 
 * <p>
 * Operates one (non-daemon, must be shutdowned) thread which
 * sequentially process requests (no timeouts, easy DoS).
 * 
 * <p>
 * Configuration:<br>
 * <pre>
 * port (default 0)
 * - port to bind to (0 means any free port)
 * backlog (default 50)
 * - size of waiting connections queue
 * addr (default nothing)
 * - address to bind to (will be bind to all local addresses if not specified)
 * attached-module (required)
 * - id of attached {@link cz.srnet.shttpdj.IIOModule}
 * </pre> 
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.8 $ $Date: 2004/05/30 13:22:10 $
 */
public class HttpConnector extends AbstractModule implements IConnector {
    
    private static final Logger log = Logger.getLogger(HttpConnector.class);

    /**
     * Constructor. 
     */
    public HttpConnector() {
        super();
    }
    
    /** port */
    private int port;
    /** backlog */
    private int backlog;
    /** local address */
    private InetAddress addr;
    /** attached I/O module */
    private IIOModule attachedModule;

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.impl.AbstractModule#doInit()
     */
    protected void doInit() throws GenericException {
        Element elem = getConfigElem();
        port = Integer.parseInt(XMLHelper.getAttributeValue(elem, "port", "0", false));
        backlog = Integer.parseInt(XMLHelper.getAttributeValue(elem, "backlog", "50", false));
        try {
            String addrStr = XMLHelper.getAttributeValue(elem, "addr", null, false);
            addr = (addrStr != null) ? InetAddress.getByName(addrStr) : null;
        } catch (UnknownHostException e) {
            throw new GenericException(e);
        }
        attachedModule = (IIOModule) getRegistry().getModule(XMLHelper.getAttributeValue(elem, "attached-module", null, true));
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.impl.AbstractModule#doShutdown()
     */
    protected void doShutdown() {
        if (serverThread != null) {
            serverThread.shutdown();
            serverThread = null;
            log.info(getId() + ": stopped listening");
        }
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IStartableModule#start()
     */
    public void start() throws GenericException {
        checkOperational();
        try {
            ServerSocket sock = new ServerSocket(port, backlog, addr);
            sock.setSoTimeout(0);
            log.info(getId() + ": start listening on " + sock.getInetAddress() + ":" + sock.getLocalPort());
            serverThread = new ServerThread(sock);
            serverThread.start();
        } catch (IOException e) {
            throw new GenericException(e);
        }
    }
    
    /** processing thread */
    private ServerThread serverThread = null;
    
    /**
     * Processing thread.
     * 
     * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
     * @version $Revision: 1.8 $ $Date: 2004/05/30 13:22:10 $
     */
    private class ServerThread extends Thread {
        
        /** server socket */
        private ServerSocket serverSock;
        
        /**
         * Constructor.
         * 
         * @param serverSock server socket
         */
        public ServerThread(ServerSocket serverSock) {
            super("connector " + getId());
            this.serverSock = serverSock;
        }
        
        /** shutdown lock - all shutdown requestors must wait on it */ 
        private Object shutdownLock = new Object();
        /** was shutdown requested? */
        private volatile boolean doShutdown = false;
        /** was shutdown completed? */
        private volatile boolean shutdowned = false;
        
        /* (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        public void run() {
            while (!doShutdown) {
                Socket sock = null;
                try {
                    sock = serverSock.accept();
                    log.info(getId() + ": accepted connection from " + sock.getInetAddress() + ":" + sock.getPort());
                    IResponse response = null;
                    HttpRequest req = null;
                    try {
                        req = new HttpRequest(sock.getInputStream());
                        response = HttpResponseFactory.getInstance().createResponse(getRegistry(), sock.getOutputStream(), req);
                        response = attachedModule.doRequest(req, response);
                    } catch (HttpException e1) {
                        log.error(e1.getMessage(), e1);
                        response = HttpResponseFactory.getInstance().createResponseFromException(getRegistry(), sock.getOutputStream(), req, e1);
                    } catch (IOException e1) {
                        log.error(e1.getMessage(), e1);
                        response = HttpResponseFactory.getInstance().createResponseFromStatus(getRegistry(), sock.getOutputStream(), req, HttpHelper.STATUS_INTERNAL_SERVER_ERROR, HttpHelper.getStatusString(HttpHelper.STATUS_INTERNAL_SERVER_ERROR), null);
                    }
                    if (response == null) {
                        response = HttpResponseFactory.getInstance().createResponseFromStatus(getRegistry(), sock.getOutputStream(), req, HttpHelper.STATUS_FORBIDDEN, HttpHelper.getStatusString(HttpHelper.STATUS_FORBIDDEN), null);
                    }
                    response.send();
                } catch (InterruptedIOException e) {
                    // ignored
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                } finally {
                    if (sock != null) {
                        try {
                            log.info(getId() + ": closing connection with " + sock.getInetAddress() + ":" + sock.getPort());
                            sock.close();
                        } catch (IOException e1) {
                            log.error(e1.getMessage(), e1);
                        }
                    }
                }
            }
            shutdowned = true;
            synchronized (shutdownLock) {
                shutdownLock.notifyAll();
            }
        }
        
        /**
         * Request thread shutdown. Blocks until thread shutdowns.
         */
        public void shutdown() {
            doShutdown = true;
            synchronized (shutdownLock) {
                try {
                    interrupt();
                    // XXX SocketClosedException will be logged in run()
                    serverSock.close();
                    while (!shutdowned) {
                        shutdownLock.wait();
                    }
                } catch (InterruptedException e) {
                    // ignored
                } catch (IOException e) {
                    // ignored
                }
            }
        }

    }

}

/*
 * $Log: HttpConnector.java,v $
 * Revision 1.8  2004/05/30 13:22:10  stepan
 * new and shiny shutdown mechanism
 *
 * Revision 1.7  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.6  2003/04/30 22:55:20  stepan
 * added javadoc
 * fixed parsing of addr attribute which did not permit omitting it
 * thread renamed to "connector..."
 *
 * Revision 1.5  2003/04/30 20:54:23  stepan
 * one createResponseFromException() renamed to createResponseFromStatus()
 *
 * Revision 1.4  2003/04/30 17:57:39  stepan
 * added server thread name
 *
 * Revision 1.3  2003/04/30 17:03:54  stepan
 * generate 404 response if no response is sent from attached module
 *
 * Revision 1.2  2003/04/28 20:18:33  stepan
 * too many changes
 *
 * Revision 1.1  2003/04/21 17:36:23  stepan
 * too many changes done
 *
 * Revision 1.1  2003/04/21 00:34:37  stepan
 * lot of code and libs added
 *
 */