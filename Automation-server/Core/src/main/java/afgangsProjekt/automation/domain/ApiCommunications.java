package afgangsProjekt.automation.domain;

import afgangsProjekt.automation.systemEnums.AutomationStatus;
import afgangsProjekt.automation.systemEnums.DeviceStatus;
import afgangsProjekt.automation.systemEnums.ParameterStatus;
import afgangsProjekt.automation.systemEnums.SensorStatus;
import afgangsProjekt.automation.systemModels.Automation;
import afgangsProjekt.automation.systemModels.Device;
import afgangsProjekt.automation.systemModels.Parameter;
import afgangsProjekt.automation.systemModels.Sensor;
import afgangsProjekt.automation.systemRigestry.AutomationRegistry;
import afgangsProjekt.automation.systemRigestry.DevicesRegistry;
import afgangsProjekt.automation.systemRigestry.ParametersRegistry;
import afgangsProjekt.automation.systemRigestry.SensorsRegistry;

import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
@CrossOrigin({"https://localhost:5001","https://localhost:5003"})
@RestController("apiCommunications")
public class ApiCommunications {
	@Autowired
	AutomationHandler automationHandler;
	@Autowired
    DevicesRegistry devicesRegistry;
	@Autowired
    SensorsRegistry sensorsRegistry;
	@Autowired
    AutomationRegistry automationRegistry;
	@Autowired
    ParametersRegistry parametersRegistry;

	@RequestMapping(value = "/getAllInfo/{databaseName}", method = { RequestMethod.GET, RequestMethod.HEAD })
	private  String getSystemInfo(@PathVariable String databaseName) throws IOException {
		Gson gson = new Gson();
		// Convert the Map to a JSON string
		String systemDevices = gson.toJson(devicesRegistry.getallDevices(databaseName));
		String systemSensors= gson.toJson(sensorsRegistry.getAllSensors(databaseName));
		String whether = gson.toJson(WheatherApiConnection.getWeathrtData());
		return "["+systemDevices +","+ systemSensors+","+whether+"]";

	}
	@PostMapping("/addDevice")
	public void addDevice(@RequestBody String body){
		JSONObject requestBody = new JSONObject(body);
		String databaseName= requestBody.getString("databaseName");
		Device device = new Device(requestBody.getInt("Id"),requestBody.getString("comProvider"), DeviceStatus.initial,requestBody.getString("deviceName") );
		devicesRegistry.addDevice(databaseName,device);


	}
	@PostMapping("/deleteDevice")
	public void deleteDevice(@RequestBody String body){
		JSONObject requestBody = new JSONObject(body);
		String databaseName=requestBody.getString("databaseName");
		devicesRegistry.removeDevice(databaseName,requestBody.getInt("Id"));

	}
	@PostMapping("/editDevice")
	public void editDevice(@RequestBody String body){
		JSONObject requestBody = new JSONObject(body);
		String databaseName=requestBody.getString("databaseName");
		Device device = DevicesRegistry.getDevice(databaseName,requestBody.getInt("Id"));
		device.setCommunicationProvider(requestBody.getString("comProvider"));

	}



	@PostMapping("/stopDevice/{deviceId}")
	public  void stopDeviceManually(@RequestBody String body,@PathVariable("deviceId") int deviceId) throws SQLException {
		JSONObject requestBody = new JSONObject(body);
		String databaseName = requestBody.getString("databaseName");
		automationHandler.stopDeviceManually(databaseName,deviceId);

	}

	@RequestMapping("/startDevice/{deviceId}")
	public  void starDeviceManually(@RequestBody String body,@PathVariable("deviceId") int deviceId)  {
		JSONObject requestBody = new JSONObject(body);
		String databaseName = requestBody.getString("databaseName");
		automationHandler.startDeviceManually(databaseName,deviceId);

	}


	@PostMapping("/stopManualControl")
	public  void stopManualControl(@RequestBody String body) {
		JSONObject requestBody = new JSONObject(body);
		String databaseName = requestBody.getString("databaseName");
		int deviceId= requestBody.getInt("deviceId");
		automationHandler.stopManualControl(databaseName,deviceId);

	}

	@PostMapping("/addSensor")
	public void addSensor(@RequestBody String body){
	JSONObject requestBody = new JSONObject(body);
	Sensor sensor = new Sensor(requestBody.getInt("sensorId"),requestBody.getString("name"),"0", SensorStatus.inactive,requestBody.getString("devId") );
	sensorsRegistry.addSensor(requestBody.getString("databaseName"),sensor);

	}
	@PostMapping("/deleteSensor")
	public void deleteSensor(@RequestBody String body) {
		JSONObject requestBody = new JSONObject(body);
		String databaseName= requestBody.getString("databaseName");
		String devId= requestBody.getString("devId");
		Sensor sensor = sensorsRegistry.getSensor(devId);
		sensorsRegistry.removeSensor(databaseName,sensor);



	}

