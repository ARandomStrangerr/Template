package chain;

import com.google.gson.JsonObject;

import java.util.LinkedList;
import java.util.List;

public abstract class Chain {
    private final JsonObject processObject;
    protected final List<Link> chain;

    public Chain(JsonObject processObject) {
        chain = new LinkedList<>();
        this.processObject = processObject;
        chainConstruction();
    }

    /**
     * resolve the processObject based on the given links
     *
     * @return true when the chain runs till the end
     * false when the chain broke midway
     */
    public final boolean resolve() {
        for (Link link : chain) {
            if (!link.resolve()) return false;
        }
        return true;
    }

    /**
     * @return get a deep copy of the chain
     */
    public final JsonObject getProcessObject() {
        return processObject;
    }

    protected abstract void chainConstruction();
}
