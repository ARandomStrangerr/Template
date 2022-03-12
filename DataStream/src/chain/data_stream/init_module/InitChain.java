package chain.data_stream.init_module;

import chain.Chain;

public class InitChain extends Chain {
    final int port;
    final int timeout;
    public InitChain(int port, int timeout) {
        super(null);
        this.port = port;
        this.timeout = timeout;
    }

    @Override
    protected void chainConstruction() {
        super.chain.add(new LinkStartListener(this));
    }
}
