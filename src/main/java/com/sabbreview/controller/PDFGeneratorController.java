package com.sabbreview.controller;


import com.sabbreview.model.Assignment;
import com.sabbreview.model.Field;
import com.sabbreview.model.FieldInstance;
import com.sabbreview.model.PDFGenerator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static spark.Spark.get;

public class PDFGeneratorController extends Controller {

    private static ByteArrayOutputStream getPDF(String assignmentID) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            Assignment assignment = em.find(Assignment.class,assignmentID);
            PDDocument document = new PDDocument();



            List<FieldInstance> fields = assignment.getApplication().fields;

            for (FieldInstance field:
                 fields) {
                PDPage pdPage = new PDPage();


                PDPageContentStream contentStream = new PDPageContentStream(document, pdPage);
                contentStream.beginText();
                contentStream.setLeading(20f);
                contentStream.newLineAtOffset(25, 725);

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
        get("/assignment/:id/pdf", (req, res) -> {
            res.raw().setContentType("application/pdf");
            res.raw().getOutputStream().write(getPDF(req.params("id")).toByteArray());
            return;
        });
    }
}
