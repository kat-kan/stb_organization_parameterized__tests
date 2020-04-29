package organizations;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class CreateOrganizationTest extends BaseTest {

    private String orgId;
    private JsonPath json;

    private static Stream<Arguments> createValidOrgData(){

        return Stream.of(
                //website starts with https
                Arguments.of("Awesome organization", "I love being a member of this organization", "awesometeam", "https://website.com"),
                //website starts with http
                Arguments.of("Awesome organization", "I love being a member of this organization", "awesomeorg", "http://website.com"),
                //minimum length of org name = 3
                Arguments.of("Awesome organization", "I love being a member of this organization", "awe", "https://website.com"),
                //name can consist of underscores
                Arguments.of("Awesome organization", "I love being a member of this organization", "awe_team", "https://website.com"),
                //name can consist of numbers
                Arguments.of("Awesome organization", "I love being a member of this organization", "awe123team", "https://website.com")
        );
    }

    private static Stream<Arguments> createInvalidOrgData(){

        return Stream.of(
                //website does not start with http or https
                Arguments.of("Awesome organization", "I love being a member of this organization", "awesometeam", "www.website.com"),
                //website is a random string
                Arguments.of("Awesome organization", "I love being a member of this organization", "awesomeorg", "kitty"),
                //org name is shorter than expected minimum length
                Arguments.of("Awesome organization", "I love being a member of this organization", "c", "https://website.com"),
                //name consists of uppercase letters
                Arguments.of("Awesome organization", "I love being a member of this organization", "KITTY", "https://website.com"),
                //name consists of symbols other than underscore
                Arguments.of("Awesome organization", "I love being a member of this organization", "!#%^&^", "https://website.com")
        );
    }

    @AfterEach
    public void afterEach(){

        orgId = json.getString("id");

        if(orgId!=null){
            given()
                    .spec(reqSpecification)
                    .pathParam("id", orgId)
                    .when()
                    .delete(BASE_URL + ORGANIZATIONS + "/{id}")
                    .then()
                    .statusCode(SC_OK);
        }
    }

    @DisplayName("Create organization with valid data")
    @ParameterizedTest(name = "Display name : {0}, desc : {1}, name : {2}, website : {3}")
    @MethodSource("createValidOrgData")
    public void createNewOrganizationTest(String displayName, String desc, String name, String website){

        Response response = given()
                .spec(reqSpecification)
                .queryParam("displayName",displayName)
                .queryParam("desc", desc)
                .queryParam("name", name)
                .queryParam("website", website)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        json = response.jsonPath();
    }

    /*createInvalidOrgData test is currently failing because of bugs in name field validation.
    * The organization with invalid data should not be created (we expect 400 status code), but it is created.
    * The test has a bit different structure to enable deleting created boards*/

    @DisplayName("Create organization with invalid data")
    @ParameterizedTest(name = "Display name : {0}, desc : {1}, name : {2}, website : {3}")
    @MethodSource("createInvalidOrgData")
    public void createNewOrganizationInvalidDataTest(String displayName, String desc, String name, String website){

        ValidatableResponse then = given()
                .spec(reqSpecification)
                .queryParam("displayName", displayName)
                .queryParam("desc", desc)
                .queryParam("name", name)
                .queryParam("website", website)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then();

        Response response = then.extract().response();
        json = response.jsonPath();
        then.statusCode(HttpStatus.SC_BAD_REQUEST);
    }



}
