package com.jayway.application;

import com.jayway.config.SpringBootRunner;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("mysql")
@WebAppConfiguration
@IntegrationTest
@SpringApplicationConfiguration(classes = {SpringBootRunner.class})
public class RestTemplateSecureBankApplicationIT {


	@Test
	public void shouldReturnOkIfProvidingGoodCredentials() {
		RestTemplate restTemplate = new TestRestTemplate("user", "secret");

		ResponseEntity<Map> responseEntity = restTemplate
				.getForEntity("http://localhost:8080/accounts/{accountNbr}", Map.class, 1);

		assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
	}


    @Test
    public void shouldNotGetAccountIfNotProvidingCredentials() {
		RestTemplate restTemplate = new TestRestTemplate();

		ResponseEntity<Void> responseEntity = restTemplate
				.getForEntity("http://localhost:8080/accounts/{accountNbr}", Void.class, 1);

		assertThat(responseEntity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }


    @Test
    public void shouldNotGetAccountIfProvidingBadCredentials() {
		RestTemplate restTemplate = new TestRestTemplate("unknown", "password");

		ResponseEntity<Void> responseEntity = restTemplate
				.getForEntity("http://localhost:8080/accounts/{accountNbr}", Void.class, 1);

		assertThat(responseEntity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

}
