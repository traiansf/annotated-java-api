/*
 * Copyright (c) 1995, 2006, Oracle and/or its affiliates. All rights reserved.
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileDescriptor;

/** {@collect.stats} 
 * {@description.open}
 * The abstract class <code>SocketImpl</code> is a common superclass
 * of all classes that actually implement sockets. It is used to
 * create both client and server sockets.
 * <p>
 * A "plain" socket implements these methods exactly as
 * described, without attempting to go through a firewall or proxy.
 * {@description.close}
 *
 * @author  unascribed
 * @since   JDK1.0
 */
public abstract class SocketImpl implements SocketOptions {
    /** {@collect.stats} 
     * {@description.open}
     * The actual Socket object.
     * {@description.close}
     */
    Socket socket = null;
    ServerSocket serverSocket = null;

    /** {@collect.stats} 
     * {@description.open}
     * The file descriptor object for this socket.
     * {@description.close}
     */
    protected FileDescriptor fd;

    /** {@collect.stats} 
     * {@description.open}
     * The IP address of the remote end of this socket.
     * {@description.close}
     */
    protected InetAddress address;

    /** {@collect.stats} 
     * {@description.open}
     * The port number on the remote host to which this socket is connected.
     * {@description.close}
     */
    protected int port;

    /** {@collect.stats} 
     * {@description.open}
     * The local port number to which this socket is connected.
     * {@description.close}
     */
    protected int localport;

    /** {@collect.stats} 
     * {@description.open}
     * Creates either a stream or a datagram socket.
     * {@description.close}
     *
     * @param      stream   if <code>true</code>, create a stream socket;
     *                      otherwise, create a datagram socket.
     * @exception  IOException  if an I/O error occurs while creating the
     *               socket.
     */
    protected abstract void create(boolean stream) throws IOException;

    /** {@collect.stats} 
     * {@description.open}
     * Connects this socket to the specified port on the named host.
     * {@description.close}
     *
     * @param      host   the name of the remote host.
     * @param      port   the port number.
     * @exception  IOException  if an I/O error occurs when connecting to the
     *               remote host.
     */
    protected abstract void connect(String host, int port) throws IOException;

    /** {@collect.stats} 
     * {@description.open}
     * Connects this socket to the specified port number on the specified host.
     * {@description.close}
     *
     * @param      address   the IP address of the remote host.
     * @param      port      the port number.
     * @exception  IOException  if an I/O error occurs when attempting a
     *               connection.
     */
    protected abstract void connect(InetAddress address, int port) throws IOException;

    /** {@collect.stats} 
     * {@description.open}
     * Connects this socket to the specified port number on the specified host.
     * A timeout of zero is interpreted as an infinite timeout. The connection
     * will then block until established or an error occurs.
     * {@description.close}
     *
     * @param      address   the Socket address of the remote host.
     * @param     timeout  the timeout value, in milliseconds, or zero for no timeout.
     * @exception  IOException  if an I/O error occurs when attempting a
     *               connection.
     * @since 1.4
     */
    protected abstract void connect(SocketAddress address, int timeout) throws IOException;

    /** {@collect.stats} 
     * {@description.open}
     * Binds this socket to the specified local IP address and port number.
     * {@description.close}
     *
     * @param      host   an IP address that belongs to a local interface.
     * @param      port   the port number.
     * @exception  IOException  if an I/O error occurs when binding this socket.
     */
    protected abstract void bind(InetAddress host, int port) throws IOException;

    /** {@collect.stats} 
     * {@description.open}
     * Sets the maximum queue length for incoming connection indications
     * (a request to connect) to the <code>count</code> argument. If a
     * connection indication arrives when the queue is full, the
     * connection is refused.
     * {@description.close}
     *
     * @param      backlog   the maximum length of the queue.
     * @exception  IOException  if an I/O error occurs when creating the queue.
     */
    protected abstract void listen(int backlog) throws IOException;

    /** {@collect.stats} 
     * {@description.open}
     * Accepts a connection.
     * {@description.close}
     *
     * @param      s   the accepted connection.
     * @exception  IOException  if an I/O error occurs when accepting the
     *               connection.
     */
    protected abstract void accept(SocketImpl s) throws IOException;

    /** {@collect.stats} 
     * {@description.open}
     * Returns an input stream for this socket.
     * {@description.close}
     *
     * @return     a stream for reading from this socket.
     * @exception  IOException  if an I/O error occurs when creating the
     *               input stream.
    */
    protected abstract InputStream getInputStream() throws IOException;

    /** {@collect.stats} 
     * {@description.open}
     * Returns an output stream for this socket.
     * {@description.close}
     *
     * @return     an output stream for writing to this socket.
     * @exception  IOException  if an I/O error occurs when creating the
     *               output stream.
     */
    protected abstract OutputStream getOutputStream() throws IOException;

    /** {@collect.stats} 
     * {@description.open}
     * Returns the number of bytes that can be read from this socket
     * without blocking.
     * {@description.close}
     *
     * @return     the number of bytes that can be read from this socket
     *             without blocking.
     * @exception  IOException  if an I/O error occurs when determining the
     *               number of bytes available.
     */
    protected abstract int available() throws IOException;

    /** {@collect.stats} 
     * {@description.open}
     * Closes this socket.
     * {@description.close}
     *
     * @exception  IOException  if an I/O error occurs when closing this socket.
     */
    protected abstract void close() throws IOException;

