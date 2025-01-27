namespace WebServer.Models
{
    public class Room
    {
      
        public  int  Id { get; set; }
        public string? roomTitle { get; set; }


        public ICollection<Automation>? roomAutomations { get; set; }

        public ICollection<Sensor>? roomSensors { get; set; }

        public ICollection<Device>? roomDevices { get; set; }

    }
}



