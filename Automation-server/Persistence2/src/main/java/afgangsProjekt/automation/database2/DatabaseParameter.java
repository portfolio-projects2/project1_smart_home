package afgangsProjekt.automation.database2;

import afgangsProjekt.automation.systemEnums.ParameterStatus;
import afgangsProjekt.automation.systemModels.Automation;
import afgangsProjekt.automation.systemModels.Device;
import afgangsProjekt.automation.systemModels.Parameter;
import afgangsProjekt.automation.systemModels.Sensor;

public class DatabaseParameter extends Parameter {
    public DatabaseParameter(int id, Sensor sensor, Device device, Automation automation, String parameterLogic, String parameterValue, ParameterStatus parameterStatus) {
        super(id, sensor, device, automation, parameterLogic, parameterValue, parameterStatus);
    }
}
