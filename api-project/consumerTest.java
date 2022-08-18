package liveProject;

import arrow.typeclasses.Hash;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
public class consumerTest {
    // Create Map for the headers
    Map<String, String> reqHeaders = new HashMap<String, String>();
    // Set resource URI
    String resourcePath = "/api/users";

    // Create Pact contract
    @Pact(consumer = "UserConsumer", provider = "UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder) throws ParseException {
        reqHeaders.put("Content-Type", "application/json");

        DslPart reqResBody = new PactDslJsonBody()
                .numberType("id", 111)
                .stringType("firstName", "Ketu")
                .stringType("lastName", "Pen")
                .stringType("email", "ketu@test.com");

        return builder.given("Request to create a user")
                .uponReceiving("Request to create a user")
                .method("POST")
                .headers(reqHeaders)
                .path(resourcePath)
                .body(reqResBody)
                .willRespondWith()
                .status(201)
                .body(reqResBody)
                .toPact();
    }

    @Test
    @PactTestFor(providerName = "UserProvider", port = "8282")
    public void consumerSideTest() {
        String baseURI = "http://localhost:8282";

        //request body
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("id", 123);
        reqBody.put("firstName", "Ketaki");
        reqBody.put("lastName", "Pendse");
        reqBody.put("email", "ketu1@test.com");

        //generate response
        given().headers(reqHeaders).body(reqBody)
                .when().post(baseURI + resourcePath)
                .then().log().all().statusCode(201);

    }

}
