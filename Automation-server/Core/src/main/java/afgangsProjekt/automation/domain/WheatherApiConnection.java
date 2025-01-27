package afgangsProjekt.automation.domain;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class WheatherApiConnection {
    static JSONObject jsonObject;

    static {
        try {
            jsonObject = new JSONObject(org.apache.commons.io.IOUtils.toString(new URL("https://api.weatherbit.io/v2.0/forecast/daily?city=odense&key=80cc9886c124437cabf115e36e5f601f")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static JSONArray objects= jsonObject.getJSONArray("data");




    public static Integer getCloudCover () throws IOException {
        JSONObject jsonObject1= new JSONObject(objects.get(0).toString());

         return jsonObject1.getInt("clouds") ;
    }
    public static double getWindSpeed () throws IOException {
        JSONObject jsonObject1= new JSONObject(objects.get(0).toString());

        return jsonObject1.getDouble("wind_spd") ;
    }
    public static double getTemperature () throws IOException {
        JSONObject jsonObject1= new JSONObject(objects.get(0).toString());

        return jsonObject1.getDouble("max_temp") ;
    }

public static double get7DaysWindSpeed () throws IOException {

    double sum=0;
    for (int i=0;i<7;i++){
        JSONObject jsonObject1= new JSONObject(objects.get(i).toString());
        sum+= jsonObject1.getDouble("wind_spd");

    }

return sum/7;
}

    public static HashMap<String,ArrayList<String>> getWeathrtData() throws IOException {
        HashMap<String,ArrayList<String>> whether = new HashMap<>();
        whether.put("whether",new ArrayList<String>(Arrays.asList(String.valueOf(getTemperature()),String.valueOf(getCloudCover()),String.valueOf(getWindSpeed()),String.valueOf(get7DaysWindSpeed()))));

        return whether;
    }
}

//the wind turbine can produce 1,5 kw for each hour and about 30 kw a day you have 4 heating devices connected to the system
//  which enough to turn your heating devices continues for 5 hours