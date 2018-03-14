package com.sabbreview.tests;

import com.sabbreview.SabbReviewEntityManager;
import com.sabbreview.controller.ApplicationController;
import com.sabbreview.controller.AssignmentController;
import com.sabbreview.controller.UserController;
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

public class AssignmentControllerTest {
    private Application testApplication;
    private User testuser = new User("test@test.sabb.review", "password");

    @Before
    public void setupAssignment() {
        testApplication = new Application();
        if((testuser = SabbReviewEntityManager.getEntityManager().find(User.class, testuser.getEmailAddress())) == null) {
            testuser = new User("test@test.sabb.review", "password");
            UserController.registerUser(testuser);
        }
        testApplication.setApplicant(testuser);
        ApplicationController.createApplication(testuser.getEmailAddress(), testApplication);
    }

    @Test
    public void createAssignment() {
        TransactionState<Assignment>  assignmentTransactionState = AssignmentController.createAssignment(testuser.getEmailAddress(), testApplication.getId(), testuser.getEmailAddress());
        Assert.assertEquals(TransactionStatus.STATUS_OK, assignmentTransactionState.getState());
        Assert.assertNotNull(assignmentTransactionState.getValue());
        Assignment assignment = assignmentTransactionState.getValue();
        Assert.assertNotNull(assignment.getId());
        AssignmentController.deleteAssignment(testuser.getEmailAddress(), assignment.getId());
    }

    @After
    public void teardownAssignment() {
        UserController.deleteUser(testuser.getEmailAddress());
        ApplicationController.deleteApplication(testuser.getEmailAddress(), testApplication.getId());
    }

    @Test
    public void setAcceptanceState() {
        TransactionState<Assignment>  assignmentTransactionState = AssignmentController.createAssignment(testuser.getEmailAddress(), testApplication.getId(), testuser.getEmailAddress());
        Assignment assignment = assignmentTransactionState.getValue();
        assignment.setState(AcceptanceState.COMPLETED);
        Assert.assertEquals(AcceptanceState.COMPLETED, assignment.getState());
        AssignmentController.deleteAssignment(testuser.getEmailAddress(), assignment.getId());
    }

    @Test
    public void setAssignee() {
        TransactionState<Assignment>  assignmentTransactionState = AssignmentController.createAssignment(testuser.getEmailAddress(), testApplication.getId(), testuser.getEmailAddress());
        Assignment assignment = assignmentTransactionState.getValue();
        Assert.assertEquals(testuser.getEmailAddress(), assignment.getAssignee().getEmailAddress());
        AssignmentController.deleteAssignment(testuser.getEmailAddress(), assignment.getId());
    }
}
