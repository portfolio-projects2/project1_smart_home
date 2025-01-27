using WebServer;

//public class Program
//{

//    public static void Main(string[] args)
//        => CreateHostBuilder(args).Build().Run();

//    // EF Core uses this method at design time to access the DbContext
//    public static IHostBuilder CreateHostBuilder(string[] args)
//        => Host.CreateDefaultBuilder(args)
//            .ConfigureWebHostDefaults(
//                webBuilder => webBuilder.UseStartup<Startup>());

//}




var builder = WebApplication.CreateBuilder(args);
var startup = new Startup(builder.Configuration);
startup.ConfigureServices(builder.Services); // calling ConfigureServices method
var app = builder.Build();
startup.Configure(app, builder.Environment);



//using Microsoft.EntityFrameworkCore;
//using WebServer.Models;

//var builder = WebApplication.CreateBuilder(args);



//// Add services to the container.
//builder.Services.AddControllersWithViews();
//var connectionString = builder.Configuration.GetConnectionString("localdb");

//builder.Services.AddDbContext<ProjectDbContext>(options => 
//options.UseMySQL(connectionString)) ;



//var app = builder.Build();

//// Configure the HTTP request pipeline.
//if (app.Environment.IsDevelopment())
//{

//    app.UseExceptionHandler("/Home/Error");
//    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
//    app.UseHsts();
//}

//app.UseHttpsRedirection();
//app.UseStaticFiles();

//app.UseRouting();

//app.UseAuthorization();

//app.MapControllerRoute(
//    name: "default",
//    pattern: "{controller=Home}/{action=Index}/{id?}");





//app.Run();
