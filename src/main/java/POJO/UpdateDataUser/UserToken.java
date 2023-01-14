package POJO.UpdateDataUser;

import org.example.CreatedUser;

import static io.restassured.RestAssured.given;

public class UserToken {
    private boolean success;
    private User user;
    private String accessToken;
    private String refreshToken;
    UserToken userToken;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return getAccessToken().substring(7);
    }

    public UserToken registration(CreatedUser createdUser) {
        return given()
                .header("Content-type", "application/json")
                .body(createdUser)
                .post("/api/auth/register")
                .then().extract().body()
                .as(UserToken.class);
    }

    public UserToken autorization(CreatedUser createdUser) {
        return given()
                .header("Content-type", "application/json")
                .body(createdUser)
                .post("/api/auth/login")
                .then().extract().body()
                .as(UserToken.class);
    }
}