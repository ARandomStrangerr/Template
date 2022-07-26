package chain;

import com.google.gson.JsonObject;

import java.util.LinkedList;

/**
 * store {@link LinkWait} in a linked list and queue it.
 *
 */
public class PauseLinkQueue implements PausedLinkStorage{
    private final LinkedList<LinkWait> storage;
    public PauseLinkQueue(){
        storage = new LinkedList<>();
    }

    @Override
    public void pause(LinkWait link) throws InterruptedException {
        storage.addFirst(link);
        synchronized (link){
            link.wait();
        }
    }

    @Override
    public void resume(JsonObject additionalInfo) throws NullPointerException, IllegalAccessException {
        throw new IllegalAccessException("This class does not support this type of method");
    }

    @Override
    public void resume() throws IllegalAccessException {
        LinkWait link = storage.removeLast();
        synchronized (link) {
            link.notify();
        }
    }
}
