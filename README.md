## Spring Testing

This is a simplistic demo project intended to show how some Spring features can be used to
improve your Spring test suite. The project has been used as demo during conference
sessions. During SpringOne 2GX 2013 the session was recorded and you can watch it on
[YouTube](https://www.youtube.com/watch?v=LYVJ69h76nw). Below you will find references to
where the concepts in the presentation have been used in the source code.


### Embedded database

An example of an application context with an embedded database can be found in
[EmbeddedDbJavaConfig](src/test/java/com/jayway/repository/EmbeddedDbJavaConfig.java) class.

Test of the example database can be seen in [EmbeddedDbJavaConfigTest](src/test/java/com/jayway/repository/EmbeddedDbJavaConfigTest.java).


### Transactional Tests

Transactions are used in some tests, e.g. [AccountEntityTransactionalTest](src/test/java/com/jayway/repository/AccountEntityTransactionalTest.java).

Remember to `flush()` your JPA entity managers and your Hibernate sessions to avoid false
positives when testing.


### Spring Profiles

The [RepositoryConfig](src/main/java/com/jayway/config/RepositoryConfig.java) interface
has been implemented using three different profiles:

* `h2` uses a H2 in-memory database, see [H2RepositoryConfig](src/main/java/com/jayway/config/H2RepositoryConfig.java).
* `mysql` connects to a local MySQL database, see [MySqlRepositoryConfig](src/main/java/com/jayway/config/MySqlRepositoryConfig.java).
* `prod` simulates a JNDI database resource lookup, see [JndiRepositoryConfig](src/main/java/com/jayway/config/JndiRepositoryConfig.java).

All three repository config classes above have been imported by the [ApplicationConfig](src/main/java/com/jayway/config/ApplicationConfig.java)
class, but only one can be used at the time, by settings the `spring.profiles.active` environment variable. The `prod`
profile has been set to the default profile by using the `spring.profiles.default`
context parameter in [web.xml](src/main/webapp/WEB-INF/web.xml).

In some tests, like the [AccountServiceImplTest](src/test/java/com/jayway/service/AccountServiceImplTest.java),
the `@ActiveProfiles` annotation has been used to specify which profile should be active
in the test.


### Mockito

A pure Mockito test that does not use any Spring related tools is exemplified in
[BankControllerBasicTest](src/test/java/com/jayway/controller/BankControllerBasicTest.java).

If a mock object is used as a Spring bean in an application context, it should be `reset()`
`@Before` or `@After` a test to certify that it is always in clean state before the next
test is executed.


### Controller Tests

The test [BankControllerMvcTest](src/test/java/com/jayway/controller/BankControllerMvcTest.java)
uses the `MockMvc` class to verify request mapping, serialization, response codes, etc.


### Integration Tests

The [BankApplicationTest](src/test/java/com/jayway/application/BankApplicationTest.java)
is a Spring based integration tests that tests the entire stack based on pure Spring features,
`MockMvc`, `@Transactional`, embedded database, etc.

Two maven plugins have been added to the [pom.xml] file to automate the integration tests.
* `jetty-maven-plugin` starts the application in pre-integration-test phase and stops it
in the post-integration-test phase.
* `maven-failsafe-plugin` executes the integration test during the integration-test phase
(ìn contrast to the `maven-surefire-plugin` that executes tests in the test phase).

The [BankApplicationIT](src/test/java/com/jayway/application/BankApplicationIT.java) is an
integration test that uses [REST-assured](https://code.google.com/p/rest-assured/) to verify
that the application's REST API is working. In order to execute it, you must connect to
MySQL, and enable the `mysql` Spring profile Moreover, you need to launch Jetty with the
Maven `itest` profile.