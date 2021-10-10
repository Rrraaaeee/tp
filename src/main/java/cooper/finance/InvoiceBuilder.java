package cooper.finance;

import org.json.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.net.URISyntaxException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

class InvoiceBuilder {
    private JSONObject requestTemplate;
    private String invoiceTemplate;
    private String invoiceEntryTemplate;
    private ArrayList<String> invoiceEntries;

    // https://www.baeldung.com/convert-input-stream-to-string
    // http://theoryapp.com/parse-json-in-java/
    // https://stackoverflow.com/questions/9151619/how-to-iterate-over-a-jsonobject
    public InvoiceBuilder () {
        try {
            // init json request template
            String postRequestTemplateStr = readFileToString("/invoice/request_template.json");
            requestTemplate = new JSONObject(postRequestTemplateStr);

            // init entry template
            invoiceTemplate = readFileToString("/invoice/template.tex");
            invoiceEntryTemplate = readFileToString("/invoice/invoice_entry_template.tex");

            invoiceEntries = new ArrayList<String>();

        } catch (Exception e) {
            System.out.println("Error encountered!!");
            e.printStackTrace();
        }
    }

    private String readFileToString(String resourceFileName) {
        InputStream fileInputStream = this.getClass().getResourceAsStream(resourceFileName);
        String contentString = new BufferedReader(
            new InputStreamReader(fileInputStream, StandardCharsets.UTF_8))
            .lines()
            .collect(Collectors.joining("\n"));
        return contentString;
    }

    private void iterateJson(JSONObject jsonObject) {
        for(int i = 0; i < jsonObject.names().length(); i++){
            System.out.println("key = " + jsonObject.names().getString(i));
            System.out.println(" value = " + jsonObject.get(jsonObject.names().getString(i)));
        }
    }
    
    private JSONObject getTemplateContentField(JSONObject jsonRequest) {
        // iterate json structure
        return jsonRequest.getJSONObject("compile")
                              .getJSONArray("resources")
                              .getJSONObject(0);
    }

    public void writeContentToJson(JSONObject jsonRequest, String s) {
        getTemplateContentField(jsonRequest).put("content", s);
    }

    public void createInvoiceEntry(String service, String rate, String quantity, String discount, String paymentDue) {
        String invoiceEntry = invoiceEntryTemplate;
        invoiceEntry = invoiceEntry.replace("% {Service}",service);
        invoiceEntry = invoiceEntry.replace("% {Rate}",rate);
        invoiceEntry = invoiceEntry.replace("% {Quantity}",quantity);
        invoiceEntry = invoiceEntry.replace("% {Discount}",discount);
        invoiceEntry = invoiceEntry.replace("% {Payment Due}",paymentDue);
        System.out.println(invoiceEntry);
        invoiceEntries.add(invoiceEntry);
    }

    public void compileInvoice() {
        String invoiceEntriesCompiled = "";
        for (String invoiceEntry : invoiceEntries) {
            invoiceEntriesCompiled += invoiceEntry + "\n";
        }
        String invoiceCompiled = invoiceTemplate.replace("% {entry field}", invoiceEntriesCompiled);
        JSONObject jsonRequest = requestTemplate;
        writeContentToJson(jsonRequest, invoiceCompiled);
        System.out.println(jsonRequest);
    }
}



