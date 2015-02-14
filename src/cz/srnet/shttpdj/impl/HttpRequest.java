/*
 * $Id: HttpRequest.java,v 1.5 2004/05/30 13:03:25 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import cz.srnet.shttpdj.IRequest;
import cz.srnet.shttpdj.URI;

/**
 * HTTP implementation of {@link cz.srnet.shttpdj.IRequest}.
 * 
 * <p>
 * Accepts <code>GET</code>, <code>HEAD</code> and <code>PUT</code>
 * methods and protocol <code>HTTP</code> in versions <code>0.9</code>,
 * <code>1.0</code> and <code>1.1</code>. 
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.5 $ $Date: 2004/05/30 13:03:25 $
 */
public class HttpRequest implements IRequest {

    /**
     * Constructor.
     * 
     * @param is input stream
     * @throws IOException 
     */
    public HttpRequest(InputStream is) throws IOException {
        super();
        this.is = is;
        loadHeaders();
    }
    
    /** input stream with request data */
    private InputStream is;
    /** request method */
    private String method;
    /** request URI */
    private URI requestURI;
    /** request protocol */
    private String protocol;
    /** request protocol version */
    private String protocolVersion;
    /** mapping between lowercased header name and its value */
    private Map headers = new HashMap();
    
    /**
     * Load headers from input stream which then points to first
     * byte of data or end of input.
     * 
     * <b>This method is buggy: it may (and will) eat more data than just headers.</b>
     * 
     * @throws IOException
     */
    private void loadHeaders() throws IOException {
    	// XXX buggy - may buffer more data than just headers
        BufferedReader r = new BufferedReader(new InputStreamReader(is, "us-ascii"));
        String line = r.readLine();
        if (line == null) {
            throw new HttpException(HttpHelper.STATUS_BAD_REQUEST, "EOF reached.");
        }
        StringTokenizer stok = new StringTokenizer(line);
        try {
            method = stok.nextToken(" ");
            if (!"GET".equals(method) && !"HEAD".equals(method) && !"PUT".equals(method)) {
                throw new HttpException(HttpHelper.STATUS_METHOD_NOT_ALLOWED, "Method " + method + " is not allowed.");
            }
            requestURI =  new URI(stok.nextToken(" "));
            if (stok.hasMoreTokens()) {
                protocol = stok.nextToken("/ ");
                if (!"HTTP".equals(protocol)) {
                    throw new HttpException(HttpHelper.STATUS_BAD_REQUEST, "Protocol " + protocol + " is not HTTP.");
                }
                protocolVersion = stok.nextToken("/ ");
                if (!"1.1".equals(protocolVersion) && !"1.0".equals(protocolVersion) && !"0.9".equals(protocolVersion)) {
                    throw new HttpException(HttpHelper.STATUS_HTTP_VERSION_NOT_SUPPORTED, "HTTP version " + protocolVersion + " is not supported. Supported versions are 0.9, 1.0 and 1.1");
                }
            } else {
                throw new HttpException(HttpHelper.STATUS_HTTP_VERSION_NOT_SUPPORTED, "HTTP version less than 0.9 is not supported.");
            }
        } catch (NoSuchElementException e) {
            throw new HttpException(HttpHelper.STATUS_BAD_REQUEST, "EOF reached.");
        }
        while ((line = r.readLine()) != null) {
            if (line.length() == 0) {
                break;
            }
            int i = line.indexOf(':');
            if (i < 1) {
                throw new HttpException(HttpHelper.STATUS_BAD_REQUEST, "Malformed header line " + line + ".");
            }
            String name = line.substring(0, i).toLowerCase().trim();
            String value = line.substring(i).trim();
            headers.put(name, value);
        }
    }
    
    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IRequest#getMethod()
     */
    public String getMethod() {
        return method;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IRequest#getRequestURL()
     */
    public URI getRequestURI() {
        return requestURI;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IRequest#getProtocol()
     */
    public String getProtocol() {
        return protocol;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IRequest#getProtocolVersion()
     */
    public String getProtocolVersion() {
        return protocolVersion;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IRequest#getHeaders()
     */
    public Map getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IRequest#getHeader(java.lang.String)
     */
    public String getHeader(String header) {
        return (String) headers.get(header.toLowerCase());
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IRequest#getData()
     */
    public InputStream getData() {
        return is;
    }

}

/*
 * $Log: HttpRequest.java,v $
 * Revision 1.5  2004/05/30 13:03:25  stepan
 * mark loadHeaders() as bugged (will read more data than expected)
 *
 * Revision 1.4  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.3  2003/04/30 22:55:56  stepan
 * added javadoc
 *
 * Revision 1.2  2003/04/30 17:04:09  stepan
 * switched from java.net.URL to our own URI
 *
 * Revision 1.1  2003/04/21 17:36:23  stepan
 * too many changes done
 *
 */