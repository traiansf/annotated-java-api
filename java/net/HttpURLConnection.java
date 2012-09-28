/*
 * Copyright (c) 1996, 2010, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package java.net;

import java.io.InputStream;
import java.io.IOException;
import java.security.Permission;
import java.util.Date;

/** {@collect.stats} 
 * {@description.open}
 * A URLConnection with support for HTTP-specific features. See
 * <A HREF="http://www.w3.org/pub/WWW/Protocols/"> the spec </A> for
 * details.
 * <p>
 *
 * Each HttpURLConnection instance is used to make a single request
 * but the underlying network connection to the HTTP server may be
 * transparently shared by other instances. Calling the close() methods
 * on the InputStream or OutputStream of an HttpURLConnection
 * after a request may free network resources associated with this
 * instance but has no effect on any shared persistent connection.
 * Calling the disconnect() method may close the underlying socket
 * if a persistent connection is otherwise idle at that time.
 * {@description.close}
 *
 * @see     java.net.HttpURLConnection#disconnect()
 * @since JDK1.1
 */
abstract public class HttpURLConnection extends URLConnection {
    /* instance variables */

    /** {@collect.stats} 
     * {@description.open}
     * The HTTP method (GET,POST,PUT,etc.).
     * {@description.close}
     */
    protected String method = "GET";

    /** {@collect.stats} 
     * {@description.open}
     * The chunk-length when using chunked encoding streaming mode for output.
     * A value of <code>-1</code> means chunked encoding is disabled for output.
     * {@description.close}
     * @since 1.5
     */
    protected int chunkLength = -1;

    /** {@collect.stats} 
     * {@description.open}
     * The fixed content-length when using fixed-length streaming mode.
     * A value of <code>-1</code> means fixed-length streaming mode is disabled
     * for output.
     * {@description.close}
     * @since 1.5
     */
    protected int fixedContentLength = -1;

    /** {@collect.stats} 
     * {@description.open}
     * Returns the key for the <code>n</code><sup>th</sup> header field.
     * Some implementations may treat the <code>0</code><sup>th</sup>
     * header field as special, i.e. as the status line returned by the HTTP
     * server. In this case, {@link #getHeaderField(int) getHeaderField(0)} returns the status
     * line, but <code>getHeaderFieldKey(0)</code> returns null.
     * {@description.close}
     *
     * @param   n   an index, where n >=0.
     * @return  the key for the <code>n</code><sup>th</sup> header field,
     *          or <code>null</code> if the key does not exist.
     */
    public String getHeaderFieldKey (int n) {
        return null;
    }

    /** {@collect.stats} 
     * {@description.open}
     * This method is used to enable streaming of a HTTP request body
     * without internal buffering, when the content length is known in
     * advance.
     * {@description.close}
     * {@property.open unchecked}
     * <p>
     * An exception will be thrown if the application
     * attempts to write more data than the indicated
     * content-length, or if the application closes the OutputStream
     * before writing the indicated amount.
     * {@property.close}
     * {@description.open}
     * <p>
     * When output streaming is enabled, authentication
     * and redirection cannot be handled automatically.
     * A HttpRetryException will be thrown when reading
     * the response if authentication or redirection are required.
     * This exception can be queried for the details of the error.
     * {@description.close}
     * {@property.open runtime formal:java.net.HttpURLConnection_SetBeforeConnect}
     * <p>
     * This method must be called before the URLConnection is connected.
     * {@property.close}
     *
     * @param   contentLength The number of bytes which will be written
     *          to the OutputStream.
     *
     * @throws  IllegalStateException if URLConnection is already connected
     *          or if a different streaming mode is already enabled.
     *
     * @throws  IllegalArgumentException if a content length less than
     *          zero is specified.
     *
     * @see     #setChunkedStreamingMode(int)
     * @since 1.5
     */
    public void setFixedLengthStreamingMode (int contentLength) {
        if (connected) {
            throw new IllegalStateException ("Already connected");
        }
        if (chunkLength != -1) {
            throw new IllegalStateException ("Chunked encoding streaming mode set");
        }
        if (contentLength < 0) {
            throw new IllegalArgumentException ("invalid content length");
        }
        fixedContentLength = contentLength;
    }

    /* Default chunk size (including chunk header) if not specified;
     * we want to keep this in sync with the one defined in
     * sun.net.www.http.ChunkedOutputStream
     */
    private static final int DEFAULT_CHUNK_SIZE = 4096;

