/*
 * $Id: URI.java,v 1.3 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj;

/**
 * Uniform Resource Identifier.
 * 
 * <p>
 * Only absolute path URIs are permitted, will be confused by URIs with
 * protocol, host etc. Our form is <code>path#reference?query;parameters</code>.
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.3 $ $Date: 2003/04/30 23:46:12 $
 */
public class URI {

    /**
     * Constructor.
     * 
     * @param s string form 
     */
    public URI(String s) {
        super();
        parseURI(s);
    }
    
    /** full (intact) URI */
    private String full;
    /** path component of URI */
    private String path;
    /** query component of URI */
    private String query;
    /** reference component of URI */
    private String ref;
    /** params component of URI */
    private String params;
    
    /**
     * Parse URI into components.
     * 
     * @param s string form
     */
    private void parseURI(String s) {
        path = full = s;
        int i = path.indexOf(';');
        if (i > -1) {
            params = path.substring(i + 1); 
            path = path.substring(0, i);
        }
        i = path.indexOf('?');
        if (i > -1) {
            query = path.substring(i + 1); 
            path = path.substring(0, i);
        }
        i = path.indexOf('#');
        if (i > -1) {
            ref = path.substring(i + 1); 
            path = path.substring(0, i);
        }
    }
    
    /**
     * Returns path component.
     */
    public String getPath() {
        return path;
    }
    
    /**
     * Returns query component.
     */
    public String getQuery() {
        return query;
    }
    
    /**
     * Returns reference component.
     */
    public String getRef() {
        return ref;
    }
    
    /**
     * Returns parameters component.
     */
    public String getParams() {
        return params;
    }
    
    /**
     * Returns external form (suitable for use in {@link #URI(String)}.
     */
    public String toExternalForm() {
        return full;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return toExternalForm();
    }

}

/*
 * $Log: URI.java,v $
 * Revision 1.3  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.2  2003/04/30 23:01:25  stepan
 * added javadoc
 *
 * Revision 1.1  2003/04/30 17:01:23  stepan
 * switched from java.net.URL to our own URI
 *
 */