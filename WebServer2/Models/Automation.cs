using System.ComponentModel.DataAnnotations.Schema;

namespace WebServer.Models
{
    public class Automation
    {


        public int Id { get; set; }

        public int? active { get; set; }

        public string? automationTitle { get; set; }

        public string? automationTimeRange { get; set; }

        [ForeignKey("energySourceDeviceId")]
        public int energySourceDeviceId { get; set; }
        public EnergySourceDevice? energySourceDevice { get; set; }

        public ICollection<Room>  automationRooms { get; set; }


        public ICollection<Device> automationDevices { get; set; }


        public ICollection<Sensor> automationSensors { get; set; }


        public ICollection<Parameter>? AutomationParameters { get; set; }

    }
}
