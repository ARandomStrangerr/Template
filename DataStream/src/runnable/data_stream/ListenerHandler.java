package runnable.data_stream;

import chain.Chain;
import chain.data_stream.reject.RejectChain;
import chain.data_stream.resolve.ResolveChain;
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
     * a runnable to handle socket function after the initial exchange of information
     *
     * @param socket socket that connect with the client side
     * @return an object that do the controlling the read / write / action of a socket
     */
    @Override
    protected HostSocketHandler getSocketHandler(Socket socket) {
        return new HostSocketHandler(socket) {
            @Override
            public void run(){
                while (true) { // while true loop to read message from socket
                    String receivedPacket;
                    try {
                        receivedPacket = socket.read(); // read message from socket
                    } catch (IOException e) {
                        System.err.println("Cannot read message from socket with hashcode " + socket.hashCode());
                        break;
                    }
                    if (receivedPacket == null) break; // socket will send NULL when the other side of the connection is closed
                    Runnable runnable;
                    runnable = () -> { // each incoming packet will be handled by a thread
                        Gson gson;
                        gson = new Gson();
                        JsonObject json = gson.fromJson(receivedPacket, JsonObject.class); // convert package to json format
                        // codes that definitely will do before executing variable chain of commands goes here
                        try {
                            boolean isDecrease = json.get("header").getAsJsonObject().remove("decrease").getAsBoolean();
                            if (isDecrease) {
                                socket.decreaseActiveRequest();
                            }
                        } catch (Exception e) {
                            System.err.printf("package from %s - %d does not contains decrease attribute, should have it\n", socket.getName(), socket.hashCode());
                        }
                        // access resolve chain of command
                        boolean isResolve = getResolveChain(json).resolve(); // resolve the request
                        if (!isResolve) getRejectChain(json).resolve(); // if fail to resolve
                    };
                    new Thread(runnable).start(); // start the runnable to handler the request
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
