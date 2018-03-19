package com.sabbreview.tests;

import com.sabbreview.SabbReviewEntityManager;
import com.sabbreview.controller.ApplicationController;
import com.sabbreview.controller.AssignmentController;
import com.sabbreview.controller.DepartmentController;
import com.sabbreview.controller.UserController;
import com.sabbreview.model.*;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DepartmentControllerTest {
    private Department testDepartment;
    private User testuser = new User("test@test.sabb.review", "password");
    private Application testApplication;
    private Template testTemplate;
    @Before
    public void setupDepartment() {
        testApplication = new Application();
        testDepartment = new Department();
        testTemplate = new Template();
        if((testuser = SabbReviewEntityManager.getEntityManager().find(User.class, testuser.getEmailAddress())) == null) {
            testuser = new User("test@test.sabb.review", "password");
            UserController.registerUser(testuser);
        }
        testDepartment.addTemplate(testTemplate);
        ApplicationController.createApplication(testuser.getEmailAddress(), testApplication);
    }

    @After
    public void tearDownDepartment() {
        UserController.deleteUser(testuser.getEmailAddress());
        ApplicationController.deleteApplication(testuser.getEmailAddress(), testApplication.getId());
        DepartmentController.deleteDepartment(testuser.getEmailAddress(), testDepartment.getId());
    }

    @Test
    public void createDepartment() {
        TransactionState<Department> departmentTransactionState  = DepartmentController.createDepartment(testuser.getEmailAddress(),testDepartment);
        Assert.assertEquals(TransactionStatus.STATUS_OK, departmentTransactionState.getState());
        Assert.assertNotNull(departmentTransactionState.getValue());
        Department department = departmentTransactionState.getValue();
        Assert.assertNotNull(department.getId());
        DepartmentController.deleteDepartment(testuser.getEmailAddress(), department.getId());
    }

    @Test
    public void updateDepartment() {
    }

    @Test
    public void getDepartment() {
    }

    @Test
    public void getDepartments() {
    }

    @Test
    public void deleteDepartment() {
    }

    @Test
    public void getApplications() {
    }
}