    /** {@collect.stats} 
     * {@description.open}
     * This method is used to enable streaming of a HTTP request body
     * without internal buffering, when the content length is <b>not</b>
     * known in advance. In this mode, chunked transfer encoding
     * is used to send the request body. Note, not all HTTP servers
     * support this mode.
     * <p>
     * When output streaming is enabled, authentication
     * and redirection cannot be handled automatically.
     * A HttpRetryException will be thrown when reading
     * the response if authentication or redirection are required.
     * This exception can be queried for the details of the error.
     * {@description.close}
     * {@property.open runtime formal:java.net.HttpURLConnection_SetBeforeConnect}
     * <p>
     * This method must be called before the URLConnection is connected.
     * {@property.close}
     *
     * @param   chunklen The number of bytes to write in each chunk.
     *          If chunklen is less than or equal to zero, a default
     *          value will be used.
     *
     * @throws  IllegalStateException if URLConnection is already connected
     *          or if a different streaming mode is already enabled.
     *
     * @see     #setFixedLengthStreamingMode(int)
     * @since 1.5
     */
    public void setChunkedStreamingMode (int chunklen) {
        if (connected) {
            throw new IllegalStateException ("Can't set streaming mode: already connected");
        }
        if (fixedContentLength != -1) {
            throw new IllegalStateException ("Fixed length streaming mode set");
        }
        chunkLength = chunklen <=0? DEFAULT_CHUNK_SIZE : chunklen;
    }

    /** {@collect.stats} 
     * {@description.open}
     * Returns the value for the <code>n</code><sup>th</sup> header field.
     * Some implementations may treat the <code>0</code><sup>th</sup>
     * header field as special, i.e. as the status line returned by the HTTP
     * server.
     * <p>
     * This method can be used in conjunction with the
     * {@link #getHeaderFieldKey getHeaderFieldKey} method to iterate through all
     * the headers in the message.
     * {@description.close}
     *
     * @param   n   an index, where n>=0.
     * @return  the value of the <code>n</code><sup>th</sup> header field,
     *          or <code>null</code> if the value does not exist.
     * @see     java.net.HttpURLConnection#getHeaderFieldKey(int)
     */
    public String getHeaderField(int n) {
        return null;
    }

    /** {@collect.stats} 
     * {@description.open}
     * An <code>int</code> representing the three digit HTTP Status-Code.
     * <ul>
     * <li> 1xx: Informational
     * <li> 2xx: Success
     * <li> 3xx: Redirection
     * <li> 4xx: Client Error
     * <li> 5xx: Server Error
     * </ul>
     * {@description.close}
     */
    protected int responseCode = -1;

    /** {@collect.stats} 
     * {@description.open}
     * The HTTP response message.
     * {@description.close}
     */
    protected String responseMessage = null;

    /* static variables */

    /* do we automatically follow redirects? The default is true. */
    private static boolean followRedirects = true;

    /** {@collect.stats} 
     * {@description.open}
     * If <code>true</code>, the protocol will automatically follow redirects.
     * If <code>false</code>, the protocol will not automatically follow
     * redirects.
     * <p>
     * This field is set by the <code>setInstanceFollowRedirects</code>
     * method. Its value is returned by the <code>getInstanceFollowRedirects</code>
     * method.
     * <p>
     * Its default value is based on the value of the static followRedirects
     * at HttpURLConnection construction time.
     * {@description.close}
     *
     * @see     java.net.HttpURLConnection#setInstanceFollowRedirects(boolean)
     * @see     java.net.HttpURLConnection#getInstanceFollowRedirects()
     * @see     java.net.HttpURLConnection#setFollowRedirects(boolean)
     */
    protected boolean instanceFollowRedirects = followRedirects;

    /* valid HTTP methods */
    private static final String[] methods = {
        "GET", "POST", "HEAD", "OPTIONS", "PUT", "DELETE", "TRACE"
    };

    /** {@collect.stats} 
     * {@description.open}
     * Constructor for the HttpURLConnection.
     * {@description.close}
     * @param u the URL
     */
    protected HttpURLConnection (URL u) {
        super(u);
    }

