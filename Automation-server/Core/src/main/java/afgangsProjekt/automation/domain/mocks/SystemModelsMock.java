package afgangsProjekt.automation.domain.mocks;

import afgangsProjekt.automation.systemEnums.AutomationStatus;
import afgangsProjekt.automation.systemEnums.DeviceStatus;
import afgangsProjekt.automation.systemEnums.ParameterStatus;
import afgangsProjekt.automation.systemEnums.SensorStatus;
import afgangsProjekt.automation.systemModels.Automation;
import afgangsProjekt.automation.systemModels.Device;
import afgangsProjekt.automation.systemModels.Parameter;
import afgangsProjekt.automation.systemModels.Sensor;

public class SystemModelsMock {

   static Sensor sensor;
   static Device device;
   static Automation automation;
   static Parameter parameter;

    public static void setSensor(Sensor sensor) {
        SystemModelsMock.sensor = sensor;
    }

    public static void setDevice(Device device) {
        SystemModelsMock.device = device;
    }

    public static void setAutomation(Automation automation) {
        SystemModelsMock.automation = automation;
    }

    public static void setParameter(Parameter parameter) {
        SystemModelsMock.parameter = parameter;
    }

    public static Sensor getSensor() {
        return sensor;
    }

    public static Device getDevice() {
        return device;
    }

    public static Automation getAutomation() {
        return automation;
    }

    public static Parameter getParameter() {
        return parameter;
    }



    public static void initialize(){

         sensor = new Sensor(1,"testSensor","19", SensorStatus.activ,"DEVIDSSSSSD");
         device = new Device(1,"rest2", DeviceStatus.initial,"testDevice");
         automation = new Automation(
                1,
                "testAutomation",
                AutomationStatus.activ,
                "{\"start\":"+"\"10-11-2022-14-49\","+"\"off\":"+"\"04-12-2022-14-53\"}"  );
        String parameter1Logic = "onBetween";
        String parameter2Logic = "onOnly";
        String parameter1Value = "{\"value1\":\"18\",\"value2\":\"21\"}";
        String parameter2Value = "20";
        String sensorValue = "19";
         parameter = ( new Parameter(
                1,
                sensor,
                device,
                automation,
                parameter1Logic,
                parameter1Value,
                ParameterStatus.satisfied));

    }

}
