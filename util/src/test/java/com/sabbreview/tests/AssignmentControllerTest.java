package com.sabbreview.;

import com.sabbreview.SabbReviewEntityManager;
import com.sabbreview.model.AcceptanceState;
import com.sabbreview.model.Application;
import com.sabbreview.model.Assignment;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AssignmentControllerTest {
    private Application testApplication
    private User testuser = new User("test@test.sabb.review", "password");

    {

    }

    @Before
    public void setupApplication() {
        testApplication = new Application();
        UserController.registerUser(testuser);
        testApplication.setApplicant(testuser);
        ApplicationController.createApplication(testuser.getEmailAddress(), testApplication);

    }


    @After
    public void teardownApplication() {
        UserController.deleteUser(testuser.getEmailAddress());
        ApplicationController.deleteApplication(testuser.getEmailAddress(), testApplication.getId());

    }

    @Test
    public void setAcceptanceState() {
        Application applicationFromDB = SabbReviewEntityManager.getEntityManager().find(Application.class, testApplication.getId());
        Assert.assertEquals(AcceptanceState.PENDING, applicationFromDB);
    }
}