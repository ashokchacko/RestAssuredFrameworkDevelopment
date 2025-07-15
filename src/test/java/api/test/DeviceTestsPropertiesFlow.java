package api.test;

import api.endpoints.DeviceEndPointsPropertiesFlow;
import api.payload.Device;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.useRelaxedHTTPSValidation;

public class DeviceTestsPropertiesFlow {

    public Logger logger; // for logs

    Faker faker;
    Device devicePayload;
    String id;

    @BeforeClass
    public void setup()
    {
        useRelaxedHTTPSValidation(); // Skip SSL validation globally

        faker = new Faker();
        devicePayload = new Device();
        devicePayload.setName("Google Pixel 6 Pro");

        Device.Data data = new Device.Data();
        data.setColor(faker.color().name());
        data.setCapacity("128 GB");

        devicePayload.setData(data);

        //logs
        logger = LogManager.getLogger(this.getClass());
    }

    @Test(priority = 1)
    public void testPostDevice()
    {
        logger.debug("******* Creating user *******");
        Response response = DeviceEndPointsPropertiesFlow.createDevice(devicePayload);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 200);

        id = response.jsonPath().getString("id");
        Assert.assertNotNull(id, "Device ID should not be null after creation");
        logger.debug("******* User is created *******");
    }

    @Test(priority = 2, dependsOnMethods = "testPostDevice")
    public void testGetDeviceByID()
    {
        logger.debug("******* Reading user info *******");
        Response response = DeviceEndPointsPropertiesFlow.readDevice(id);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("name"), devicePayload.getName());
        logger.debug("******* User info is displayed *******");
    }

    @Test(priority = 3, dependsOnMethods = "testPostDevice")
    public void testPutDevice()
    {
        logger.debug("******* Updating user *******");
        devicePayload.getData().setColor(faker.color().name());
        devicePayload.getData().setCapacity("256 GB");

        Response response = DeviceEndPointsPropertiesFlow.updateDevice(id, devicePayload);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("data.color"), devicePayload.getData().getColor());
        Assert.assertEquals(response.jsonPath().getString("data.capacity"), devicePayload.getData().getCapacity());
        logger.debug("******* User is updated *******");
    }

    @Test(priority = 4, dependsOnMethods = "testPutDevice")
    public void testDeleteDeviceByID()
    {
        logger.debug("******* Deleting user *******");
        Response response = DeviceEndPointsPropertiesFlow.deleteDevice(id);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("message"), "Object with id = " +id+ " has been deleted.");
        logger.debug("******* User deleted *******");
    }
}
