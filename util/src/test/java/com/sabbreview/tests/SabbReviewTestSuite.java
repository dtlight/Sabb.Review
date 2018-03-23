package com.sabbreview.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    ApplicationControllerTest.class,
    DepartmentControllerTest.class,
    FieldControllerTest.class,
    RoleControllerTest.class,
    TemplateControllerTest.class,
    UserControllerTest.class
})
public class SabbReviewTestSuite {
}
