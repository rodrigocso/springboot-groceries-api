package com.rodrigocso.groceries.test;

import com.rodrigocso.groceries.model.*;
import com.rodrigocso.groceries.test.util.builder.*;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class GroceriesApplicationTests {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private URI getUri(String path) throws Exception {
		return new URI("http://localhost:" + port + path);
	}

	@Test
	public void basicIntegrationTest() throws Exception {
		Brand brand = restTemplate.postForEntity(
				getUri("/brands"),
				BrandBuilder.builder().withName("Kirkland").build(),
				Brand.class)
				.getBody();

		Store store = restTemplate.postForEntity(
				getUri("/stores"),
				StoreBuilder.builder().withName("Costco").build(),
				Store.class)
				.getBody();

		Product product = restTemplate.postForEntity(
				getUri("/products"),
				ProductBuilder.builder().withBrand(brand).build(),
				Product.class)
				.getBody();

		Item item = restTemplate.postForEntity(
				getUri("/items"),
				ItemBuilder.builder().withProduct(product).build(),
				Item.class)
				.getBody();

		Purchase purchase = restTemplate.postForEntity(
				getUri("/purchases"),
				PurchaseBuilder.builder().withItem(item).withStore(store).build(),
				Purchase.class)
				.getBody();

		assertThat(restTemplate.getForEntity(getUri("/brands"), Brand[].class)
				.getStatusCodeValue())
				.isEqualTo(200);

		assertThat(restTemplate.getForEntity(getUri("/stores"), Store[].class)
				.getStatusCodeValue())
				.isEqualTo(200);

		assertThat(restTemplate.getForEntity(getUri("/products"), Product[].class)
				.getStatusCodeValue())
				.isEqualTo(200);

		assertThat(restTemplate.getForEntity(getUri("/items"), Item[].class)
				.getStatusCodeValue())
				.isEqualTo(200);

		assertThat(restTemplate.getForEntity(getUri("/purchases"), Purchase[].class)
				.getStatusCodeValue())
				.isEqualTo(200);
	}
}
