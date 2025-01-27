package afgangsProjekt.automation.domain;

import java.text.ParseException;

public interface IAutomationHandler {
    public void doAutomation() throws ParseException, InterruptedException;
    public void stopDeviceManually(String databaseName,int deviceId);
    public void startDeviceManually(String databaseName,int deviceId) ;
    public void stopManualControl(String databaseName,int deviceId);


}
