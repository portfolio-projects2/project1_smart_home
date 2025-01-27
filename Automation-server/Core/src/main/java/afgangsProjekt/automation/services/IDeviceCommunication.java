package afgangsProjekt.automation.services;

import afgangsProjekt.automation.systemEnums.DeviceStatus;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("services")
public interface IDeviceCommunication {


    String getDeviceStatus (int deviceId, String communicationProvider) throws Exception;
    Set<String> getCommunicationProviders();
    void setDeviceBehavior(int deviceId, String communicationProvider, DeviceStatus status) throws Exception;

    boolean checkConnection(String communicationProvider) throws Exception;
}
