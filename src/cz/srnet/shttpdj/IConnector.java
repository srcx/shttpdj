/*
 * $Id: IConnector.java,v 1.4 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj;

/**
 * Special server module which listens for client's requests and
 * sends back responses. 
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.4 $ $Date: 2003/04/30 23:46:12 $
 */
public interface IConnector extends IStartableModule {
    
}

/*
 * $Log: IConnector.java,v $
 * Revision 1.4  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.3  2003/04/30 23:01:13  stepan
 * added javadoc
 *
 * Revision 1.2  2003/04/28 20:18:33  stepan
 * too many changes
 *
 * Revision 1.1  2003/04/21 00:34:37  stepan
 * lot of code and libs added
 *
 */