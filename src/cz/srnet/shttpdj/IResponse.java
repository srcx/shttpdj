/*
 * $Id: IResponse.java,v 1.8 2004/12/16 10:01:33 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;

/**
 * Server's response.
 * 
 * <p>
 * All setters and state-modifying methods must return
 * {@link java.lang.IllegalStateException} if {@link #send()} was
 * already called.
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.8 $ $Date: 2004/12/16 10:01:33 $
 */
public interface IResponse {

    /**
     * Returns protocol.
     */
    public String getProtocol();

    /**
     * Returns protocol version.
     */
    public String getProtocolVersion();

    /**
     * Returns status code.
     */
    public int getStatusCode();

    /**
     * Returns status code's short description (aka reason phrase).
     */
    public String getStatusInfo();
    
    /**
     * Set protocol.
     * 
     * @param protocol protocol name
     */
    public void setProtocol(String protocol);
    
    /**
     * Set protocol version.
     * 
     * @param version protocol version
     */
    public void setProtocolVersion(String version);
    
    /**
     * Set status code.
     * 
     * @param status status code
     */
    public void setStatusCode(int status);
    
    /**
     * Set status code's short description (aka reason phrase).
     * 
     * @param status reason phrase
     */
    public void setStatusInfo(String status);
    
    /**
     * Returns headers.
     */
    public Map getHeaders();
    
    /**
     * Returns header's value.
     *  
     * @param header header name (case insensitive)
     */
    public String getHeader(String header);
    
    /**
     * Set header value.
     * 
     * @param header header name (case insensitive)
     * @param value value
     */
    public void setHeader(String header, String value);
    
    /**
     * Removes header.
     * 
     * @param header header name
     */
    public void removeHeader(String header);
    
    /**
     * Set content type. This will set <code>Content-Type</code>
     * header to <code><i>content_type</i>; charset=<i>content_encoding</i></code>
     * ({@link #setContentEncoding(String)}).
     * 
     * @param type content type
     */
    public void setContentType(String type);
    
    /**
     * Returns content type.
     */
    public String getContentType();

    /**
     * Set content encoding. This will set <code>Content-Type</code>
     * header to <code><i>content_type</i>; charset=<i>content_encoding</i></code>
     * ({@link #setContentType(String)}).
     * 
     * @param enc content encoding
     */
    public void setContentEncoding(String enc);
    
    /**
     * Returns content encoding.
     */
    public String getContentEncoding();
    
    /**
     * Returns {@link Collection} of {@link IResponseData}s.
     */
    public Collection getData();
    
    /**
     * Add data to response.
     *  
     * @param data response data
     */
    public void addData(IResponseData data);
    
    /**
     * Clears all response data.
     */
    public void clearData();
    
    /**
     * Returns whether response data wil be sent on {@link #send()}.
     */
    public boolean getSendData();
    
    /**
     * Send response.
     * 
     * @throws IOException
     */
    public void send() throws IOException;
    
    /**
     * Returns output stream wher response was/will be sent.
     */
    public OutputStream getOutputStream();

}

/*
 * $Log: IResponse.java,v $
 * Revision 1.8  2004/12/16 10:01:33  stepan
 * javadoc typo fixed
 *
 * Revision 1.7  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.6  2003/04/30 23:01:13  stepan
 * added javadoc
 *
 * Revision 1.5  2003/04/30 20:54:51  stepan
 * added getOutputStream()
 *
 * Revision 1.4  2003/04/30 17:01:50  stepan
 * added getSendData()
 *
 * Revision 1.3  2003/04/28 20:18:33  stepan
 * too many changes
 *
 * Revision 1.2  2003/04/21 17:36:23  stepan
 * too many changes done
 *
 * Revision 1.1  2003/04/21 00:34:37  stepan
 * lot of code and libs added
 *
 */