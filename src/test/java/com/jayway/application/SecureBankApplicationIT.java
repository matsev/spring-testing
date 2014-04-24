package com.jayway.application;

import com.jayway.config.ApplicationConfig;
import com.jayway.config.SecurityConfig;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationConfig.class, SecurityConfig.class})
// @ContextConfiguration({"/application-context.xml", "/security-context.xml"})
@ActiveProfiles("mysql")
public class SecureBankApplicationIT {

    @Autowired
    DataSource dataSource;


    @Before
    public void setUp() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("TRUNCATE TABLE account_t");
        jdbcTemplate.execute("INSERT INTO account_t (account_number, balance) VALUES (1, 100)");
        jdbcTemplate.execute("INSERT INTO account_t (account_number, balance) VALUES (2, 200)");
    }


	@Test
	public void shouldReturnOkIfProvidingGoodCredentials() {
		given().
				auth().
					basic("user", "secret").
		expect().
				response().
				statusCode(HttpStatus.SC_OK).
		when().
				get("/accounts/1");
	}


    @Test
    public void shouldNotGetAccountIfNotProvidingCredentials() {
        expect().
                response().
                    statusCode(HttpStatus.SC_UNAUTHORIZED).
        when().
                get("/accounts/1");
    }


    @Test
    public void shouldNotGetAccountIfProvidingBadCredentials() {
        given().
                auth().
                    basic("unknown", "password").
        expect().
                response().
                statusCode(HttpStatus.SC_UNAUTHORIZED).
        when().
                get("/accounts/1");
    }

}
