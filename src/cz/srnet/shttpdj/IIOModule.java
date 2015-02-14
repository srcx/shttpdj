/*
 * $Id: IIOModule.java,v 1.5 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj;

import java.io.IOException;

/**
 * Module which processes requests and produces responses.
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.5 $ $Date: 2003/04/30 23:46:12 $
 */
public interface IIOModule extends IModule {

    /**
     * Process request. Given response may or may not be used.
     * If module does not want to process the request,
     * <code>null</code> must be returned.
     *  
     * @param request client request
     * @param response response
     * @return either <code>response</code> (modified or not), newly created response or <code>null</code>
     * @throws IOException
     */
    public IResponse doRequest(IRequest request, IResponse response) throws IOException;

}

/*
 * $Log: IIOModule.java,v $
 * Revision 1.5  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.4  2003/04/30 23:01:13  stepan
 * added javadoc
 *
 * Revision 1.3  2003/04/28 20:18:33  stepan
 * too many changes
 *
 * Revision 1.2  2003/04/21 17:36:23  stepan
 * too many changes done
 *
 * Revision 1.1  2003/04/21 00:34:37  stepan
 * lot of code and libs added
 *
 */