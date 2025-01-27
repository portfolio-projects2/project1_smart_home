package afgangsProjekt.automation.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MqttSecrets {
    //Provider1 secrets
    @Value("${MqttClientId}")
    private String mqtt1ClientId;

    @Value("${MqttPassword}")
    private String mqtt1Password;

    @Value("${MqttUserName}")
    private String mqtt1UserName;

    @Value("${MqttBroker}")
    private String mqtt1Broker;
/*
    @Value("${MqttStatusClientId}")
    private String mqtt1StatusClientId;

    //Provider2 secrets
    @Value("${Mqtt2ClientId}")
    private String mqtt2ClientId;

    @Value("${Mqtt2Password}")
    private String mqtt2Password;

    @Value("${Mqtt2UserName}")
    private String mqtt2UserName;

    @Value("${Mqtt2Broker}")
    private String mqtt2Broker;

    @Value("${Mqtt2StatusClientId}")
    private String mqtt2StatusClientId;*/

    public String getMqtt1ClientId() {
        return mqtt1ClientId;
    }

    public String getMqtt1Password() {
        return mqtt1Password;
    }

    public String getMqtt1UserName() {
        return mqtt1UserName;
    }

    public String getMqtt1Broker() {
        return mqtt1Broker;
    }
/*
    public String getMqtt1StatusClientId() {
        return mqtt1StatusClientId;
    }

    public String getMqtt2ClientId() {
        return mqtt2ClientId;
    }

    public String getMqtt2Password() {
        return mqtt2Password;
    }

    public String getMqtt2UserName() {
        return mqtt2UserName;
    }

    public String getMqtt2Broker() {
        return mqtt2Broker;
    }

    public String getMqtt2StatusClientId() {
        return mqtt2StatusClientId;
    }*/
}