    /** {@collect.stats} 
     * {@description.open}
     * Places the input stream for this socket at "end of stream".
     * Any data sent to this socket is acknowledged and then
     * silently discarded.
     *
     * If you read from a socket input stream after invoking
     * shutdownInput() on the socket, the stream will return EOF.
     * {@description.close}
     *
     * @exception IOException if an I/O error occurs when shutting down this
     * socket.
     * @see java.net.Socket#shutdownOutput()
     * @see java.net.Socket#close()
     * @see java.net.Socket#setSoLinger(boolean, int)
     * @since 1.3
     */
    protected void shutdownInput() throws IOException {
      throw new IOException("Method not implemented!");
    }

    /** {@collect.stats} 
     * {@description.open}
     * Disables the output stream for this socket.
     * For a TCP socket, any previously written data will be sent
     * followed by TCP's normal connection termination sequence.
     * {@description.close}
     *
     * {@property.open}
     * If you write to a socket output stream after invoking
     * shutdownOutput() on the socket, the stream will throw
     * an IOException.
     * {@property.close}
     *
     * @exception IOException if an I/O error occurs when shutting down this
     * socket.
     * @see java.net.Socket#shutdownInput()
     * @see java.net.Socket#close()
     * @see java.net.Socket#setSoLinger(boolean, int)
     * @since 1.3
     */
    protected void shutdownOutput() throws IOException {
      throw new IOException("Method not implemented!");
    }

    /** {@collect.stats} 
     * {@description.open}
     * Returns the value of this socket's <code>fd</code> field.
     * {@description.close}
     *
     * @return  the value of this socket's <code>fd</code> field.
     * @see     java.net.SocketImpl#fd
     */
    protected FileDescriptor getFileDescriptor() {
        return fd;
    }

    /** {@collect.stats} 
     * {@description.open}
     * Returns the value of this socket's <code>address</code> field.
     * {@description.close}
     *
     * @return  the value of this socket's <code>address</code> field.
     * @see     java.net.SocketImpl#address
     */
    protected InetAddress getInetAddress() {
        return address;
    }

    /** {@collect.stats} 
     * {@description.open}
     * Returns the value of this socket's <code>port</code> field.
     * {@description.close}
     *
     * @return  the value of this socket's <code>port</code> field.
     * @see     java.net.SocketImpl#port
     */
    protected int getPort() {
        return port;
    }

    /** {@collect.stats} 
     * {@description.open}
     * Returns whether or not this SocketImpl supports sending
     * urgent data. By default, false is returned
     * unless the method is overridden in a sub-class
     * {@description.close}
     *
     * @return  true if urgent data supported
     * @see     java.net.SocketImpl#address
     * @since 1.4
     */
    protected boolean supportsUrgentData () {
        return false; // must be overridden in sub-class
    }

    /** {@collect.stats} 
     * {@description.open}
     * Send one byte of urgent data on the socket.
     * The byte to be sent is the low eight bits of the parameter
     * {@description.close}
     * @param data The byte of data to send
     * @exception IOException if there is an error
     *  sending the data.
     * @since 1.4
     */
    protected abstract void sendUrgentData (int data) throws IOException;

    /** {@collect.stats} 
     * {@description.open}
     * Returns the value of this socket's <code>localport</code> field.
     * {@description.close}
     *
     * @return  the value of this socket's <code>localport</code> field.
     * @see     java.net.SocketImpl#localport
     */
    protected int getLocalPort() {
        return localport;
    }

    void setSocket(Socket soc) {
        this.socket = soc;
    }

    Socket getSocket() {
        return socket;
    }

    void setServerSocket(ServerSocket soc) {
        this.serverSocket = soc;
    }

    ServerSocket getServerSocket() {
        return serverSocket;
    }

    /** {@collect.stats} 
     * {@description.open}
     * Returns the address and port of this socket as a <code>String</code>.
     * {@description.close}
     *
     * @return  a string representation of this socket.
     */
    public String toString() {
        return "Socket[addr=" + getInetAddress() +
            ",port=" + getPort() + ",localport=" + getLocalPort()  + "]";
    }

    void reset() throws IOException {
        address = null;
        port = 0;
        localport = 0;
    }

    /** {@collect.stats} 
     * {@description.open}
     * Sets performance preferences for this socket.
     *
     * <p> Sockets use the TCP/IP protocol by default.  Some implementations
     * may offer alternative protocols which have different performance
     * characteristics than TCP/IP.  This method allows the application to
     * express its own preferences as to how these tradeoffs should be made
     * when the implementation chooses from the available protocols.
     *
     * <p> Performance preferences are described by three integers
     * whose values indicate the relative importance of short connection time,
     * low latency, and high bandwidth.  The absolute values of the integers
     * are irrelevant; in order to choose a protocol the values are simply
     * compared, with larger values indicating stronger preferences. Negative
     * values represent a lower priority than positive values. If the
     * application prefers short connection time over both low latency and high
     * bandwidth, for example, then it could invoke this method with the values
     * <tt>(1, 0, 0)</tt>.  If the application prefers high bandwidth above low
     * latency, and low latency above short connection time, then it could
     * invoke this method with the values <tt>(0, 1, 2)</tt>.
     *
     * By default, this method does nothing, unless it is overridden in a
     * a sub-class.
     * {@description.close}
     *
     * @param  connectionTime
     *         An <tt>int</tt> expressing the relative importance of a short
     *         connection time
     *
     * @param  latency
     *         An <tt>int</tt> expressing the relative importance of low
     *         latency
     *
     * @param  bandwidth
     *         An <tt>int</tt> expressing the relative importance of high
     *         bandwidth
     *
     * @since 1.5
     */
    protected void setPerformancePreferences(int connectionTime,
                                          int latency,
                                          int bandwidth)
    {
        /* Not implemented yet */
    }
}
