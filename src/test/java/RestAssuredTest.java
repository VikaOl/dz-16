import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import io.restassured.http.ContentType;
import static org.testng.Assert.assertEquals;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class RestAssuredTest {
    @BeforeTest
    public void setup() {
        RestAssured.baseURI = "http://restful-booker.herokuapp.com";
    }

    @Test
    public void getBookingIdTest() {
        Response response = RestAssured.get("/booking");
        response.then().statusCode(200);
        String responseBody = response.getBody().asString();
        System.out.println("Response Body: " + responseBody);


        int[] bookingIds = response.jsonPath().get("bookingid");
        for (int bookingId : bookingIds) {
            System.out.println("Booking ID: " + bookingId);
        }
    }

    @Test
    public void createBooking(){

        CreateBookingBody body = new CreateBookingBody().builder()
                .firstname("test")
                .lastname("test")
                .totalprice(123)
                .depositpaid(true)
                /*.checkin("2022-12-04")
                .checkout("2022-12-04")*/
                .additionalneeds("abc")
                .build();


        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(body)
                .post("/booking");

        response.prettyPrint();
    }

    @Test
    public void updateBookingPriceTest() {

        Response getResponse = RestAssured.get("/booking");
        int statusCode = getResponse.getStatusCode();
        assertEquals(statusCode, 200);

        int bookingId = getResponse.jsonPath().getInt("[0].bookingid");

        String requestBody = "{\"totalprice\": 100}";

        Response updateResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/booking" + bookingId);

        int updateStatusCode = updateResponse.getStatusCode();
        String responseBody = updateResponse.getBody().asString();

        System.out.println("Actual Status Code: " + updateStatusCode);
        System.out.println("Response Body: " + responseBody);

        assertEquals(updateStatusCode, 200);
    }
    private int bookingId = 470;
@Test
public void updateBookingDetailsTest() {

    String newFirstName = "Test";
    String newAdditionalNeeds = "Book for month";

    given()
            .contentType("application/json")
            .body("{\"firstname\":\"" + newFirstName + "\",\"additionalneeds\":\"" + newAdditionalNeeds + "\"}")
            .when()
            .put("/booking" + bookingId)
            .then()
            .statusCode(200)
            .body("firstname", equalTo(newFirstName))
            .body("additionalneeds", equalTo(newAdditionalNeeds));
}

    @Test
    public void deleteBookingTest() {
        Response getResponse = RestAssured.get("/booking");
        int statusCode = getResponse.getStatusCode();
        assertEquals(statusCode, 200);

        int bookingId = getResponse.jsonPath().getInt("[0].bookingid");
        Response deleteResponse = RestAssured.delete("/booking" + bookingId);

        int deleteStatusCode = deleteResponse.getStatusCode();
        assertEquals(deleteStatusCode, 201);

        String responseBody = deleteResponse.getBody().asString();
        System.out.println("Response Body: " + responseBody);
    }
}
