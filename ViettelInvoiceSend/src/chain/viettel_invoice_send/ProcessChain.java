package chain.viettel_invoice_send;

import chain.Chain;
import com.google.gson.JsonObject;

import java.io.File;

public class ProcessChain extends Chain {
    private File excelFile;
    public ProcessChain(JsonObject processObject) {
        super(processObject);
    }

    @Override
    protected void chainConstruction() {
        super.chain.add(new ProcessLinkMaterializeExcelFile(this));
        super.chain.add(new ProcessLinkReadExcelFile(this));
        super.chain.add(new ProcessLinkSendInvoice(this));
    }

    public void setExcelFile(File excelFile) {
        this.excelFile = excelFile;
    }

    public File getExcelFile() {
        return this.excelFile;
    }
}
