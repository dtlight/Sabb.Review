package com.sabbreview.endpoints;


import com.sabbreview.SabbReviewEntityManager;
import com.sabbreview.model.Application;
import com.sabbreview.model.Assignment;
import com.sabbreview.model.FieldInstance;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;

import static spark.Spark.get;

public class PDFGeneratorEndpoint {
    static PDFont font = PDType1Font.HELVETICA;
    static EntityManager em = SabbReviewEntityManager.getEntityManager();

    private static ByteArrayOutputStream getPDF(String assignmentID) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            Application application = em.find(Application.class,assignmentID);
            PDDocument document = new PDDocument();

            PDPage pdPage = new PDPage(); // Page 1: Details about application

            PDPageContentStream contentStream = new PDPageContentStream(document, pdPage);
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.setLeading(20f);
            contentStream.newLineAtOffset(25, 725);
            contentStream.showText("Application #"+application.getId());

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





            PDPage fieldPage = new PDPage(); // Page 1: Fields in Application
            List<FieldInstance> fields = application.getFields();

            PDPageContentStream fieldContentStream = new PDPageContentStream(document, fieldPage);
            fieldContentStream.beginText();
            fieldContentStream.setFont(font, 12);
            fieldContentStream.setLeading(20f);
            fieldContentStream.newLineAtOffset(25, 725);
            fieldContentStream.showText("Application Questions");
            fieldContentStream.newLine();

            for (FieldInstance fieldInstance:
                fields) {

                fieldContentStream.showText("Question: "+ fieldInstance.getField().getTitle());
                fieldContentStream.newLine();
                if(fieldInstance.getField().getType().toString().contains("text")) {
                    fieldContentStream.showText("Answer: "+ fieldInstance.getValue());
                } else {
                    fieldContentStream.showText("Answer: "+ fieldInstance.getSelected());
                }

                fieldContentStream.newLine();
            }
            fieldContentStream.endText();
            fieldContentStream.close();

            document.addPage(fieldPage);

            List<Assignment> assignments = application.getAssignments();
            document.addPage(createAssignmentPage(document, assignments));

            document.save(baos);
            return baos;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static PDPage createAssignmentPage(PDDocument document,
        List<Assignment> assignments) throws IOException {
        PDPage assignmentPage = new PDPage(); // Page 1+n: Assignments attached to application

        PDPageContentStream assignmentContentStream = new PDPageContentStream(document, assignmentPage);
        assignmentContentStream.beginText();

        assignmentContentStream.setFont(font, 12);
        assignmentContentStream.setLeading(20f);
        assignmentContentStream.newLineAtOffset(25, 725);

        for (Assignment assignment:
            assignments){
            assignmentContentStream.setFont(font, 15);
            assignmentContentStream.newLine();
            assignmentContentStream.showText("Assignment #"+ assignment.getId()+" by "+ assignment.getAssignee().getEmailAddress());
            assignmentContentStream.setFont(font, 12);
            assignmentContentStream.showText("Status: "+ assignment.getState());
            assignmentContentStream.newLine();
        }
        assignmentContentStream.endText();
        assignmentContentStream.close();
        return assignmentPage;
    }

    public static void attach() {
        get("/pdf/application/:id", (req, res) -> {
            res.raw().setContentType("application/pdf");
            res.raw().getOutputStream().write(getPDF(req.params("id")).toByteArray());
            return "";
        });
    }
}
