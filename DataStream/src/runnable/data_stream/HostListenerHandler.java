package runnable.data_stream;

import chain.Chain;
import chain.data_stream.reject.RejectChain;
import chain.data_stream.resolve.ResolveChain;
import com.google.gson.JsonObject;
import runnable.HostSocketHandler;
import socket.Listener;
import socket.Socket;
import socket.SocketVerification;

import java.io.IOException;

public class HostListenerHandler extends runnable.ListenerHandler {
    public HostListenerHandler(Listener listener, int millisecond) {
        super(listener, millisecond);
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
    @Override
    protected SocketVerification getSocketVerification(Socket socket, Listener listener) {
        return () -> {
            // get the socket name
            // this step is where it might fail
            try {
                socket.setName(socket.read());
            } catch (IOException e) {
                System.err.println("Cannot read the name of the socket");
                return false;
            }
            //write back the id of the socket
            try {
                socket.write(String.valueOf(socket.hashCode()));
            } catch (IOException e) {
                System.err.println("Cannot write the id back to the socket");
                return false;
            }
            //store the socket into collection
            listener.putSocket(socket.getName(), socket);
            return true;
        };
    }

    /**
     * a runnable to handle socket function after the initial exchange of infomation
     *
     * @param socket socket that connect with the client side
     * @return an object that do the controlling the read / write / action of a socket
     */
    @Override
    protected HostSocketHandler getSocketHandler(Socket socket) {
        return new HostSocketHandler(socket) {
            @Override
            public Chain getResolveChain(JsonObject receivedObject) {
                return new ResolveChain(receivedObject);
            }

            @Override
            public Chain getRejectChain(JsonObject receivedObject) {
                return new RejectChain(receivedObject);
            }
        };
    }
}
