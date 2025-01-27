package afgangsProjekt.automation.database2;

import afgangsProjekt.automation.systemEnums.SensorStatus;
import afgangsProjekt.automation.systemModels.Sensor;

public class DatabaseSensor extends Sensor {
    public DatabaseSensor(int id, String sensorName, String liveValue, SensorStatus sensorStatus,String dev_Id) {
        super(id, sensorName, liveValue, sensorStatus, dev_Id);
    }
}
