/*
 * $Id: IResponseData.java,v 1.5 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj;

import java.io.IOException;
import java.io.OutputStream;


/**
 * Data to be sent in {@link cz.srnet.shttpdj.IResponse}.
 * 
 * <p>
 * Implementors can ignore data encoding parameter if they want.
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.5 $ $Date: 2003/04/30 23:46:12 $
 */
public interface IResponseData {
    
    /**
     * Returns data length in given encoding.
     * 
     * @param enc desired data encoding
     * @throws IOException
     */
    public long getLength(String enc) throws IOException;
    
    /**
     * Send data through given stream.
     * 
     * @param os output stream
     * @param enc desired data encoding
     * @throws IOException
     */
    public void send(OutputStream os, String enc) throws IOException;

}

/*
 * $Log: IResponseData.java,v $
 * Revision 1.5  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.4  2003/04/30 23:01:13  stepan
 * added javadoc
 *
 * Revision 1.3  2003/04/30 17:01:39  stepan
 * added getLength()
 *
 * Revision 1.2  2003/04/28 20:18:33  stepan
 * too many changes
 *
 * Revision 1.1  2003/04/21 17:36:23  stepan
 * too many changes done
 *
 */