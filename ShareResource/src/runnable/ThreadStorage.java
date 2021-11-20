package runnable;

import chain.LinkObserver;

import java.util.Hashtable;

public class ThreadStorage {
    private final Hashtable<Integer, LinkObserver> storageStructure;

    public ThreadStorage (){
        storageStructure = new Hashtable<>();
    }

    public void put(int identifier, LinkObserver pausedLink){
        storageStructure.put(identifier, pausedLink);
    }

    public LinkObserver get(int identifier){
        return storageStructure.get(identifier);
    }
}
