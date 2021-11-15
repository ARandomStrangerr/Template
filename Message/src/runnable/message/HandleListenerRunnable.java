package runnable.message;

import connection_and_storage.connection.listener.Listener;
import connection_and_storage.connection.socket.PlainSocket;
import runnable.HandleIncomingSocketRunnable;

public class HandleListenerRunnable extends runnable.HandleListenerRunnable<PlainSocket> {
    public HandleListenerRunnable(Listener<PlainSocket> listener) {
        super(listener);
    }

    @Override
    protected HandleIncomingSocketRunnable getHandleSocketRunnable(Listener<PlainSocket> listener, PlainSocket socket) {
        return new runnable.message.HandleIncomingSocketRunnable(listener, socket);
    }
}
