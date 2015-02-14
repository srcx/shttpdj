/*
 * $Id: HttpResponse.java,v 1.6 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj.impl;

import java.io.BufferedWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.srnet.shttpdj.IResponse;
import cz.srnet.shttpdj.IResponseData;

/**
 * HTTP implementation of {@link cz.srnet.shttpdj.IResponse}.
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.6 $ $Date: 2003/04/30 23:46:12 $
 */
public class HttpResponse implements IResponse {

    /**
     * Constructor. <b>Do not use, use {@link HttpResponseFactory} instead.
     * 
     * @param os output stream
     * @param sendData should data be sent (pass <code>false</code> for <code>HEAD</code> method) 
     */
    public HttpResponse(OutputStream os, boolean sendData) {
        super();
        this.os = os;
        this.sendData = sendData;
    }

    /** send already? */
    private boolean sendAlready = false;
    /** output stream */
    private OutputStream os;
    /** should data be sent? */
    private boolean sendData;
    /** protocol */
    private String protocol;
    /** protocol version */
    private String protocolVersion;
    /** status code */
    private int statusCode;
    /** reason phrase */
    private String statusInfo;
    /** mapping between lowercased header name and its value */
    private Map headers = new HashMap();
    /** content type - cached value from Content-Type header */
    private String contentType;
    /** content encoding - cached value from Content-Type header */
    private String contentEnc;
    /** list of {@link IResponseData} */
    private List data = new LinkedList();
    
