package base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {

    protected static final String BASE_URL = "https://api.trello.com/1/";
    protected static final String ORGANIZATIONS = "organizations";

    protected static String key;
    protected static String token;
    protected static RequestSpecification reqSpecification;

    @BeforeAll
    public static void beforeAll() {

        key = System.getProperty("key");
        token = System.getProperty("token");

        reqSpecification = new RequestSpecBuilder()
                .addQueryParam("key", key)
                .addQueryParam("token", token)
                .setContentType(ContentType.JSON)
                .build();
    }
}
