package chain;

import com.google.gson.JsonObject;

public abstract class PausedLinkStorage {
    public abstract void pause(Link link) throws InterruptedException;
    public abstract void resume(JsonObject additionalInfo)  throws NullPointerException, IllegalAccessException;
    public abstract void resume() throws IllegalAccessException;
}
