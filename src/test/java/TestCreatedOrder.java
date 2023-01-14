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

public class TestCreatedOrder {
    public CreatedUser user;
    public UserToken userTokenRegistrarion;
    public UserToken userTokenAutorization;
    public CreatedOrder order;
    public CreatedOrder orderNonIngridienst;
    public CreatedOrder orderFalseHashIngridienst;


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        user = new CreatedUser("karlos_26@mail.ru", "123456", "Karlos");
        userTokenRegistrarion = new UserToken().registration(user);
        userTokenAutorization = new UserToken()
                .autorization(new CreatedUser("karlos_26@mail.ru", "123456"));

        order = new CreatedOrder(List.of("61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6f"));
        orderNonIngridienst = new CreatedOrder();
        orderFalseHashIngridienst = new CreatedOrder(List.of("123","32!!!aa6f"));
    }

    @Test
    public void createdOrderAutorization() {
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(userTokenAutorization.getToken())
                .and()
                .body(order)
                .when()
                .post("/api/orders")
                .then().statusCode(200).and()
                .assertThat().body("success", equalTo(true)).and()
                .assertThat().body("order.number", notNullValue());
    }

    //В документации нет данных про создания заказа без токена авторизации.
    //По логике должна быть прикручена 401 ошибка и месседж - You should be authorised
    //Тест падает, так как он написан с ожиданием 401 ошибки
    //А по факту выходит так, что можно создать заказ без авторизации - это баг
    @Test
    public void createdOrderNonAutorization() {
        given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/orders")
                .then().statusCode(401).and()
                .assertThat().body("success", equalTo(false)).and()
                .assertThat().body("message",  equalTo("You should be authorised"));
    }

    @Test
    public void createdOrderNonIndigrients() {
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(userTokenAutorization.getToken())
                .and()
                .body(orderNonIngridienst)
                .when()
                .post("/api/orders")
                .then().statusCode(400).and()
                .assertThat().body("success", equalTo(false)).and()
                .assertThat().body("message",  equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void createdOrderFalseHashIngridienst() {
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(userTokenAutorization.getToken())
                .and()
                .body(orderFalseHashIngridienst)
                .when()
                .post("/api/orders")
                .then().statusCode(500);
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
