package runnable;

import chain.Chain;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import socket.Socket;

import java.io.IOException;

public abstract class SocketHandler implements Runnable {
    private final Socket socket;

    public SocketHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
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

    /**
     * @return process do to when the process chain is successfully complete
     */
    public abstract Chain getResolveChain(JsonObject receivedObject);

    /**
     * @return process to do when the process chain is fail to complete
     */
    public abstract Chain getRejectChain(JsonObject receivedObject);

}
