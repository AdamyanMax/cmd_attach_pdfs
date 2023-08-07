package org.example;

import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java Test <sourcePdfPath> [<attachmentPdf1>, <attachmentPdf2>, <attachmentPdf3> ...] <compressionLevel>");
            return;
        }

        String sourcePdfPath = args[0];
        int compressionLevel = Integer.parseInt(args[args.length - 1]);

        List<String> attachmentPdfs = new ArrayList<>(Arrays.asList(args).subList(1, args.length - 1));

        PdfAttachmentRequest pdfAttachmentRequest = new PdfAttachmentRequest();
        pdfAttachmentRequest.setSourcePdfPath(sourcePdfPath);
        pdfAttachmentRequest.setAttachmentPdfs(attachmentPdfs.toArray(new String[0]));
        pdfAttachmentRequest.setCompressionLevel(compressionLevel);
        System.out.println(pdfAttachmentRequest.toString());
        attachPdfs(pdfAttachmentRequest);

        /*
        * mvn compile exec:java -Dexec.mainClass="org.example.Test" -Dexec.args="C:\\Users\\MAX\\IdeaProjects\\cmd_attach_pdfs\\src\\main\\java\\org\\example\\AddImplant.pdf
 C:\\Users\\MAX\\IdeaProjects\\cmd_attach_pdfs\\src\\main\\java\\org\\example\\AddAnamnese_en.pdf C:\\Users\\MAX\\IdeaProjects\\cmd_attach_pdfs\\src\\main\\java\\org\\example\\AddAnamnese_de.pdf 9"
        *
        * */
    }

    public static void attachPdfs(PdfAttachmentRequest pdfAttachmentRequest) {
        try {
            if (pdfAttachmentRequest.getAttachmentPdfs() != null && pdfAttachmentRequest.getAttachmentPdfs().length != 0) {
                Path sourcePath = Paths.get(pdfAttachmentRequest.getSourcePdfPath());
                String destination = sourcePath.toString().replace(sourcePath.getFileName().toString(), sourcePath.getFileName().toString().split("\\.")[0] + "_attach.pdf");

                PdfReader reader = new PdfReader(sourcePath.toString());
                PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(destination));

                for (String dest : pdfAttachmentRequest.getAttachmentPdfs()) {
                    Path attachmentPath = Paths.get(dest);
                    byte[] fileContent = Files.readAllBytes(attachmentPath);

                    PdfFileSpecification fs = PdfFileSpecification.fileEmbedded(
                            stamper.getWriter(),
                            dest,
                            attachmentPath.getFileName().toString(),
                            fileContent,
                            "application/pdf",
                            null,
                            pdfAttachmentRequest.getCompressionLevel()
                    );
                    stamper.addFileAttachment("", fs);

                }
                stamper.close();
                reader.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static class PdfAttachmentRequest {
        private String sourcePdfPath;
        private String[] attachmentPdfs;
        private Integer compressionLevel;

        public Integer getCompressionLevel() {
            return compressionLevel;
        }

        public void setCompressionLevel(Integer compressionLevel) {
            this.compressionLevel = compressionLevel;
        }

        public String getSourcePdfPath() {
            return sourcePdfPath;
        }

        public void setSourcePdfPath(String sourcePdfPath) {
            this.sourcePdfPath = sourcePdfPath;
        }

        public String[] getAttachmentPdfs() {
            return attachmentPdfs;
        }

        public void setAttachmentPdfs(String[] attachmentPdfs) {
            this.attachmentPdfs = attachmentPdfs;
        }

        @Override
        public String toString() {
            return "PdfAttachmentRequest{" +
                    "sourcePdfPath='" + sourcePdfPath + '\'' +
                    ", attachmentPdfs=" + Arrays.toString(attachmentPdfs) +
                    ", compressionLevel=" + compressionLevel +
                    '}';
        }
    }
}
