package chain.data_stream.resolve;

import chain.Chain;
import com.google.gson.JsonObject;

public class ResolveChain extends Chain {
    public ResolveChain(JsonObject processObject) {
        super(processObject);
    }

    @Override
    protected void chainConstruction() {
        super.chain.add(new SendPackage(this));
    }
}
