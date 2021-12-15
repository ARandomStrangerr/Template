package chain.incoming_connection;

import chain.Link;
import memorable.IncomingConnectionMemorable;

import java.io.IOException;

public class ProcessLinkOutgoingSocketWriteToClient extends Link<ProcessChainOutgoingSocket> {
    public ProcessLinkOutgoingSocketWriteToClient(ProcessChainOutgoingSocket chain) {
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
        try {
            IncomingConnectionMemorable
                    .getListener()
                    .get(
                            chain.getProcessObject()
                                    .get("header")
                                    .getAsJsonObject()
                                    .get("clientIdentity")
                                    .getAsString()
                    )
                    .write(
                            chain.getProcessObject()
                                    .get("body")
                                    .toString()
                    );
        } catch (IOException e) {
            System.err.println(IncomingConnectionMemorable.getName() + " - Cannot write to the client");
            e.printStackTrace();
            chain.getProcessObject()
                    .get("header")
                    .getAsJsonObject()
                    .addProperty("status", false);
        }
        return true;
    }
}
