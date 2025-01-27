package afgangsProjekt.automation.domain;


import afgangsProjekt.automation.Environment;
import afgangsProjekt.automation.presentation.FlowTracker;
import afgangsProjekt.automation.systemEnums.*;
import afgangsProjekt.automation.systemModels.Automation;
import afgangsProjekt.automation.systemModels.Device;
import afgangsProjekt.automation.systemModels.Parameter;
import afgangsProjekt.automation.services.IDeviceCommunication;

import afgangsProjekt.automation.systemModels.Sensor;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class AutomationLogic  {

    private static final ApplicationContext applicationContext = SystemStarter.getApplicationContext();


    public static Boolean checkTimeConditionIsSatisfied(Automation automation) throws ParseException {
       JSONObject automationTimeRange =new JSONObject(automation.getAutomationTimeRange());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");
        String currentTime = LocalDateTime.now().format(formatter);
        boolean timeSatisfied = false;

        String startTime = automationTimeRange.getString("start");
        Date startTimeDate = new SimpleDateFormat("dd-MM-yyyy-HH-mm").parse(startTime);
        Calendar startTimeCalendarInstance = Calendar.getInstance();
        startTimeCalendarInstance.setTime(startTimeDate);
        startTimeCalendarInstance.add(Calendar.DATE, 0);


        String offTime = automationTimeRange.getString("off");
        Date offTimeDate = new SimpleDateFormat("dd-MM-yyyy-HH-mm").parse(offTime);
        Calendar offTimeCalendarInstance = Calendar.getInstance();
        offTimeCalendarInstance.setTime(offTimeDate);
        offTimeCalendarInstance.add(Calendar.DATE, 0);


        Date nowTime = new SimpleDateFormat("dd-MM-yyyy-HH-mm").parse(currentTime);
        Calendar nowCalendarInstance = Calendar.getInstance();
        nowCalendarInstance.setTime(nowTime);
        nowCalendarInstance.add(Calendar.HOUR, 1);

        Date nowDate = nowCalendarInstance.getTime();
        System.out.println("now  :"+nowDate+"  first  :"+startTime +"  off  :"+offTime);
        if (nowDate.after(startTimeCalendarInstance.getTime()) && nowDate.before(offTimeCalendarInstance.getTime())) {
            timeSatisfied = true;
        }
        return timeSatisfied;
    }





    public static void setDeviceBehavior(Device device)  {
        AtomicBoolean providerFound= new AtomicBoolean(false);
        Environment environment =(Environment) applicationContext.getBean("Environment");
        if (!environment.getEnvironmentStatus().equals(EnvironmentStatus.unitTesting)) {
            FlowTracker.trackFlow("Try connect to the communication provider for this device");
            applicationContext.getBeansOfType(IDeviceCommunication.class).forEach((k, v) -> {
                System.out.println(v.getCommunicationProviders());
                if (v.getCommunicationProviders().contains(device.getCommunicationProvider())) {
                    FlowTracker.trackFlow("Communication provider found");
                    providerFound.set(true);
                    boolean connectionValid=false;
                    try {
                         connectionValid= v.checkConnection(device.getCommunicationProvider());

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }


                        if(connectionValid){
                            try {
                                v.setDeviceBehavior(device.getDeviceId(), device.getCommunicationProvider(), device.getStatus());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {

                            FlowTracker.trackFlow("communication provider server not connected");
                            device.setStatus(DeviceStatus.communicationProviderDown);
                            FlowTracker.trackFlow("Setting the device status to CommunicationProviderDown");

                        }
                }
            });
            if (!providerFound.get()) {

                FlowTracker.trackFlow("Communication service provider not found");
                device.setStatus(DeviceStatus.notRegistredYet);
                FlowTracker.trackFlow("Setting the device status to NotRegistredYet");

            }
        }
    }




    public static boolean validateParameterValues(Parameter parameter) {
        boolean valuesSatisfied=false;
        String parameterLogic = parameter.getParameterLogic();


        switch (parameterLogic) {
            case "onBetween":
                try {


                    JSONObject jsonObject = new JSONObject(parameter.getParameterValue());
                    String value1 = jsonObject.getString("value1");
                    String value2 = jsonObject.getString("value2");

                    if (Integer.parseInt(value1) < Integer.parseInt(parameter.getSensor().getLiveValue()) && Integer.valueOf(parameter.getSensor().getLiveValue()) < Integer.valueOf(value2)) {
                        parameter.setParameterStatus(ParameterStatus.satisfied);
                        valuesSatisfied = true;

                    } else {
                        parameter.setParameterStatus(ParameterStatus.unsatisfied);
                        valuesSatisfied = false;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case "onOnly":
                try {


                    if (parameter.getSensor().getLiveValue().equals(parameter.getParameterValue())) {
                        parameter.setParameterStatus(ParameterStatus.satisfied);
                        valuesSatisfied = true;

                    } else {
                        parameter.setParameterStatus(ParameterStatus.unsatisfied);
                        valuesSatisfied = false;

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }

return valuesSatisfied;



}


    public static boolean isDeviceManuallyControlled(Device device) {
        boolean controllStatus= false;
        if (device.getStatus().equals(DeviceStatus.manuallyStartted)||device.getStatus().equals(DeviceStatus.manuallyStopped)){
            controllStatus=true;
        }
        return controllStatus;
    }

    public static boolean isAutomationActive(Automation automation) {
        boolean active= false;
        if (automation.getAutomationStatus().equals(AutomationStatus.activ)){
            active=true;
        }
        return active;
    }



    public static boolean isSensorActive(Sensor sensor) {
        if (sensor.getSensorStatus().equals(SensorStatus.activ)){
            return true;
        }

    return false;
    }
}
