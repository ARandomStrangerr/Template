package chain;

import com.google.gson.JsonObject;

public abstract class PausedLinkStorage {
    abstract void pause(LinkWait link) throws InterruptedException;
    public abstract void resume(JsonObject additionalInfo)  throws NullPointerException, IllegalAccessException;
    public abstract void resume() throws IllegalAccessException;
}
