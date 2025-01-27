package afgangsProjekt.automation.data;

import afgangsProjekt.automation.systemModels.CommunicationProvider;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public class MqttProvider extends CommunicationProvider {
    MqttClient mqttClient;
    MqttConnectOptions mqttConnectOptions;
    String providerName;
    public MqttProvider(MqttClient _mqttClient, MqttConnectOptions _mqttConnectOptions) {
    mqttClient=_mqttClient;
    mqttConnectOptions=_mqttConnectOptions;
    }

    public MqttClient getMqttClient() {
        return mqttClient;
    }

    public MqttConnectOptions getMqttConnectOptions() {
        return mqttConnectOptions;
    }
}
