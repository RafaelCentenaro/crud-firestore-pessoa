package org.firetore.pessoa;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class PessoaControllerTest {

	@Test
	public void testHelloEndpoint() {
		given().when().get("pessoa/listar").then().statusCode(200);
	}

}