package chain.viettel_invoice_send.resolve;

import chain.Chain;
import chain.viettel_invoice_send.LinkSendToDataStream;
import com.google.gson.JsonObject;

import java.io.File;

public class ResolveChain extends Chain {
    File excelFile;
    public ResolveChain(JsonObject processObject) {
        super(processObject);
    }

    @Override
    protected void chainConstruction() {
        chain.add(new LinkMaterializeExcelFile(this));
        chain.add(new LinkReadExcelFile(this));
        chain.add(new LinkSendInvoice(this));
        chain.add(new LinkSendToDataStream(this));
    }
}
