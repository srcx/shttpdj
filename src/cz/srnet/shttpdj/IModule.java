/*
 * $Id: IModule.java,v 1.3 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj;

import org.w3c.dom.Element;


/**
 * Server module. Implementation must define one public no-args
 * constructor.
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.3 $ $Date: 2003/04/30 23:46:12 $
 */
public interface IModule {

    /**
     * Returns module id (<code>null</code> is not permitted and ids
     * starting with <code>'_'</code> are reserved for internal usage).
     */
    public String getId();
    
    /**
     * Returns module's name (arbitrary string).
     */
    public String getName();

    /**
     * Performs initialization. Is called once in a module's lifetime.
     * 
     * @param registry module registry
     * @param config element with module's configuration
     * @throws GenericException
     */
    public void init(IRegistry registry, Element config) throws GenericException;
    
    /**
     * Performs shutdown. Is called once in a module's lifetime.
     */
    public void shutdown();

}

/*
 * $Log: IModule.java,v $
 * Revision 1.3  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.2  2003/04/30 23:01:13  stepan
 * added javadoc
 *
 * Revision 1.1  2003/04/21 00:34:37  stepan
 * lot of code and libs added
 *
 */