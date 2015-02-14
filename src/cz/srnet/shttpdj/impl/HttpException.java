/*
 * $Id: HttpException.java,v 1.3 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj.impl;

import java.io.IOException;

/**
 * Special {@link java.io.IOException} which holds HTTP response
 * status.
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.3 $ $Date: 2003/04/30 23:46:12 $
 */
public class HttpException extends IOException {

    /**
     * Constructor.
     * 
     * @param statusCode status code
     * @param detailedStatus detailed status description
     */
    public HttpException(int statusCode, String detailedStatus) {
        this(statusCode, HttpHelper.getStatusString(statusCode), detailedStatus);
    }
    
    /**
     * Constructor.
     * 
     * @param statusCode status code
     * @param statusString status string info (aka reason phrase)
     * @param detailedStatus detailed status description
     */
    public HttpException(int statusCode, String statusString, String detailedStatus) {
        super(statusCode + " " + statusString + "\n\t" + detailedStatus);
        this.statusCode = statusCode;
        this.statusString = statusString;
        this.detailedStatus = detailedStatus;
    }
    
    /** status code */
    private int statusCode;
    /** reason phrase */
    private String statusString;
    /** detailed status description */
    private String detailedStatus;
    
    /**
     * Returns status code.
     */
    public int getStatusCode() {
        return statusCode;
    }
    
    /**
     * Returns reason phrase.
     */
    public String getStatusString() {
        return statusString;
    }
    
    /**
     * Returns detailed status description.
     */
    public String getDetailedStatus() {
        return detailedStatus;
    }
    
}

/*
 * $Log: HttpException.java,v $
 * Revision 1.3  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.2  2003/04/30 22:55:30  stepan
 * added javadoc
 *
 * Revision 1.1  2003/04/21 17:36:23  stepan
 * too many changes done
 *
 */