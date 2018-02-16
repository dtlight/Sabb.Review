package com.sabbreview.controller;


import com.sabbreview.model.Application;
import com.sabbreview.model.FieldInstance;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static spark.Spark.get;

public class PDFGeneratorController extends Controller {
    static PDFont font = PDType1Font.HELVETICA;

    private static ByteArrayOutputStream getPDF(String assignmentID) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            Application application = em.find(Application.class,assignmentID);
            PDDocument document = new PDDocument();
            List<FieldInstance> fields = application.getFields();

            for (FieldInstance field:
                 fields) {
                PDPage pdPage = new PDPage();

                PDPageContentStream contentStream = new PDPageContentStream(document, pdPage);
                contentStream.beginText();
                contentStream.setFont(font, 12);
                contentStream.setLeading(20f);
                contentStream.newLineAtOffset(25, 725);

                contentStream.showText("ID: "+ application.getId());
                contentStream.newLine();
                contentStream.showText("Assignee: "+application.getApplicant());
                contentStream.newLine();
                /*
                contentStream.showText("Comments: " +application.getComments());
                contentStream.newLine();
                contentStream.showText("Due date: "+application.getDueDate());
                contentStream.newLine();
                */
                contentStream.showText("Current state: "+application.getState());
                contentStream.endText();
                contentStream.close();
                document.addPage(pdPage);
            }

            document.save(baos);
            return baos;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void attach() {
        get("/application/:id/pdf", (req, res) -> {
            res.raw().setContentType("application/pdf");
            res.raw().getOutputStream().write(getPDF(req.params("id")).toByteArray());
            return "";
        });
    }
}
