package com.chrisp1985.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.docx4j.Docx4J;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.*;
import java.util.Map;

@Slf4j
@Service
public class DocumentConversionService {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.generated.name}")
    private String templateBucketName;

    public DocumentConversionService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public byte[] processTemplateFromS3(String s3Key, Map<String, Object> data) {
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(templateBucketName)
                .key(s3Key)
                .build();

        try (InputStream s3Stream = s3Client.getObject(getRequest);
             XWPFDocument document = new XWPFDocument(s3Stream);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            replacePlaceholders(document, data);
            document.write(outputStream);
            return convertDocxBytesToPdf(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Failed to process document", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void replacePlaceholders(XWPFDocument document, Map<String, Object> data) {
        // Replace in paragraphs
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            replaceInParagraph(paragraph, data);
        }

        // Replace in tables
        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        replaceInParagraph(paragraph, data);
                    }
                }
            }
        }
    }

    private void replaceInParagraph(XWPFParagraph paragraph, Map<String, Object> data) {
        for (XWPFRun run : paragraph.getRuns()) {
            String text = run.getText(0);
            if (text != null) {
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    String placeholder = "{{" + entry.getKey() + "}}";
                    if (text.contains(placeholder)) {
                        text = text.replace(placeholder, String.valueOf(entry.getValue()));
                    }
                }
                run.setText(text, 0);
            }
        }
    }

    private byte[] convertDocxBytesToPdf(byte[] docxBytes) throws Exception {
        try (InputStream docxInputStream = new ByteArrayInputStream(docxBytes);
             ByteArrayOutputStream pdfOut = new ByteArrayOutputStream()) {

            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(docxInputStream);

            // Set PDF conversion settings
            FOSettings foSettings = Docx4J.createFOSettings();
            foSettings.setWmlPackage(wordMLPackage);

            // Use docx4j to convert to PDF
            Docx4J.toFO(foSettings, pdfOut, Docx4J.FLAG_EXPORT_PREFER_XSL);

            return pdfOut.toByteArray();
        }
    }

}
