/*
 * $Id: IResponseFileData.java,v 1.3 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj;

import java.io.File;

/**
 * Response data read from file.
 * 
 * <p>
 * Implementors should ignored data encoding parameter or at least
 * make such behaviour toggable.
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.3 $ $Date: 2003/04/30 23:46:12 $
 */
public interface IResponseFileData extends IResponseData {

    /**
     * Returns associated file.
     */
    public File getFileData();

}

/*
 * $Log: IResponseFileData.java,v $
 * Revision 1.3  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.2  2003/04/30 23:01:13  stepan
 * added javadoc
 *
 * Revision 1.1  2003/04/30 17:57:07  stepan
 * created
 *
 */