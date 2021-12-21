package constant;

public enum ModuleName {
    AUTHENTICATION(""),
    GMAIL_SEND("Gmail"),
    MESSAGE(null),
    VIETTEL_INVOICE_SEND("Tạo hóa đơn"),
    VIETTEL_INVOICE_GET("Lấy hóa đơn"),
    VIETTEL_INVOICE_DELETE("Xóa hóa đơn");

    private final String displayName;
    ModuleName(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
