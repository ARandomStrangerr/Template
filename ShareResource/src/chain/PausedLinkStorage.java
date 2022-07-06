package chain;

import com.google.gson.JsonObject;

public interface PausedLinkStorage {
    void pause(LinkWait link) throws InterruptedException;
    void resume(JsonObject additionalInfo)  throws NullPointerException, IllegalAccessException;
    void resume() throws IllegalAccessException;
}
