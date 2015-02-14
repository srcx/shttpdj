/*
 * $Id: HttpHelper.java,v 1.7 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Utility class for HTTP-related constants and methods.
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.7 $ $Date: 2003/04/30 23:46:12 $
 */
public class HttpHelper {

    /**
     * Singleton.
     */
    private HttpHelper() {
        super();
    }
    
    // status codes

    public static final int STATUS_CONTINUE = 100;
    public static final int STATUS_SWITCHING_PROTOCOLS = 101;
    public static final int STATUS_OK = 200;
    public static final int STATUS_CREATED = 201;
    public static final int STATUS_ACCEPTED = 202;
    public static final int STATUS_NON_AUTHORITATIVE_INFORMATION = 203;
    public static final int STATUS_NO_CONTENT = 204;
    public static final int STATUS_RESET_CONTENT = 205;
    public static final int STATUS_PARTIAL_CONTENT = 206;
    public static final int STATUS_MULTIPLE_CHOICES = 300;
    public static final int STATUS_MOVED_PERMANENTLY = 301;
    public static final int STATUS_FOUND = 302;
    public static final int STATUS_SEE_OTHER = 303;
    public static final int STATUS_NOT_MODIFIED = 304;
    public static final int STATUS_USE_PROXY = 305;
    public static final int STATUS_TEMPORARY_REDIRECT = 307;
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_UNAUTHORIZED = 401;
    public static final int STATUS_PAYMENT_REQUIRED = 402;
    public static final int STATUS_FORBIDDEN = 403;
    public static final int STATUS_NOT_FOUND = 404;
    public static final int STATUS_METHOD_NOT_ALLOWED = 405;
    public static final int STATUS_NOT_ACCEPTABLE = 406;
    public static final int STATUS_PROXY_AUTHENTICATION_REQUIRED = 407;
    public static final int STATUS_REQUEST_TIMEOUT = 408;
    public static final int STATUS_CONFLICT = 409;
    public static final int STATUS_GONE = 410;
    public static final int STATUS_LENGTH_REQUIRED = 411;
    public static final int STATUS_PRECONDITION_FAILED = 412;
    public static final int STATUS_REQUEST_ENTITY_TOO_LARGE = 413;
    public static final int STATUS_REQUEST_URI_TOO_LONG = 414;
    public static final int STATUS_UNSUPPORTED_MEDIA_TYPE = 415;
    public static final int STATUS_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    public static final int STATUS_EXPECTATION_FAILED = 417;
    public static final int STATUS_INTERNAL_SERVER_ERROR = 500;
    public static final int STATUS_NOT_IMPLEMENTED = 501;
    public static final int STATUS_BAD_GATEWAY = 502;
    public static final int STATUS_SERVICE_UNAVAILABLE = 503;
    public static final int STATUS_GATEWAY_TIMEOUT = 504;
    public static final int STATUS_HTTP_VERSION_NOT_SUPPORTED = 505;
    
    private static Map statusStringMap = new HashMap();
    
    private static void setStatusString(int statusCode, String str) {
        statusStringMap.put(new Integer(statusCode), str);
    }
    
