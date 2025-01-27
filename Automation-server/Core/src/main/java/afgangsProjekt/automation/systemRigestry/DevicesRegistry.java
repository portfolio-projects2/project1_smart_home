
package afgangsProjekt.automation.systemRigestry;

import afgangsProjekt.automation.systemModels.Device;
import afgangsProjekt.automation.systemEnums.DeviceStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
@Service
@Component("DevicesRegistry")
public class DevicesRegistry {

private static HashMap<String, ArrayList<Device>> devicesRegistry;

    public DevicesRegistry() {
        devicesRegistry= new HashMap<>();
    }

    public static HashMap<String, ArrayList<Device>> getDevicesRegistry() {
        return devicesRegistry;
    }



    public static Device getDevice(String databaseName,int deviceId) {
        Device device =null;
            for(Device d: devicesRegistry.get(databaseName)){
           if (d.getDeviceId()==deviceId){
               device=d;
           }

        }


return device;
}

    public HashMap<String, ArrayList<Device>> getallDevices(String databaseName) {
        HashMap<String, ArrayList<Device>> devices = new HashMap<>();
        devices.put("devices",devicesRegistry.get(databaseName));
        return  devices;
    }



    public void setDeviceStatus(String databaseName, int deviceId, DeviceStatus initial) {
    for(Device d:devicesRegistry.get(databaseName)){
        if(d.getDeviceId()==deviceId){
            d.setStatus(DeviceStatus.initial);
        }
    }

    }

    public void setAllDevices(String databaseName,ArrayList<Device> allDevices) {

        devicesRegistry.put(databaseName,allDevices);




    }

    public void addDevice(String databaseName,Device device) {
        System.out.println("removeDevice"+databaseName+"Id:"+device.getDeviceId());

        devicesRegistry.computeIfPresent(databaseName,(k,v)->{v.add(device);return v;});
        devicesRegistry.computeIfAbsent(databaseName,v->new ArrayList<Device>(Arrays.asList(device)));
        System.out.println(devicesRegistry.get(databaseName));
    }

    public void removeDevice(String databaseName, int id) {
        Device device = getDevice(databaseName,id);
        devicesRegistry.get(databaseName).remove(device);


    }
}

