package com.sabbreview.tests;

import com.sabbreview.SabbReviewEntityManager;
import com.sabbreview.controller.RoleController;
import com.sabbreview.controller.UserController;
import com.sabbreview.model.Role;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RoleControllerTest {
    private User testuser = new User("test@test.sabb.review", "password");
    private Role testrole = new Role();
    @Before
    public void setUp(){

        if((testuser = SabbReviewEntityManager.getEntityManager().find(User.class, testuser.getEmailAddress())) == null) {
            testuser = new User("test@test.sabb.review", "password");
            UserController.registerUser(testuser);
        }
    }

    @After
    public void tearDown() {
        UserController.deleteUser(testuser.getEmailAddress());
        RoleController.removeRole(testuser.getEmailAddress(), testrole.getName());
    }

    @Test
    public void createRole() {
        TransactionState<Role> roleTransactionState = RoleController.createRole(testuser.getEmailAddress(), testrole);
        Assert.assertEquals(TransactionStatus.STATUS_OK, roleTransactionState.getState());
        Assert.assertNotNull(roleTransactionState.getValue());
        Role role = roleTransactionState.getValue();
        Assert.assertNotNull(role.getName());
        RoleController.removeRole(testuser.getEmailAddress(), role.getName());
    }

    @Test
    public void removeRole() {
        TransactionState<Role> roleTransactionState = RoleController.removeRole(testuser.getEmailAddress(), testrole.getName());
        Assert.assertEquals(TransactionStatus.STATUS_OK, roleTransactionState.getState());
        Assert.assertNotNull(roleTransactionState.getValue());
        Role role = roleTransactionState.getValue();
        Assert.assertNull(role.getName());
    }
}