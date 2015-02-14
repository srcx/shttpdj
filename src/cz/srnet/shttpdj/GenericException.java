/*
 * $Id: GenericException.java,v 1.3 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Generic exception.
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.3 $ $Date: 2003/04/30 23:46:12 $
 */
public class GenericException extends Exception {

    /** inner exception */
    private Throwable inner = null;
    
    /**
     * Constructor.
     */
    public GenericException() {
        super();
    }

    /**
     * Constructor with detail message.
     *
     * @param msg detail message
     */
    public GenericException(String msg) {
        this (msg, null);
    }
    
    /**
     * Constructor with inner exception.
     * 
     * @param inner inner exception (throwable object)
     */
    public GenericException(Throwable inner) {
        this (null, inner);
    }
    
    /**
     * Constructor with detail message and inner exception.
     *
     * @param msg detail message
     * @param inner inner exception (throwable object)
     */
    public GenericException(String msg, Throwable inner) {
        super ((inner == null) ? msg : ((msg == null) ? inner.getMessage() : msg + ": " + inner.getMessage()));
        this.inner = inner;
    }
    
    /**
     * Returns inner exception or null if none available.
     */
    public Throwable getInnerException () {
        return inner;
    }
    
    /**
     * Prints stack trace of inner exception too.
     */
    public void printStackTrace () {
        super.printStackTrace ();
        if (inner != null) inner.printStackTrace ();
    }
    
    /**
     * Prints stack trace of inner exception too.
     */
    public void printStackTrace (PrintStream s) {
        super.printStackTrace (s);
        if (inner != null) inner.printStackTrace (s);
    }
    
    /**
     * Prints stack trace of inner exception too.
     */
    public void printStackTrace (PrintWriter s) {
        super.printStackTrace (s);
        if (inner != null) inner.printStackTrace (s);
    }

}

/*
 * $Log: GenericException.java,v $
 * Revision 1.3  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.2  2003/04/30 23:00:20  stepan
 * added javadoc
 *
 * Revision 1.1  2003/04/21 00:34:37  stepan
 * lot of code and libs added
 *
 */