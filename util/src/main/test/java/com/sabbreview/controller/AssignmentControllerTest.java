package com.sabbreview.controller;

import com.sabbreview.model.Application;
import com.sabbreview.model.Assignment;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AssignmentControllerTest {
    private Application testApplication;
    private User testuser;

    @Before
    public void setupApplication() {
        testApplication = new Application();
    }

    public void setupUser() {
        testuser = new User();
    }

    @Test
    public void createAssignment() {

    }

    @Test
    public void deleteAssignment() {
    }

    @Test
    public void setAcceptanceState() {
    }
}