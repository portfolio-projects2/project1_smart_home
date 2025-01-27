package afgangsProjekt.automation.data;

import afgangsProjekt.automation.systemModels.CommunicationProvider;

import java.net.URISyntaxException;
import java.net.http.HttpClient;

public class RestProvider extends CommunicationProvider {
    HttpClient httpClient;
    String uri;
    String port;

    public RestProvider(String _uri,String _port, HttpClient _httpClient) throws URISyntaxException {
httpClient=_httpClient;
        uri=_uri;
        port=_port;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public String getUri() {
        return uri;
    }

    public String getPort() {
        return port;
    }
}
