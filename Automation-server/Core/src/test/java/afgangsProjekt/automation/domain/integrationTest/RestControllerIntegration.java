
package afgangsProjekt.automation.domain.integrationTest;

import afgangsProjekt.automation.domain.WheatherApiConnection;
import afgangsProjekt.automation.domain.mocks.SystemModelsMock;
import afgangsProjekt.automation.systemEnums.DeviceStatus;
import afgangsProjekt.automation.systemModels.Device;
import afgangsProjekt.automation.systemModels.Sensor;
import afgangsProjekt.automation.systemRigestry.AutomationRegistry;
import afgangsProjekt.automation.systemRigestry.DevicesRegistry;
import afgangsProjekt.automation.systemRigestry.ParametersRegistry;
import afgangsProjekt.automation.systemRigestry.SensorsRegistry;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RestControllerIntegration {
    String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ii1LSTNROW5OUjdiUm9meG1lWm9YcWJIWkdldyIsImtpZCI6Ii1LSTNROW5OUjdiUm9meG1lWm9YcWJIWkdldyJ9.eyJhdWQiOiJhcGk6Ly9kNTQ5ZDg3ZS04ZDRhLTRiY2YtOWFmYy0wNDJjMGNkZjk0NjUiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9iZDA3NDgwYy1lN2Q3LTQ3N2EtOGMwMi0wZWU3ZjcyOWVmYTQvIiwiaWF0IjoxNjc1MTk4NDEyLCJuYmYiOjE2NzUxOTg0MTIsImV4cCI6MTY3NTIwMzMwNSwiYWNyIjoiMSIsImFpbyI6IkFhUUFXLzhUQUFBQTQxeW9zbk5lWXZVNCtoaC84ZlRyQ1d6c2hkM2JYQTV6TW9teFB3SmE1RGYydHV3SmRaZmtEc20zdmxTaVBhTVRYZW1vVkVTbzZwdUdQemxDdlRYRkZLZm9KZkhYdGRMN0h5UG9hNnd5amlURlNIWGQ2VkQwWEc1WHBINlNBUzJ6NVJhQ1NSb0hIYnBHRHlPRmd2SXVqVDJoeWtpaXZreDdXRG1za09vazdEaHBXWnRNNTRjTWJzVmZ6ZkRZdjdiemM0ZUJzWE9MTGtDTVhVS09yZ2VkK3c9PSIsImFtciI6WyJwd2QiLCJtZmEiXSwiYXBwaWQiOiJkNTQ5ZDg3ZS04ZDRhLTRiY2YtOWFmYy0wNDJjMGNkZjk0NjUiLCJhcHBpZGFjciI6IjEiLCJlbWFpbCI6Im5hd3Jhc3N0b3JhZ2V0ZXN0QGdtYWlsLmNvbSIsImZhbWlseV9uYW1lIjoiTW8iLCJnaXZlbl9uYW1lIjoiTmF3cmFzIiwiaWRwIjoibGl2ZS5jb20iLCJpcGFkZHIiOiI4MC42Mi4xMTcuMTcxIiwibmFtZSI6Ik5hd3JhcyBNbyIsIm9pZCI6IjYxODU0YzY4LWI5YWItNDFjYS05OTM4LWNjZWFhMGI5M2JlNyIsInJoIjoiMC5BWGtBREVnSHZkZm5la2VNQWc3bjl5bnZwSDdZU2RWS2pjOUxtdndFTEF6ZmxHV1VBSXMuIiwic2NwIjoiYWxsIiwic3ViIjoidERibk5RVWpuMm9HMGprYmVsdk5CUHhhdmFqWHlzVWd5TXozY0VuaVh2dyIsInRpZCI6ImJkMDc0ODBjLWU3ZDctNDc3YS04YzAyLTBlZTdmNzI5ZWZhNCIsInVuaXF1ZV9uYW1lIjoibGl2ZS5jb20jbmF3cmFzc3RvcmFnZXRlc3RAZ21haWwuY29tIiwidXRpIjoiWU5CZzVTZ295VTZ3b09TTDdycFRBQSIsInZlciI6IjEuMCJ9.SoNx7kA7coXsUcToGO0lk_uomqhP0Bcdcn81fTW6HAm4qkVTWeEsBhl3zYzYcnqewR1ZzE8kWiraqCIQoZ-8_I2sg0i9IolPhWwHA7wq_IH3LhulUG_FQgUrG3xgyDaHM0t1deoXGu6SHgPWCje41yRmGo1BXojcgcYUG_8weMzkScHiDLW5ZfxWb3oWPgXd5fHIeEI6n3wOCuI_sZz94Du-ZbIN5A2TAgClOjB3CnkZO2Lx14Gr7mQdgc1aoJjNQxuI0wJq5WbqHCHHHsS2oe2RuHBkn932ZJVWlvx-ycNR8O1WvdzjsTE7JGJVLrL-KiPTcN1abztuU98y9dyT7g";
    @Autowired
    DevicesRegistry devicesRegistry;
    @Autowired
    SensorsRegistry sensorsRegistry;
    @Autowired
    AutomationRegistry automationRegistry;
    @Autowired
    ParametersRegistry parametersRegistry;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    WebApplicationContext webApplicationContext;




    @BeforeEach
    void initializeSystem(){
        SystemModelsMock.initialize();
        System.out.println("Mocks initialized");
        devicesRegistry.addDevice("test", SystemModelsMock.getDevice());
        sensorsRegistry.addSensor("test", SystemModelsMock.getSensor());
        parametersRegistry.addParameter("test", SystemModelsMock.getParameter());
        automationRegistry.addAutomation("test", SystemModelsMock.getAutomation());
    }


    @Test
    void addSensor() throws Exception {

        String requestBody = "{\"databaseName\":\"test\",\"sensorId\":"+10+",\"devId\":\"testDevId\""+",\"name\":\"testSensor\""+ " }";

        MvcResult mvcResult = mockMvc
                .perform(
                        MockMvcRequestBuilders.post("http://localhost:8080/addSensor").header("authorization","Bearer"+" "+token).content(requestBody)).andReturn();

        // assert that the request reaches the server
        Assert.assertEquals( mvcResult.getResponse().getStatus(),200);
        // assert the sensor is existing in the registry
        Assert.assertNotEquals(sensorsRegistry.getSensor("testDevId"),null);
    }

@Test
void removeSensor() throws Exception {

//get the Mocked sensor
    Sensor sensor=SystemModelsMock.getSensor();

    String devId=sensor.getdev_Id();
    String requestBody = "{\"databaseName\":\"test\",\"devId\":\""+devId+"\"}";

    MvcResult mvcResult = mockMvc
            .perform(
                    MockMvcRequestBuilders.post("http://localhost:8080/deleteSensor").header("authorization","Bearer"+" "+token).content(requestBody)).andReturn();

    // assert that the request reaches the server
    Assert.assertEquals( mvcResult.getResponse().getStatus(),200);
    // assert the sensor is removed from the registry
Assert.assertEquals(sensorsRegistry.getAllSensors("test").get("sensors").contains(sensor),false);
}
//this test ensures changing device status manually

    @Test
    void stopDeviceManually() throws Exception {
        String requestBody = "{\"databaseName\":\"test\"}";

        Device device = devicesRegistry.getallDevices("test").get("devices").get(0);
        //set the device status to initial
        device.setStatus(DeviceStatus.initial);
        MvcResult mvcResult = mockMvc
                .perform(
                        MockMvcRequestBuilders.post("http://localhost:8080/stopDevice/"+device.getDeviceId()).header("authorization","Bearer"+" "+token).content(requestBody)).andReturn();
        // assert that the request reaches the server
        Assert.assertEquals( mvcResult.getResponse().getStatus(),200);
        // assert that deviceStatus for device with id 1 is changed
        Assert.assertNotEquals(device.getStatus(),DeviceStatus.initial);
    }

    @Test
    void startDeviceManually() throws Exception {
        String requestBody = "{\"databaseName\":\"test\"}";
        Device device = devicesRegistry.getallDevices("test").get("devices").get(0);
        device.setStatus(DeviceStatus.initial);
        MvcResult mvcResult = mockMvc
                .perform(
                        MockMvcRequestBuilders.post("http://localhost:8080/startDevice/1").header("authorization","Bearer"+" "+token).content(requestBody)).andReturn();
        // assert that the request reaches the server
        Assert.assertEquals( mvcResult.getResponse().getStatus(),200);
        // assert that deviceStatus for device with id 1 is changed
        Assert.assertNotEquals(device.getStatus(),DeviceStatus.initial);
    }

    @Test
    void stopManualControl() throws Exception {
       //Getting the mock device created within beforeEach method
        Device device = SystemModelsMock.getDevice();
        device.setDeviceId(3);
        String body ="{\"deviceId\":3,\"databaseName\":\"test\"}";
       //Setting device status to manuallyStarted
        device.setStatus(DeviceStatus.manuallyStartted);
        MvcResult mvcResult = mockMvc
                .perform(
                        MockMvcRequestBuilders.post("http://localhost:8080/stopManualControl").
                                header("authorization","Bearer"+" "+token).content(body)).andReturn();
        // assert that the request reaches the server
        Assert.assertEquals( mvcResult.getResponse().getStatus(),200);
        // assert that deviceStatus is not manuallyStarted
        Assert.assertNotEquals(device.getStatus(),DeviceStatus.manuallyStartted);
        Assert.assertEquals(device.getStatus(),DeviceStatus.initial);


    }
    @Test
    void getSystemInfo() throws Exception {
        Gson gson = new Gson();

        String sensors = gson.toJson(sensorsRegistry.getAllSensors("test"));
        String devices = gson.toJson(devicesRegistry.getallDevices("test"));
        String wheather = gson.toJson(WheatherApiConnection.getWeathrtData());
        MvcResult mvcResult = mockMvc
                .perform(
                        MockMvcRequestBuilders.get("http://localhost:8080/getAllInfo/test").header("authorization","Bearer"+" "+token)).andReturn();

        System.out.println("fromTest"+ wheather);
        Assert.assertEquals( mvcResult.getResponse().getStatus(),200);

        Assert.assertEquals( mvcResult.getResponse().getContentAsString(),"["+devices+","+ sensors +","+wheather+"]");

    }


}

