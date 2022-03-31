package runnable.data_stream;

import chain.Chain;
import chain.incoming_connection.resolve.listener.ResolveChain;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import runnable.HostSocketHandler;
import socket.Listener;
import socket.Socket;
import socket.SocketVerification;

import java.io.IOException;

public class ListenerHandler extends runnable.ListenerHandler {
    public ListenerHandler(Listener listener, int millisecond) {
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
            // read from the socket, this supposed to be mac address of the connected computer. todo - change to better type of authentication later
            String receivedString;
            try {
                receivedString = socket.read();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            // store the mac address
            socket.setName(receivedString);
            // store the socket into the listener collection
            listener.putSocket(receivedString, socket);
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
            public void run() {
                // while true loop to read data from the socket
                while (true) {
                    // receive package from socket
                    String pkg;
                    try {
                        pkg = socket.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                        continue;
                    }
                    // if the package is null, the socket from the other side is closed.
                    if (pkg == null) break;
                    // spawn thread to handle newly received package
                    Runnable handlePkg;
                    handlePkg = () -> {
                        // convert the pkg from string to Json
                        Gson gson;
                        gson = new Gson();
                        JsonObject jsonPkg;
                        jsonPkg = gson.fromJson(pkg, JsonObject.class);
                        // include name of the socket into the package;
                        jsonPkg.addProperty("clientId", socket.getName());
                        // pass the json pkg into the designated process chain
                        boolean isResolve;
                        isResolve = getResolveChain(jsonPkg).resolve();
                        if (!isResolve) getRejectChain(jsonPkg).resolve();
                    };
                    // start thread to handle
                    new Thread(handlePkg).start();
                }
            }

            @Override
            public Chain getResolveChain(JsonObject receivedObject) {
                return new ResolveChain(receivedObject);
            }

            @Override
            public Chain getRejectChain(JsonObject receivedObject) {
                return null;
            }
        };
    }
}
