package afgangsProjekt.automation.data;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.HashMap;

@Component("restProviderRegistryBean")
public class RestProvidersRegistry {
    private final HashMap<String, RestProvider> restProvidersRegistry;


    @Autowired
public RestProvidersRegistry(RestSecrets restSecrets) throws MqttException, URISyntaxException {
    restProvidersRegistry= new HashMap<>(){{
       // put("rest1",new RestProvider(restSecrets.getRest1URI()));
        put("rest1",new RestProvider("http://172.29.0.8",":8082", HttpClient.newHttpClient()));
    }};
}

    public HashMap<String, RestProvider> getRestProvidersRegistry() {
        return restProvidersRegistry;
    }






}