package com.sabbreview.tests;

import com.sabbreview.SabbReviewEntityManager;
import com.sabbreview.controller.DepartmentController;
import com.sabbreview.controller.UserController;
import com.sabbreview.model.*;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DepartmentControllerTest {
    private Department testDepartment;
    private User testuser = new User("test@test.sabb.review", "password");
    private Template testTemplate;
    @Before
    public void setupDepartment() {
        testDepartment = new Department();
        testTemplate = new Template();
        if((testuser = SabbReviewEntityManager.getEntityManager().find(User.class, testuser.getEmailAddress())) == null) {
            testuser = new User("test@test.sabb.review", "password");
            UserController.registerUser(testuser);
        }
        testDepartment.addTemplate(testTemplate);
    }

    @After
    public void tearDownDepartment() {
        UserController.deleteUser(testuser.getEmailAddress());
        DepartmentController.deleteDepartment(testuser.getEmailAddress(), testDepartment.getId());
    }

    @Test
    public void createDepartment() {
        TransactionState<Department> departmentTransactionState  = DepartmentController.createDepartment(testuser.getEmailAddress(),testDepartment);
        Assert.assertEquals(TransactionStatus.STATUS_OK, departmentTransactionState.getState());
        Assert.assertNotNull(departmentTransactionState.getValue());
        Department department = departmentTransactionState.getValue();
        Assert.assertNotNull(department);
    }

    @Test
    public void getDepartment() {
        TransactionState<Department> departmentTransactionState  = DepartmentController.getDepartment(testuser.getEmailAddress(),testDepartment.getId());
        Assert.assertEquals(TransactionStatus.STATUS_OK, departmentTransactionState.getState());
        Assert.assertNotNull(departmentTransactionState.getValue());
        Department department = departmentTransactionState.getValue();
        Assert.assertNotNull(department.getId());
    }

    @Test
    public void deleteDepartment() {
        TransactionState<Department> departmentTransactionState  = DepartmentController.deleteDepartment(testuser.getEmailAddress(),testDepartment.getId());
        Assert.assertEquals(TransactionStatus.STATUS_OK, departmentTransactionState.getState());
        Assert.assertNotNull(departmentTransactionState.getValue());
        Department department = departmentTransactionState.getValue();
        Assert.assertNotNull(department.getId());
    }

}