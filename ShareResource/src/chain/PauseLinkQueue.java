package chain;

import com.google.gson.JsonObject;

import java.util.LinkedList;

/**
 * store {@link Link} in a linked list and queue it.
 *
 */
public class PauseLinkQueue extends PausedLinkStorage{
    private final LinkedList<Link> storage;
    public PauseLinkQueue(){
        storage = new LinkedList<>();
    }

    @Override
    public void pause(Link link) throws InterruptedException {
        synchronized (link){
            storage.addFirst(link);
            link.wait();
        }
    }

    @Override
    public void resume(JsonObject additionalInfo) throws NullPointerException, IllegalAccessException {
        throw new IllegalAccessException("This class does not support this type of method");
    }

    @Override
    public void resume() throws IllegalAccessException {
        synchronized (storage) {
            Link link = storage.removeLast();
            link.notify();
        }
    }

    public boolean isEmpty() {
        return storage.isEmpty();
    }
}
