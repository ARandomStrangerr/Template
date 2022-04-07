package chain.viettel_invoice_send;

import chain.Chain;
import chain.viettel_invoice_send.resolve.LinkMaterializeExcelFile;
import chain.viettel_invoice_send.resolve.LinkReadExcelFile;
import chain.viettel_invoice_send.resolve.LinkSendInvoice;
import com.google.gson.JsonObject;

import java.io.File;

public class ProcessChain extends Chain {
    private File excelFile;
    public ProcessChain(JsonObject processObject) {
        super(processObject);
    }

    @Override
    protected void chainConstruction() {
        super.chain.add(new LinkMaterializeExcelFile(this));
        super.chain.add(new LinkReadExcelFile(this));
        super.chain.add(new LinkSendInvoice(this));
        super.chain.add(new ProcessLinkCreateResponse(this));
        super.chain.add(new LinkCleanupJsonObject(this));
        super.chain.add(new LinkSendToMessageModule(this));
    }

    public void setExcelFile(File excelFile) {
        this.excelFile = excelFile;
    }

    public File getExcelFile() {
        return this.excelFile;
    }
}
