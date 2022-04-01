package chain;

import com.google.gson.JsonObject;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class Chain {
    private final JsonObject processObject;
    protected final List<Link> chain;
    private boolean resolveEarly;

    public Chain(JsonObject processObject) {
        chain = new LinkedList<>();
        this.processObject = processObject;
        this.resolveEarly = false;
        chainConstruction();
    }

    /**
     * resolve the processObject based on the given links
     *
     * @return true when the chain runs till the end
     * false when the chain broke midway
     */
    public final boolean resolve() {
        Iterator<Link> iterator = chain.iterator();
        while (iterator.hasNext() && !resolveEarly) {
            Link link = iterator.next();
            if (!link.resolve()) return false;
        }
        return true;
    }

    /**
     * @return the Json Object modifying
     */
    public final JsonObject getProcessObject() {
        return processObject;
    }

    /**
     * mark the chain to end early and return true without reaching to the end of its chain.
     */
    public final void resolveEarly(){
        resolveEarly = true;
    }

    protected abstract void chainConstruction();
}
