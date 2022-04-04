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
    private final boolean autoSocketTermination;

    /**
     * @param listener              the listener which is needed to be controlled
     * @param millisecond           amount of time the newly accepted socket needed to declare its initial message
     * @param autoSocketTermination automatically close the socket accepted by this listener when done with the given
     *                              {@link HostSocketHandler}
     */
    public ListenerHandler(Listener listener,
                           int millisecond,
                           boolean autoSocketTermination) {
        this.listener = listener;
        this.millisecond = millisecond;
        this.autoSocketTermination = autoSocketTermination;
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
                // set the timer
                try {
                    socket.setTimeout(millisecond);
                } catch (IOException e) {
                    System.err.println("Cannot set timeout");
                    e.printStackTrace();
                }
                // do the verification steps like getting socket name, store the socket
                if (getSocketVerification(socket, this.listener).run()) { // if the verification is a success
                    // remove the timeout of the socket
                    try {
                        socket.setTimeout(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // notify the system that a socket is successfully established
                    System.out.printf("Module connected to the network %s - %d\n", socket.getName(), socket.hashCode());
                    // run the socket handler to put it into infinity while loop to read and write (not spawning a thread, it is a continuance of this one)
                    try {
                        getSocketHandler(socket).run();
                    } catch (Exception ignore) {
                    }
                }
                // either the verification fail or the read loop is complete
                // close the socket to prevent memory leak
                if (autoSocketTermination) {
                    try {
                        socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // remove the socket out from the listener collection
                    try {
                        listener.removeSocket(socket);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // notify the system that a socket is logged out
                    System.out.printf("Module disconnected from the network %s - %d\n", socket.getName(), socket.hashCode());
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
    protected abstract HostSocketHandler getSocketHandler(Socket socket);
}
