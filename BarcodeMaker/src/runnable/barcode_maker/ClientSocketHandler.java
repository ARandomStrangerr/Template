package runnable.barcode_maker;

import chain.Chain;
import chain.barcode_maker.reject.RejectChain;
import chain.barcode_maker.resolve.ResolveChain;
import memorable.BarcodeMaker;

public class ClientSocketHandler extends runnable.ClientSocketHandler {
    @Override
    protected void saveInstanceNumber(int instNumb) {
        BarcodeMaker.getInstance().id = instNumb;
    }

    @Override
    protected Chain getResolveChain(JsonObject processObject) {
        return new ResolveChain(processObject);
    }

    @Override
    protected Chain getRejectChain(JsonObject processObject) {
        return new RejectChain(processObject);
    }
}
