package afgangsProjekt.automation.systemEnums;

public enum ParameterStatus {


    satisfied(0),
    unsatisfied(1),
    initial(2);

    int statusValue;
    ParameterStatus(int value){

        statusValue=value;
    }

}
