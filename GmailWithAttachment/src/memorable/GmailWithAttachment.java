package memorable;

import socket.Socket;

public class GmailWithAttachment {
    private static GmailWithAttachment cache;

    public static GmailWithAttachment getInstance() {
        if (cache == null) cache = new GmailWithAttachment();
        return cache;
    }

    public Socket socketToDataStream;
    public int moduleId;
    public String moduleName,
            clientId,
            clientSecret;

    private GmailWithAttachment() {
    }
}
