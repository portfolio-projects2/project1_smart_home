package afgangsProjekt.automation.domain;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TestClass {


    public static void main(String[] args) throws URISyntaxException {

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI("http://localhost:8081/rsss"))
                .PUT(HttpRequest.BodyPublishers.ofString(",læ,æl,æ,"))
                .build();
        HttpResponse httpResponse = null;

        try {

            httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
        }
        System.out.println(httpResponse.body());
    }
}