package afgangsProjekt.automation;

import afgangsProjekt.automation.systemEnums.DeviceStatus;
import afgangsProjekt.automation.services.IDeviceCommunication;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;

import java.util.Set;
@Component("SerialConnector")
public class SerialPortClient implements IDeviceCommunication {

   private static ArduinoConfiguration arduionoConfiguration;

   SerialPortClient(){
       arduionoConfiguration = new ArduinoConfiguration();
   }

    @Override
    public String getDeviceStatus(int deviceId, String communicationProvider) throws MqttException, InterruptedException {
        return null;
    }

    @Override
    public Set<String> getCommunicationProviders() {
        Set<String> set = Set.of("Serial");

       return set;
    }

    @Override
    public void setDeviceBehavior(int deviceId, String communicationProvider, DeviceStatus status) throws MqttException {
    arduionoConfiguration.setStatus(status);
    arduionoConfiguration.initialize();
    }

    @Override
    public boolean checkConnection(String communicationProvider) throws Exception {

        return arduionoConfiguration.portConnected();
    }


}
