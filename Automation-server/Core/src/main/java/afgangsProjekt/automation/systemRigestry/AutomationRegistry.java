package afgangsProjekt.automation.systemRigestry;

import afgangsProjekt.automation.systemModels.Automation;
import com.google.gson.Gson;
import com.hazelcast.com.google.common.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component("AutomationRegistry")
public class AutomationRegistry {


    private static HashMap<String, ArrayList<Automation>> automationsRegistry= new HashMap<>();


    public void setAllAutomations(String databaseName,ArrayList<Automation> allAutomations) {
    automationsRegistry.put(databaseName,allAutomations);

    }

    public void addAutomation(String databaseName,Automation automation) {
        automationsRegistry.computeIfPresent(databaseName,(k,v)->{v.add(automation);return v;});
        automationsRegistry.computeIfAbsent(databaseName,v->new ArrayList<Automation>(Arrays.asList(automation)));


    }
    public  Automation getAutomation(String databaseName,int automationId) {
      Automation automation=null;
       for(Automation a: automationsRegistry.get(databaseName)){
           if (a.getId()==automationId){
               System.out.println("autId"+automationId);
               System.out.println((a.getId()));
               automation=a;
           }
       }
return automation;
    }


    public void removeAutomation(String databaseName, Automation automation) {

        automationsRegistry.get(databaseName).remove(automation);
    }
}
