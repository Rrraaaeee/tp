package cooper.finance;


import java.io.InputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import cooper.util.Util;

class InvoiceBuilder {
    private final String invoiceTemplatePath  = "/invoice/template.tex";
    private final String invoiceEntryTemplatePath  = "/invoice/invoice_entry_template.tex";
    private final String texLiveUrl = "https://texlive.net/cgi-bin/latexcgi";
    private final String lineFeed = "\r\n";
    private String invoiceTemplate;
    private String invoiceEntryTemplate;
    private ArrayList<String> invoiceEntries;

    // https://www.baeldung.com/convert-input-stream-to-string
    // http://theoryapp.com/parse-json-in-java/
    // https://stackoverflow.com/questions/9151619/how-to-iterate-over-a-jsonobject
    public InvoiceBuilder() {
        invoiceEntries = new ArrayList<String>();
        loadInvoiceTemplate(invoiceTemplatePath);
        loadInvoiceEntryTemplate(invoiceEntryTemplatePath);
    }

    private void loadInvoiceTemplate(String resourcePath) {
        // load invoice template
        InputStream invoiceTemplateStream = this.getClass().getResourceAsStream(resourcePath);
        invoiceTemplate = Util.inputStreamToString(invoiceTemplateStream);
    }

    private void loadInvoiceEntryTemplate(String resourcePath) {
        // load invoice entry template
        InputStream invoiceEntryTemplateStream = this.getClass().getResourceAsStream(resourcePath);
        invoiceEntryTemplate = Util.inputStreamToString(invoiceEntryTemplateStream);
    }
    
    public void createInvoiceEntry(String service, String rate, String quantity, String discount, String paymentDue) {
        String invoiceEntry = invoiceEntryTemplate;
        invoiceEntry = invoiceEntry.replace("% {Service}",service);
        invoiceEntry = invoiceEntry.replace("% {Rate}",rate);
        invoiceEntry = invoiceEntry.replace("% {Quantity}",quantity);
        invoiceEntry = invoiceEntry.replace("% {Discount}",discount);
        invoiceEntry = invoiceEntry.replace("% {Payment Due}",paymentDue);
        invoiceEntries.add(invoiceEntry);
    }

    private String compileInvoice() {
        String invoiceEntriesCompiled = "";
        for (String invoiceEntry : invoiceEntries) {
            invoiceEntriesCompiled += invoiceEntry + "\n";
        }
        String invoiceCompiled = invoiceTemplate.replace("% {entry field}", invoiceEntriesCompiled);
        return invoiceCompiled;
    }

    private void sendInvoice(HttpURLConnection con, String invoiceCompiled) {
        // The following code mimic this curl command
        // curl -v -L -X POST -o document.pdf -F return=pdf -F engine=pdflatex 
        // -F 'filecontents[]=' -F 'filename[]=document.tex' 'https://texlive.net/cgi-bin/latexcgi'
        
        // 1. there is extra 2 -- at every boundary
        // 2. there is extra 2 -- at last boundary

        try {
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=--12345678");
            con.setDoOutput(true);
            // Note: This is the POST multipart/form-data request packet format
            // +"Content-Type: multipart/form-data" + lineFeed
            byte[] input = ("----12345678" + lineFeed
                    + "Content-Disposition: form-data; name=\"filecontents[]\"" + lineFeed + lineFeed
                    + invoiceCompiled + lineFeed
                    + "----12345678" + lineFeed
                    + "Content-Disposition: form-data; name=\"filename[]\"" + lineFeed + lineFeed
                    + "document.tex" + lineFeed
                    + "----12345678" + lineFeed
                    + "Content-Disposition: form-data; name=\"engine\"" + lineFeed + lineFeed
                    + "pdflatex" + lineFeed
                    + "----12345678" + lineFeed
                    + "Content-Disposition: form-data; name=\"return\"" + lineFeed + lineFeed
                    + "pdf" + lineFeed
                    + "----12345678" + lineFeed 
                    + "--").getBytes("utf-8");
            OutputStream connectionOutput = con.getOutputStream();
            connectionOutput.write(input, 0, input.length);
        } catch (ProtocolException e) {
            System.out.println("Protocol exception occurred");
        } catch (UnsupportedEncodingException e) {
            System.out.println("UnsupportedEncoding exception");
        } catch (IOException e) {
            System.out.println("Cannot write to http connection output stream");
        }
    }

    //https://www.baeldung.com/httpurlconnection-post
    public void compileInvoiceAndSend() {
        String invoiceCompiled = compileInvoice();

        try {
            URL url = new URL(texLiveUrl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            sendInvoice(con, invoiceCompiled);

            // TODO: This is the perfect place to add logging
            String reply = con.getResponseMessage();
            int replyCode = con.getResponseCode();
            if (replyCode == 200) {
                // send success
                byte[] buffer = con.getInputStream().readAllBytes();
                processPostResponse(buffer);
            } else {
                System.out.println("Error encountered when sending post request! Any fallback plan?");
            }
        } catch (MalformedURLException e) {
            System.out.println("Incorrect url");
        } catch (IOException e) {
            System.out.println("Unable to open connection");
        }
    }

    // https://www.baeldung.com/java-download-file
    private void processPostResponse(byte[] response) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(
                System.getProperty("user.dir") + "/output.pdf");
            fileOutputStream.write(response);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