    static {
        setStatusString(STATUS_CONTINUE, "Continue");
        setStatusString(STATUS_SWITCHING_PROTOCOLS, "Switching Protocols");
        setStatusString(STATUS_OK, "OK");
        setStatusString(STATUS_CREATED, "Created");
        setStatusString(STATUS_ACCEPTED, "Accepted");
        setStatusString(STATUS_NON_AUTHORITATIVE_INFORMATION, "Non-Authoritative Information");
        setStatusString(STATUS_NO_CONTENT, "No Content");
        setStatusString(STATUS_RESET_CONTENT, "Reset Content");
        setStatusString(STATUS_PARTIAL_CONTENT, "Partial Content");
        setStatusString(STATUS_MULTIPLE_CHOICES, "Multiple Choices");
        setStatusString(STATUS_MOVED_PERMANENTLY, "Moved Permanently");
        setStatusString(STATUS_FOUND, "Found");
        setStatusString(STATUS_SEE_OTHER, "See Other");
        setStatusString(STATUS_NOT_MODIFIED, "Not Modified");
        setStatusString(STATUS_USE_PROXY, "Use Proxy");
        setStatusString(STATUS_TEMPORARY_REDIRECT, "Temporary Redirect");
        setStatusString(STATUS_BAD_REQUEST, "Bad Request");
        setStatusString(STATUS_UNAUTHORIZED, "Unauthorized");
        setStatusString(STATUS_PAYMENT_REQUIRED, "Payment Required");
        setStatusString(STATUS_FORBIDDEN, "Forbidden");
        setStatusString(STATUS_NOT_FOUND, "Not Found");
        setStatusString(STATUS_METHOD_NOT_ALLOWED, "Method Not Allowed");
        setStatusString(STATUS_NOT_ACCEPTABLE, "Not Acceptable");
        setStatusString(STATUS_PROXY_AUTHENTICATION_REQUIRED, "Proxy Authentication Required");
        setStatusString(STATUS_REQUEST_TIMEOUT, "Request Timeout");
        setStatusString(STATUS_CONFLICT, "Conflict");
        setStatusString(STATUS_GONE, "Gone");
        setStatusString(STATUS_LENGTH_REQUIRED, "Length Required");
        setStatusString(STATUS_PRECONDITION_FAILED, "Precondition Failed");
        setStatusString(STATUS_REQUEST_ENTITY_TOO_LARGE, "Request Entity Too Large");
        setStatusString(STATUS_REQUEST_URI_TOO_LONG, "Request-URI Too Long");
        setStatusString(STATUS_UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type");
        setStatusString(STATUS_REQUESTED_RANGE_NOT_SATISFIABLE, "Requested Range Not Satisfiable");
        setStatusString(STATUS_EXPECTATION_FAILED, "Expectation Failed");
        setStatusString(STATUS_INTERNAL_SERVER_ERROR, "Internal Server Error");
        setStatusString(STATUS_NOT_IMPLEMENTED, "Not Implemented");
        setStatusString(STATUS_BAD_GATEWAY, "Bad Gateway");
        setStatusString(STATUS_SERVICE_UNAVAILABLE, "Service Unavailable");
        setStatusString(STATUS_GATEWAY_TIMEOUT, "Gateway Timeout");
        setStatusString(STATUS_HTTP_VERSION_NOT_SUPPORTED, "HTTP Version Not Supported");
    }
    
    // header names
    
    public static final String HEADER_CONTENT_TYPE = "content-type";
    public static final String HEADER_CONTENT_LENGTH = "content-length";
    public static final String HEADER_LOCATION = "location";
    
    /** end-of-line sequence */
    public static final String EOL = "\r\n";
    
    /** RFC1123 date format */
    private static final DateFormat httpDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
    static {
        httpDateFormat.setTimeZone(TimeZone.getTimeZone("GMT")); 
    }
    
    /**
     * Returns status info string (aka reason phrase) for given status.
     * 
     * @param statusCode status code
     */
    public static String getStatusString(int statusCode) {
        return (String) statusStringMap.get(new Integer(statusCode));
    }
    
    /**
     * Formats given {@link Date} as RFC1123 date.
     * 
     * @param date date to format
     */
    public static String formatHttpDate(Date date) {
        return httpDateFormat.format(date);
    }

}

/*
 * $Log: HttpHelper.java,v $
 * Revision 1.7  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.6  2003/04/30 22:55:43  stepan
 * added javadoc
 *
 * Revision 1.5  2003/04/30 20:54:33  stepan
 * added Location header
 *
 * Revision 1.4  2003/04/30 20:42:38  stepan
 * added status strings
 *
 * Revision 1.3  2003/04/30 16:59:41  stepan
 * fixed end-of-lines
 * do not send empty status string
 * send date with GMT time zone
 *
 * Revision 1.2  2003/04/28 20:18:33  stepan
 * too many changes
 *
 * Revision 1.1  2003/04/21 17:36:23  stepan
 * too many changes done
 *
 */