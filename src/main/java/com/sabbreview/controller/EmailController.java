package com.sabbreview.controller;

import net.sargue.mailgun.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EmailController {

    public static String read() {
        try {
            BufferedReader bf = new BufferedReader(new FileReader("src/main/resources/API-Key.txt"));
            return bf.readLine();
        }catch(IOException ex){

        }
        return null;
    }
    private static Configuration configuration = new Configuration()
            .domain("sabb.review")
            .apiKey(read())
            .from("sabbbot", "postmaster@sabb.review");

    public static void Send (String subject, String content, String reciever) {
        Mail.using(configuration)
                .to(reciever)
                .subject(subject)
                .text(content)
                .build()
                .send();
    }
}