    /** {@collect.stats} 
     * {@description.open}
     * Sets whether HTTP redirects  (requests with response code 3xx) should
     * be automatically followed by this class.  True by default.  Applets
     * cannot change this variable.
     * <p>
     * If there is a security manager, this method first calls
     * the security manager's <code>checkSetFactory</code> method
     * to ensure the operation is allowed.
     * This could result in a SecurityException.
     * {@description.close}
     *
     * @param set a <code>boolean</code> indicating whether or not
     * to follow HTTP redirects.
     * @exception  SecurityException  if a security manager exists and its
     *             <code>checkSetFactory</code> method doesn't
     *             allow the operation.
     * @see        SecurityManager#checkSetFactory
     * @see #getFollowRedirects()
     */
    public static void setFollowRedirects(boolean set) {
        SecurityManager sec = System.getSecurityManager();
        if (sec != null) {
            // seems to be the best check here...
            sec.checkSetFactory();
        }
        followRedirects = set;
    }

    /** {@collect.stats} 
     * {@description.open}
     * Returns a <code>boolean</code> indicating
     * whether or not HTTP redirects (3xx) should
     * be automatically followed.
     * {@description.close}
     *
     * @return <code>true</code> if HTTP redirects should
     * be automatically followed, <tt>false</tt> if not.
     * @see #setFollowRedirects(boolean)
     */
    public static boolean getFollowRedirects() {
        return followRedirects;
    }

    /** {@collect.stats} 
     * {@description.open}
     * Sets whether HTTP redirects (requests with response code 3xx) should
     * be automatically followed by this <code>HttpURLConnection</code>
     * instance.
     * <p>
     * The default value comes from followRedirects, which defaults to
     * true.
     * {@description.close}
     *
     * @param followRedirects a <code>boolean</code> indicating
     * whether or not to follow HTTP redirects.
     *
     * @see    java.net.HttpURLConnection#instanceFollowRedirects
     * @see #getInstanceFollowRedirects
     * @since 1.3
     */
     public void setInstanceFollowRedirects(boolean followRedirects) {
        instanceFollowRedirects = followRedirects;
     }

     /** {@collect.stats} 
      * {@description.open}
     * Returns the value of this <code>HttpURLConnection</code>'s
     * <code>instanceFollowRedirects</code> field.
     * {@description.close}
     *
     * @return  the value of this <code>HttpURLConnection</code>'s
     *          <code>instanceFollowRedirects</code> field.
     * @see     java.net.HttpURLConnection#instanceFollowRedirects
     * @see #setInstanceFollowRedirects(boolean)
     * @since 1.3
     */
     public boolean getInstanceFollowRedirects() {
         return instanceFollowRedirects;
     }

    /** {@collect.stats} 
     * {@description.open}
     * Set the method for the URL request, one of:
     * <UL>
     *  <LI>GET
     *  <LI>POST
     *  <LI>HEAD
     *  <LI>OPTIONS
     *  <LI>PUT
     *  <LI>DELETE
     *  <LI>TRACE
     * </UL> are legal, subject to protocol restrictions.  The default
     * method is GET.
     * {@description.close}
     * {@property.open runtime formal:java.net.HttpURLConnection_SetBeforeConnect}
     * {@new.open}
     * <p>
     * This method must be called before the URLConnection is connected.
     * {@new.close}
     * {@property.close}
     *
     * @param method the HTTP method
     * @exception ProtocolException if the method cannot be reset or if
     *              the requested method isn't valid for HTTP.
     * @see #getRequestMethod()
     */
    public void setRequestMethod(String method) throws ProtocolException {
        if (connected) {
            throw new ProtocolException("Can't reset method: already connected");
        }
        // This restriction will prevent people from using this class to
        // experiment w/ new HTTP methods using java.  But it should
        // be placed for security - the request String could be
        // arbitrarily long.

        for (int i = 0; i < methods.length; i++) {
            if (methods[i].equals(method)) {
		if (method.equals("TRACE")) {
		    SecurityManager s = System.getSecurityManager();
		    if (s != null) {
		        s.checkPermission(new NetPermission("allowHttpTrace"));
		    }
		}
                this.method = method;
                return;
            }
        }
        throw new ProtocolException("Invalid HTTP method: " + method);
    }

    /** {@collect.stats} 
     * {@description.open}
     * Get the request method.
     * {@description.close}
     * @return the HTTP request method
     * @see #setRequestMethod(java.lang.String)
     */
    public String getRequestMethod() {
        return method;
    }

