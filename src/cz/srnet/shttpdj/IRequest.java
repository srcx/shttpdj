/*
 * $Id: IRequest.java,v 1.5 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj;

import java.io.InputStream;
import java.util.Map;

/**
 * Client's request.
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.5 $ $Date: 2003/04/30 23:46:12 $
 */
public interface IRequest {

    /**
     * Returns request's method. 
     */
    public String getMethod();
    
    /**
     * Returns requested URI.
     */
    public URI getRequestURI();
    
    /**
     * Returns request's protocol.
     */
    public String getProtocol();
    
    /**
     * Returns request's protocol version.
     */
    public String getProtocolVersion();

    /**
     * Returns request's headers.
     */
    public Map getHeaders();

    /**
     * Returns request's header value.
     * 
     * @param header header name
     */
    public String getHeader(String header);

    /**
     * Returns request's data. Returned stream is shared and without
     * any cache so it should be used only once.
     */
    public InputStream getData();

}

/*
 * $Log: IRequest.java,v $
 * Revision 1.5  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.4  2003/04/30 23:01:13  stepan
 * added javadoc
 *
 * Revision 1.3  2003/04/30 17:01:23  stepan
 * switched from java.net.URL to our own URI
 *
 * Revision 1.2  2003/04/21 17:36:23  stepan
 * too many changes done
 *
 * Revision 1.1  2003/04/21 00:34:37  stepan
 * lot of code and libs added
 *
 */