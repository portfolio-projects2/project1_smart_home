using System.ComponentModel.DataAnnotations.Schema;

namespace WebServer.Models
{
    public class Device
    {
        public int Id { get; set; }

        public string? deviceName { get; set; }
       
        
        [ForeignKey("Room")]
        public int roomId { get; set; }
        public Room room { get; set; }

        public string communicationProvider { get; set; }


        public ICollection<Automation>? deviceAutomations { get; set; }

        public ICollection<Parameter>? deviceParameters { get; set; }


    }
}


