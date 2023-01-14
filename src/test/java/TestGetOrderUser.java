import POJO.UpdateDataUser.UserToken;
import io.restassured.RestAssured;
import org.example.CreatedOrder;
import org.example.CreatedUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class TestGetOrderUser {
    public CreatedUser user;
    public UserToken userTokenRegistrarion;
    public UserToken userTokenAutorization;
    public CreatedOrder order;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        user = new CreatedUser("karlos_26@mail.ru", "123456", "Karlos");
        userTokenRegistrarion = new UserToken().registration(user);
        userTokenAutorization = new UserToken()
                .autorization(new CreatedUser("karlos_26@mail.ru", "123456"));

        order = new CreatedOrder(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
    }

    @Test
    public void getOrderAutorization() {
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(userTokenAutorization.getToken())
                .and()
                .body(order)
                .when()
                .post("/api/orders");

        given()
                .header("Content-type", "application/json")
                .auth().oauth2(userTokenAutorization.getToken())
                .and()
                .when()
                .get("/api/orders")
                .then().statusCode(200).and()
                .assertThat().body("success", equalTo(true)).and()
                .assertThat().body("orders", notNullValue());
    }

    @Test
    public void getOrderNonAutorization() {
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(userTokenAutorization.getToken())
                .and()
                .body(order)
                .when()
                .post("/api/orders");

        given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get("/api/orders")
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
