package chain;

import com.google.gson.JsonObject;

import java.util.Hashtable;

/**
 * store paused link storage in a hash table.
 */
public class PausedLinkTable implements PausedLinkStorage {
    private final Hashtable<Integer, LinkWait> storage;

    public PausedLinkTable() {
        storage = new Hashtable<>();
    }

    @Override
    public void pause(LinkWait link) throws InterruptedException {
        // store the link
        storage.put(link.hashCode(), link);
        // pause the link at this point
        synchronized (link){
            link.wait();
        }
    }

    @Override
    public void resume(JsonObject additionalInfo) throws NullPointerException, IllegalAccessException {
        // remove the link corresponding to the id then resume it. it might throw NullPointerException due to no such id does not exist
        LinkWait link = storage.remove(additionalInfo.get("header").getAsJsonObject().get("thread").getAsInt());
        link.setAdditionalInfo(additionalInfo);
        synchronized (link){
            link.notify();
        }
    }

    @Override
    public void resume() throws NullPointerException, IllegalAccessException {
        throw new IllegalAccessException("this method is not supported for this class");
    }
}
