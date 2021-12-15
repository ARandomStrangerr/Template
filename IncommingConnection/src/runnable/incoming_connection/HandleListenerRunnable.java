package runnable.incoming_connection;

import connection_and_storage.connection.listener.Listener;
import connection_and_storage.connection.socket.PlainSocket;
import runnable.HandleIncomingSocketRunnable;

// todo: later on, upgrade this to ssh socket
public class HandleListenerRunnable extends runnable.HandleListenerRunnable<PlainSocket> {
    public HandleListenerRunnable(Listener<PlainSocket> listener) {
        super(listener);
    }

    @Override
    protected HandleIncomingSocketRunnable<PlainSocket> getHandleSocketRunnable(Listener<PlainSocket> listener, PlainSocket socket) {
        return new runnable.incoming_connection.HandleIncomingSocketRunnable(listener, socket);
    }
}
