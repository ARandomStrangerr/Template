package chain.message;

import chain.Link;
import com.google.gson.JsonObject;

public final class ProcessLinkDecreaseCounter extends Link<ProcessChain> {
    public ProcessLinkDecreaseCounter(ProcessChain chain) {
        super(chain);
    }

    @Override
    protected boolean resolve() {
        JsonObject headerObject = chain.getProcessObject().get("header").getAsJsonObject();
        if (headerObject.has("decreaseCounter") && headerObject.get("decreaseCounter").getAsBoolean())
            chain.getPlainSocket().decreaseCounter();

        return true;
    }
}
