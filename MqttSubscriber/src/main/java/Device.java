import java.util.HashMap;

public class Device {
    int deviceId;
   static String deviceStatus;
   static HashMap<Integer,String>devicesStatus=new HashMap<>();

    public static String getDeviceStatus(int deviceId) {
        if (devicesStatus.containsKey(deviceId)){
            return devicesStatus.get(deviceId);
        }
        return "Not Registered yet";
    }
    public static void setDeviceStatus(int deviceId, String status) {
        devicesStatus.put(deviceId,status);
    }


}
