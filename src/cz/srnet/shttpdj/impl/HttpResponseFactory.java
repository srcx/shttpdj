/*
 * $Id: HttpResponseFactory.java,v 1.5 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj.impl;

import java.io.OutputStream;
import java.util.Date;

import cz.srnet.shttpdj.IConfigModule;
import cz.srnet.shttpdj.IRegistry;
import cz.srnet.shttpdj.IRequest;

/**
 * Factory for creating {@link cz.srnet.shttpdj.impl.HttpResponse}s
 * with some sane headers.
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.5 $ $Date: 2003/04/30 23:46:12 $
 */
public class HttpResponseFactory {

    /**
     * Singleton. 
     */
    private HttpResponseFactory() {
        super();
    }
    
    /** instance */
    private static HttpResponseFactory instance = new HttpResponseFactory();
    
    /**
     * Returns one, and only, instance.
     */
    public static HttpResponseFactory getInstance() {
        return instance;
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
     * Creates response. It has protocol <code>HTTP/1.0</code>,
     * status <code>200 OK</code>, server banner, date, content type
     * <code>text/html</code> and content encoding {@link IConfigModule#getOutputEncoding()}. 
     * 
     * @param registry modules registry
     * @param os output stream
     * @param req request
     * @return created response
     */
    public HttpResponse createResponse(IRegistry registry, OutputStream os, IRequest req) {
        IConfigModule configModule = getConfigModule(registry);
        boolean sendData = (req == null) ? true : !"HEAD".equals(req.getMethod()); 
        HttpResponse response = new HttpResponse(os, sendData);
        response.setProtocol("HTTP");
        response.setProtocolVersion("1.0");
        response.setStatusCode(HttpHelper.STATUS_OK);
        response.setStatusInfo(HttpHelper.getStatusString(HttpHelper.STATUS_OK));
        response.setHeader("server", configModule.getServerBanner());
        response.setHeader("connection", "close");
        response.setHeader("date", HttpHelper.formatHttpDate(new Date()));
        response.setContentType("text/html");
        response.setContentEncoding(configModule.getOutputEncoding());
        return response;
    }
    
    /**
     * Same as {@link #createResponse(IRegistry, OutputStream, IRequest)}, but
     * with status set from given exception and with error page data.
     *  
     * @param registry modules registry
     * @param os output stream
     * @param req request
     * @param e exception
     * @return created response
     */
    public HttpResponse createResponseFromException(IRegistry registry, OutputStream os, IRequest req, HttpException e) {
        return createResponseFromStatus(registry, os, req, e.getStatusCode(), e.getStatusString(), e.getDetailedStatus());
    }

    /**
     * Same as {@link #createResponse(IRegistry, OutputStream, IRequest)}, but
     * with given status and with error page data.
     *  
     * @param registry modules registry
     * @param os output stream
     * @param req request
     * @param statusCode status code
     * @param statusString status string (aka reason phrase)
     * @param detailedStatus detailed status description
     * @return created response
     */
    public HttpResponse createResponseFromStatus(IRegistry registry, OutputStream os, IRequest req, int statusCode, String statusString, String detailedStatus) {
        IConfigModule configModule = getConfigModule(registry);
        HttpResponse response = createResponse(registry, os, req);
        response.setStatusCode(statusCode);
        response.setStatusInfo(statusString);
        StringBuffer page = new StringBuffer();
        page.append("<html><head><title>");
        page.append(statusCode);
        page.append(' ');
        page.append(statusString);
        page.append("</title></head>\n");
        page.append("<body><h1>");
        page.append(statusCode);
        page.append(' ');
        page.append(statusString);
        page.append("</h1>\n");
        if (detailedStatus != null) {
            page.append(detailedStatus);
        }
        page.append("</body></html>"); 
        response.addData(new ResponseStringData(page.toString()));
        return response;
    }

}

/*
 * $Log: HttpResponseFactory.java,v $
 * Revision 1.5  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.4  2003/04/30 22:56:33  stepan
 * added javadoc
 *
 * Revision 1.3  2003/04/30 20:54:23  stepan
 * one createResponseFromException() renamed to createResponseFromStatus()
 *
 * Revision 1.2  2003/04/30 17:02:10  stepan
 * take care of sendData response option
 *
 * Revision 1.1  2003/04/28 20:18:33  stepan
 * too many changes
 *
 */