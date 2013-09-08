## Spring Testing

This is a simplistic demo project intended to show how some of the Spring features can be used to improve your Spring test suite.
You can read the comments below to find out where each concept has been applied.


### Embedded database

An example of an application context with an embedded database can be found in *src/test/resources/embedded-db-application-context.xml*.

Test of the example database can be seen in *src/test/java/com/jayway/repository/EmbeddedDbConfigurationTest.java*.


### Transactional Tests

Transactions are used in some tests, e.g. *src/test/java/com/jayway/repository/AccountEntityTransactionalTest.java*.

Remember to `flush()` your JPA entity managers and your Hibernate sessions to avoid false positives when testing.


### Spring Profiles

An example of an application context with three profiles can be seen in *src/main/webapp/WEB-INF/application-context.xml*.
The test *src/test/java/com/jayway/service/AccountServiceImplTest.java* uses the `@ActiveProfiles` annotation, and the *src/main/webapp/WEB-INF/web.xml* uses the `spring.profiles.default` context parameter.
The profiles used are:
* `h2` that uses a H2 in-memory database
* `mysql` that connects to a local MySQL database
* `prod` (default) which simulates a JNDI database resource lookup
Active a profile by setting `spring.profiles.active=[profile(s) to be active]`

### Mockito

A pure Mockito test that does not use any Spring releated tools is exemplified in *src/test/java/com/jayway/controller/BankControllerBasicTest*.

If mock object is used as a Spring bean in an application context, it should be *reset* `@Before` or `@After` a test to certify that it is always in clean state before the next test is executed.


### Controller Tests

The test *src/test/java/com/jayway/controller/BankControllerRequestTest.java* uses the existing `MockHttpServletRequest` and `MockHttpServletRespone` classes.
The test *src/test/java/com/jayway/controller/BankControllerMvcTest.java* is based on the `Spring MVC Test Framework`.


### Integration Tests

The *src/test/java/com/jayway/application/BankApplication.java* is an Spring based integration tests that tests the entire stack based on pure Spring features, `MockMvc`, `@Transactional`, embedded database, etc.

Two maven plugins have been added to the *pom.xml* file to automate the integration tests.
* `jetty-maven-plugin` starts the application in pre-integration-test phase and stops it in the post-integration-test phase.
* `maven-failsafe-plugin` executes the integration test during the integration-test phase (ìn contrast to the `maven-surefire-plugin` that executes tests in the test phase).

The *src/test/java/com/jayway/application/BankApplicationIT.java* is an integration test that uses `REST-assured` to verify that the application's REST API is working.
In order to execute it, you must connect to MySQL, see the `mysql` profile in the *src/main/webapp/WEB-INF/application-context.xml*. Moreover, you need to launch Jetty with the `itest` profile.