	@PostMapping("/editSensor")
	public void editSensor(@RequestBody String body){
		System.out.println(body);
		JSONObject requestBody = new JSONObject(body);
		Sensor sensor= sensorsRegistry.getSensor(requestBody.getString("devId"));
		sensor.setSensorName(requestBody.getString("name"));

	}



	@PostMapping("/addAutomation")
	public void addAutomation(@RequestBody String body){
		JSONObject requestBody = new JSONObject(body);
		AutomationStatus automationStatus = AutomationStatus.inactive;
		if(requestBody.getInt("active")==1){
			automationStatus= AutomationStatus.activ;
		}
		String timeRange = requestBody.getJSONObject("timeRange").toString();
		System.out.println("from add aut"+timeRange);
		String databaseName = requestBody.getString("databaseName");
		int automationId = requestBody.getInt("automationId");
		String automationTitle= requestBody.getString("title");
		Automation automation = new Automation(automationId,automationTitle, automationStatus,timeRange );
		automationRegistry.addAutomation(databaseName,automation);


	}
	@PostMapping("/deleteAutomation")
	public void deleteAutomation(@RequestBody String body) {
		JSONObject requestBody = new JSONObject(body);
		String databaseName = requestBody.getString("databaseName");
		int automationId = requestBody.getInt("automationId");
		Automation automation = automationRegistry.getAutomation(databaseName,automationId);
		Parameter parameter = parametersRegistry.getParameter(databaseName,automation);
		parametersRegistry.removeParameter(databaseName,parameter);
		automationRegistry.removeAutomation(databaseName,automation);
	}

	@PostMapping("/editAutomation")
	public void editAutomation(@RequestBody String body){
		System.out.println("fromEditAut");
		JSONObject requestBody = new JSONObject(body);
		int automationId = requestBody.getInt("automationId");
		String databaseName= requestBody.getString("databaseName");
		Automation automation=automationRegistry.getAutomation(databaseName,automationId);
		automation.setAutomationTimeRange(requestBody.getJSONObject("timeRange").toString());
		automation.setAutomationTitle(requestBody.getString("automationTitle"));



	}


	@PostMapping("/addParameter")
	public void addParameter(@RequestBody String body){
		JSONObject requestBody = new JSONObject(body);
		int parameterId = requestBody.getInt("parameterId");
		String parameterLogic = requestBody.getString("parameterLogic");
		String parameterValue="";
		if (parameterLogic.equals("onOnly")){
			parameterValue = requestBody.getString("parameterValue");
		}else{

			parameterValue = requestBody.getJSONObject("parameterValue").toString();
		}		String databaseName=requestBody.getString("databaseName");
		String sensordevId = requestBody.getString("sensordevId");
		int deviceId = requestBody.getInt("deviceId");
		int automationId = requestBody.getInt("automationId");
		Sensor sensor = sensorsRegistry.getSensor(sensordevId);
		Device device = DevicesRegistry.getDevice(databaseName,deviceId);
		Automation automation = automationRegistry.getAutomation(databaseName,automationId);
		Parameter parameter = new Parameter(parameterId,sensor,device,automation,parameterLogic,parameterValue, ParameterStatus.initial);
		parametersRegistry.addParameter(databaseName,parameter);


	}
	@PostMapping("/deleteParameter")
	public void deleteParameter(@RequestBody String body) {
		JSONObject requestBody = new JSONObject(body);
		String databaseName = requestBody.getString("databaseName");
		int parameterId = requestBody.getInt("parameterId");
		Parameter parameter = parametersRegistry.getParameter(databaseName,parameterId);
		parametersRegistry.removeParameter(databaseName,parameter);
	}

	@PostMapping("/editParameter")
	public void editParameter(@RequestBody String body){
		JSONObject requestBody = new JSONObject(body);
		int parameterId = requestBody.getInt("parameterId");
		String parameterLogic = requestBody.getString("parameterLogic");
		String parameterValue="";
		if (parameterLogic.equals("onOnly")){
			 parameterValue = requestBody.getString("parameterValue");
		}else{

			 parameterValue = requestBody.getJSONObject("parameterValue").toString();
		}
		System.out.println("paaaaaaaaaaaar"+parameterValue);
		String databaseName=requestBody.getString("databaseName");
		String sensordevId = requestBody.getString("sensordevId");
		int deviceId = requestBody.getInt("deviceId");
		int automationId = requestBody.getInt("automationId");
		Sensor sensor = sensorsRegistry.getSensor(sensordevId);
		System.out.println(sensor.getSensorName());
		Device device = DevicesRegistry.getDevice(databaseName,deviceId);
		Automation automation = automationRegistry.getAutomation(databaseName,automationId);
		Parameter parameter = parametersRegistry.getParameter(databaseName,parameterId);
		parameter.setParameterValue(parameterValue);
		parameter.setParameterLogic(parameterLogic);
		parameter.setAutomation(automation);
		parameter.setDevice(device);
		parameter.setSensor(sensor);

	}

}
