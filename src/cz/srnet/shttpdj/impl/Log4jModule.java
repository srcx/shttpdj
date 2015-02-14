/*
 * $Id: Log4jModule.java,v 1.3 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj.impl;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import cz.srnet.shttpdj.GenericException;

/**
 * Module which initializes log4j. 
 *
 * <p>
 * Configuration consists of one nested element <code>log4j:configuration</code>
 * whose contents are specified by {@link org.apache.log4j.xml.DOMConfigurator}. 
 *  
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.3 $ $Date: 2003/04/30 23:46:12 $
 */
public class Log4jModule extends AbstractModule {

    private static final Logger log = Logger.getLogger(Log4jModule.class);

    /**
     * Constructor.
     */
    public Log4jModule() {
        super();
    }
    
    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.impl.AbstractModule#doInit()
     */
    protected void doInit() throws GenericException {
        DOMConfigurator.configure(XMLHelper.getContainedElement(getConfigElem()));
        log.info(getId() + ": log initialized");
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.impl.AbstractModule#doShutdown()
     */
    protected void doShutdown() {
        // void
    }

}

/*
 * $Log: Log4jModule.java,v $
 * Revision 1.3  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.2  2003/04/30 22:56:43  stepan
 * added javadoc
 *
 * Revision 1.1  2003/04/21 00:34:37  stepan
 * lot of code and libs added
 *
 */