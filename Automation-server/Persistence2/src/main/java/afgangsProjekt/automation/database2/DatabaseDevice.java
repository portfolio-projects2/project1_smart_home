package afgangsProjekt.automation.database2;

import afgangsProjekt.automation.systemEnums.DeviceStatus;
import afgangsProjekt.automation.systemModels.Device;

public class DatabaseDevice extends Device {
    public DatabaseDevice(int deviceId, String communicationProvider, DeviceStatus status,String deviceName) {
        super(deviceId, communicationProvider, status,deviceName);
    }
}
