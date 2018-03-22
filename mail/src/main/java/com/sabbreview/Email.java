package com.sabbreview;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Enum class for the automated email templates.
 */
public enum Email {
    APPLICATIONCREATION("applicationCreation", "Application Created"),
    LOREMIPSUM("loremIpsum", "Test Email"),
    SENSITIVENOTIFICATION("sensitiveNotification", "Sabb.Review Notification"),
    ACCOUNTCREATION("loremIpsum", "Sabb.Review Account Created"),
    ACCOUNTCLOSED("loremIpsum", "Sabb.Review Account Closed");

    private String fileName;
    private String emailTitle;

    Email(String emailTypeName, String emailTitle){
        this.fileName = emailTypeName;
        this.emailTitle = emailTitle;
    }

    /**
     * Converts a notification type name into an enum Email Object.
     * @param emailName Name of the email type (e.g ApplicationCreation).
     * @return An Email object with the same name.
     */
    public static Email emailNameToEnum(String emailName){

        emailName = emailName.toUpperCase();
        if( emailName.equals(APPLICATIONCREATION.getFileName().toUpperCase())){
            return Email.APPLICATIONCREATION;
        }
        if( emailName.equals(LOREMIPSUM.getFileName().toUpperCase())){
            return Email.LOREMIPSUM;
        }
        if( emailName.equals(SENSITIVENOTIFICATION.getFileName().toUpperCase())){
            return Email.SENSITIVENOTIFICATION;
        }
        if( emailName.equals(ACCOUNTCREATION.getFileName().toUpperCase())){
            return Email.ACCOUNTCREATION;
        }
        if( emailName.equals(ACCOUNTCLOSED.getFileName().toUpperCase())){
            return Email.ACCOUNTCLOSED;
        }

        return Email.LOREMIPSUM;
    }



    public String getTitle(){
        return this.emailTitle;
    }

    private String getFileName(){
        return this.fileName + ".txt";
    }


    /**
     * Reads contents of an email
     * @param filePath The path of the file to load relative to the 'static' folder, including the file extension
     * @return The contents of the file, including line breaks.
     * @throws IOException If something goes wrong reading the given file.
     */
    private String loadFile(String filePath) throws IOException {

        BufferedReader bf = new BufferedReader(new FileReader(filePath));

        String line = "";
        StringBuilder text = new StringBuilder();

        while((line = bf.readLine())!= null){
            text.append(line);
            text.append("\n");
        }
        bf.close();

        //Removing final newline (which is added even if there isn't one in the file)
        return text.substring(0, text.length() - 1);
    }


    /**
     * Generates the HTML for an email
     * @param userName The name of the person the email is to be sent to.
     * @return The full email HTML.
     */
    public String generateHTML(String userName) {
        try {
            String html = loadFile( new File("").getAbsolutePath() + "/mail/target/classes/static/emails/notificationTemplate.html");

            //Adding message+name to email
            html = html.replaceFirst("\\{body}", loadFile(new File("").getAbsolutePath() + "/mail/target/classes/static/emails/text/" + this.getFileName()));
            html = html.replaceFirst("\\{name}", userName);

            return html;
        } catch (IOException e) {
            e.printStackTrace();
            return "IOException while generating email HTML!<br><br>" + e.toString() + "<br><br>";
        }
    }


}
