package cooper.finance;

import org.json.*;

import java.io.IOException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.net.URL;
import java.net.HttpURLConnection;

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

    //https://www.baeldung.com/httpurlconnection-post
    public void compileInvoiceAndSend() {
        String invoiceEntriesCompiled = "";
        for (String invoiceEntry : invoiceEntries) {
            invoiceEntriesCompiled += invoiceEntry + "\n";
        }
        String invoiceCompiled = invoiceTemplate.replace("% {entry field}", invoiceEntriesCompiled);
        JSONObject jsonRequest = requestTemplate;
        writeContentToJson(jsonRequest, invoiceCompiled);

        // curl -X POST -H 'Content-Type: application/json' -d @data.json http://47.241.250.203:3013/project/1/compile
        try {
            URL url = new URL ("http://47.241.250.203:3013/project/1/compile");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            byte[] input = jsonRequest.toString().getBytes("utf-8");
            OutputStream os = con.getOutputStream(); 
            os.write(input, 0, input.length);			

            String reply = con.getResponseMessage();
            int replyCode = con.getResponseCode();
            if (replyCode == 200) {
                // send success
                try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    processPostResponse(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error encountered when sending post request! Any fallback plan?");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // https://www.baeldung.com/java-download-file
    private void processPostResponse(String response) {

        JSONObject jsonResponse = new JSONObject(response);
        // iterate json structure
        String compileStatus = jsonResponse.getJSONObject("compile")
                                           .getString("status");
        if (!compileStatus.equals("success")) {
            System.out.println("Compilation failed on the server side. Any fallback plan?");
            return;
        }
        String outputUrl = jsonResponse.getJSONObject("compile")
                                        .getJSONArray("outputFiles")
                                        .getJSONObject(8)
                                        .getString("url");
        outputUrl = outputUrl.replace("localhost", "47.241.250.203"); // replace url with the actual public ip

        try (BufferedInputStream in = new BufferedInputStream(new URL(outputUrl).openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(
                System.getProperty("user.dir") + "/output.pdf")) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            fileOutputStream.close();
        } catch (IOException e) {
            System.out.println("Error encountered when downloading output from server!");
            e.printStackTrace();
        }
    }
}


