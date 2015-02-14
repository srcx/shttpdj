/*
 * $Id: TypeModule.java,v 1.3 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.w3c.dom.Element;

import cz.srnet.shttpdj.GenericException;
import cz.srnet.shttpdj.IConfigModule;
import cz.srnet.shttpdj.IRegistry;
import cz.srnet.shttpdj.ITypeModule;

/**
 * Implementation of {@link cz.srnet.shttpdj.ITypeModule}.
 * 
 * <p>
 * Configuration consists of one required attribute <code>mime-types</code>
 * with location of mime.types file in Apache-compatible format.
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.3 $ $Date: 2003/04/30 23:46:12 $
 */
public class TypeModule extends AbstractModule implements ITypeModule {

    /**
     * Constructor.
     */
    public TypeModule() {
        super();
    }
    
    /** mapping between lowercased extension and MIME type */
    private Map extMap = new HashMap();

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.impl.AbstractModule#doInit()
     */
    protected void doInit() throws GenericException {
        Element config = getConfigElem();
        File mimeTypesFile = new File(XMLHelper.getAttributeValue(config, "mime-types", null, true));
        parseMimeTypesFile(mimeTypesFile);
    }

    /**
     * Parses mime.types file.
     * 
     * @param mimeTypesFile mime.types file
     * @throws GenericException
     */
    private void parseMimeTypesFile(File mimeTypesFile) throws GenericException {
        BufferedReader r = null;
        try {
            r = new BufferedReader(new InputStreamReader(new FileInputStream(mimeTypesFile), "us-ascii"));
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if ((line.length() == 0) || (line.charAt(0) == '#')) {
                    continue;
                }
                StringTokenizer stok = new StringTokenizer(line, " \t");
                // there must be at least 1 token thanks to trim() above
                String type = stok.nextToken();
                while (stok.hasMoreTokens()) {
                    String ext = stok.nextToken().toLowerCase();
                    extMap.put(ext, type);
                }
            }
        } catch (IOException e) {
            throw new GenericException("Error occurred while parsing file " + mimeTypesFile, e);
        } finally {
            if (r != null) {
                try {
                    r.close();
                } catch (IOException e) {
                    // ignored
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.impl.AbstractModule#doShutdown()
     */
    protected void doShutdown() {
        // void
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.ITypeModule#getContentType(java.lang.String)
     */
    public String getContentType(String fileExt) {
        String type = (String) extMap.get(fileExt.toLowerCase());
        if (type == null) {
            return getConfigModule(getRegistry()).getDefaultContentType();
        }
        return type;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.ITypeModule#getContentType(java.io.File)
     */
    public String getContentType(File f) {
        String name = f.getName();
        int i = name.lastIndexOf('.');
        if (i < 0) {
            return getConfigModule(getRegistry()).getDefaultContentType();
        }
        String ext = name.substring(i + 1);
        return getContentType(ext);
    }

    /**
     * Returns {@link IConfigModule} from given registry.
     * 
     * @param registry modules registry
     */
    private IConfigModule getConfigModule(IRegistry registry) {
        IConfigModule module = (IConfigModule) registry.getModule(IConfigModule.MODULE_ID);
        return module;
    }

}

/*
 * $Log: TypeModule.java,v $
 * Revision 1.3  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.2  2003/04/30 23:00:00  stepan
 * added javadoc
 *
 * Revision 1.1  2003/04/30 20:24:43  stepan
 * created
 *
 */