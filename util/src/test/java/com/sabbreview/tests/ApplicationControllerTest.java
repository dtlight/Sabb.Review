package com.sabbreview.tests;

        import com.sabbreview.SabbReviewEntityManager;
        import com.sabbreview.controller.ApplicationController;
        import com.sabbreview.controller.UserController;
        import com.sabbreview.model.AcceptanceState;
        import com.sabbreview.model.Application;
        import com.sabbreview.model.User;
        import org.junit.After;
        import org.junit.Assert;
        import org.junit.Before;
        import org.junit.Test;

public class ApplicationControllerTest {
    private Application testApplication;
    private User testuser = new User("test@test.sabb.review", "password");

    @Before
    public void setupApplication() {
        testApplication = new Application();
        if((testuser = SabbReviewEntityManager.getEntityManager().find(User.class, testuser.getEmailAddress())) == null) {
            testuser = new User("test@test.sabb.review", "password");
            UserController.registerUser(testuser);
        }
        testApplication.setApplicant(testuser);
        testApplication.setState(AcceptanceState.PENDING);
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
        Assert.assertEquals(AcceptanceState.PENDING, applicationFromDB.getState());
    }
}
