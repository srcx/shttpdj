/*
 * $Id: Main.java,v 1.3 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj.impl;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import cz.srnet.shttpdj.GenericException;
import cz.srnet.shttpdj.IRegistry;
import cz.srnet.shttpdj.IStartableModule;

/**
 * Main class with command line interface.
 * 
 * <p>
 * Command line: <code>config_file</code>.
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.3 $ $Date: 2003/04/30 23:46:12 $
 */
public class Main {
    
    private static final Logger log = Logger.getLogger(Main.class);

    /**
     * Constructor.
     * 
     * @param configFile config file
     * @throws GenericException 
     */
    public Main(File configFile) throws GenericException {
        super();
        init(configFile);
    }
    
    /** top-level registry */
    private IRegistry registry = null;
    
    /**
     * Initialize. Reads configuration file, creates top-level registry,
     * registers shutdown hook and starts all startable modules.
     * 
     * @param configFile
     * @throws GenericException
     */
    private void init(File configFile) throws GenericException {
        Runtime.getRuntime().addShutdownHook(new Thread("shutdown") {
            public void run () {
                log.info("shutdown request from user");
                // not so kosher, but better than nothing
                registry.shutdown();
            }
        });
        try {
            Document doc = XMLHelper.parseDocument(configFile);
            registry = new Registry();
            registry.init(null, doc.getDocumentElement());
            log.info("Registry initialized");
            Collection startableModules = registry.findModules(IStartableModule.class);
            Iterator iter = startableModules.iterator();
            while (iter.hasNext()) {
                IStartableModule module = (IStartableModule) iter.next();
                module.start();
            }
            log.info("Modules started");
        } catch (SAXException e) {
            throw new GenericException("Unable to process config file " + configFile, e);
        } catch (IOException e) {
            throw new GenericException("Unable to process config file " + configFile, e);
        } catch (ParserConfigurationException e) {
            throw new GenericException("Unable to process config file " + configFile, e);
        }
    }

    /**
     * Main entry-point.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("usage: config_file");
            System.exit(1);
        }
        File configFile = new File(args[0]);
        try {
            new Main(configFile);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // there might be some threads running
            System.exit(2);
        }
    }
}

/*
 * $Log: Main.java,v $
 * Revision 1.3  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.2  2003/04/30 22:57:53  stepan
 * added javadoc
 * added name to shutdown thread
 *
 * Revision 1.1  2003/04/21 00:34:37  stepan
 * lot of code and libs added
 *
 */