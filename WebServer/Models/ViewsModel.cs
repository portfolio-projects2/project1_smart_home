namespace WebServer.Models
{
    public class ViewsModel
    {
        public int? parametersSensorId { get; set; }
        public Dictionary<string, string> map { get; set; }

        public int automationState { get; set; }
        public int energySource { get; set; }
        public string automationTitle { get; set; }
        public List<int> sensors { get; set; }
        public List<int> devices { get; set; }
        public List<ParameterObject> parametersList { get; set; }
        public TimeJsonObject automationTimeRange { get; set; }
        public Dictionary<string, string> sensorsParameters { get; set; }





    }


    public class TimeJsonObject
    {
        public String start { get; set; }
        public string off { get; set; }
    }


    public class ParameterObject
    {
        public string parameterTitle { get; set; }
        public int automationId { get; set; }
        public int relatedDeviceId { get; set; }
        public string parameterValue { get; set; }
        public string parameterLogic { get; set; }
        public int sensorId { get; set; }


    }

}
        