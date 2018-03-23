package com.sabbreview.tests;

import com.sabbreview.SabbReviewEntityManager;
import com.sabbreview.controller.UserController;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserControllerTest {
    private User testuser = new User("test@test.sabb.review", "password");
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
    }

    @Test
    public void registerUser() {
        TransactionState<User> userTransactionState = UserController.registerUser(testuser);
        Assert.assertEquals(TransactionStatus.STATUS_OK, userTransactionState.getState());
        Assert.assertNotNull(userTransactionState.getValue());
        User user = userTransactionState.getValue();
        Assert.assertNotNull(user.getEmailAddress());
        UserController.deleteUser(testuser.getEmailAddress());
    }

    @Test
    public void getUser() {
        TransactionState<User> userTransactionState = UserController.getUser(testuser.getEmailAddress());
        Assert.assertEquals(TransactionStatus.STATUS_OK, userTransactionState.getState());
        Assert.assertNotNull(userTransactionState.getValue());
        User user = userTransactionState.getValue();
        Assert.assertNotNull(user.getEmailAddress());
        UserController.deleteUser(testuser.getEmailAddress());
    }


    @Test
    public void deleteUser() {
        TransactionState<User> userTransactionState = UserController.deleteUser(testuser.getEmailAddress());
        Assert.assertEquals(TransactionStatus.STATUS_OK, userTransactionState.getState());
        Assert.assertNotNull(userTransactionState.getValue());
        User user = userTransactionState.getValue();
        Assert.assertNotNull(user.getEmailAddress());
        UserController.deleteUser(testuser.getEmailAddress());
    }

}