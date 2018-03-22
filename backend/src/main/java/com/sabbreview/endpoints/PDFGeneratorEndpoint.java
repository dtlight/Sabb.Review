package com.sabbreview.endpoints;


import com.sabbreview.SabbReview;
import com.sabbreview.SabbReviewEntityManager;
import com.sabbreview.model.Application;
import com.sabbreview.model.Assignment;
import com.sabbreview.model.Comment;
import com.sabbreview.model.FieldInstance;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;

import static spark.Spark.get;

public class PDFGeneratorEndpoint {
    static PDFont font = PDType1Font.HELVETICA;
    static EntityManager em = SabbReviewEntityManager.getEntityManager();
    static final double SIGNATURE_SCALE = 0.5;

    private static ByteArrayOutputStream getPDF(String assignmentID) {

      try(PDDocument document = new PDDocument()) {
          Application application = em.find(Application.class, assignmentID);


          PDImageXObject pdImage = PDImageXObject.createFromFile(SabbReview.getStaticResource("backend", "sabbreview.png"), document);
          BASE64Decoder decoder = new BASE64Decoder();

          PDImageXObject pdSignature = (application.getSignature() != null)?PDImageXObject.createFromByteArray(document, decoder.decodeBuffer(application.getSignature()), "sig.png"):null;

          ByteArrayOutputStream baos = new ByteArrayOutputStream();


            PDDocumentInformation pdDocumentInformation = new PDDocumentInformation();
            pdDocumentInformation.setTitle("SabbReview Appraisal #"+application.getId());
            document.setDocumentInformation(pdDocumentInformation);
            PDPage pdPage = new PDPage(); // Page 1: Details about application

            PDPageContentStream contentStream = new PDPageContentStream(document, pdPage);
            contentStream.drawImage( pdImage, 380, 700, Math.round(pdImage.getWidth()*0.2), Math.round(pdImage.getHeight()*0.2));
            contentStream.beginText();
            contentStream.setFont(font, 15);
            contentStream.setLeading(20f);
            contentStream.newLineAtOffset(25, 725);
            contentStream.showText("Application #"+application.getId());
            contentStream.setFont(font, 12);
            contentStream.newLine();

            contentStream.newLine();
            contentStream.showText("Assignee: "+application.getApplicant().getEmailAddress());
            contentStream.newLine();

            contentStream.showText("Current state: "+application.getState());
          contentStream.newLine();
          contentStream.newLine();
          if(pdSignature != null) {
            contentStream.showText("Signed by "+application.getApplicant().getEmailAddress());
            contentStream.endText();
            contentStream.drawImage(pdSignature, 30, 500, Math.round(pdSignature.getWidth()*SIGNATURE_SCALE), Math.round(pdSignature.getHeight()*SIGNATURE_SCALE));
          } else {
            contentStream.showText("Application has not been signed");
            contentStream.endText();

          }


            contentStream.close();
            document.addPage(pdPage);





            PDPage fieldPage = new PDPage(); // Page 1: Fields in Application
            List<FieldInstance> fields = application.getFields();

            PDPageContentStream fieldContentStream = new PDPageContentStream(document, fieldPage);
            fieldContentStream.beginText();
            fieldContentStream.setFont(font, 12);
            fieldContentStream.setLeading(20f);
            fieldContentStream.newLineAtOffset(25, 725);
            fieldContentStream.setFont(font, 15);
            fieldContentStream.showText("Application Questions");
            fieldContentStream.setFont(font, 12);
            fieldContentStream.newLine();
            fieldContentStream.newLine();

            for (FieldInstance fieldInstance:
                fields) {

                switch (fieldInstance.getField().getType()) {
                    case LONGTEXT:
                        fieldContentStream.showText("Question: "+ fieldInstance.getField().getTitle());
                        fieldContentStream.newLine();
                        fieldContentStream.showText("Answer: "+ fieldInstance.getValue());
                        break;
                    case TEXT:
                        fieldContentStream.showText("Question: "+ fieldInstance.getField().getTitle());
                        fieldContentStream.newLine();
                        fieldContentStream.showText("Answer: "+ fieldInstance.getValue());
                        break;
                    case MULTICHOICE:
                        fieldContentStream.showText("Question: "+ fieldInstance.getField().getTitle());
                        fieldContentStream.newLine();
                        fieldContentStream.showText("Answer: "+ fieldInstance.getSelected());
                        break;
                    case SINGLECHOICE:
                        fieldContentStream.showText("Question: "+ fieldInstance.getField().getTitle());
                        fieldContentStream.newLine();
                        fieldContentStream.showText("Answer: "+ ((fieldInstance.getSelected() != null
                            && fieldInstance.getSelected().size() > 0)?fieldInstance.getSelected().get(0).getTitle():""));
                        break;

                    case DIVIDER:
                        fieldContentStream.newLine();
                        fieldContentStream.showText("-> "+ fieldInstance.getField().getTitle());
                        fieldContentStream.newLine();
                        break;
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
            assignmentContentStream.showText("  Status: "+ assignment.getState());
            assignmentContentStream.newLine();
            for(Comment comment: assignment.getComments()) {
              assignmentContentStream.showText("Comment: "+ comment.getBody());
              assignmentContentStream.newLine();
            }
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
