package chain.barcode_maker.init;

import chain.Link;
import memorable.BarcodeMaker;
import org.w3c.dom.events.UIEvent;
import socket.Socket;

import java.io.IOException;

public class LinkStartListener extends Link<InitChain> {
    public LinkStartListener(InitChain chain) {
        super(chain);
    }

    @Override
    protected boolean resolve() {
        Socket socket;
        try {
            socket = new Socket(chain.hostAddress, chain.hostPort);
        } catch (IOException e){
            System.err.println("Cannot open connection to the DataStream");
            e.printStackTrace();
            return false;
        }
        BarcodeMaker.getInstance().socket = socket;

        return false;
    }
}
