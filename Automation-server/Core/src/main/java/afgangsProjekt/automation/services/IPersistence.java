package afgangsProjekt.automation.services;

import afgangsProjekt.automation.systemModels.Automation;
import afgangsProjekt.automation.systemModels.Device;
import afgangsProjekt.automation.systemModels.Parameter;
import afgangsProjekt.automation.systemModels.Sensor;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IPersistence {

    ArrayList<Sensor> getAllSensors() throws SQLException;

    ArrayList<Device> getAllDevices() throws SQLException;

    ArrayList<Automation> getAllAutomations() throws SQLException;

    ArrayList<Parameter> getAllParameters() throws SQLException;

    boolean isActiveDatabase();

    String getDatabaseName();


}
