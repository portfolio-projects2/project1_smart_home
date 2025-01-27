package afgangsProjekt.automation.domain.integrationTest;

import afgangsProjekt.automation.Environment;
import afgangsProjekt.automation.domain.AutomationHandler;
import afgangsProjekt.automation.domain.AutomationLogic;
import afgangsProjekt.automation.domain.mocks.SystemModelsMock;
import afgangsProjekt.automation.systemEnums.AutomationStatus;
import afgangsProjekt.automation.systemEnums.DeviceStatus;
import afgangsProjekt.automation.systemEnums.EnvironmentStatus;
import afgangsProjekt.automation.systemEnums.ParameterStatus;
import afgangsProjekt.automation.systemModels.Automation;
import afgangsProjekt.automation.systemModels.Device;
import afgangsProjekt.automation.systemModels.Parameter;
import afgangsProjekt.automation.systemModels.Sensor;
import afgangsProjekt.automation.systemRigestry.AutomationRegistry;
import afgangsProjekt.automation.systemRigestry.DevicesRegistry;
import afgangsProjekt.automation.systemRigestry.ParametersRegistry;
import afgangsProjekt.automation.systemRigestry.SensorsRegistry;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;

import static java.lang.Thread.sleep;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UnitTests {

    @Autowired
    DevicesRegistry devicesRegistry;
    @Autowired
    SensorsRegistry sensorsRegistry;
    @Autowired
    AutomationRegistry automationRegistry;
    @Autowired
    ParametersRegistry parametersRegistry;
    @Autowired
    AutomationHandler automationHandler;
    @Autowired
    Environment environment;


//The following test ensure that the system components interact successfully to perform an automation process
// The test assumes that a sensor object with valid data, a device not connected to its communication provider's server
// , an automation process with valid timeRange, and a parameter  has initialized by the system.

    @BeforeEach
    void initializeSystemProperties() {
        environment.setEnvironmentStatus(EnvironmentStatus.unitTesting);
        SystemModelsMock.initialize();

    }


    @Test
    void doAutomation() throws ParseException, InterruptedException {

        // creating the mock object instances
        Device device = SystemModelsMock.getDevice();
        Sensor sensor = SystemModelsMock.getSensor();
        Parameter parameter = SystemModelsMock.getParameter();
        Automation automation = SystemModelsMock.getAutomation();
        // fill the system registry with the mock instances
        devicesRegistry.addDevice("test", device);
        sensorsRegistry.addSensor("test", sensor);
        parametersRegistry.addParameter("test", parameter);
        automationRegistry.addAutomation("test", automation);
        // set the device status to startedManually
        device.setStatus(DeviceStatus.manuallyStartted);
        // ensure the system does not validate the automation process when the device is manually controlled
        automationHandler.doAutomation();
        Assertions.assertEquals(DeviceStatus.manuallyStartted,device.getStatus());
        // set the automation to inactive
        automation.setAutomationStatus(AutomationStatus.inactive);
        // ensure the system does not validate the automation process when it is not active
        automationHandler.doAutomation();
        Assertions.assertEquals(DeviceStatus.manuallyStartted,device.getStatus());
        // change the device status to initial
        device.setStatus(DeviceStatus.initial);
        // set the automation to active
        automation.setAutomationStatus(AutomationStatus.activ);
        // set the sensor's live value
        sensor.setLiveValue("10");
        // set the parameter's values and parameter logic
        parameter.setParameterLogic("onBetween");
        parameter.setParameterValue("{\"value1\":\"5\",\"value2\":\"15\"}");
        // set a time range for this automation satisfy the current time between 22/11/2022 to 22/11/2023
        automation.setAutomationTimeRange("{\"start\":\"22-11-2022-10-00\",\"off\":\"22-11-2023-10-30\"}");
        //ensure the system sets the device status to "active" when values and time conditions are satisfied
        automationHandler.doAutomation();
        Assertions.assertEquals(DeviceStatus.activ, SystemModelsMock.getDevice().getStatus());
        //Change the time condition to be invalid values
        automation.setAutomationTimeRange("{\"start\":\"22-11-2022-10-00\",\"off\":\"23-11-2022-10-30\"}");
        // ensure the system sets the device status to inactive when the time condition is not satisfied
       automationHandler.doAutomation();
       Assertions.assertEquals(DeviceStatus.inactive,device.getStatus());
        // set the time conditions back to valid values
        automation.setAutomationTimeRange("{\"start\":\"22-11-2022-10-00\",\"off\":\"22-11-2023-10-30\"}");
        // change the sensor value to a value does not satisfy the parameter values
        sensor.setLiveValue("30");
        automationHandler.doAutomation();
        // ensure the system sets the device status to inactive when the parameter values are not satisfied
        Assertions.assertEquals(DeviceStatus.inactive,device.getStatus());
    }

    @Test
     void DataSatisfyTimeConditions() throws ParseException,  JSONException {
        // first situation the device has startTime on 22/11/2022 10:00 o'clock and offTime on 29/11/2023 10:30
        Automation automation1 = new Automation(1,"test", AutomationStatus.activ,"{\"start\":\"22-11-2022-10-00\",\"off\":\"29-11-2023-10-30\"}");
        // the second situation the device has startTime on 22/11 10:00 o'clock and offTime on 23/11 10:30
        Automation automation2 = new Automation(1,"test", AutomationStatus.activ,"{\"start\":\"22-11-2022-10-00\",\"off\":\"23-11-2022-10-30\"}");
        // expected result first situation time is satisfied
        Assertions.assertEquals(true, AutomationLogic.checkTimeConditionIsSatisfied(automation1));
        JSONObject jsonObject2 = new JSONObject();
        // expected result second situation time is not satisfied
        Assertions.assertEquals(false, AutomationLogic.checkTimeConditionIsSatisfied(automation2));

    }


    @Test
    void validateParameterValues() throws ParseException, InterruptedException {
        Sensor sensor = SystemModelsMock.getSensor();
        sensor.setLiveValue("10");
        Parameter parameter = SystemModelsMock.getParameter();
        parameter.setParameterLogic("onBetween");
        parameter.setParameterValue("{\"value1\":\"5\",\"value2\":\"15\"}");
        sensorsRegistry.addSensor("test", sensor);
        parametersRegistry.addParameter("test", parameter);
        AutomationLogic.validateParameterValues(parameter);
        //Check that the sensor value is between the two values defined by a user
        Assertions.assertEquals(ParameterStatus.satisfied,parameter.getParameterStatus());
        // Change the sensor's live data value to not satsfying the values defined by a user
        sensor.setLiveValue("20");
        AutomationLogic.validateParameterValues(parameter);
        Assertions.assertEquals(ParameterStatus.unsatisfied,parameter.getParameterStatus());
    }

   /* @Test
     void testGetActiveHours() throws SQLException {

        String[] dataRow = new String[6];
        ArrayList<String[]> dayData= new ArrayList<>();
        HashMap<String,ArrayList<Integer>> expectedActiveHoursByDay= new HashMap<String,ArrayList<Integer>>(){{
            put("10-10-2022",new ArrayList<>(Arrays.asList(10,13)));
            put("11-10-2022",new ArrayList<>(Arrays.asList(10,11,12)));
            put("12-10-2022",new ArrayList<>(Arrays.asList(10,14)));
        }} ;
        // System.out.println(expectedActiveHoursByDay);
        HashMap<String, ArrayList<String[]>> daysMap = new HashMap<>();
        int idCounter=0;
        int hoursTestRange=5;
        int daysTestRange=3;
        int minutesTestRange=1;
        int propelatyRange=(int) ( Math.random())  ;
        for (int day = 0; day < daysTestRange; day++) {
            for (int hour = 0; hour < hoursTestRange; hour++) {
                for (int minute = 10; minute < 60; minute += minutesTestRange) {
                    idCounter++;
                    String dayDate="1" + String.valueOf(day) + "-10-2022";
                    String hourTime="1"+String.valueOf(hour) +"-"+ String.valueOf(minute)+"-00";
                    AtomicReference<String> sensorValue = new AtomicReference<>("0");
                    dataRow[0] = String.valueOf(idCounter);
                    dataRow[1] = "kitchen";
                    dataRow[2] = "occupancySensor";
                    dataRow[3] = dayDate;
                    dataRow[4] = hourTime;
                    String[] finalDataRow = dataRow;
                    String compareHour = "1"+String.valueOf( hour);
                    expectedActiveHoursByDay.forEach((d, activeHoursList)->{
                        // System.out.println(finalHour);
                        //  System.out.println(dayDate + " " +d);
                        // System.out.println(compareHour + " "+activeHoursList);
                        // System.out.println(d.equals(dayDate)&activeHoursList.contains(compareHour));
                        if(d.equals(dayDate)&activeHoursList.contains(Integer.valueOf(compareHour))){

                            sensorValue.set(String.valueOf((((int) (10 * Math.random()) & 1)|((int) (10 * Math.random()) & 1))));
                        }
                    });
                    dataRow[5]=sensorValue.get();
                    // System.out.println(finalDataRow);

                    // System.out.println(idCounter);
                    dayData.add(dataRow);
                    dataRow=new String[6];
                }



            }


        }


      //  Assertions.assertEquals(expectedActiveHoursByDay, ActiveTimeLogic.getActiveHours( dayData));

        Assertions.assertEquals(expectedActiveHoursByDay.values(), ActiveTimeLogic.getActiveHours(dayData).values());


    }*/

 /*   @Test
     void testSensorValueChanged() throws InterruptedException {
        Sensor sensor = SystemPropertiesMock.getSensor();
        System.out.println(sensor.getLiveValue());
        sensor.setLiveValue("20");
        SensorValueListener.updateSensorValue(sensor,"19");

        Assertions.assertEquals(true,SensorValueListener.isValueChanged(sensor));
        sensor.setLiveValue("20");
        SensorValueListener.updateSensorValue(sensor,"20");

        Assertions.assertEquals(false,SensorValueListener.isValueChanged(sensor));
        System.out.println("sasa");
    }*/


}

