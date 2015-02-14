/*
 * $Id: NullModule.java,v 1.4 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj.impl;

import java.io.IOException;

import cz.srnet.shttpdj.GenericException;
import cz.srnet.shttpdj.IIOModule;
import cz.srnet.shttpdj.IRequest;
import cz.srnet.shttpdj.IResponse;

/**
 * {@link cz.srnet.shttpdj.IIOModule} which does nothing.
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.4 $ $Date: 2003/04/30 23:46:12 $
 */
public class NullModule extends AbstractModule implements IIOModule {

    /**
     * Constructor.
     */
    public NullModule() {
        super();
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.impl.AbstractModule#doInit()
     */
    protected void doInit() throws GenericException {
        // void
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.impl.AbstractModule#doShutdown()
     */
    protected void doShutdown() {
        // void
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IIOModule#doRequest(cz.srnet.shttpdj.IRequest, cz.srnet.shttpdj.IResponse)
     */
    public IResponse doRequest(IRequest request, IResponse response) throws IOException {
        return null;
    }

}

/*
 * $Log: NullModule.java,v $
 * Revision 1.4  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.3  2003/04/30 22:57:29  stepan
 * added javadoc
 *
 * Revision 1.2  2003/04/30 17:00:03  stepan
 * removed debugging left-over
 *
 * Revision 1.1  2003/04/28 20:18:33  stepan
 * too many changes
 *
 */