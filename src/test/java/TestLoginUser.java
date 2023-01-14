import io.restassured.RestAssured;
import org.example.CreatedUser;
import org.example.LoginUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class TestLoginUser {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    public void loginUser() {
        LoginUser login = new LoginUser("karlos_1@mail.ru", "123456");
        CreatedUser createdUser = new CreatedUser("karlos_1@mail.ru", "123456", "Karlos");
        given()
                .header("Content-type", "application/json")
                .and()
                .body(createdUser)
                .when()
                .post("/api/auth/register");

        given()
                .header("Content-type", "application/json")
                .and()
                .body(login)
                .when()
                .post("/api/auth/login")
                .then().statusCode(200).and()
                .assertThat().body("accessToken", notNullValue()).and()
                .assertThat().body("refreshToken", notNullValue()).and()
                .assertThat().body("user.email", equalTo("karlos_1@mail.ru")).and()
                .assertThat().body("user.name", equalTo("Karlos"));
    }

    @Test
    public void loginUserWithInvalidEmail() {
        LoginUser login = new LoginUser("karlos_17293793183@mail.ru", "123456");

        given()
                .header("Content-type", "application/json")
                .and()
                .body(login)
                .when()
                .post("/api/auth/login")
                .then().statusCode(401).and()
                .assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void loginUserWithInvalidPassword() {
        LoginUser login = new LoginUser("karlos_1@mail.ru", "JFDKoje292fj");

        given()
                .header("Content-type", "application/json")
                .and()
                .body(login)
                .when()
                .post("/api/auth/login")
                .then().statusCode(401).and()
                .assertThat().body("message", equalTo("email or password are incorrect"));
    }
}
