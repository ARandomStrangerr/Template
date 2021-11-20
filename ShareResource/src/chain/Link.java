package chain;

import com.google.gson.JsonObject;

public abstract class Link<T extends Chain> {
    protected final T chain;

    public Link(T chain) {
        this.chain = chain;
    }

    /**
     * resolve the request within this chain
     *
     * @return <code>true</code> when successfully run this block code
     * <code>false</code> when unsuccessfully run this block code
     */
    protected abstract boolean resolve();
}
