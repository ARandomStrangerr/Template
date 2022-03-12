package runnable;

import socket.Listener;
import socket.Socket;
import socket.SocketVerification;

import java.io.IOException;

/**
 * handle the listener, put the accept action into an endless loop.
 */
public abstract class ListenerHandler implements Runnable {
    private final Listener listener;
    private final int millisecond;

    /**
     * @param listener    the listener needs to be controlled
     * @param millisecond amount of time the newly accepted socket needed to declare its initial message
     */
    public ListenerHandler(Listener listener,
                           int millisecond) {
        this.listener = listener;
        this.millisecond = millisecond;
    }

    /**
     * a runnable to make the loop run infinitely
     */
    @Override
    public void run() {
        while (true) { // while true loop to accept incoming sockets
            Socket socket;
            try {
                socket = this.listener.accept(); // accept incoming sockets
            } catch (IOException e) {
                System.err.println("The listener is closed");
                e.printStackTrace();
                break;
            }
            Runnable runnable = () -> { // each accepted sockets will be handled by a thread
                try {
                    socket.setTimeout(millisecond);
                } catch (IOException e) {
                    System.err.println("Cannot set timeout");
                    e.printStackTrace();
                }
                if (getSocketVerification(socket, this.listener).run()) { // do the verification steps like getting socket name, store the socket
                    try { // remove the timeout of the socket
                        socket.setTimeout(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    getSocketHandler(socket).run(); // run the socket handler to put it into infinity while loop to read and write
                }
                // either the verification fail or the socket initiate close
                try { // close the socket to prevent memory leak
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try { // remove the socket out from the listener collection
                    listener.removeSocket(socket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            new Thread(runnable).start();
        }
        try {
            listener.close(); // this code happens when the listener have trouble accepting socket, which is more likely the listener is closed.
        } catch (IOException e) {
            System.err.println("Cannot close listener");
        }
    }

    /**
     * initial socket verification:
     * get necessary information,
     * store it into listener storage for later retreat
     *
     * @param socket   the socket just got accepted
     * @param listener the listener accepted the given socket
     * @return an object do the listed above tasks
     */
    protected abstract SocketVerification getSocketVerification(Socket socket, Listener listener);

    /**
     * a runnable to handle socket function after the initial exchange of infomation
     *
     * @param socket socket that connect with the client side
     * @return an object that do the controlling the read / write / action of a socket
     */
    protected abstract SocketHandler getSocketHandler(Socket socket);
}
