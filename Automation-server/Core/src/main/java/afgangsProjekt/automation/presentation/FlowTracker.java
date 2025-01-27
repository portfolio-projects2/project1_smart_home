package afgangsProjekt.automation.presentation;

import afgangsProjekt.automation.Environment;
import afgangsProjekt.automation.systemEnums.EnvironmentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component("flowTracker")
public class FlowTracker{
private static HashMap<Integer,String> flows= new HashMap<>();;
static int counter=0;

  Environment environment;
    @Autowired
public FlowTracker(Environment _environment){
    environment=_environment;

}

 public static void trackFlow(String flow) {
if (flow.equals("done"))
{
    System.out.println(".................................................................................");
}else {
counter++;
flows.put(counter,flow);


 }}

public void printFlow() throws InterruptedException {

            if(environment.getEnvironmentStatus().equals(EnvironmentStatus.development)||environment.getEnvironmentStatus().equals(EnvironmentStatus.unitTesting)){
                Thread.sleep(3000);
                flows.forEach((k,v)->{
                    System.out.println(k+"-"+v);
                });
                flows=new HashMap<>();
                counter=0;
            }
Thread.sleep(3000);

}



}