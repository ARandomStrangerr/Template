package chain;

import com.google.gson.JsonObject;
import runnable.HandleOutgoingSocketRunnable;

public abstract class LinkObserver<T extends Chain> extends Link<T>{
    protected JsonObject additionalInfo;

    public LinkObserver(T chain) {
        super(chain);
    }

    protected synchronized void synchronizedWait() throws InterruptedException{
        getOutgoingSocket().
        wait();
    }

    protected abstract HandleOutgoingSocketRunnable getOutgoingSocket();

    public synchronized void synchronizedNotify(JsonObject additionalInfo) throws InterruptedException{
        this.additionalInfo = additionalInfo;
        notify();
    }
}
