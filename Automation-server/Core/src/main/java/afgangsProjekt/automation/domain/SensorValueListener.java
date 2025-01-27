/*
package afgangsProjekt.automation.domain;

import afgangsProjekt.automation.systemModels.Sensor;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component("SensorValueListener")
public class SensorValueListener {

    static String oldValue;
    static String newValue;
   static HashMap<Sensor,HashMap<String,String>> sensrosValuesMap= new HashMap<>();

   public static boolean isValueChanged(Sensor sensor) {
       sensrosValuesMap.computeIfPresent(sensor, (k, v) -> {
           v.put("newValue", sensor.getLiveValue());
           return v;
       });
       sensrosValuesMap.computeIfAbsent(sensor, map -> new HashMap<String, String>() {{
           put("newValue", sensor.getLiveValue());
       }});

       if (sensrosValuesMap.get(sensor).containsKey("oldValue")) {
           if (sensrosValuesMap.get(sensor).get("oldValue").equals(sensrosValuesMap.get(sensor).get("newValue"))) {
               return false;
           }

       }
           return true;


   }

    public static void updateSensorValue(Sensor sensor, String _newValue) {
       sensrosValuesMap.computeIfPresent(sensor,(k,v)->{v.put("oldValue",_newValue);
           return v;});
       sensrosValuesMap.computeIfAbsent(sensor,map->new HashMap<String,String>(){{put("oldValue",_newValue);}});

    }



}
*/
