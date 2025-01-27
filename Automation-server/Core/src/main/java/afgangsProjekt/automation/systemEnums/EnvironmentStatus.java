package afgangsProjekt.automation.systemEnums;

import org.springframework.stereotype.Component;


public enum EnvironmentStatus {

    testing(0),
    development(1),
    production(2),
    unitTesting(4);

    int statusValue;
    EnvironmentStatus(int value){

        statusValue=value;
    }
}
