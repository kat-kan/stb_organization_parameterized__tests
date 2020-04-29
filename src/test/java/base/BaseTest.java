package base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {

    protected static final String KEY = "YOUR KEY";
    protected static final String TOKEN = "YOUR TOKEN";
    protected static final String BASE_URL = "https://api.trello.com/1/";
    protected static final String ORGANIZATIONS = "organizations";

    protected static RequestSpecification reqSpecification;

    @BeforeAll
    public static void beforeAll(){

        reqSpecification = new RequestSpecBuilder()
                .addQueryParam("key", KEY)
                .addQueryParam("token", TOKEN)
                .setContentType(ContentType.JSON)
                .build();
    }
}
