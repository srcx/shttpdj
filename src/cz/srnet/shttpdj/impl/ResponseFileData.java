/*
 * $Id: ResponseFileData.java,v 1.4 2004/05/30 13:22:57 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cz.srnet.shttpdj.IResponseFileData;

/**
 * Implementation of {@link cz.srnet.shttpdj.IResponseFileData}.
 * 
 * <p>
 * Encoding parameter is ignored.
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.4 $ $Date: 2004/05/30 13:22:57 $
 */
public class ResponseFileData implements IResponseFileData {

    /**
     * Constructor.
     * 
     * @param file file data
     * @throws IllegalArgumentException if file is not regular file 
     */
    public ResponseFileData(File file) throws IllegalArgumentException {
        super();
        this.file = file;
        if (!file.isFile()) {
            throw new IllegalArgumentException("File " + file + " is not a normal file");
        }
    }
    
    /** file data */
    private File file;

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponseFileData#getFileData()
     */
    public File getFileData() {
        return file;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponseData#getLength(java.lang.String)
     */
    public long getLength(String enc) throws IOException {
        // no recoding
        return file.length();
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponseData#send(java.io.OutputStream, java.lang.String)
     */
    public void send(OutputStream os, String enc) throws IOException {
        // no recoding
        InputStream is = new FileInputStream(file);
        try {
	        byte[] buf = new byte[10000];
	        int len;
	        while ((len = is.read(buf)) > -1) {
	            os.write(buf, 0, len);
	        }
        } finally {
            is.close();
        }
    }

}

/*
 * $Log: ResponseFileData.java,v $
 * Revision 1.4  2004/05/30 13:22:57  stepan
 * close the openned file
 *
 * Revision 1.3  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.2  2003/04/30 22:59:39  stepan
 * added javadoc
 *
 * Revision 1.1  2003/04/30 17:57:07  stepan
 * created
 *
 */