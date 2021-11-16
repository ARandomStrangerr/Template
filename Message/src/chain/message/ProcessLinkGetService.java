package chain.message;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;

public class ProcessLinkGetService extends Link {
    public ProcessLinkGetService(Chain chain) {
        super(chain);
    }

    @Override
    protected boolean resolve() {
        JsonObject headerObj = chain.getProcessObject().get("header").getAsJsonObject();
        if (headerObj.has("decreaseCounter") && headerObj.has("hashCode")){

        } else {

        }
        return false;
    }
}
