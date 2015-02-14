/*
 * $Id: Registry.java,v 1.4 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import cz.srnet.shttpdj.GenericException;
import cz.srnet.shttpdj.IConfigModule;
import cz.srnet.shttpdj.IModule;
import cz.srnet.shttpdj.IRegistry;

/**
 * Implementation of {@link cz.srnet.shttpdj.IRegistry}.
 * 
 * <p>
 * Modules are loaded with current thread's {@link java.lang.Thread#getContextClassLoader()}.<br>
 * Presence of {@link cz.srnet.shttpdj.IConfigModule} is checked.<br>
 * Registry should be created by {@link #Registry()} and initialized
 * with {@link IModule#init(IRegistry, Element)}, where registry parameter
 * is ignored and should be <code>null</code>.<br>
 * Registry initializes all modules after their creation (but startable
 * modules are NOT started).<br>
 * 
 * <p>
 * Configuration consists of one top-level element <code>registry</code>
 * with multiple nested <code>module</code> elements with required
 * <code>class</code> attributes. These elements are also passed to
 * modules as their configuration. 
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.4 $ $Date: 2003/04/30 23:46:12 $
 */
public class Registry extends AbstractModule implements IRegistry {

    /**
     * Constructor. 
     */
    public Registry() {
        super();
    }
    
    /** mapping between module ids and {@link IModule}s */ 
    private Map modules = new HashMap();

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IRegistry#getModules()
     */
    public Collection getModules() {
        return Collections.unmodifiableCollection(modules.values());
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IRegistry#findModules(java.lang.Class)
     */
    public Collection findModules(Class clazz) {
        Collection ret = new LinkedList();
        Iterator iter = modules.values().iterator();
        while (iter.hasNext()) {
            IModule module = (IModule) iter.next();
            if (clazz.isInstance(module)) {
                ret.add(module);
            }
        }
        return Collections.unmodifiableCollection(ret);
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IRegistry#getModule(java.lang.String)
     */
    public IModule getModule(String id) {
        return (IModule) modules.get(id);
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.impl.AbstractModule#doInit()
     */
    protected void doInit() throws GenericException {
        Node n = getConfigElem().getFirstChild();
        while (n != null) {
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                if ("module".equals(n.getNodeName())) {
                    registerModule(createModule((Element) n));
                } else {
                    throw new GenericException("Unknown config element " + n.getNodeName());
                }
            }
            n = n.getNextSibling();
        }
        IModule configModule = getModule(IConfigModule.MODULE_ID);
        if ((configModule == null) || !(configModule instanceof IConfigModule)) {
            throw new GenericException("Config module with id '" + ConfigModule.MODULE_ID + "' must be specified");
        }
    }
    
    /**
     * Registers new module (must have id already).
     * 
     * @param module module
     */
    private void registerModule(IModule module) {
        String id = module.getId();
        if ((id == null) || id.startsWith("_")) {
            throw new IllegalArgumentException("Id " + id + " may not be null or start with '_'");  
        }
        if (modules.containsKey(id)) {
            throw new IllegalArgumentException("Id " + id + " already exists");
        }
        modules.put(id, module);
    }

    /**
     * Creates and initializes new module. 
     * 
     * @param elem <code>module</code> element
     * @return new module
     * @throws GenericException
     */
    private IModule createModule(Element elem) throws GenericException {
        String className = elem.getAttribute("class");
        if (className.length() == 0) {
            throw new GenericException("Class attribute is missing");
        }
        try {
            Class clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
            IModule module = (IModule) clazz.newInstance();
            module.init(this, elem);
            return module;
        } catch (ClassNotFoundException e) {
            throw new GenericException("Unable to create module " + className, e);
        } catch (InstantiationException e) {
            throw new GenericException("Unable to create module " + className, e);
        } catch (IllegalAccessException e) {
            throw new GenericException("Unable to create module " + className, e);
        }
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IModule#getId()
     */
    public String getId() {
        String id = (super.getId() == null) ? "_registry" : super.getId();
        return id;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.impl.AbstractModule#doShutdown()
     */
    protected void doShutdown() {
        Iterator iter = modules.values().iterator();
        while (iter.hasNext()) {
            IModule module = (IModule) iter.next();
            module.shutdown();
        }
    }

}

/*
 * $Log: Registry.java,v $
 * Revision 1.4  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.3  2003/04/30 22:59:08  stepan
 * added javadoc
 * return _registry id only if no id was read from configuration (should allow nested registries)
 *
 * Revision 1.2  2003/04/28 20:18:33  stepan
 * too many changes
 *
 * Revision 1.1  2003/04/21 00:34:37  stepan
 * lot of code and libs added
 *
 */