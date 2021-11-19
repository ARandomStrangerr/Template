package runnable;

import chain.Chain;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import connection_and_storage.connection.socket.SocketInterface;

import java.io.IOException;

public abstract class HandleOutgoingSocketRunnable<T extends SocketInterface> implements Runnable {
    private final T socket;

    public HandleOutgoingSocketRunnable(T socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // verify and do various thing with the socket before read loop. Wrong procedure might lead to rejection of the socket
        verification(socket);
        // loop to read the request receive from the socket
        Gson gson = new Gson();
        while (true) {
            String requestString;
            try {
                requestString = socket.read();
            } catch (IOException e) {
                System.err.println("Likely the socket is closed");
                e.printStackTrace();
                break;
            }
            new Thread(() -> {
                JsonObject jsonObject;
                try {
                    jsonObject = gson.fromJson(requestString, JsonObject.class);
                } catch (Exception e) {
                    System.err.println("Cannot convert to Json Object, skip the request");
                    e.printStackTrace();
                    return;
                }
                if (getProcessChain(jsonObject).resolve()) {
                    getResolveChain(jsonObject).resolve();
                } else {
                    getRejectChain(jsonObject).resolve();
                }
            }).start();
        }
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Fatal error, cannot close the socket after socket to main branch is broken");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public abstract void verification(T socket);

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
