package runnable.data_stream;

import chain.Chain;
import chain.incoming_connection.reject.listener.RejectChain;
import chain.incoming_connection.resolve.listener.ResolveChain;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import memorable.IncomingConnection;
import runnable.HostSocketHandler;
import socket.Listener;
import socket.Socket;
import socket.SocketVerification;

import java.io.IOException;

public class ListenerHandler extends runnable.ListenerHandler {
    public ListenerHandler(Listener listener, int millisecond) {
        super(listener, millisecond, false);
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
        /*
        there is no check up, just receive the socket, see the package.
        the save socket and name of the socket will be done at socket handling level.
        */
        return () -> true;
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
            public void run() {
                // receive package from socket
                String pkg;
                try {
                    pkg = socket.read();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                // if the package is null, the socket from the other side is closed.
                if (pkg == null) return;
                // spawn thread to handle newly received package
                // convert the pkg from string to Json
                Gson gson;
                gson = new Gson();
                JsonObject jsonPkg;
                jsonPkg = gson.fromJson(pkg, JsonObject.class);
                // set socket name
                socket.setName(jsonPkg.get("clientId").getAsString());
                // store socket
                IncomingConnection.getInstance().getListener().putSocket(socket.getName(), socket);
                // pass the json pkg into the designated process chain
                boolean isResolve;
                isResolve = getResolveChain(jsonPkg).resolve();
                if (!isResolve) getRejectChain(jsonPkg).resolve();
            }

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
