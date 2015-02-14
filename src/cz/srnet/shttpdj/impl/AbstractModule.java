/*
 * $Id: AbstractModule.java,v 1.3 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj.impl;

import org.w3c.dom.Element;

import cz.srnet.shttpdj.GenericException;
import cz.srnet.shttpdj.IModule;
import cz.srnet.shttpdj.IRegistry;

/**
 * Abstract {@link cz.srnet.shttpdj.IModule} implementation.
 * 
 * <p>
 * Users must implement {@link #doInit()} and {@link #doShutdown()}
 * methods and use {@link #checkOperational()}.
 * <p>
 * Module id is parsed from configuration as <code>"id"</code> attribute. 
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.3 $ $Date: 2003/04/30 23:46:12 $
 */
public abstract class AbstractModule implements IModule {

    /**
     * Constructor. 
     */
    public AbstractModule() {
        super();
    }
    
    /** is module operational? (initialized and not shutdowned) */
    private boolean operational = false;
    /** configuration */
    private Element config = null;
    /** module id */
    private String id = null;
    /** modules registry */
    private IRegistry registry = null;
    
    /**
     * @throws IllegalStateException if module is not operational
     */
    protected void checkOperational() {
        if (!operational) {
            throw new IllegalStateException("Module is not operational");
        }
    }
    
    /**
     * Returns if module is operational.
     */
    protected boolean isOperational() {
        return operational;
    }
    
    /**
     * Sets operational state. 
     *  
     * @param operational new operational state
     */
    protected void setOperational(boolean operational) {
        this.operational = operational;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IModule#getName()
     */
    public String getName() {
        return getClass().getName();
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IModule#init(org.w3c.dom.Element)
     */
    public void init(IRegistry registry, Element elem) throws GenericException {
        shutdown();
        this.registry = registry;
        this.config = elem;
        parseId();
        doInit();
        setOperational(true);
    }
    
    /**
     * Parse module id from configuration.
     */
    private void parseId() {
        id = config.getAttribute("id");
        if (id.length() == 0) {
            id = null;
        }
    }

    /**
     * Initialize module (e.g. read configuration).
     * 
     * @throws GenericException
     */
    protected abstract void doInit() throws GenericException;
    
    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IModule#shutdown()
     */
    public void shutdown() {
        doShutdown();
        setOperational(false);
    }
    
    /**
     * Shutdown module.
     */
    protected abstract void doShutdown();
    
    /**
     * Returns configuration.
     */
    protected Element getConfigElem() {
        return config;
    }
    
    /**
     * Returns modules registry.
     */
    protected IRegistry getRegistry() {
        return registry;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IModule#getId()
     */
    public String getId() {
        return id;
    }

}

/*
 * $Log: AbstractModule.java,v $
 * Revision 1.3  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.2  2003/04/30 22:53:50  stepan
 * added javadoc
 *
 * Revision 1.1  2003/04/21 00:34:37  stepan
 * lot of code and libs added
 *
 */