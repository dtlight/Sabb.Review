package com.sabbreview;

import java.time.LocalDate;

public class DueDateWorker implements Runnable{

    public void run() {
        System.out.println(LocalDate.now());
    }
}
