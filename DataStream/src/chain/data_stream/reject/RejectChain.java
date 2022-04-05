package chain.data_stream.reject;

import chain.Chain;
import com.google.gson.JsonObject;

public class RejectChain extends Chain {
    public RejectChain(JsonObject processObject) {
        super(processObject);
    }

    @Override
    protected void chainConstruction() {
        chain.add(new SendPackage(this));
    }
}
