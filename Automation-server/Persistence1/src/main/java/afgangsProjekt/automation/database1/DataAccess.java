package afgangsProjekt.automation.database1;


import afgangsProjekt.automation.services.IPersistence;
import afgangsProjekt.automation.systemEnums.AutomationStatus;
import afgangsProjekt.automation.systemEnums.DeviceStatus;
import afgangsProjekt.automation.systemEnums.ParameterStatus;
import afgangsProjekt.automation.systemEnums.SensorStatus;
import afgangsProjekt.automation.systemModels.Automation;
import afgangsProjekt.automation.systemModels.Device;
import afgangsProjekt.automation.systemModels.Parameter;
import afgangsProjekt.automation.systemModels.Sensor;
import afgangsProjekt.automation.systemSecurity.CoreSecrets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;

@Component("DataAccess")
public class DataAccess implements IPersistence {
    static private Connection connection;
    private final ArrayList<Sensor> allSensors;
    private final ArrayList<Device> allDevices;
    private final ArrayList<Automation> allAutomations;
    private final ArrayList<Parameter> allParameters;


@Autowired
    public DataAccess(CoreSecrets coreSecrets) throws SQLException {
    allSensors = new ArrayList<>();
    allDevices= new ArrayList<>();
    allAutomations= new ArrayList<>();
    allParameters= new ArrayList<>();
    String dataSourceURL="";
    if(System.getenv("dataSource").equals("docker")){
        dataSourceURL= coreSecrets.getUrlDocker();
    }else {dataSourceURL=coreSecrets.getUrl(); }
    connection=DriverManager.getConnection(dataSourceURL, coreSecrets.getServerUserName(), coreSecrets.getPassword());

    }

    @Override
    public  ArrayList<Sensor> getAllSensors() throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from sensor");
        ResultSet resultSet= preparedStatement.executeQuery();
        while (resultSet.next()){
            Sensor sensor = new DatabaseSensor(
                    resultSet.getInt("Id"),
                    resultSet.getString("sensorName"),
                    "0",
                    SensorStatus.inactive,
                    resultSet.getString("devId"));

            allSensors.add(sensor);

        }

        return allSensors;
    }
@Override
    public  ArrayList<Device> getAllDevices() throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from device");
    ResultSet resultSet= preparedStatement.executeQuery();
    while (resultSet.next()){
        Device device = new DatabaseDevice(
                resultSet.getInt("Id"),resultSet.getString("communicationProvider"), DeviceStatus.initial, resultSet.getString("deviceName") );
                allDevices.add(device);



    }
    return allDevices;


    }

    @Override
    public ArrayList<Automation> getAllAutomations() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from automation");
        ResultSet resultSet= preparedStatement.executeQuery();
        while (resultSet.next()){
            int automationId = resultSet.getInt("Id");

            AutomationStatus automationStatus=AutomationStatus.inactive;
            if (resultSet.getInt("active")==1){automationStatus=AutomationStatus.activ;}
            Automation automation = new DatabaseAutomation(automationId,
                    resultSet.getString("automationTitle"),
                    automationStatus
                    ,resultSet.getString("automationTimeRange"));
            allAutomations.add(automation);



        }
        System.out.println();
        return allAutomations;


    }

    @Override
    public ArrayList<Parameter> getAllParameters() throws SQLException {
       PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from parameter ");
       ResultSet resultSet= preparedStatement.executeQuery();
       while (resultSet.next()){
           Sensor sensor=null;
           for (Sensor s: allSensors) {
               if (s.getId()==resultSet.getInt("sensorId")){ sensor=s;}
           }
           Device device = null;
           for (Device d:allDevices) {
               if (d.getDeviceId()==resultSet.getInt("deviceId")){ device=d;}
           }

           Automation automation =null;
           for (Automation a:allAutomations) {
               if (a.getId()==resultSet.getInt("automationId")){ automation=a;}
           }
           Parameter parameter = new DatabaseParameter(
                   resultSet.getInt("Id"),
                   sensor,
                   device,
                   automation,
                   resultSet.getString("parameterLogic"),
                   resultSet.getString("parameterValue"),
                   ParameterStatus.initial

           );

           allParameters.add(parameter);



       }
        return allParameters;
    }

    @Override
    public boolean isActiveDatabase() {

    return true;
    }

    @Override
    public String getDatabaseName() {
        return "database1";
    }

}
