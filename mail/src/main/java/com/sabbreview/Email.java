package com.sabbreview;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

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

    public static Email emailNameToEnum(String emailName){
        if( emailName.equals(APPLICATIONCREATION.getFileName())){
            return Email.APPLICATIONCREATION;
        }
        if( emailName.equals(LOREMIPSUM.getFileName())){
            return Email.LOREMIPSUM;
        }
        if( emailName.equals(SENSITIVENOTIFICATION.getFileName())){
            return Email.SENSITIVENOTIFICATION;
        }
        if( emailName.equals(ACCOUNTCREATION.getFileName())){
            return Email.ACCOUNTCREATION;
        }
        if( emailName.equals(ACCOUNTCLOSED.getFileName())){
            return Email.ACCOUNTCLOSED;
        }

        return Email.LOREMIPSUM;
    }



    public String getTitle(){
        return this.emailTitle;
    }

   Email(String emailType) {
       this.fileName = emailType;
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
        String path = getEmailModuleBuildRoot() + filePath;

        BufferedReader bf = new BufferedReader(new FileReader(path));

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
     * Generates a html for use in an email notifications.
     *
     */
    public String generateHTML(String userName) {
        try {
            String html = loadFile("classes/static/emails/notificationTemplate.html");

            //Adding message+name to email
            html = html.replaceFirst("\\{body}", loadFile("classes/static/emails/text/" + this.getFileName()));
            html = html.replaceFirst("\\{name}", userName);

            return html;
        } catch (IOException e) {
            e.printStackTrace();
            return "IOException while generating email HTML!<br><br>" + e.toString() + "<br><br>";
        }
    }

    /**
     * Gets the path of the target folder in the mail module.
     * Assumes that folder structure is similar to mail/target/classes/com/sabbreview
     * @return The absolute path of the mail module target folder
     */
    private String getEmailModuleBuildRoot() {
        try {
            String fullpath = EmailController.class.getResource("").toURI().getPath();

            //Moving slash at start of path.
            fullpath = fullpath.substring(1, fullpath.length() - 1);

            String[] splitPath = fullpath.split("/");


            StringBuilder newPath = new StringBuilder();
            for (int i = 0; (i == 0) || !splitPath[i - 1].equals("target"); i++) {
                newPath.append(splitPath[i]).append("/");
            }

            System.out.println(newPath.toString());

            //Removing leading slash if present
            if (newPath.charAt(0) == '/' || newPath.charAt(0) == '\\') {
                return newPath.toString().substring(1, newPath.length() - 1);
            }
            return newPath.toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return "";
        }
    }
}
