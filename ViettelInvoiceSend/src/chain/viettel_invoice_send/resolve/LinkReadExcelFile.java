package chain.viettel_invoice_send.resolve;

import chain.Link;
import chain.viettel_invoice_send.ProcessChain;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import file_operation.ExcelFile;
import memorable.MemorableViettelInvoiceSend;
import org.apache.poi.hssf.OldExcelFormatException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

public class LinkReadExcelFile extends Link<ProcessChain> {
    public LinkReadExcelFile(ProcessChain chain) {
        super(chain);
    }

    /**
     * read the Excel file
     *
     * @return <code>true</code> when successfully run this block code
     * <code>false</code> when unsuccessfully run this block code
     */
    @Override
    protected boolean resolve() {
        List<List<String>> rawInvoiceList;
        // read the Excel file
        try {
            rawInvoiceList = ExcelFile.getInstance().read(chain.getExcelFile());
        } catch (IOException e) {
            chain.getProcessObject().get("body").getAsJsonObject()
                    .addProperty("response", "Tệp tin Excel đính kèm không hoạt động, vui lòng xem lại");
            System.err.println("Cannot read the excel file, probably somebody else is reading it");
            e.printStackTrace();
            return false;
        } catch (OldExcelFormatException e){
            chain.getProcessObject().get("body").getAsJsonObject()
                    .addProperty("presponse", "Định dạng tệp tin Excel đính kèm quá cũ, vui lòng đổi sang ddingj dạng ới hơn và thử lại");
            System.err.println("Old format excel file");
            e.printStackTrace();
            return false;
        }
        // convert the Excel into useful format
        JsonObject rootObj,
                generalInvoiceObj,
                buyerObj,
                paymentObj,
                sellerObj,
                itemObj,
                summarizeObj;
        JsonArray paymentsArray,
                itemArray,
                taxBreakdownsArray,
                sendArray;
        LocalDate currentDate,
                biggestDate,
                invoiceDate;

        String tempStr;

        int lineNumberInt,
                tempInt,
                rowIndex;
        long tempLong,
                sumOfTotalLineAmountWithoutTaxLong,
                totalAmountWithoutTaxLong,
                totalTaxAmountLong;
        float tempFloat;
        //get the current date
        biggestDate = null;
        currentDate = LocalDate.now();
        //row index to tell which line has error at the first check
        rowIndex = 1;
        //create json array
        sendArray = new JsonArray();
        //create json for data
        try {
            for (List<String> invoice : rawInvoiceList) {
                if (invoice.size() == 0)
                    continue; //if the size of the row is = 0 -> the row is empty, Excel error and add empty row
                //generalInvoiceInfo
                generalInvoiceObj = new JsonObject();
                //invoice type
                tempStr = invoice.get(0).trim();
                //try to catch invalid invoice type parameter from the given Excel
                if (tempStr.equals("01GTKT") ||
                        tempStr.equals("02GTTT") ||
                        tempStr.equals("07KPTQ") ||
                        tempStr.equals("03XKNB") ||
                        tempStr.equals("04HGDL") ||
                        tempStr.equals("01BLP"))
                    generalInvoiceObj.addProperty("invoiceType",
                            tempStr);
                else {
                    chain.getProcessObject().get("body").getAsJsonObject()
                            .addProperty("response","Loại mã hoá đơn không chính xác ở dòng số " + rowIndex);
                    System.err.println(MemorableViettelInvoiceSend.getName() + " invalid invoice type at index " + rowIndex);
                    return false;
                }
                generalInvoiceObj.addProperty("invoiceType",
                        tempStr);
                //invoice series
                tempStr = invoice.get(1).trim();
                //try to catch invalid invoice series parameter from the given Excel
                if (Pattern.matches("[A-Z][A-Z]/[0-9][0-9][A-Z]", tempStr))
                    generalInvoiceObj.addProperty("invoiceSeries",
                            tempStr);
                else {
                    chain.getProcessObject().get("body").getAsJsonObject()
                            .addProperty("response", "Loại mã hoá đơn không chính xác " + rowIndex);
                    System.err.println(MemorableViettelInvoiceSend.getName() + " invalid invoice series at index " + rowIndex);
                    return false;
                }
                //templateCode
                // try to catch the invoice type number format
                try {
                    tempInt = Integer.parseInt(invoice.get(2).trim());
                } catch (NumberFormatException e) {
                    chain.getProcessObject().get("body").getAsJsonObject()
                            .addProperty("response", "Số mẫu kí hiệu hoá đơn không chính xác " + rowIndex);
                    System.err.println(MemorableViettelInvoiceSend.getName() + " invalid invoice type at index " + rowIndex);
                    return false;
                }
                tempStr = String.format("%s0/%03d",
                        generalInvoiceObj.get("invoiceType").getAsString(),
                        tempInt);
                generalInvoiceObj.addProperty("templateCode",
                        tempStr);
                //issue date
                tempStr = invoice.get(5).trim();
                if (tempStr.isEmpty()) {
                    invoiceDate = LocalDate.now();
                } else {
                    try {
                        invoiceDate = LocalDate.parse(tempStr, DateTimeFormatter.ofPattern("yyyy\\MM\\dd"));
                    } catch (DateTimeParseException e) {
                        chain.getProcessObject().get("body").getAsJsonObject()
                                .addProperty("response", "Định dạng ngày không chính xác ở dòng " + rowIndex);
                        e.printStackTrace();
                        return false;
                    }
                }
                if (biggestDate == null)
                    biggestDate = invoiceDate;
                if (invoiceDate.compareTo(biggestDate) < 0) {
                    chain.getProcessObject().get("body").getAsJsonObject()
                            .addProperty("response", "Ngày lập hóa đơn sau không được nhỏ hơn số hóa đơn trước ở dòng " + rowIndex);
                    return false;
                } else if (invoiceDate.compareTo(currentDate) > 0) {
                    chain.getProcessObject().get("body").getAsJsonObject()
                            .addProperty("response", "Ngày lập hóa đơn không được lớn hơn ngày hôm nay ở dòng " + rowIndex);
                    return false;
                }
                biggestDate = invoiceDate;
                generalInvoiceObj.addProperty("invoiceIssuedDate", invoiceDate.toString());
                //adjustment type
                tempInt = Integer.parseInt(invoice.get(3).trim());
                switch (tempInt) {
                    case 1:
                        generalInvoiceObj.addProperty("adjustmentType",
                                tempInt);
                        break;
                    case 3:
                    case 5:
                    case 7:
                        chain.getProcessObject().get("body").getAsJsonObject()
                                .addProperty("response", "Chức năng này chưa được hỗ trợ hoá đơn dòng số " + rowIndex);
                        System.err.println("not supported operation " + rowIndex);
                        return false;
                    default:
                        chain.getProcessObject().get("body").getAsJsonObject()
                                .addProperty("response", "loại hoá đơn không hợp lệ " + rowIndex);
                        System.err.println("adjustment type is incorrect " + rowIndex);
                        return false;
                }
                //currency code
                tempStr = invoice.get(7).trim();
                generalInvoiceObj.addProperty("currencyCode",
                        tempStr);
                //payment status
                generalInvoiceObj.addProperty("paymentStatus",
                        true);
                //customer able to invoice or not
                generalInvoiceObj.addProperty("cusGetInvoiceRight",
                        true);

                //buyerInfo
                buyerObj = new JsonObject();
                //name of the buyer
                tempStr = invoice.get(6).trim();
                if (!tempStr.isBlank())
                    buyerObj.addProperty("buyerName",
                            tempStr);
                //buyer code
                tempStr = invoice.get(11).trim();
                if (!tempStr.isBlank())
                    buyerObj.addProperty("buyerCode",
                            tempStr);
                //name of the company of the buyer
                tempStr = invoice.get(12).trim();
                if (!tempStr.isBlank())
                    buyerObj.addProperty("buyerLegalName",
                            tempStr);
                //tax code of the company
                tempStr = invoice.get(13).trim();
                if (!tempStr.isBlank())
                    buyerObj.addProperty("buyerTaxCode",
                            tempStr);
                //check if missing info
                if (!buyerObj.has("buyerName") && !buyerObj.has("buyerLegalName")) {
                    chain.getProcessObject().get("body").getAsJsonObject()
                            .addProperty("response", "Tên cá nhân và tên đoàn thể không được cùng bỏ trống ở dòng số " + rowIndex);
                    System.err.println(MemorableViettelInvoiceSend.getName() + " person name and company name cannot be blank at the same time " + rowIndex);
                    return false;
                }
                //address of the buyer
                tempStr = invoice.get(14).trim();
                if (!tempStr.isBlank())
                    buyerObj.addProperty("buyerAddressLine",
                            tempStr);
                else {
                    chain.getProcessObject().get("body").getAsJsonObject()
                            .addProperty("response", "Địa chỉ nhận hoá đơn bị bỏ trống ở dòng số " + rowIndex);
                    System.err.println(MemorableViettelInvoiceSend.getName() + " address is blank " + rowIndex);
                    return false;
                }

                //seller info
                sellerObj = new JsonObject();

                //payments
                paymentsArray = new JsonArray();
                paymentObj = new JsonObject();
                tempStr = invoice.get(16).trim(); //payment method
                paymentObj.addProperty("paymentMethodName",
                        tempStr);
                paymentsArray.add(paymentObj);

                //item info
                /*
                quantity
                price
                total money = quantity * price
                money after discount = total money * ( 1 - discount 1 / 100 )
                money after discount = total money * ( 1 - discount 2 / 100 )
                tax amount = money after discount * tax rate
                 */
                itemArray = new JsonArray();
                lineNumberInt = 1;
                sumOfTotalLineAmountWithoutTaxLong = 0;
                totalAmountWithoutTaxLong = 0;
                totalTaxAmountLong = 0;
                for (int index = 18; index < invoice.size(); index++) {
                    itemObj = new JsonObject();
                    String[] arr = invoice.get(index).split(";");
                    if (arr.length == 1) break;
                    //selection
                    try {
                        tempInt = Integer.parseInt(arr[0].trim());
                    } catch (NumberFormatException e) {
                        chain.getProcessObject().get("body").getAsJsonObject()
                                .addProperty("response", "Đánh dấu loại hàng hoá - dịch vụ không phải chữ số ở dòng số " + rowIndex);
                        e.printStackTrace();
                        return false;
                    }
                    itemObj.addProperty("selection",
                            tempInt);
                    switch (tempInt) {
                        case 3:
                            //is decrease
                            tempStr = arr[5].trim().toLowerCase();

                            if (tempStr.contains("true")) { //
                                chain.getProcessObject().get("body").getAsJsonObject()
                                        .addProperty("response", "Biểu thị tiền hóa đơn là số âm sai khi dòng sản phẩm là chiết khấu\n" +
                                        "ở dòng số: " + rowIndex);
                            } else if (tempStr.contains("false")) {
                                itemObj.addProperty("isIncreaseItem",
                                        false);
                            } else {
                                chain.getProcessObject().get("body").getAsJsonObject()
                                        .addProperty("response", "Giá trị điều chỉnh tăng / giảm không chính xác ở dòng số " + rowIndex);
                                return false;
                            }
                        case 1:
                            //line Number
                            itemObj.addProperty("lineNumber",
                                    lineNumberInt);
                            //item name
                            tempStr = arr[1].trim();
                            if (tempStr.isBlank()) {
                                chain.getProcessObject().get("body").getAsJsonObject()
                                        .addProperty("response", "Tên hàng hoá - dịch vụ không đươc bỏ trống ở dòng số " + rowIndex);
                                return false;
                            }
                            itemObj.addProperty("itemName",
                                    tempStr);
                            //unit name
                            tempStr = arr[2].trim();
                            if (tempStr.isBlank()) {
                                chain.getProcessObject().get("body").getAsJsonObject()
                                        .addProperty("response", "Tên đơn vị tính không được bỏ trống ở dòng số " + rowIndex);
                                return false;
                            }
                            itemObj.addProperty("unitName",
                                    tempStr);
                            //quantity
                            try {
                                tempInt = Integer.parseInt(arr[3].trim());
                            } catch (NumberFormatException e) {
                                chain.getProcessObject().get("body").getAsJsonObject()
                                        .addProperty("response", "Số lượng không hợp lệ ở dòng số " + rowIndex);
                                e.printStackTrace();
                                return false;
                            }
                            if (tempInt < 1) {
                                chain.getProcessObject().get("body").getAsJsonObject()
                                        .addProperty("response", "Số lượng không được nhỏ hơn 1 ở dòng số " + rowIndex);
                                return false;
                            }
                            itemObj.addProperty("quantity",
                                    tempInt);
                            //unit price
                            try {
                                tempLong = Long.parseLong(arr[4].trim());
                            } catch (NumberFormatException e) {
                                chain.getProcessObject().get("body").getAsJsonObject()
                                        .addProperty("response", "Đơn giá không hợp lệ ở dòng số " + rowIndex);
                                e.printStackTrace();
                                return false;
                            }
                            if (tempLong < 0) {
                                chain.getProcessObject().get("body").getAsJsonObject()
                                        .addProperty("response", "Đơn giá không được có giá trị âm ở dòng số " + rowIndex);
                                return false;
                            }
                            itemObj.addProperty("unitPrice",
                                    tempLong);
                            //total without tax
                            tempLong = itemObj.get("quantity").getAsInt() * itemObj.get("unitPrice").getAsLong();
                            itemObj.addProperty("itemTotalAmountWithoutTax",
                                    tempLong);
                            //total after discount without tax
                            itemObj.addProperty("itemTotalAmountAfterDiscount",
                                    tempLong);

                            //discount 1
                            try {
                                tempFloat = Float.parseFloat(arr[6].trim());
                                if (tempFloat < 0) throw new NumberFormatException();
                                double percentage = 1 - tempFloat / 100;
                                tempLong = (long) (tempLong * percentage);
                                itemObj.addProperty("itemTotalAmountAfterDiscount",
                                        tempLong);
                            } catch (NumberFormatException e) {
                                chain.getProcessObject().get("body").getAsJsonObject()
                                        .addProperty("response", "Giá trị phần trăm chiết khấu 1 không hợp lệ ở dòng số " + rowIndex);
                                e.printStackTrace();
                                return false;
                            }

                            //discount 2
                            try {
                                tempFloat = Float.parseFloat(arr[7].trim());
                                if (tempFloat < 0) throw new NumberFormatException();
                                double percentage = 1 - tempFloat / 100;
                                tempLong = (long) (tempLong * percentage);
                                itemObj.addProperty("itemTotalAmountAfterDiscount",
                                        tempLong);
                            } catch (NumberFormatException e) {
                                chain.getProcessObject().get("body").getAsJsonObject()
                                        .addProperty("response", "Giá trị phần trăm chiết khấu 2 không hợp lệ " + rowIndex);
                                e.printStackTrace();
                                return false;
                            }

                            //tax amount
                            tempFloat = Float.parseFloat(invoice.get(17).trim());
                            if (tempFloat == -2 || tempFloat == -1 || tempFloat == 0) {
                                tempLong = 0;
                            } else if (tempFloat > 0) {
                                tempLong = (long) (itemObj.get("itemTotalAmountAfterDiscount").getAsLong() * tempFloat);
                            } else {
                                chain.getProcessObject().get("body").getAsJsonObject()
                                        .addProperty("response", "Phần trăm thuế không hợp lệ ở dòng số " + rowIndex);
                                return false;
                            }
                            itemObj.addProperty("taxPercentage",
                                    tempFloat);
                            itemObj.addProperty("taxAmount",
                                    tempLong);

                            //increment
                            lineNumberInt++;
                            if (itemObj.has("isIncreaseItem") && !itemObj.get("isIncreaseItem").getAsBoolean()) {
                                sumOfTotalLineAmountWithoutTaxLong -= itemObj.get("itemTotalAmountWithoutTax").getAsLong();
                                totalAmountWithoutTaxLong -= itemObj.get("itemTotalAmountAfterDiscount").getAsLong();
                                totalTaxAmountLong -= itemObj.get("taxAmount").getAsLong();
                            } else {
                                sumOfTotalLineAmountWithoutTaxLong += itemObj.get("itemTotalAmountWithoutTax").getAsLong();
                                totalAmountWithoutTaxLong += itemObj.get("itemTotalAmountAfterDiscount").getAsLong();
                                totalTaxAmountLong += itemObj.get("taxAmount").getAsLong();
                            }
                            break;
                        case 2:
                            //item name
                            tempStr = arr[1].trim();
                            if (tempStr.isBlank()) {
                                System.out.println(" Tên hàng hoá - dịch vụ không đươc bỏ trống ở dòng số " + rowIndex);
                                chain.getProcessObject().get("body").getAsJsonObject()
                                        .addProperty("response", "Tên hàng hoá - dịch vụ không đươc bỏ trống ở dòng số " + rowIndex);
                                return false;
                            }
                            itemObj.addProperty("itemName",
                                    tempStr);
                            break;
                        default:
                            chain.getProcessObject().get("body").getAsJsonObject()
                                    .addProperty("response", "Đánh dấu loại hàng hoá - dịch vụ không hợp lệ ở dòng số " + rowIndex);
                            return false;
                    }
                    itemArray.add(itemObj);
                }

                //tax breakdown
                taxBreakdownsArray = new JsonArray();
                //summarize info
                summarizeObj = new JsonObject();
                //quantity * price
                if (sumOfTotalLineAmountWithoutTaxLong < 0) {
                    sumOfTotalLineAmountWithoutTaxLong = Math.abs(sumOfTotalLineAmountWithoutTaxLong);
                }
                summarizeObj.addProperty("sumOfTotalLineAmountWithoutTax",
                        sumOfTotalLineAmountWithoutTaxLong);
                //quantity * price - discount
                if (totalAmountWithoutTaxLong < 0) {
                    summarizeObj.addProperty("isTotalAmtWithoutTaxPos",
                            false);
                    totalAmountWithoutTaxLong = Math.abs(totalAmountWithoutTaxLong);
                }
                summarizeObj.addProperty("totalAmountWithoutTax",
                        totalAmountWithoutTaxLong);
                //(quantity * price - discount) * tax percentage
                if (totalTaxAmountLong < 0) {
                    summarizeObj.addProperty("isTotalTaxAmountPos",
                            false);
                    totalTaxAmountLong = Math.abs(totalTaxAmountLong);
                }
                summarizeObj.addProperty("totalTaxAmount",
                        totalTaxAmountLong);
                //price after discount + tax
                tempLong = totalAmountWithoutTaxLong + totalTaxAmountLong;
                if (tempLong < 0) {
                    tempLong = Math.abs(tempLong);
                    summarizeObj.addProperty("isTotalAmountPos",
                            false);
                }
                summarizeObj.addProperty("totalAmountWithTax",
                        tempLong);
                //discount
                tempLong = sumOfTotalLineAmountWithoutTaxLong - totalAmountWithoutTaxLong;
                summarizeObj.addProperty("discountAmount",
                        tempLong);

                rootObj = new JsonObject();
                rootObj.add("generalInvoiceInfo", generalInvoiceObj);
                rootObj.add("buyerInfo", buyerObj);
                rootObj.add("sellerInfo", sellerObj);
                rootObj.add("payments", paymentsArray);
                rootObj.add("itemInfo", itemArray);
                rootObj.add("taxBreakdowns", taxBreakdownsArray);
                rootObj.add("summarizeInfo", summarizeObj);

                rowIndex++;

                sendArray.add(rootObj);
            }
        } catch (Exception e) {
            chain.getProcessObject().get("body").getAsJsonObject()
                            .addProperty("response", e.getMessage());
            e.printStackTrace();
            return false;
        }
        chain.getProcessObject().get("body").getAsJsonObject()
                .add("send", sendArray);
        return true;
    }
}
