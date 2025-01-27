package afgangsProjekt.automation;
import afgangsProjekt.automation.systemEnums.DeviceStatus;
import afgangsProjekt.automation.data.RestProvider;
import afgangsProjekt.automation.data.RestProvidersRegistry;
import afgangsProjekt.automation.services.IDeviceCommunication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;

@Component("RestDevice")
public class RestDeviceClient implements IDeviceCommunication {
    RestProvidersRegistry restProvidersRegistry;
    @Autowired
    public RestDeviceClient(RestProvidersRegistry _restProvidersRegistry)  {
        restProvidersRegistry=_restProvidersRegistry;

    }

    @Override
    public String getDeviceStatus(int deviceId, String communicationProvider) throws  InterruptedException, URISyntaxException, IOException {
        RestProvider restProvider=restProvidersRegistry
                .getRestProvidersRegistry()
                .get(communicationProvider);
        HttpClient httpClient= restProvider.getHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI(restProvider.getUri()+"status"+deviceId))
                .GET()
                .build();
        HttpResponse<String> httpResponse= httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()) ;
        System.out.println(httpResponse);
    return httpResponse.body();
    }

    @Override
    public Set<String> getCommunicationProviders() {

        return restProvidersRegistry.getRestProvidersRegistry().keySet();
    }

    @Override
    public void setDeviceBehavior(int deviceId, String communicationProvider, DeviceStatus status) throws IOException, InterruptedException, URISyntaxException {
        RestProvider restProvider=restProvidersRegistry
                .getRestProvidersRegistry()
                .get(communicationProvider);
        HttpClient httpClient= restProvider.getHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI(restProvider.getUri()+restProvider.getPort()+"/device/"+deviceId))
                .PUT(HttpRequest.BodyPublishers.ofString(status.toString()))
                .build();
        HttpResponse httpResponse =null;
        boolean serverAvailable =false;


            try {

                httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                e.printStackTrace();
            }


    }

    @Override
    public boolean checkConnection(String communicationProvider)  {
        boolean connectionValid=false;
        RestProvider restProvider=restProvidersRegistry
                .getRestProvidersRegistry()
                .get(communicationProvider);

            try {
                InetAddress address = InetAddress.getByName(restProvider.getUri().substring(7));
                connectionValid = address.isReachable(2000);
                System.out.println(restProvider.getUri() + " is reachable? " + connectionValid);

            } catch (IOException e) {
                System.out.println(e.getMessage());}


        return connectionValid;
        }



}
