using Microsoft.EntityFrameworkCore;
using WebServer.Models;

namespace WebServer.Models
{
    public class ProjectDbContext : DbContext
    {
        public ProjectDbContext(DbContextOptions<ProjectDbContext> options)
        : base(options)

        {

        }


        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);
        }



        public virtual DbSet<Automation> Automation { get; set; } = default!;
        public virtual DbSet<Room> Room { get; set; } = default!;
        public virtual DbSet<Device> Device { get; set; } = default!;
        public virtual DbSet<Sensor> Sensor { get; set; } = default!;
        public virtual DbSet<sensorsdata> sensorsdata { get; set; } = default!;
        public virtual DbSet<Parameter> Parameter { get; set; } = default!;
        public DbSet<WebServer.Models.EnergySourceDevice> EnergySourceDevice { get; set; }


        
      

    }
}