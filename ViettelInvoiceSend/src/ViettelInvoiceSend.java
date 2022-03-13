import chain.viettel_invoice_send.InitChain;

import java.net.InetAddress;

public class ViettelInvoiceSend {
    public static void main(String[] args) throws Exception {
        new InitChain("ViettelInvoiceSend",
                InetAddress.getByName("localhost"),
                9999)
                .resolve();
    }
}
