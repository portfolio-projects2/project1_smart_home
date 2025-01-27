namespace WebServer.Models
{
    public class EnergySourceDevice
    {
        public int Id { get; set; }
        public string? title { get; set; }

        public string? specifications { get; set; }

        public ICollection<Automation>? energySourceAutomations { get; set; }

    }
}
