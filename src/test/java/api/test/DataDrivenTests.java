package api.test;

import api.endpoints.DeviceEndPoints;
import api.payload.Device;
import api.utilities.DataProviders;
import api.utilities.XLUtility;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.IOException;

import static io.restassured.RestAssured.useRelaxedHTTPSValidation;

public class DataDrivenTests {

    public Logger logger = LogManager.getLogger(this.getClass());

    @BeforeClass
    public void setup() {
        useRelaxedHTTPSValidation(); // Skip SSL validation globally
    }

    @Test(priority = 1, dataProvider = "Data", dataProviderClass = DataProviders.class)
    public void testPostUser(String name, String color, String capacity, String idFromExcel) throws IOException
    {
        logger.debug("******* Creating user *******");
        Device devicePayload = new Device();
        devicePayload.setName(name);

        Device.Data data = new Device.Data();
        data.setColor(color);
        data.setCapacity(capacity);

        devicePayload.setData(data);

        Response response = DeviceEndPoints.createDevice(devicePayload);
        Assert.assertEquals(response.getStatusCode(), 200);

        String generatedId = response.jsonPath().getString("id");
        Assert.assertNotNull(generatedId, "Device ID should not be null after creation");

        String path = DataProviders.EXCEL_PATH;
        XLUtility xlUtil = new XLUtility(path);
        int rowNum = xlUtil.findRowByDeviceName("Sheet1", name);
        int idColumnNum = 3;

        xlUtil.clearCell("Sheet1", rowNum, idColumnNum);
        xlUtil.setCellData("Sheet1", rowNum, idColumnNum, generatedId);
        System.out.println("Device Id " + rowNum + " → " +generatedId);
        logger.debug("******* User is created *******");
    }

    @Test(priority = 2, dataProvider = "Id", dataProviderClass = DataProviders.class)
    public void testGetUserById(String id)
    {
        logger.debug("******* Reading user info *******");
        if (id == null || id.trim().isEmpty()) {
            throw new org.testng.SkipException("Skipping delete test due to missing ID");
        }
        Response response = DeviceEndPoints.readDevice(id);
        System.out.println("Device Id: " +response.jsonPath().getString("id"));
        System.out.println("Device name: " +response.jsonPath().getString("name"));
        Assert.assertEquals(response.getStatusCode(), 200);
        logger.debug("******* User info is displayed *******");
    }

    @Test(priority = 3, dataProvider = "UpdateData", dataProviderClass = DataProviders.class)
    public void testUpdateUserData(String id, String newColor, String newCapacity, String name) throws IOException
    {
        logger.debug("******* Updating user *******");
        if (id == null || id.trim().isEmpty()) {
            throw new org.testng.SkipException("Skipping update due to missing ID");
        }

        Device updatedDevice = new Device();
        updatedDevice.setName(name);

        Device.Data data = new Device.Data();
        data.setColor(newColor);
        data.setCapacity(newCapacity);
        updatedDevice.setData(data);

        Response response = DeviceEndPoints.updateDevice(id, updatedDevice);
        Assert.assertEquals(response.getStatusCode(), 200);

        System.out.println("Device → ID: " + response.jsonPath().getString("id") + ", New Color: " + response.jsonPath().getString("data.color") + ", New Capacity: " + response.jsonPath().getString("data.capacity"));

        // Optionally write the new color & capacity back to Excel
        String path = DataProviders.EXCEL_PATH;
        XLUtility xlUtil = new XLUtility(path);
        int rowNum = xlUtil.findRowByDeviceName("Sheet1", name);
        xlUtil.setCellData("Sheet1", rowNum, 1, newColor);     // Column 1 = Color
        xlUtil.setCellData("Sheet1", rowNum, 2, newCapacity);  // Column 2 = Capacity
        logger.debug("******* User is updated *******");
    }


    @Test(priority = 4, dataProvider = "Id", dataProviderClass = DataProviders.class)
    public void testDeleteUserById(String id)
    {
        logger.debug("******* Deleting user *******");
        if (id == null || id.trim().isEmpty()) {
            throw new org.testng.SkipException("Skipping delete test due to missing ID");
        }
        Response response = DeviceEndPoints.deleteDevice(id);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("message"), "Object with id = " +id+ " has been deleted.");
        System.out.println(response.jsonPath().getString("message"));
        logger.debug("******* User deleted *******");
    }

}
