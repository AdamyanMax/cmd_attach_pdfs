package org.example;

import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    /*
    * java Main
    *  "{\"sourcePdfPath\"
    * :\"C:\\\\Users\\\\MAX\\\\IdeaProjects\\\\pdf_attachment_3\\\\src\\\\main\\\\java\\\\org\\\\example\\\\AddAnamnese_en.pdf\",
    * \"attachmentPdfs\":[\"C:\\\\Users\\\\MAX\\\\IdeaProjects\\\\pdf_attachment_3\\\\src\\\\main\\\\java\\\\org\\\\example\\\\Get_Started_With_Smallpdf.pdf\",\"C:\\\\Users\\\\MAX\\\\IdeaProjects\\\\pdf_attachment_3\\\\src\\\\main\\\\java\\\\org\\\\example\\\\Get_Started_With_Smallpdf2.pdf\"],\"compressionLevel\":9}"
    * */
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
    }
}