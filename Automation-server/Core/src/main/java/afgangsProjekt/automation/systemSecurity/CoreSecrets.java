package afgangsProjekt.automation.systemSecurity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("secrets")
public class CoreSecrets {
   // @Value("${DatabaseUrl}")
    String url ="jdbc:mysql://localhost:3306/test2";
   // @Value("${databasedocker}")
    String urlDocker;
   // @Value("${DatabaseUrl3}")
    String url2 ="jdbc:mysql://localhost:3306/test2" ;
   // @Value("${databasedocker2}")
    String url2Docker;
   // @Value("${DatabaseServerUserName}")
    private String serverUserName="root" ;

   // @Value("${databaseServerPassword}")
    private String Password="project" ;

    public String getUrl2() {
        return url2;
    }

    public String getUrl2Docker() {
        return url2Docker;
    }

    public String getUrl() {
        return url;
    }

    public String getServerUserName() {
        return serverUserName;
    }

    public String getPassword() {
        return Password;
    }

    public String getUrlDocker() {
        return urlDocker;
    }
}