    /** {@collect.stats} 
     * {@description.open}
     * Gets the status code from an HTTP response message.
     * For example, in the case of the following status lines:
     * <PRE>
     * HTTP/1.0 200 OK
     * HTTP/1.0 401 Unauthorized
     * </PRE>
     * It will return 200 and 401 respectively.
     * Returns -1 if no code can be discerned
     * from the response (i.e., the response is not valid HTTP).
     * {@description.close}
     * @throws IOException if an error occurred connecting to the server.
     * @return the HTTP Status-Code, or -1
     */
    public int getResponseCode() throws IOException {
        /*
         * We're got the response code already
         */
        if (responseCode != -1) {
            return responseCode;
        }

        /*
         * Ensure that we have connected to the server. Record
         * exception as we need to re-throw it if there isn't
         * a status line.
         */
        Exception exc = null;
        try {
            getInputStream();
        } catch (Exception e) {
            exc = e;
        }

        /*
         * If we can't a status-line then re-throw any exception
         * that getInputStream threw.
         */
        String statusLine = getHeaderField(0);
        if (statusLine == null) {
            if (exc != null) {
                if (exc instanceof RuntimeException)
                    throw (RuntimeException)exc;
                else
                    throw (IOException)exc;
            }
            return -1;
        }

        /*
         * Examine the status-line - should be formatted as per
         * section 6.1 of RFC 2616 :-
         *
         * Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase
         *
         * If status line can't be parsed return -1.
         */
        if (statusLine.startsWith("HTTP/1.")) {
            int codePos = statusLine.indexOf(' ');
            if (codePos > 0) {

                int phrasePos = statusLine.indexOf(' ', codePos+1);
                if (phrasePos > 0 && phrasePos < statusLine.length()) {
                    responseMessage = statusLine.substring(phrasePos+1);
                }

                // deviation from RFC 2616 - don't reject status line
                // if SP Reason-Phrase is not included.
                if (phrasePos < 0)
                    phrasePos = statusLine.length();

                try {
                    responseCode = Integer.parseInt
                            (statusLine.substring(codePos+1, phrasePos));
                    return responseCode;
                } catch (NumberFormatException e) { }
            }
        }
        return -1;
    }

    /** {@collect.stats} 
     * {@description.open}
     * Gets the HTTP response message, if any, returned along with the
     * response code from a server.  From responses like:
     * <PRE>
     * HTTP/1.0 200 OK
     * HTTP/1.0 404 Not Found
     * </PRE>
     * Extracts the Strings "OK" and "Not Found" respectively.
     * Returns null if none could be discerned from the responses
     * (the result was not valid HTTP).
     * {@description.close}
     * @throws IOException if an error occurred connecting to the server.
     * @return the HTTP response message, or <code>null</code>
     */
    public String getResponseMessage() throws IOException {
        getResponseCode();
        return responseMessage;
    }

    public long getHeaderFieldDate(String name, long Default) {
        String dateString = getHeaderField(name);
        try {
            if (dateString.indexOf("GMT") == -1) {
                dateString = dateString+" GMT";
            }
            return Date.parse(dateString);
        } catch (Exception e) {
        }
        return Default;
    }


    /** {@collect.stats} 
     * {@description.open}
     * Indicates that other requests to the server
     * are unlikely in the near future. Calling disconnect()
     * should not imply that this HttpURLConnection
     * instance can be reused for other requests.
     * {@description.close}
     */
    public abstract void disconnect();

    /** {@collect.stats} 
     * {@description.open}
     * Indicates if the connection is going through a proxy.
     * {@description.close}
     * @return a boolean indicating if the connection is
     * using a proxy.
     */
    public abstract boolean usingProxy();

    public Permission getPermission() throws IOException {
        int port = url.getPort();
        port = port < 0 ? 80 : port;
        String host = url.getHost() + ":" + port;
        Permission permission = new SocketPermission(host, "connect");
        return permission;
    }

   /** {@collect.stats} 
    * {@description.open}
    * Returns the error stream if the connection failed
    * but the server sent useful data nonetheless. The
    * typical example is when an HTTP server responds
    * with a 404, which will cause a FileNotFoundException
    * to be thrown in connect, but the server sent an HTML
    * help page with suggestions as to what to do.
    *
    * <p>This method will not cause a connection to be initiated.  If
    * the connection was not connected, or if the server did not have
    * an error while connecting or if the server had an error but
    * no error data was sent, this method will return null. This is
    * the default.
    * {@description.close}
    *
    * @return an error stream if any, null if there have been no
    * errors, the connection is not connected or the server sent no
    * useful data.
    */
    public InputStream getErrorStream() {
        return null;
    }

    /** {@collect.stats} 
     * {@description.open}
     * The response codes for HTTP, as of version 1.1.
     * {@description.close}
     */

    // REMIND: do we want all these??
    // Others not here that we do want??

