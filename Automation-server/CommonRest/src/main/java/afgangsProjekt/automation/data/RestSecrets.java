package afgangsProjekt.automation.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RestSecrets {
    //Provider1 secrets
  //  @Value("${Rest1URI}")
    private String Rest1URI;

    public String getRest1URI() {
        return Rest1URI;
    }
}