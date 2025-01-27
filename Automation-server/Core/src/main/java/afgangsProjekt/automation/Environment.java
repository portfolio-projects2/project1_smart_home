package afgangsProjekt.automation;

import afgangsProjekt.automation.systemEnums.EnvironmentStatus;
import org.springframework.stereotype.Component;

@Component("Environment")
public class Environment {
    EnvironmentStatus environmentStatus;

    public void setEnvironmentStatus(EnvironmentStatus environmentStatus) {
        this.environmentStatus = environmentStatus;
    }

    public Environment() {
try {

    if(System.getenv("env").equals("development")){environmentStatus = EnvironmentStatus.development;}
    else if (System.getenv("env").equals("production")){environmentStatus = EnvironmentStatus.production;}
    else if (System.getenv("env").equals("testing")){environmentStatus = EnvironmentStatus.testing;}
}catch (Exception e){
    System.out.println(e.getMessage());
      environmentStatus = EnvironmentStatus.development;

}
    }
    public EnvironmentStatus getEnvironmentStatus() {
        return environmentStatus;
    }

}
