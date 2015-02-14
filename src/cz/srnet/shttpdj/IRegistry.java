/*
 * $Id: IRegistry.java,v 1.3 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj;

import java.util.Collection;

/**
 * Modules registry.
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.3 $ $Date: 2003/04/30 23:46:12 $
 */
public interface IRegistry extends IModule {
    
    /**
     * Returns module with given id or <code>null</code>. 
     * 
     * @param id module id
     */
    public IModule getModule(String id);
    
    /**
     * Returns all modules.
     */
    public Collection getModules();
    
    /**
     * Returns all modules implementing or extending given class.
     * 
     * @param clazz module class
     */
    public Collection findModules(Class clazz);

}

/*
 * $Log: IRegistry.java,v $
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