package org.example;


import com.google.gson.Gson;

public class AttachPdfsApp {
    public static void main(String[] args) {
        if (args.length == 1) {
            String jsonInput = args[0];
            Gson gson = new Gson();
            Main.PdfAttachmentRequest pdfAttachmentRequest = gson.fromJson(jsonInput, Main.PdfAttachmentRequest.class);

            if (pdfAttachmentRequest != null) {
                Main main = new Main();
                main.attachPdfs(pdfAttachmentRequest);
            } else {
                System.out.println("Invalid JSON input.");
            }
        } else {
            System.out.println("Please provide the JSON input.");
        }
    }

}
