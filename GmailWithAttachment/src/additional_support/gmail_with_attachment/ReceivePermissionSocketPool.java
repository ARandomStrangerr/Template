package additional_support.gmail_with_attachment;

import chain.LinkWait;
import socket.Socket;

import java.util.LinkedList;

/**
 * wrapper and automation class. the primary job of sockets stores by this class is to listen to Gmail auth. when a
 * socket is requested, it will be pulled out from a LIFO structure. <b>REMEMBER TO RETURN THE SOCKET AFTER FINISHED
 * USING IT</b>. in case a thread request a socket when there is none available, the thread will be put to sleep and
 * storage on a FIFO query structure. the front stored thread will be notified once a socket is returned. due to methods
 * of this class will be called across various threads, it is designed with synchronization
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
     * @param caller the link called this method, in case none available, we have the reference to stop and store it
     * @return socket to listen to Google auth
     */
    public synchronized Socket get(LinkWait caller){
        if (socketPort.size() != 0){ // case when there is avai
            
        }
    }

    /**
     * return a socket after the job is done
     * @param returnSocket the socket to return
     */
    public void put(Socket returnSocket){
        if (linkStorage.size() != 0) {

        } else {

        }
    }
}
