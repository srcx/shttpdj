/*
 * $Id: FileModule.java,v 1.6 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj.impl;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import cz.srnet.shttpdj.GenericException;
import cz.srnet.shttpdj.IConfigModule;
import cz.srnet.shttpdj.IIOModule;
import cz.srnet.shttpdj.IRegistry;
import cz.srnet.shttpdj.IRequest;
import cz.srnet.shttpdj.IResponse;
import cz.srnet.shttpdj.ITypeModule;

/**
 * Implementation of {@link cz.srnet.shttpdj.IIOModule} which returns
 * files from filesystem.
 *
 * <p>
 * Permits only <code>HEAD</code> and <code>GET</code> methods.<br>
 * Directory requests are transformed to file {@link cz.srnet.shttpdj.IConfigModule#getDirectoryIndexName()}.
 * If directory name does not end with <code>'/'</code>, status
 * <code>302 Moved Permanently</code> is returned (that's Apachism).  
 *  
 * <p>
 * Configuration consists of one attribute <code>directory</code>
 * with location of base directory.
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.6 $ $Date: 2003/04/30 23:46:12 $
 */
public class FileModule extends AbstractModule implements IIOModule {

    private static final Logger log = Logger.getLogger(FileModule.class);

    /**
     * Constructor. 
     */
    public FileModule() {
        super();
    }
    
    /** base directory */
    private File directory;

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.impl.AbstractModule#doInit()
     */
    protected void doInit() throws GenericException {
        Element config = getConfigElem();
        directory = new File(XMLHelper.getAttributeValue(config, "directory", null, true));
        if (!directory.exists()) {
            throw new GenericException("Directory " + directory + " does not exist");
        }
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.impl.AbstractModule#doShutdown()
     */
    protected void doShutdown() {
        // void
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IIOModule#doRequest(cz.srnet.shttpdj.IRequest, cz.srnet.shttpdj.IResponse)
     */
    public IResponse doRequest(IRequest request, IResponse response) throws IOException {
        String method = request.getMethod();
        if (!"GET".equals(method) && !"HEAD".equals(method)) {
            throw new HttpException(HttpHelper.STATUS_METHOD_NOT_ALLOWED, "Method " + method + " is not allowed.");
        }
        IConfigModule configModule = getConfigModule(getRegistry());
        ITypeModule typeModule = getTypeModule(getRegistry());
        String requestPath = request.getRequestURI().getPath();
        requestPath = requestPath.replace('/', File.separatorChar);
        // security check
        checkSecurePath(requestPath);
        File f = new File(directory, requestPath);
        if (f.isDirectory()) {
            if (!requestPath.endsWith("/")) {
                requestPath += "/";
                response = HttpResponseFactory.getInstance().createResponseFromStatus(getRegistry(), response.getOutputStream(), request, HttpHelper.STATUS_MOVED_PERMANENTLY, HttpHelper.getStatusString(HttpHelper.STATUS_MOVED_PERMANENTLY), "Requested file was permanently moved to " + requestPath);
                response.setHeader(HttpHelper.HEADER_LOCATION, requestPath);
                return response;
            }
            f = new File(f, configModule.getDirectoryIndexName());
        }
        log.info("Requested path " + requestPath + " is treated as file " + f);
        if (!f.isFile()) {
            throw new HttpException(HttpHelper.STATUS_NOT_FOUND, "Requested file " + requestPath + " not found"); 
        }
        response.setContentType(typeModule.getContentType(f));
        response.addData(new ResponseFileData(f));
        return response;
    }

    /**
     * Checks whether path is secure and does not lead outside of
     * base directory.
     * 
     * @param requestPath request path
     * @throws HttpException if path contains some '..'
     */
    private void checkSecurePath(String requestPath) throws HttpException {
        int i;
        if (((i = requestPath.indexOf("../")) > -1) 
        || ((File.separatorChar != '/') && ((i = requestPath.indexOf(".." + File.separator)) > -1))) {
            throw new HttpException(HttpHelper.STATUS_BAD_REQUEST, "Insecure path"); 
        }
    }

    /**
     * Returns {@link IConfigModule} from given registry.
     * 
     * @param registry modules registry
     */
    private IConfigModule getConfigModule(IRegistry registry) {
        IConfigModule module = (IConfigModule) registry.getModule(IConfigModule.MODULE_ID);
        return module;
    }

    /**
     * Returns {@link ITypeModule} from given registry.
     * 
     * @param registry modules registry
     */
    private ITypeModule getTypeModule(IRegistry registry) {
        ITypeModule module = (ITypeModule) registry.getModule(ITypeModule.MODULE_ID);
        return module;
    }

}

/*
 * $Log: FileModule.java,v $
 * Revision 1.6  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.5  2003/04/30 22:54:14  stepan
 * added javadoc
 *
 * Revision 1.4  2003/04/30 21:06:07  stepan
 * allow only GET and HEAD methods
 *
 * Revision 1.3  2003/04/30 20:55:46  stepan
 * check for directories without trailing slash
 *
 * Revision 1.2  2003/04/30 20:25:02  stepan
 * determine content type of requested file
 *
 * Revision 1.1  2003/04/30 17:57:18  stepan
 * created
 *
 * Revision 1.1  2003/04/30 17:04:22  stepan
 * created
 *
 */