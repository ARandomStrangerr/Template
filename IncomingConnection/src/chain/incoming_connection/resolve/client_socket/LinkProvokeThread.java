package chain.incoming_connection.resolve.client_socket;

import chain.Link;
import com.google.gson.JsonObject;
import memorable.IncomingConnection;

/**
 * this class provoke a {@link chain.LinkWait} which is pausing and stored in {@link java.util.Hashtable}
 * within {@link IncomingConnection}
 */
class LinkProvokeThread extends Link<ResolveChain> {
    LinkProvokeThread(ResolveChain chain) {
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
        JsonObject header = chain.getProcessObject().get("header").getAsJsonObject();
        if (header.has("thread")) {
            try {
                IncomingConnection.getInstance().getTable().resume(chain.getProcessObject());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
            chain.resolveEarly();
        }
        return true;
    }
}
