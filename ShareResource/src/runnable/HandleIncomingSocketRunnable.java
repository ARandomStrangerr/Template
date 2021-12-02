package runnable;

import chain.Chain;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import connection_and_storage.connection.listener.Listener;
import connection_and_storage.connection.socket.SocketInterface;

import java.io.IOException;

public abstract class HandleIncomingSocketRunnable<T extends SocketInterface> implements Runnable {
    private final T socket;
    private final Listener<T> listener;

    public HandleIncomingSocketRunnable(Listener<T> listener,
                                        T socket) {
        this.socket = socket;
        this.listener = listener;
    }

    @Override
    public void run() {
        // set time limit
        try {
            socket.setSoTimeout(5000);
        } catch (IOException e) {
            System.err.println("Cannot set timeout. The socket has been closed");
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        // verification step and set key of the socket
        try {
            if (!verificationAndSetKeySocket(socket)) { // fail to verify, throw an exception
                throw new SecurityException("incoming socket does not have the permission to connect");
            }
            // success to verify and set the key, keep going with the flow
        } catch (Exception e) {
            System.err.println("Trouble happened in the verification step");
            //print out the line to debug
            e.printStackTrace();
            //close the socket
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return;
        }
        //remove the time limit
        try {
            socket.setSoTimeout(0);
        } catch (IOException e) {
            System.err.println("Cannot remove timeout. Socket has been closed");
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        System.out.printf("%s - %d module is connected%n", socket.getKey(), socket.hashCode());
        // put the socket into the listener collection
        try {
            listener.put(socket.getKey(), socket);
        } catch (NullPointerException e) { // throw when the key of the socket has not been set
            System.err.println("The name of the socket has not been set");
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return;
        }
        // reading loop
        Gson gson = new Gson();
        while (true) {
            // read string data from socket
            String requestString;
            try {
                requestString = socket.read();
            } catch (IOException e) {
                System.err.println("Broken pipe");
                e.printStackTrace();
                break;
            }
            // give the string to a thread to handle
            new Thread(() -> {
                // convert the data into json object
                JsonObject requestJsonObject;
                try {
                    requestJsonObject = gson.fromJson(requestString, JsonObject.class);
                } catch (Exception e) {
                    System.err.println("Cannot convert to json format, skip the request");
                    return;
                }
                if (getProcessChain(requestJsonObject).resolve())
                    getResolveChain(requestJsonObject).resolve();
                else
                    getRejectChain(requestJsonObject).resolve();
            }).start();
        }
        // remove the socket from the listener which accepted it
        listener.remove(socket);

        // close the socket after done with reading the loop
        try {
            socket.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.printf("%s - %d module is disconnected%n", socket.getKey(), socket.hashCode());
    }

    /**
     * verify the socket
     *
     * @param socket the instance that being verified
     * @return true when the socket pass the verification
     * false when the socket does not pass the verification
     */
    protected abstract boolean verificationAndSetKeySocket(T socket);

    /**
     * chain which process the request
     *
     * @param request the request to process
     * @return a Chain object to run
     */
    protected abstract Chain getProcessChain(JsonObject request);

    /**
     * chain which runs after the request is successfully handled
     *
     * @param request the request to process
     * @return a Chain object to run
     */
    protected abstract Chain getResolveChain(JsonObject request);

    /**
     * chain which runs after the request is failed to handle
     *
     * @param request the request to process
     * @return a Chain object to run
     */
    protected abstract Chain getRejectChain(JsonObject request);
}
