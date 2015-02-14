/*
 * $Id: ConfigModule.java,v 1.5 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj.impl;

import org.w3c.dom.Element;

import cz.srnet.shttpdj.GenericException;
import cz.srnet.shttpdj.IConfigModule;

/**
 * Implementation of {@link cz.srnet.shttpdj.IConfigModule}.
 * 
 * <p>
 * Configuration:<br>
 * <pre>
 * server-banner (default "shttpdj/0.1")
 * - server banner
 * output-encoding (default "iso-8859-1")
 * - default output encoding
 * directory-index (default, "index.html")
 * - directory index file name
 * default-content-type (default "text/plain")
 * - default content type
 * </pre>
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.5 $ $Date: 2003/04/30 23:46:12 $
 */
public class ConfigModule extends AbstractModule implements IConfigModule {

    /**
     * Constructor. 
     */
    public ConfigModule() {
        super();
    }
    
    /** server banner */
    private String serverBanner;
    /** output encoding */
    private String outputEnc;
    /** directory index file name */
    private String indexName;
    /** default content type */
    private String defaultContentType;

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.impl.AbstractModule#doInit()
     */
    protected void doInit() throws GenericException {
        Element config = getConfigElem();
        serverBanner = XMLHelper.getAttributeValue(config, "server-banner", "shttpdj/0.1", false);
        outputEnc = XMLHelper.getAttributeValue(config, "output-encoding", "iso-8859-1", false);
        indexName = XMLHelper.getAttributeValue(config, "directory-index", "index.html", false);
        defaultContentType = XMLHelper.getAttributeValue(config, "default-content-type", "text/plain", false);
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.impl.AbstractModule#doShutdown()
     */
    protected void doShutdown() {
        // void
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IConfigModule#getServerBanner()
     */
    public String getServerBanner() {
        return serverBanner;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IConfigModule#getOutputEncoding()
     */
    public String getOutputEncoding() {
        return outputEnc;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IConfigModule#getDirectoryIndexName()
     */
    public String getDirectoryIndexName() {
        return indexName;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IConfigModule#getDefaultContentType()
     */
    public String getDefaultContentType() {
        return defaultContentType;
    }
    
}

/*
 * $Log: ConfigModule.java,v $
 * Revision 1.5  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.4  2003/04/30 22:54:02  stepan
 * added javadoc
 *
 * Revision 1.3  2003/04/30 20:24:32  stepan
 * added getDefaultContentType()
 *
 * Revision 1.2  2003/04/30 17:56:32  stepan
 * fixed omitted config values
 * added directory index name
 *
 * Revision 1.1  2003/04/28 20:18:33  stepan
 * too many changes
 *
 */