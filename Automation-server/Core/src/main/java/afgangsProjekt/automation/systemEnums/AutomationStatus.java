package afgangsProjekt.automation.systemEnums;

public enum AutomationStatus {
    activ(0),
    inactive(1),
    ;

    int statusValue;
    AutomationStatus(int value){

        statusValue=value;
    }
}
