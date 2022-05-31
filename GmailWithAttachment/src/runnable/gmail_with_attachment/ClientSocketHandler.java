package runnable.gmail_with_attachment;

import chain.Chain;
import com.google.gson.JsonObject;
import memorable.GmailWithAttachment;
import socket.Socket;

public class ClientSocketHandler extends runnable.ClientSocketHandler {
    public ClientSocketHandler(Socket socket, String moduleName) {
        super(socket, moduleName);
    }

    /**
     * action to save the instance number to use later on message which will be sent to the DataStream
     *
     * @param instNumb assigned identification number given by DataStream
     */
    @Override
    protected void saveInstanceNumber(int instNumb) {
        GmailWithAttachment.getInstance().moduleId = instNumb;
    }

    /**
     * the resolve chain to process the request
     *
     * @param processObject received Object
     * @return boolean value represent that the chain is successfully resolved or not
     */
    @Override
    protected Chain getResolveChain(JsonObject processObject) {
        return null;
    }

    /**
     * if the resolve chain is fail to process the request, this chain will be run
     *
     * @param processObject received object and half processed by resolve chain
     * @return boolean value represent that the chain is successfully resolved or not
     */
    @Override
    protected Chain getRejectChain(JsonObject processObject) {
        return null;
    }
}
