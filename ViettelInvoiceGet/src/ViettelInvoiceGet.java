import chain.viettel_invoice_get.InitChain;

import java.net.InetAddress;

public class ViettelInvoiceGet {
    public static void main(String[] args) throws Exception{
        new InitChain("ViettelInvoiceGet",
                InetAddress.getByName("localhost"),
                1998)
                .resolve();
    }
}
