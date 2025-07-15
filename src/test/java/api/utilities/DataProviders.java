package api.utilities;

import org.testng.annotations.DataProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataProviders {

    public static final String EXCEL_PATH = System.getProperty("user.dir") + "//testdata//UserData.xlsx";

    @DataProvider(name="Data")
    public  String[][] getAllData() throws IOException {
        String path = EXCEL_PATH;
        XLUtility xl = new XLUtility(path);

        int rowcount  = xl.getRowCount("Sheet1");
        int colcount = xl.getCellCount("Sheet1", 1);
        if (colcount == 0) throw new IOException("No columns found in Sheet1 row 1");

        String apidata[][] = new String[rowcount][colcount];

        for (int i = 1; i <= rowcount; i++) {
            for (int j = 0; j < colcount; j++) {
                apidata[i - 1][j] = xl.getCellData("Sheet1", i, j);
            }
            System.out.println("Loading data row " + i + ": " + java.util.Arrays.toString(apidata[i - 1]));
        }
        return apidata;
    }

    @DataProvider(name="Id")
    public String[] getId() throws IOException {
        String path = EXCEL_PATH;
        XLUtility xl = new XLUtility(path);

        int rowcount = xl.getRowCount("Sheet1");

        String apidata[] = new String[rowcount];

        for (int i = 1; i <= rowcount; i++) {
            String id = xl.getCellData("Sheet1", i, 3);
            if (id == null || id.isEmpty()) {
                System.out.println("⚠️ Warning: Missing ID in row " + i);
            }
            apidata[i - 1] = id;
        }
        return apidata;
    }

    @DataProvider(name = "UpdateData")
    public Object[][] getUpdatedDeviceData() throws IOException {
        XLUtility xl = new XLUtility(EXCEL_PATH);
        int rowCount = xl.getRowCount("Sheet1");

        List<Object[]> updatedRows = new ArrayList<>();

        for (int i = 1; i <= rowCount; i++) {
            String name     = xl.getCellData("Sheet1", i, 0);
            String color    = xl.getCellData("Sheet1", i, 1);
            String capacity = xl.getCellData("Sheet1", i, 2);
            String id       = xl.getCellData("Sheet1", i, 3);

            if (id != null && !id.trim().isEmpty()) {
                // Example update logic: append "Updated" to color/capacity
                String newColor    = color + " Updated";
                String newCapacity = capacity + " Modified";
                updatedRows.add(new Object[] {id, newColor, newCapacity, name});
            }
        }

        return updatedRows.toArray(new Object[0][0]);
    }
}
