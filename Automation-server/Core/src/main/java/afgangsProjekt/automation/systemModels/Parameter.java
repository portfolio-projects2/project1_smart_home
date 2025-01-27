package afgangsProjekt.automation.systemModels;

import afgangsProjekt.automation.systemEnums.ParameterStatus;

public class Parameter {
    int Id;
    private Sensor sensor;
    private Device device;
    private Automation automation;
    private String ParameterLogic;
    private String parameterValue;
    private ParameterStatus parameterStatus;

    public Parameter(int id, Sensor sensor, Device device, Automation automation, String parameterLogic, String parameterValue, ParameterStatus parameterStatus) {
        Id = id;
        this.sensor = sensor;
        this.device = device;
        this.automation = automation;
        ParameterLogic = parameterLogic;
        this.parameterValue = parameterValue;
        this.parameterStatus = parameterStatus;
    }

    public void setParameterStatus(ParameterStatus parameterStatus) {
        this.parameterStatus = parameterStatus;
    }

    public ParameterStatus getParameterStatus() {
        return parameterStatus;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public void setAutomation(Automation automation) {
        this.automation = automation;
    }

    public void setParameterLogic(String parameterLogic) {
        ParameterLogic = parameterLogic;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public int getId() {
        return Id;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public Device getDevice() {
        return device;
    }

    public Automation getAutomation() {
        return automation;
    }

    public String getParameterLogic() {
        return ParameterLogic;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    @Override
    public String toString(){
        return "Parameter Id :"+this.Id+" Logic :"+this.getParameterLogic()+"value :"+this.getParameterValue();
    }
}
