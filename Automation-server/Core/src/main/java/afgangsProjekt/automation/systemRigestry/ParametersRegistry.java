package afgangsProjekt.automation.systemRigestry;

import afgangsProjekt.automation.systemModels.Automation;
import afgangsProjekt.automation.systemModels.Parameter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
@Component("ParametersRegistry")
public class ParametersRegistry {
    private static HashMap<String, ArrayList<Parameter>> parametersRegistry= new HashMap<>();





    public static ArrayList<Parameter> getAllParameters() {
       ArrayList<Parameter> parameters = new ArrayList<>();
       parametersRegistry.forEach((k,v)->{
           for (Parameter p:v)
               {
               parameters.add(p);
           }


       });
return parameters;

    }

    public void addParameter(String databaseName,Parameter parameter) {
        parametersRegistry.computeIfPresent(databaseName,(k,v)->{v.add(parameter);return v;});
        parametersRegistry.computeIfAbsent(databaseName,v->new ArrayList<Parameter>(Arrays.asList(parameter)));


    }

    public Parameter getParameter(String databaseName, int parameterId){
        for(Parameter p : parametersRegistry.get(databaseName)){
            if (p.getId()==parameterId){
                return p;
            }
        }
      return  null;
    }
    public Parameter getParameter(String databaseName, Automation automation){
        for(Parameter p : parametersRegistry.get(databaseName)){
            if (p.getAutomation().equals(automation)){
                return p;
            }
        }
        return  null;
    }


    public void setAllParameters(String databaseName, ArrayList<Parameter> allParameters) {
        parametersRegistry.put(databaseName,allParameters);

    }

    public void removeParameter(String databaseName, Parameter parameter) {

        parametersRegistry.get(databaseName).remove(parameter);
    }
}
