using System.ComponentModel.DataAnnotations.Schema;

namespace WebServer.Models
{
    public class Parameter
    {

        public int Id { get; set; }

        public string parameterTitle { get; set; }
        public string? parameterLogic { get; set; }
        public string? parameterValue { get; set; }
       
        [ForeignKey("Sensor")]
        public int sensorId { get; set; }
        public Sensor sensor { get; set; }
      
        
        [ForeignKey("Device")]
        public int deviceId { get; set; }
        public Device device { get; set; }
       
       
        
        [ForeignKey("Automation")]
        public int automationId { get; set; }
        public Automation? automation { get; set; }

    }
}
