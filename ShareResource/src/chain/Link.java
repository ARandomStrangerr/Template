package chain;

public abstract class Link {
    protected final Chain chain;

    public Link(Chain chain) {
        this.chain = chain;
    }

    protected abstract boolean resolve();
}
