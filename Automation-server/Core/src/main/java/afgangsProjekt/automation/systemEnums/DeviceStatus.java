package afgangsProjekt.automation.systemEnums;

public enum DeviceStatus {
activ(0),
inactive(1),
manuallyStartted(3),
manuallyStopped(4),
notRegistredYet(5),
initial(6),
communicationProviderDown(7);

int statusValue;
 DeviceStatus(int value){

statusValue=value;
}



}
