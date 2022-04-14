import chain.viettel_invoice_send.init.InitChain;

import java.net.InetAddress;

public class ViettelInvoiceSend {
    public static void main(String[] args) throws Exception {
        new InitChain(InetAddress.getByName("localhost"),
                "ViettelInvoiceSend",
                9999)
                .resolve();
    }
}
