package afgangsProjekt.automation.data;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component("providerRegistryBean")
public class MqttProvidersRegistry {
    private final HashMap<String, MqttProvider> mqttProvidersRegistry;

    @Autowired
public MqttProvidersRegistry(MqttSecrets mqttSecrets) throws MqttException {
    mqttProvidersRegistry= new HashMap<>(){{
        put("mqtt1",new MqttProvider(new  MqttClient(mqttSecrets.getMqtt1Broker(),mqttSecrets.getMqtt1ClientId()), new MqttConnectOptions(){{setUserName(mqttSecrets.getMqtt1UserName());setPassword(mqttSecrets.getMqtt1Password().toCharArray());setAutomaticReconnect(true);}}));
      //  put("mqtt1",new MqttProvider(new  MqttClient(mqttSecrets.getMqtt2Broker(),mqttSecrets.getMqtt2ClientId()), new MqttConnectOptions(){{setUserName(mqttSecrets.getMqtt2UserName());setPassword(mqttSecrets.getMqtt2Password().toCharArray());}}));
    }};
}

    public HashMap<String, MqttProvider> getMqttProvidersRegistry() {
        return mqttProvidersRegistry;
    }
}
