package afgangsProjekt.automation.systemModels;

import afgangsProjekt.automation.systemEnums.DeviceStatus;

public class Device {
    private int deviceId;
    private String communicationProvider;
    private DeviceStatus status;
    private String deviceName;


    public Device(int deviceId, String communicationProvider, DeviceStatus status, String deviceName) {
        this.deviceId = deviceId;
        this.communicationProvider = communicationProvider;
        this.status = status;
        this.deviceName=deviceName;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public void setCommunicationProvider(String communicationProvider) {
        this.communicationProvider = communicationProvider;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public String getCommunicationProvider() {
        return communicationProvider;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Override
    public String toString() {
        return "Device{" +
                "deviceId=" + deviceId +
                ", communicationProvider='" + communicationProvider + '\'' +
                ", status=" + status +
                ", deviceName='" + deviceName + '\'' +
                '}';
    }
}
