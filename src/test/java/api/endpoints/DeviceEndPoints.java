package api.endpoints;
import api.payload.Device;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

// DeviceEndPoints class is created to perform Create, Read, Update, Delete requests in the Device API

public class DeviceEndPoints {

    public static Response createDevice(Device payload)
    {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post(Routes.post_url);
        return response;
    }

    public static Response readDevice(String id)
    {
        Response response = given()
                .pathParam("Id", id)
                .when()
                .get(Routes.get_url);
        return response;
    }

    public static Response updateDevice(String id, Device payload)
    {
        Response response = given()
                .contentType(ContentType.JSON)
                .pathParam("Id", id)
                .body(payload)
                .when()
                .put(Routes.update_url);
        return response;
    }

    public static Response deleteDevice(String id)
    {
        Response response = given()
                .pathParam("Id", id)
                .when()
                .delete(Routes.delete_url);
        return response;
    }
}
