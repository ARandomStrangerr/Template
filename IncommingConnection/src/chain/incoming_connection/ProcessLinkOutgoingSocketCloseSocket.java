package chain.incoming_connection;

import chain.Link;
import connection_and_storage.connection.socket.PlainSocket;
import memorable.IncomingConnectionMemorable;

import java.io.IOException;

public class ProcessLinkOutgoingSocketCloseSocket extends Link<ProcessChainOutgoingSocket> {
    public ProcessLinkOutgoingSocketCloseSocket(ProcessChainOutgoingSocket chain) {
        super(chain);
    }

    /**
     * resolve the request within this chain
     *
     * @return <code>true</code> when successfully run this block code
     * <code>false</code> when unsuccessfully run this block code
     */
    @Override
    protected boolean resolve() {
        if (!chain.getProcessObject()
                .get("header")
                .getAsJsonObject()
                .get("status")
                .getAsBoolean()
        ) {
            PlainSocket socket = IncomingConnectionMemorable
                    .getListener()
                    .get(
                            chain.getProcessObject().get("header")
                            .getAsJsonObject()
                            .get("clientIdentity")
                            .getAsString()
                    );
            try {
                socket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
            IncomingConnectionMemorable.getListener().remove(socket);
        }
        return true;
    }
}
