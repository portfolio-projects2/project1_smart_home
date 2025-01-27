package afgangsProjekt.automation.systemRigestry;

import afgangsProjekt.automation.systemModels.Sensor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
@Component("SensorsRegistry")
public class SensorsRegistry {

    private HashMap<String, ArrayList<Sensor>> sensorsRegistryMap = new HashMap<>();


    public HashMap<String, ArrayList<Sensor>> getSensorsRegistry() {
        return sensorsRegistryMap;
    }

    public Sensor getSensor(String devId) {
        Sensor sensor=null;
        ArrayList<Sensor> allSensors = new ArrayList<>();
        sensorsRegistryMap.forEach((k, v) -> {
            for (Sensor s : v) {
                allSensors.add(s);

            }
        });
        for (Sensor s : allSensors) {
            System.out.println("sssssssss"+s.getdev_Id());
            System.out.println("sssssssss"+devId);
            if (s.getdev_Id().equals(devId)) {
                sensor=s;
            }
        }
        return sensor;
    }


    public void setAllSensors(String databaseName, ArrayList<Sensor> allSensors) {
        for (Sensor s : allSensors) {
            System.out.println("devDI" + allSensors);
        }
        sensorsRegistryMap.put(databaseName, allSensors);

    }

    public HashMap<String, ArrayList<Sensor>> getAllSensors(String databaseName) {
        HashMap<String, ArrayList<Sensor>> sensors = new HashMap<>();
        sensors.put("sensors", sensorsRegistryMap.get(databaseName));
        return sensors;
    }

    public void addSensor(String databaseName, Sensor sensor) {
        sensorsRegistryMap.computeIfPresent(databaseName, (k, v) -> {
            v.add(sensor);
            return v;
        });
        sensorsRegistryMap.computeIfAbsent(databaseName, list -> new ArrayList<Sensor>(Arrays.asList(sensor)));

    }

    public void removeSensor(String databaseName, Sensor sensor) {
       sensorsRegistryMap.get(databaseName).remove(sensor);


    }
}