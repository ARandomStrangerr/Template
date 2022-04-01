package chain;

import com.google.gson.JsonObject;

import java.util.Hashtable;

/**
 * Type of link which is able to pause and request additional information from another module during its execution.
 *
 * @param <T>
 */
public abstract class LinkWait<T extends Chain> extends Link<T> {
    private final Hashtable<Integer, LinkWait> threadTable;
    private JsonObject additionalInfo;

    public LinkWait(T chain, Hashtable<Integer, LinkWait> threadTable) {
        super(chain);
        this.threadTable = threadTable;
    }

    protected synchronized void waitSync() throws InterruptedException {
        // add current link into the collection to resume later
        threadTable.put(Thread.currentThread().hashCode(), this);
        // pause the current thread
        wait();
    }

    public synchronized void notifySync(JsonObject additionalInfo) throws InterruptedException {
        // get the thread number, not "current thread" because this method will be accessed by other thread than the current paused one.
        int threadHashcode = additionalInfo.get("header").getAsJsonObject().get("thread").getAsInt();
        // remove current thread from the collection
        threadTable.remove(threadHashcode);
        // set additional info object for access
        this.additionalInfo = additionalInfo;
        // notify / wake up the thread
        notify();
    }

    public JsonObject getAdditionalInfo() {
        return additionalInfo;
    }
}
