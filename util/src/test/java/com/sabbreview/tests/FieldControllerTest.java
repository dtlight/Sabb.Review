package com.sabbreview.tests;

import com.sabbreview.SabbReviewEntityManager;
import com.sabbreview.controller.ApplicationController;
import com.sabbreview.controller.FieldController;
import com.sabbreview.controller.UserController;
import com.sabbreview.model.Application;
import com.sabbreview.model.Field;
import com.sabbreview.model.FieldOption;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FieldControllerTest {
    private Field testField;
    private Application testApplication;
    private FieldOption fieldOption;
    private User testuser = new User("test@test.sabb.review", "password");

    @Before
    public void setupField() {
        testApplication = new Application();
        if((testuser = SabbReviewEntityManager.getEntityManager().find(User.class, testuser.getEmailAddress())) == null) {
            testuser = new User("test@test.sabb.review", "password");
            UserController.registerUser(testuser);
        }
        testApplication.setApplicant(testuser);
        ApplicationController.createApplication(testuser.getEmailAddress(), testApplication);
    }

    @After
    public void tearDownField()  {
        UserController.deleteUser(testuser.getEmailAddress());
        ApplicationController.deleteApplication(testuser.getEmailAddress(), testApplication.getId());
    }

    @Test
    /**
     * To to create and get a field.
     */
    public void getField() {
        testField = new Field();
        TransactionState<Field> fieldTransactionState = FieldController.createField(testuser.getEmailAddress(), testField);
        Assert.assertEquals(TransactionStatus.STATUS_OK, fieldTransactionState.getState());
        Assert.assertNotNull(fieldTransactionState.getValue());
        Field field = fieldTransactionState.getValue();
        Assert.assertNotNull(field.getId());
    }

    @Test
    public void editField() {
        testField = new Field();
        TransactionState<Field> fieldTransactionState = FieldController.editField(testuser.getEmailAddress(), testField);
        Assert.assertEquals(TransactionStatus.STATUS_OK, fieldTransactionState.getState());
    }

    /**
     * To to create and delete a field.
     */
    @Test
    public void deleteField() {
        testField = new Field();
        TransactionState<Field> fieldTransactionState1 = FieldController.createField(testuser.getEmailAddress(), testField);
        Assert.assertEquals(TransactionStatus.STATUS_OK, fieldTransactionState1.getState());
        Assert.assertNotNull(fieldTransactionState1.getValue());
        TransactionState<Field> fieldTransactionState = FieldController.deleteField(testuser.getEmailAddress(), testField.getId());
        Assert.assertEquals(TransactionStatus.STATUS_OK, fieldTransactionState.getState());
        Assert.assertNull(fieldTransactionState.getValue());
    }

}