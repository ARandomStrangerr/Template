package chain;

import com.google.gson.JsonObject;
import runnable.HandleOutgoingSocketRunnable;
import runnable.ThreadStorage;

public abstract class LinkObserver<T extends Chain> extends Link<T> {
    private final ThreadStorage storage;
    protected JsonObject additionalInfo;

    public LinkObserver(T chain,
                        ThreadStorage storage) {
        super(chain);
        this.storage = storage;
    }

    protected synchronized void synchronizedWait() throws InterruptedException {
        storage.put(Thread.currentThread().hashCode(), this);
        wait();
    }

    public synchronized void synchronizedNotify(JsonObject additionalInfo) throws InterruptedException {
        this.additionalInfo = additionalInfo;
        storage.remove(additionalInfo
                .get("header")
                .getAsJsonObject()
                .get("threadCode")
                .getAsInt()
        );
        notify();
    }
}
