package afgangsProjekt.automation.domain;

import afgangsProjekt.automation.Environment;
import afgangsProjekt.automation.presentation.FlowTracker;
import afgangsProjekt.automation.systemEnums.AutomationStatus;
import afgangsProjekt.automation.systemEnums.EnvironmentStatus;
import afgangsProjekt.automation.systemModels.Automation;
import afgangsProjekt.automation.systemModels.Device;
import afgangsProjekt.automation.systemModels.Parameter;
import afgangsProjekt.automation.systemEnums.DeviceStatus;
import afgangsProjekt.automation.systemEnums.ParameterStatus;
import afgangsProjekt.automation.systemModels.Sensor;
import afgangsProjekt.automation.systemRigestry.DevicesRegistry;
import afgangsProjekt.automation.systemRigestry.ParametersRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.text.ParseException;
import java.util.ArrayList;

@Component("AutomationHandlerBean")
public class AutomationHandler implements Runnable, IAutomationHandler {
    FlowTracker flowTracker;
    Environment environment;

@Autowired
    public AutomationHandler(FlowTracker _flowTracker,Environment _environment) {
    flowTracker=_flowTracker;
    environment=_environment;
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void doAutomation() throws ParseException, InterruptedException {

        FlowTracker.trackFlow("AutomationValidation started");
        // get All Parameters
        ArrayList<Parameter> parameters = ParametersRegistry.getAllParameters();
        FlowTracker.trackFlow("Iterating parameters ");
        for (Parameter p: parameters) {
            // get parameter device
            Device device = p.getDevice();
            // get parameter sensor
            Sensor sensor = p.getSensor();
            // get parameter automation
            Automation automation = p.getAutomation();
            // is device manually controlled?
            boolean deviceManuallyControlled= AutomationLogic.isDeviceManuallyControlled(device);
            // is automation process active?
            boolean automationActive= AutomationLogic.isAutomationActive(automation);
            // is automation time satisfied?
            boolean automationTimeSatisfied= AutomationLogic.checkTimeConditionIsSatisfied(automation);
            // is parameter values satisfied
            boolean parameterValuesSatisfied=AutomationLogic.validateParameterValues(p);
            FlowTracker.trackFlow(
                    "Automation validation result: \n" +
                    "Parameter :"+p.getId()+" values("+p.getParameterValue()+") satisfy sensor(" +
                            p.getSensor().getSensorName()+")which has live value("+p.getSensor().getLiveValue()+")Result --> Parameter status :"+p.getParameterStatus().toString()+"\n"+
                    "The automation with name : "+ automation.getAutomationTitle() + ", is :" + automation.getAutomationStatus()+"\n"+
                    "Time conditions are :" + automationTimeSatisfied + "\n"+
                    "The device " + device.getDeviceName() + "is :" + device.getStatus());

            if (automationActive&&!deviceManuallyControlled){

                if(parameterValuesSatisfied&&automationTimeSatisfied){
                    device.setStatus(DeviceStatus.activ);
                    FlowTracker.trackFlow("Setting device "+ device.getDeviceName() + "status to active");

                }else {
                    device.setStatus(DeviceStatus.inactive);
                    FlowTracker.trackFlow("Setting device "+ device.getDeviceName() + "status to inactive");

                }
            }

               AutomationLogic.setDeviceBehavior(device);

        }
        FlowTracker.trackFlow("done");
    flowTracker.printFlow();
    }


    // When a device stop/start manually
@Override
    public  void stopDeviceManually(String databaseName,int deviceId)  {
        FlowTracker.trackFlow("Device :" + deviceId + " is stopped manually");
        Device device = DevicesRegistry.getDevice(databaseName,deviceId);
        device.setStatus(DeviceStatus.manuallyStopped);
            AutomationLogic.setDeviceBehavior(device);

    }
    @Override
    public  void startDeviceManually(String databaseName,int deviceId)  {
        FlowTracker.trackFlow("Device :" + deviceId + " is started manually");
        Device device = DevicesRegistry.getDevice(databaseName,deviceId);
        device.setStatus(DeviceStatus.manuallyStartted);
        AutomationLogic.setDeviceBehavior(device);

    }
    @Override
    public  void stopManualControl(String databaseName,int deviceId)  {
        FlowTracker.trackFlow("Stop manual control for device :" + deviceId );
        Device device = DevicesRegistry.getDevice(databaseName,deviceId);
        device.setStatus(DeviceStatus.initial);


    }





    @Override
    public void run() {
        System.out.println("Environment is  :"+environment.getEnvironmentStatus());
    if (!environment.getEnvironmentStatus().equals(EnvironmentStatus.unitTesting)){
        while (true) {
            try {
                doAutomation();
            } catch ( InterruptedException | ParseException e) {
                e.printStackTrace();
            }
        }

    }
}
}

