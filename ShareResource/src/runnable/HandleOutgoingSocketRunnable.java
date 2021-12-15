package runnable;

import chain.Chain;
import chain.LinkObserver;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import connection_and_storage.connection.socket.SocketInterface;

import java.io.IOException;

public abstract class HandleOutgoingSocketRunnable<T extends SocketInterface> implements Runnable {
    protected final T socket;
    protected final ThreadStorage storage;

    public HandleOutgoingSocketRunnable(T socket) {
        this.socket = socket;
        this.storage = new ThreadStorage();
    }

    @Override
    public void run() {
        // verify and do various thing with the socket before read loop. Wrong procedure might lead to rejection of the socket
        if (!verification(socket)){
            System.err.println(getModuleName() + " - fail to verify outgoing socket");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        // loop to read the request receive from the socket
        Gson gson = new Gson();
        while (true) {
            String requestString;
            try {
                requestString = socket.read();
            } catch (IOException e) {
                System.err.println(getModuleName() + " - Likely the other-side socket is closed");
                e.printStackTrace();
                break;
            }
            Runnable runnable = () -> {
                // convert the message to json object
                JsonObject jsonObject;
                try {
                    jsonObject = gson.fromJson(requestString, JsonObject.class);
                } catch (Exception e) {
                    System.err.println(getModuleName() + " - Cannot convert to Json Object, skip the request");
                    e.printStackTrace();
                    return;
                }
                // get from module name of the incoming message
                String fromModuleName = jsonObject.get("header").getAsJsonObject().get("from").getAsString();
                if(fromModuleName.equals(getModuleName())) {
                    // treat the request as old request
                    int threadIdentifier = jsonObject.get("header").getAsJsonObject().get("thread").getAsInt();
                    LinkObserver linkObserver = storage.get(threadIdentifier);
                    try {
                        linkObserver.synchronizedNotify(jsonObject);
                    } catch (InterruptedException e) {
                        System.err.println(getModuleName() + " - cannot past the old request to the paused thread");
                        e.printStackTrace();
                    }
                } else {
                    // treat the request as new request
                    if (getProcessChain(jsonObject).resolve()) {
                        getResolveChain(jsonObject).resolve();
                    } else {
                        getRejectChain(jsonObject).resolve();
                    }
                }
            };
            new Thread(runnable, String.valueOf(runnable.hashCode())).start();
        }
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(getModuleName() + " - Fatal error, cannot close the socket after socket to main branch is broken");
            e.printStackTrace();
        }
        System.exit(1); // the outgoing socket does not work, proceed to close the program
    }

    /**
     * step to verify sockets and get necessary information back from the host
     *
     * @param socket socket which we are operating on
     * @return <code>True</code> when successfully verify
     * <code>false</code> when unsuccessfully verif
     */
    public abstract boolean verification(T socket);

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

    protected abstract String getModuleName();

    public void onHold(int identifier, LinkObserver link){
        storage.put(identifier, link);
    }
}
