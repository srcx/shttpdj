/*
 * $Id: IConfigModule.java,v 1.5 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj;

/**
 * Special server module which stores global configuration.
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.5 $ $Date: 2003/04/30 23:46:12 $
 */
public interface IConfigModule extends IModule {

    /** module id */
    public static final String MODULE_ID = "config";

    /**
     * Returns output encoding.
     */
    public String getOutputEncoding();
    
    /**
     * Returns server banner.
     */
    public String getServerBanner();
    
    /**
     * Returns directory index name.
     */
    public String getDirectoryIndexName();
    
    /**
     * Returns default content type (as MIME type).
     */
    public String getDefaultContentType();

}

/*
 * $Log: IConfigModule.java,v $
 * Revision 1.5  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.4  2003/04/30 23:01:13  stepan
 * added javadoc
 *
 * Revision 1.3  2003/04/30 20:24:32  stepan
 * added getDefaultContentType()
 *
 * Revision 1.2  2003/04/30 17:56:58  stepan
 * added getDirectoryIndexName()
 *
 * Revision 1.1  2003/04/28 20:18:33  stepan
 * too many changes
 *
 */