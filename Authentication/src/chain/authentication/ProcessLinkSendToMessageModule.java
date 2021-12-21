package chain.authentication;

import chain.Link;
import com.google.gson.JsonObject;
import memorable.AuthenticationMemorable;

import java.io.IOException;

public class ProcessLinkSendToMessageModule extends Link<ProcessChain> {
    public ProcessLinkSendToMessageModule(ProcessChain chain) {
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
            AuthenticationMemorable.getOutgoingSocket().write(chain.getProcessObject().toString());
        } catch (IOException e) {
            System.err.println(AuthenticationMemorable.getName() + " - Cannot write back to the message module");
        }
        return true;
    }
}
