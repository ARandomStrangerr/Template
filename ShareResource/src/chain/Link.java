package chain;

public abstract class Link<T extends Chain> {
    protected final T chain;

    public Link(T chain) {
        this.chain = chain;
    }

    protected abstract boolean resolve();
}
