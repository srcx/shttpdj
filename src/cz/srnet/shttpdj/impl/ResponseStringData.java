/*
 * $Id: ResponseStringData.java,v 1.4 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj.impl;

import java.io.IOException;
import java.io.OutputStream;

import cz.srnet.shttpdj.IResponseStringData;

/**
 * Implementation of {@link ResponseStringData}.
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.4 $ $Date: 2003/04/30 23:46:12 $
 */
public class ResponseStringData implements IResponseStringData {

    /**
     * Constructor.
     * 
     * @param str string data 
     */
    public ResponseStringData(String str) {
        super();
        this.str = str;
    }
    
    /** string data */
    private String str;

    /** encoding of byte cache */
    private String cachedEnc = null;
    /** byte cache */
    private byte[] cachedStrBytes = null;

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponseStringData#getStringData()
     */
    public String getStringData() {
        return str;
    }
    
    /**
     * Creates (if necessary) byte cache for given encoding.  
     * 
     * @param enc encoding
     * @throws IOException
     */
    private void createCached(String enc) throws IOException {
        if (((enc == null) && (cachedEnc != null))
        || ((enc != null) && !enc.equals(cachedEnc))) {
            cachedStrBytes = null;
        } 
        if (cachedStrBytes == null) {
            if (enc != null) {
                cachedStrBytes = str.getBytes(enc);
            } else {
                cachedStrBytes = str.getBytes();
            }
        }
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponseData#send(null, java.lang.String)
     */
    public void send(OutputStream os, String enc) throws IOException {
        createCached(enc);
        os.write(cachedStrBytes);
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponseData#getLength(java.lang.String)
     */
    public long getLength(String enc) throws IOException {
        createCached(enc);
        return cachedStrBytes.length;
    }

}

/*
 * $Log: ResponseStringData.java,v $
 * Revision 1.4  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.3  2003/04/30 22:59:49  stepan
 * added javadoc
 *
 * Revision 1.2  2003/04/30 17:00:31  stepan
 * implemented getLength()
 *
 * Revision 1.1  2003/04/28 20:18:33  stepan
 * too many changes
 *
 */