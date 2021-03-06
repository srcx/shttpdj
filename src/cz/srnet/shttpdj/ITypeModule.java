/*
 * $Id: ITypeModule.java,v 1.3 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj;

import java.io.File;

/**
 * Special server module which is able to determine content type.
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.3 $ $Date: 2003/04/30 23:46:12 $
 */
public interface ITypeModule extends IModule {
    
    /** module id */
    public static final String MODULE_ID = "types";

    /**
     * Returns content type of given file or {@link IConfigModule#getDefaultContentType()} if not known.
     * @param f file
     */
    public String getContentType(File f);

    /**
     * Returns content type for given file extension or {@link IConfigModule#getDefaultContentType()} if not known.
     * @param fileExt file extension (case insensitive, without leading <code>'.'</code>)
     */
    public String getContentType(String fileExt);

}

/*
 * $Log: ITypeModule.java,v $
 * Revision 1.3  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.2  2003/04/30 23:01:13  stepan
 * added javadoc
 *
 * Revision 1.1  2003/04/30 20:24:43  stepan
 * created
 *
 */