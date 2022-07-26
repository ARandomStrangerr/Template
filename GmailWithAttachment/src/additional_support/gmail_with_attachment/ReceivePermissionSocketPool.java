package additional_support.gmail_with_attachment;

import chain.LinkWait;

import java.util.LinkedList;

/**
 * wrapper and automation class. the primary job of <{@link java.net.ServerSocket}> which is stored by this class is to listen to Gmail auth. when a
 * socket is requested, it will be pulled out from a FIFO structure. <b>REMEMBER TO RETURN THE SOCKET AFTER FINISHED
 * USING IT</b>. in case a thread request a socket when there is none available, the thread will be put to sleep and
 * storage on a FIFO query structure. the front stored thread will be notified once a socket is returned. due to methods
 * of this class will be called across various threads, it is designed with synchronization.<br>
 * To ensure absolute order, this class can only be accessed one at a time. When a thread try to obtain a
 * socket, and the number of available socket = 0, that thread will be put into sleep. it will be awake once
 * another thread return a socket.
 * Dilemma: so there is a monitor to govern over what accept on the socket, what if when a thread provoke this class
 * during the running of current thread? the order of monitor will be:
 * currentThread(running in the put() method), newThread(at 2nd position waited to be executed after currentThread),
 * provokedThread(at 3rd position).
 * This make the newThread cuts line. we want the provokedThread to get the socket first. Possible solution: we cannot
 * reorder the monitor; however, we can ensure that newThread obtain socket after provokedThread by the following mean:
 * create a custom queue when a thread try to obtain a socket, if the queue is not empty, the thread has to queue.
 */
public class ReceivePermissionSocketPool {
    private final LinkedList<Integer> socketPort; // structure storing the socket receive permission from OAuth2.0 of Google
    private final LinkedList<LinkWait> linkStorage; // structure storing the Link that on hold due to none of storage available

    public ReceivePermissionSocketPool() {
        socketPort = new LinkedList<>();
        linkStorage = new LinkedList<>();
    }

    /**
     * get a socket ket to listen to the response of google
     *
     * @param caller the link called this method, in case none available, we have the reference to stop and store it
     * @return socket to listen to Google auth
     */
    public synchronized Socket get(LinkWait caller) {
        if (linkStorage.size() != 0 || socketPort.size() == 0) { // case when there is a socket queue ahead or no available socket
            linkStorage.addLast(caller); // add the thread into collection
            caller.waitSync(); // put thread into sleep
            linkStorage.removeFirst(); // at this point thread is active again, and it is on the front of the list; therefore, remove first
        }
        ServerSocket
    }

    public synchronized void put(LinkWait caller, Socket returnSocket) {
        if (linkStorage.size() != 0) {

        } else {

        }
    }
}
