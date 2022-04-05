import chain.viettel_invoice_get.init_module.InitChain;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ViettelInvoiceGet {
    public static void main(String[] args) throws Exception{
        new InitChain(InetAddress.getByName("localhost"),
                9999,
                "ViettelInvoiceGet")
                .resolve();
    }
}