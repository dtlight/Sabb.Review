package com.sabbreview.controller;

import com.sabbreview.model.Application;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ApplicationControllerTest {

    private User testuser;

    @Before
    public void setup() {
      testuser = new User();
    }

    @Test
    public void createApplication() {
        Application testApplication = new Application(testuser);
        String id = testApplication.getId();
        TransactionState<Application> ts =
                ApplicationController.createApplication(testuser.getEmailAddress(), testApplication)
        assertEquals(id, ts.getValue().getId());
        assertEquals(TransactionStatus.STATUS_OK, ts.getState());
    }

    @org.junit.Test
    public void deleteApplication() {

    }

    @org.junit.Test
    public void getApplication() {
    }

    @org.junit.Test
    public void setAcceptanceState() {
    }

    @org.junit.Test
    public void useTemplate() {
    }

    @org.junit.Test
    public void changeFieldValue() {
    }
}
