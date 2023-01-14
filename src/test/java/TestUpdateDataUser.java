import io.restassured.RestAssured;
import org.example.CreatedUser;
import POJO.UpdateDataUser.UserToken;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.authentication;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class TestUpdateDataUser {
    public CreatedUser user;
    public UserToken userTokenRegistrarion;
    public UserToken userTokenAutorization;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        user = new CreatedUser("karlos_26@mail.ru", "123456", "Karlos");
        userTokenRegistrarion = new UserToken().registration(user);
        userTokenAutorization = new UserToken()
                .autorization(new CreatedUser("karlos_26@mail.ru", "123456"));
    }

    @Test
    public void updateEmailAuthorizationUser() {
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(userTokenAutorization.getToken())
                .and()
                .body("{\"email\":\"karman8@mail.ru\"}")
                .when()
                .patch("/api/auth/user")
                .then().statusCode(200).and()
                .assertThat().body("success", equalTo(true)).and()
                .assertThat().body("user.email", equalTo("karman8@mail.ru"));
    }

    @Test
    public void updatePasswordAuthorizationUser(){
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(userTokenAutorization.getToken())
                .and()
                .body("{\"password\":\"qwerty123\"}")
                .when()
                .patch("/api/auth/user")
                .then().statusCode(200).and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    public void updateNameAuthorizationUser() {
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(userTokenAutorization.getToken())
                .and()
                .body("{\"name\":\"KARMAN!!!\"}")
                .when()
                .patch("/api/auth/user")
                .then().statusCode(200).and()
                .assertThat().body("success", equalTo(true)).and()
                .assertThat().body("user.name", equalTo("KARMAN!!!"));
    }

    @Test
    public void updateUserNonAuthorization() {
        given()
                .header("Content-type", "application/json")
                .and()
                .body("{\"name\":\"KARMAN!!!\"}")
                .when()
                .patch("/api/auth/user")
                .then().statusCode(401).and()
                .assertThat().body("success", equalTo(false)).and()
                .assertThat().body("message", equalTo("You should be authorised"));

    }

    @After
    public void deleteUser() {
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(userTokenAutorization.getToken())
                .and()
                .when()
                .delete("api/auth/user")
                .then()
                .statusCode(202);
    }
}