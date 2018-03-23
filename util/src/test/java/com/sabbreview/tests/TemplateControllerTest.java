package com.sabbreview.tests;

import com.sabbreview.controller.DepartmentController;
import com.sabbreview.controller.TemplateController;
import com.sabbreview.controller.UserController;
import com.sabbreview.model.Department;
import com.sabbreview.model.Template;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TemplateControllerTest {
    private Template testTemplate;
    private User newUser;
    @Before
    public void setUp() {
        testTemplate = new Template();
        newUser = new User();
        TemplateController.createTemplate(newUser.getEmailAddress(), testTemplate);
    }

    @After
    public void tearDown(){
        UserController.deleteUser(newUser.getEmailAddress());
        DepartmentController.deleteDepartment(newUser.getEmailAddress(), testTemplate.getId());
    }

    @Test
    public void createTemplate() {
        TransactionState<Template> templateTransactionState = TemplateController.createTemplate(newUser.getEmailAddress(), testTemplate);
        Assert.assertEquals(TransactionStatus.STATUS_OK, templateTransactionState.getState());
        Assert.assertNotNull(templateTransactionState.getValue());
        Template template= templateTransactionState.getValue();
        Assert.assertNotNull(template.getId());
        TemplateController.deleteTemplate(newUser.getEmailAddress(), testTemplate.getId());
    }

    @Test
    public void deleteTemplate() {
        TransactionState<Template> templateTransactionState = TemplateController.deleteTemplate(newUser.getEmailAddress(), testTemplate.getId());
        Assert.assertEquals(TransactionStatus.STATUS_OK, templateTransactionState.getState());
        Assert.assertNull(templateTransactionState.getValue());
        Template templateDelete = templateTransactionState.getValue();
        Assert.assertNull(templateDelete.getId());
    }
}