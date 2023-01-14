import io.restassured.RestAssured;
import org.example.CreatedUser;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class TestCreatedUser{
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    public void createdUser() {
        CreatedUser createdUser = new CreatedUser("karlo3245333@mail.ru", "123456", "Karlos");

        given()
                .header("Content-type", "application/json")
                .and()
                .body(createdUser)
                .when()
                .post("/api/auth/register")
                .then().statusCode(200).and()
                .assertThat().body("accessToken", notNullValue()).and()
                .assertThat().body("refreshToken", notNullValue());
    }

    @Test
    public void createdExistingUser() {
        CreatedUser createdUser = new CreatedUser("karlosina@mail.ru", "123456", "Karlos");

        given()
                .header("Content-type", "application/json")
                .and()
                .body(createdUser)
                .when()
                .post("/api/auth/register")
                .then().statusCode(403).and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    public void createdUserWithoutRequiredField() {
        CreatedUser createdUser = new CreatedUser("karlos@mail.ru", "123456");

        given()
                .header("Content-type", "application/json")
                .and()
                .body(createdUser)
                .when()
                .post("/api/auth/register")
                .then().statusCode(403).and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