    /**
     * Check if response was already sent.
     * 
     * @throws IllegalStateException if response was already sent
     */
    private void checkSendAlready() {
        if (sendAlready) {
            throw new IllegalStateException("Response has been already sent");
        }
    }
    
    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponse#getProtocol()
     */
    public String getProtocol() {
        return protocol;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponse#getProtocolVersion()
     */
    public String getProtocolVersion() {
        return protocolVersion;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponse#getStatusCode()
     */
    public int getStatusCode() {
        return statusCode;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponse#getStatusInfo()
     */
    public String getStatusInfo() {
        return statusInfo;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponse#setProtocol(java.lang.String)
     */
    public void setProtocol(String protocol) {
        checkSendAlready();
        this.protocol = protocol;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponse#setProtocolVersion(java.lang.String)
     */
    public void setProtocolVersion(String version) {
        checkSendAlready();
        protocolVersion = version;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponse#setStatusCode(int)
     */
    public void setStatusCode(int status) {
        checkSendAlready();
        statusCode = status;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponse#setStatusInfo(java.lang.String)
     */
    public void setStatusInfo(String status) {
        checkSendAlready();
        statusInfo = status;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponse#getHeaders()
     */
    public Map getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponse#getHeader(java.lang.String)
     */
    public String getHeader(String header) {
        return (String) headers.get(header);
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponse#setHeader(java.lang.String, java.lang.String)
     */
    public void setHeader(String header, String value) {
        checkSendAlready();
        headers.put(header, value);
        if (HttpHelper.HEADER_CONTENT_TYPE.equals(header)) {
            parseContentTypeHeader(value);
        }
    }
    
    /**
     * Parses value of Content-Type header to cached {@link #contentType}
     * and {@link #contentEnc}.
     * 
     * @param value value of Content-Type header
     */
    private void parseContentTypeHeader(String value) {
        int i = value.indexOf(';');
        if (i > -1) {
            contentType = value.substring(0, i);
            // FIX: ugly
            int j = value.lastIndexOf('=');
            if (j > -1) {
                 contentEnc = value.substring(j + 1);
            }
        } else {
            contentType = value;
        }
    }
    
    /**
     * {@link FilterOutputStream} which does not close its underlying stream
     * 
     * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
     * @version $Revision: 1.6 $ $Date: 2003/04/30 23:46:12 $
     */
    private static class StrongOutputStream extends FilterOutputStream {
        
        /**
         * Constructor.
         * 
         * @param out underlying output stream
         */
        public StrongOutputStream(OutputStream out) {
            super(out);
        }

        /* (non-Javadoc)
         * @see java.io.OutputStream#close()
         */
        public void close() throws IOException {
            // do not close underlying stream
        }

    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponse#send()
     */
    public void send() throws IOException {
        checkSendAlready();
        try {
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new StrongOutputStream(os), "us-ascii"));
            w.write(protocol);
            w.write('/');
            w.write(protocolVersion);
            w.write(' ');
            w.write(String.valueOf(statusCode));
            w.write(' ');
            w.write(statusInfo);
            w.write(HttpHelper.EOL);
            computeDataLength();
            Iterator iter = headers.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                key = normalizeHeaderName(key);
                w.write(key);
                w.write(": ");
                w.write(value);
                w.write(HttpHelper.EOL);
            }
            w.write(HttpHelper.EOL);
            w.flush();
            w.close();
            if (sendData) {
                iter = data.iterator();
                while (iter.hasNext()) {
                    IResponseData resData = (IResponseData) iter.next();
                    resData.send(os, contentEnc);
                }
            }
        } finally {
            sendAlready = true;
        }
    }
    
    /**
     * Computes length of all data and stores it into Content-Length
     * header.
     * 
     * @throws IOException
     */
    private void computeDataLength() throws IOException {
        long len = 0;
        Iterator iter = data.iterator();
        while (iter.hasNext()) {
            IResponseData resData = (IResponseData) iter.next();
            long l = resData.getLength(contentEnc);
            if (l == -1) {
                len = -1;
                break;
            }
            len += l;
        }
        setHeader(HttpHelper.HEADER_CONTENT_LENGTH, String.valueOf(len));
    }
    
    /**
     * Returns prettyfied header name (instead of content-type
     * returns Content-Type). It's just for beauty. 
     * 
     * @param s header name
     */
    private String normalizeHeaderName(String s) {
        // just for pretty headers
        char[] nc = new char[s.length()];
        boolean wantUC = true;
        for (int i = 0; i < nc.length; i++) {
            char c = s.charAt(i);
            if (wantUC) {
                c = Character.toUpperCase(c);
                wantUC = false;
            }
            if (c == '-') {
                wantUC = true;
            }
            nc[i] = c;
        }
        return new String(nc);
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponse#addData(cz.srnet.shttpdj.IResponseData)
     */
    public void addData(IResponseData rdata) {
        checkSendAlready();
        data.add(rdata);
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponse#clearData()
     */
    public void clearData() {
        checkSendAlready();
        data.clear();
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponse#getData()
     */
    public Collection getData() {
        return Collections.unmodifiableCollection(data);
    }
    
    /**
     * Resets Content-Type header from cached values {@link #contentType}
     * and {@link #contentEnc}.
     */
    private void resetContentTypeHeader() {
        if (contentType != null) {
            StringBuffer buf = new StringBuffer();
            buf.append(contentType);
            if (contentEnc != null) {
                buf.append("; charset=");
                buf.append(contentEnc);
            }
            setHeader(HttpHelper.HEADER_CONTENT_TYPE, buf.toString());
        } else {
            removeHeader(HttpHelper.HEADER_CONTENT_TYPE);
        }
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponse#setContentType(java.lang.String)
     */
    public void setContentType(String type) {
        contentType = type;
        resetContentTypeHeader();
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponse#getContentType()
     */
    public String getContentType() {
        return contentType;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponse#setContentEncoding(java.lang.String)
     */
    public void setContentEncoding(String enc) {
        contentEnc = enc;
        resetContentTypeHeader();
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponse#getContentEncoding()
     */
    public String getContentEncoding() {
        return contentEnc;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponse#removeHeader(java.lang.String)
     */
    public void removeHeader(String header) {
        headers.remove(header);
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponse#getSendData()
     */
    public boolean getSendData() {
        return sendData;
    }

    /* (non-Javadoc)
     * @see cz.srnet.shttpdj.IResponse#getOutputStream()
     */
    public OutputStream getOutputStream() {
        return os;
    }

}

/*
 * $Log: HttpResponse.java,v $
 * Revision 1.6  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.5  2003/04/30 22:56:20  stepan
 * added javadoc
 *
 * Revision 1.4  2003/04/30 20:54:51  stepan
 * added getOutputStream()
 *
 * Revision 1.3  2003/04/30 17:03:14  stepan
 * take care of sendData response option
 * compute Content-Length
 * prettify header names
 * fixed bug in content type parsing
 *
 * Revision 1.2  2003/04/28 20:18:33  stepan
 * too many changes
 *
 * Revision 1.1  2003/04/21 17:36:23  stepan
 * too many changes done
 *
 */