import com.google.gson.JsonObject;

public class ViettelInvoiceSend {
    public static void main(String[] args) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("prop1", "bacd");
        jsonObject.get("prop1").getAsInt();
    }
}
