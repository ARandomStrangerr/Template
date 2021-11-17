package runnable;

import connection_and_storage.connection.listener.Listener;
import connection_and_storage.connection.socket.SocketInterface;

import java.io.IOException;

public abstract class HandleListenerRunnable<T extends SocketInterface> implements Runnable {
    private final Listener<T> listener;

    public HandleListenerRunnable(Listener<T> listener) {
        this.listener = listener;
    }

    public void run() {
        T socket;
        while (true) {
            // accept the socket
            try {
                socket = listener.accept();
            } catch (IOException e) { // happens when the server socket is closed
                e.printStackTrace();
                break;
            }
            // give the socket to a thread to handle
            new Thread(getHandleSocketRunnable(listener, socket)).start();
        }
        try {
            listener.close();
        } catch (IOException e) {
            System.err.println("fatal error");
            System.exit(1);
        }
    }

    protected abstract HandleIncomingSocketRunnable<T> getHandleSocketRunnable(Listener<T> listener, T socket);
}
