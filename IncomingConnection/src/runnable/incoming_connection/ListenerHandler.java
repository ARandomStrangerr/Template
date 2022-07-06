package runnable.incoming_connection;

import chain.Chain;
import chain.incoming_connection.reject.listener.RejectChain;
import chain.incoming_connection.resolve.listener.ResolveChain;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import memorable.IncomingConnection;
import runnable.HostSocketHandler;
import socket.Listener;
import socket.Socket;
import socket.SocketVerification;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

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
                // if the package is null, the socket from the other side is closed.
                String pkg;
                try {
                    pkg = socket.read();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                if (pkg == null) return;
                // spawn thread to handle newly received package
                // convert the pkg from string to Json
                Gson gson;
                gson = new Gson();
                JsonObject jsonPkg;
                try {
                    jsonPkg = gson.fromJson(pkg, JsonObject.class);
                } catch (JsonSyntaxException e) {
                    System.err.println("Given information is not json");
                    e.printStackTrace();
                    try {
                        socket.write("{\"error\":\"incorrect json object format\"}");
                        socket.close();
                        System.out.printf("Module disconnected from the network %s - %d\n", socket.getName(), socket.hashCode());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    return;
                }
                // set socket name
                String clientId;
                try {
                    clientId = jsonPkg.get("clientId").getAsString();
                } catch (NullPointerException e) {
                    System.err.println("Where is your clientId?");
                    e.printStackTrace();
                    try {
                        socket.write("{\"error\":\"missing clientId\"}");
                        socket.close();
                        System.out.printf("Module disconnected from the network %s - %d\n", socket.getName(), socket.hashCode());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    return;
                }
                socket.setName(clientId);
                // store socket
                try {
                    IncomingConnection.getInstance().getListener().putSocket(socket.getName(), socket);
                } catch (IllegalArgumentException e){
                    System.err.printf("socket under the name %s is already connected to the net work, probably is under another job", socket.getName());
                    e.printStackTrace();
                    try{
                        socket.write("{\"error\":\"hiện tại phần mềm đang sử lí một công việc khác, vui lòng đợi\"}");
                        socket.close();
                        System.out.printf("Module disconnected from the network %s - %d\n", socket.getName(), socket.hashCode());
                    } catch (IOException e1){
                        e1.printStackTrace();
                    }
                    return;
                }
                // pass the json pkg into the designated process chain
                boolean isResolve;
                isResolve = getResolveChain(jsonPkg).resolve();
                if (!isResolve) {
                    jsonPkg.get("header").getAsJsonObject().addProperty("status", false);
                    jsonPkg.get("header").getAsJsonObject().addProperty("terminate", true);
                    getRejectChain(jsonPkg).resolve();
                }
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