    /* 2XX: generally "OK" */

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 200: OK.
     * {@description.close}
     */
    public static final int HTTP_OK = 200;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 201: Created.
     * {@description.close}
     */
    public static final int HTTP_CREATED = 201;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 202: Accepted.
     * {@description.close}
     */
    public static final int HTTP_ACCEPTED = 202;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 203: Non-Authoritative Information.
     * {@description.close}
     */
    public static final int HTTP_NOT_AUTHORITATIVE = 203;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 204: No Content.
     * {@description.close}
     */
    public static final int HTTP_NO_CONTENT = 204;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 205: Reset Content.
     * {@description.close}
     */
    public static final int HTTP_RESET = 205;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 206: Partial Content.
     * {@description.close}
     */
    public static final int HTTP_PARTIAL = 206;

    /* 3XX: relocation/redirect */

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 300: Multiple Choices.
     * {@description.close}
     */
    public static final int HTTP_MULT_CHOICE = 300;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 301: Moved Permanently.
     * {@description.close}
     */
    public static final int HTTP_MOVED_PERM = 301;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 302: Temporary Redirect.
     * {@description.close}
     */
    public static final int HTTP_MOVED_TEMP = 302;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 303: See Other.
     * {@description.close}
     */
    public static final int HTTP_SEE_OTHER = 303;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 304: Not Modified.
     * {@description.close}
     */
    public static final int HTTP_NOT_MODIFIED = 304;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 305: Use Proxy.
     * {@description.close}
     */
    public static final int HTTP_USE_PROXY = 305;

    /* 4XX: client error */

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 400: Bad Request.
     * {@description.close}
     */
    public static final int HTTP_BAD_REQUEST = 400;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 401: Unauthorized.
     * {@description.close}
     */
    public static final int HTTP_UNAUTHORIZED = 401;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 402: Payment Required.
     * {@description.close}
     */
    public static final int HTTP_PAYMENT_REQUIRED = 402;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 403: Forbidden.
     * {@description.close}
     */
    public static final int HTTP_FORBIDDEN = 403;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 404: Not Found.
     * {@description.close}
     */
    public static final int HTTP_NOT_FOUND = 404;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 405: Method Not Allowed.
     * {@description.close}
     */
    public static final int HTTP_BAD_METHOD = 405;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 406: Not Acceptable.
     * {@description.close}
     */
    public static final int HTTP_NOT_ACCEPTABLE = 406;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 407: Proxy Authentication Required.
     * {@description.close}
     */
    public static final int HTTP_PROXY_AUTH = 407;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 408: Request Time-Out.
     * {@description.close}
     */
    public static final int HTTP_CLIENT_TIMEOUT = 408;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 409: Conflict.
     * {@description.close}
     */
    public static final int HTTP_CONFLICT = 409;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 410: Gone.
     * {@description.close}
     */
    public static final int HTTP_GONE = 410;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 411: Length Required.
     * {@description.close}
     */
    public static final int HTTP_LENGTH_REQUIRED = 411;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 412: Precondition Failed.
     * {@description.close}
     */
    public static final int HTTP_PRECON_FAILED = 412;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 413: Request Entity Too Large.
     * {@description.close}
     */
    public static final int HTTP_ENTITY_TOO_LARGE = 413;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 414: Request-URI Too Large.
     * {@description.close}
     */
    public static final int HTTP_REQ_TOO_LONG = 414;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 415: Unsupported Media Type.
     * {@description.close}
     */
    public static final int HTTP_UNSUPPORTED_TYPE = 415;

    /* 5XX: server error */

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 500: Internal Server Error.
     * {@description.close}
     * @deprecated   it is misplaced and shouldn't have existed.
     */
    @Deprecated
    public static final int HTTP_SERVER_ERROR = 500;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 500: Internal Server Error.
     * {@description.close}
     */
    public static final int HTTP_INTERNAL_ERROR = 500;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 501: Not Implemented.
     * {@description.close}
     */
    public static final int HTTP_NOT_IMPLEMENTED = 501;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 502: Bad Gateway.
     * {@description.close}
     */
    public static final int HTTP_BAD_GATEWAY = 502;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 503: Service Unavailable.
     * {@description.close}
     */
    public static final int HTTP_UNAVAILABLE = 503;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 504: Gateway Timeout.
     * {@description.close}
     */
    public static final int HTTP_GATEWAY_TIMEOUT = 504;

    /** {@collect.stats} 
     * {@description.open}
     * HTTP Status-Code 505: HTTP Version Not Supported.
     * {@description.close}
     */
    public static final int HTTP_VERSION = 505;

}
