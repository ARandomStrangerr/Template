import chain.viettel_invoice_send.init.InitChain;

import java.net.InetAddress;

public class ViettelInvoiceSend {
    public static void main(String[] args) throws Exception {
        InetAddress hostAddress = InetAddress.getByName(args[0]);
        int hostPort = Integer.parseInt(args[1]);
        String moduleName = args[2];
        new InitChain(InetAddress.getByName("localhost"),
                "ViettelInvoiceSend",
                9999)
                .resolve();
    }
}
