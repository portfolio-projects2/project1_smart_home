package afgangsProjekt.automation;

import afgangsProjekt.automation.systemEnums.DeviceStatus;
import afgangsProjekt.automation.data.MqttProvidersRegistry;
import afgangsProjekt.automation.services.IDeviceCommunication;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;

@Component("MQTTDevice")
public class MqttDeviceClient implements IDeviceCommunication {

    MqttProvidersRegistry providersRegistry;

    @Autowired
public MqttDeviceClient(MqttProvidersRegistry _mqttProvidersRegistry)  {
    providersRegistry=_mqttProvidersRegistry;
    providersRegistry.getMqttProvidersRegistry().forEach((providerName,provider)->{
        try {
            provider.getMqttClient().connect(provider.getMqttConnectOptions());
            System.out.println("weee"+provider.getMqttClient());
        } catch (MqttException e) {
            e.printStackTrace();
        }


    });


}

    @Override
    public String getDeviceStatus(int deviceId,String communicationProvider) throws MqttException, InterruptedException {
        final String[] status = {""};
        MqttClient mqttClient= providersRegistry.getMqttProvidersRegistry().get(communicationProvider).getMqttClient();
        MqttMessage message=new MqttMessage("getDeviceStatus".getBytes(StandardCharsets.UTF_8));
        mqttClient.subscribe("GSH/status/#");
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
             status[0] =mqttMessage.toString();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        mqttClient.publish("GreenHome"+"/"+"status"+"/"+String.valueOf(deviceId),message);

       Thread.sleep(3000);
       System.out.println("ssss"+"status");
        return Arrays.toString(status);

    }

    @Override
    public Set<String> getCommunicationProviders() {
        return providersRegistry.getMqttProvidersRegistry().keySet();
    }

    @Override
    public void setDeviceBehavior(int deviceId, String communicationProvider, DeviceStatus status) throws MqttException {
        System.out.println("mqttDeviceClient"+status);


    providersRegistry.getMqttProvidersRegistry()
            .get(communicationProvider)
            .getMqttClient()
            .publish("GreenHome/start/"+status.toString()+"/"+String.valueOf(deviceId),"publish".getBytes(StandardCharsets.UTF_8),2,true);
    }


    @Override
    public boolean checkConnection(String communicationProvider) throws MqttException {
        boolean connectionValid= true;
       try {
           providersRegistry.getMqttProvidersRegistry()
                   .get(communicationProvider)
                   .getMqttClient()
                   .publish("GreenHome/start/",new MqttMessage());
       }catch (Exception e){
           System.out.println(e.getMessage());
           connectionValid=false;
       }

        return connectionValid;
    }


}
