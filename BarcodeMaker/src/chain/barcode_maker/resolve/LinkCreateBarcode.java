package chain.barcode_maker.resolve;

import chain.Link;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class LinkCreateBarcode extends Link<ResolveChain> {
    public LinkCreateBarcode(ResolveChain chain) {
        super(chain);
    }

    @Override
    protected boolean resolve() {
        JsonObject body;
        body = chain.getProcessObject().get("body").getAsJsonObject();
        // get necessary info to create barcode
        double dpi, printerWidth, stampHeight, stampWidth;
        JsonArray barcodeArr;
        try {
            dpi = body.remove("dpi").getAsDouble();
        } catch (NullPointerException e) {
            System.err.printf("Package from %s missing dpi object", chain.getProcessObject().get("header").getAsJsonObject().get("clientId").getAsJsonObject());
            e.printStackTrace();
            return false;
        }
        try {
            stampHeight = body.remove("stampHeight").getAsDouble();
        } catch (NullPointerException e) {
            System.err.printf("Package from %s missing stamp height object", chain.getProcessObject().get("header").getAsJsonObject().get("clientId").getAsJsonObject());
            e.printStackTrace();
            return false;
        }
        try {
            stampWidth = body.remove("stampWidth").getAsDouble();
        } catch (NullPointerException e) {
            System.err.printf("Package from %s missing stamp height object", chain.getProcessObject().get("header").getAsJsonObject().get("clientId").getAsJsonObject());
            e.printStackTrace();
            return false;
        }
        try {
            printerWidth = body.remove("printerWidth").getAsDouble();
        } catch (NullPointerException e) {
            System.err.printf("package from %s missing printer width object", chain.getProcessObject().get("header").getAsJsonObject().get("clientId").getAsString());
            e.printStackTrace();
            return false;
        }
        try{
            barcodeArr = body.remove("barcodeList").getAsJsonArray();
        } catch (NullPointerException e){
            System.err.printf("Package from %s is missing barcode list", chain.getProcessObject().get("header").getAsJsonObject().get("clientId").getAsString());
            e.printStackTrace();
            return false;
        }

        // calculate size of the 
        return true;
    }
}