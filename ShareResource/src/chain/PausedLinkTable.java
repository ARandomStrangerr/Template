package chain;

import com.google.gson.JsonObject;

import java.util.Hashtable;

/**
 * store paused link storage in a hash table.
 */
public class PausedLinkTable extends PausedLinkStorage {
    private final Hashtable<Integer, Link> storage;

    public PausedLinkTable() {
        storage = new Hashtable<>();
    }

    @Override
    public void pause(Link link) throws InterruptedException {
        // store the link
        // pause the link at this point, set synchronized here to prevent the lock close here and nobody else can access the class.
        synchronized (link) {
            storage.put(link.hashCode(), link);
            link.wait();
        }
    }

    @Override
    public void resume(JsonObject additionalInfo) throws NullPointerException, IllegalAccessException {
        synchronized (storage) {
            // remove the link corresponding to the id then resume it. it might throw NullPointerException due to no such id does not exist
            Link link = storage.remove(additionalInfo.get("header").getAsJsonObject().get("thread").getAsInt());
            synchronized (link) {
                link.setAdditionalInfo(additionalInfo);
                link.notify();
            }
        }
    }

    @Override
    public void resume() throws NullPointerException, IllegalAccessException {
        throw new IllegalAccessException("this method is not supported for this class");
    }
}
