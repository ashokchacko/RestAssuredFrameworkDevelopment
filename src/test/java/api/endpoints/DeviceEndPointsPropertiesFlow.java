package api.endpoints;

import api.payload.Device;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.ResourceBundle;
import static io.restassured.RestAssured.given;

// DeviceEndPointsPropertiesFlow class is created to perform Create, Read, Update, Delete requests in the Device API

public class DeviceEndPointsPropertiesFlow {

    //Method created for getting URL's from routes.properties file
    static ResourceBundle getURL()
    {
        ResourceBundle routes = ResourceBundle .getBundle("routes"); //Load routes.properties file //routes is the name of the properties file
        return routes;
    }

    public static Response createDevice(Device payload)
    {
        String post_url = getURL().getString("post_url");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post(post_url);
        return response;
    }

    public static Response readDevice(String id)
    {
        String get_url = getURL().getString("get_url");

        Response response = given()
                .pathParam("Id", id)
                .when()
                .get(get_url);
        return response;
    }

    public static Response updateDevice(String id, Device payload)
    {
        String update_url = getURL().getString("update_url");

        Response response = given()
                .contentType(ContentType.JSON)
                .pathParam("Id", id)
                .body(payload)
                .when()
                .put(update_url);
        return response;
    }

    public static Response deleteDevice(String id)
    {
        String delete_url = getURL().getString("delete_url");

        Response response = given()
                .pathParam("Id", id)
                .when()
                .delete(delete_url);
        return response;
    }
}
