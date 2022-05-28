package chain.barcode_maker.init;

import chain.Link;
import memorable.BarcodeMaker;

public class LinkStoreVariable extends Link<InitChain> {
    public LinkStoreVariable(InitChain chain) {
        super(chain);
    }

    @Override
    protected boolean resolve() {
        BarcodeMaker.getInstance().moduleName = chain.moduleName;
        return true;
    }
}
