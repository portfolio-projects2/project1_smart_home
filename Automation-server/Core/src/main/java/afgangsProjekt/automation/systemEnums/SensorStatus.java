package afgangsProjekt.automation.systemEnums;

public enum SensorStatus {
    activ(0),
    inactive(1),
;

    int statusValue;
    SensorStatus(int value){

        statusValue=value;
    }
}
