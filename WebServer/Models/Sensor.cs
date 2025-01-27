using System.ComponentModel.DataAnnotations.Schema;

namespace WebServer.Models
{
    public class Sensor
    {
        public int Id { get; set; }
        public string? sensorName { get; set; }
     
        public string devId { get; set; }
      
        [ForeignKey("Room")]
        public int roomId { get; set; }

        public Room? room { get; set; }


        public ICollection<Automation>? SensorAutomations { get; set; }

        public ICollection<Parameter>? SensorParameters { get; set; }

    }
}



