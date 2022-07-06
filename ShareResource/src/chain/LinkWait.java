package chain;

import com.google.gson.JsonObject;

/**
 * Type of link which is able to pause and request additional information from another module during its execution.
 * This class will be retreated by name using a Hashtable.
 *
 * @param <T>
 */
public abstract class LinkWait<T extends Chain> extends Link<T> {
    private final PausedLinkStorage storage;
    private JsonObject additionalInfo;

    /**
     * this constructor is use for this class when get by name
     *
     * @param chain       the chain that call this class
     * @param storage the structure to store this link
     */
    public LinkWait(T chain, PausedLinkStorage storage) {
        super(chain);
        this.storage = storage;
    }

    /**
     * set this thread to sleep, store this thread in the given threadTable
     *
     * @throws InterruptedException   most likely nothing will go wrong with this exception
     * @throws IllegalCallerException when this method is called with the instance used the no threadTable constructor
     */
    public synchronized void waitSync() throws InterruptedException, IllegalCallerException {
        storage.pause(this);
    }

    protected JsonObject getAdditionalInfo() {
        return additionalInfo;
    }

    void setAdditionalInfo(JsonObject additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
