
using WebServer.Models;
using Microsoft.AspNetCore.Authentication.OpenIdConnect;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Authorization;
using Microsoft.EntityFrameworkCore.Design;
using Microsoft.Identity.Web;
using MySql.EntityFrameworkCore.Extensions;
using Microsoft.AspNetCore.DataProtection;
using Microsoft.EntityFrameworkCore;

namespace WebServer
{
    public class Startup : Controller
    {
        public IConfiguration configRoot
        {
            get;
        }
        public Startup(IConfiguration configuration)
        {
            configRoot = configuration;
        }
        public void ConfigureServices(IServiceCollection services)

        {
            services.AddScoped<DownStreamCaller>();
            services.AddDbContext<ProjectDbContext>(
                options =>
                {
                    string dataSource = "";
                    if (System.Environment.GetEnvironmentVariables().Contains("dataSource"))
                    {
                        dataSource = System.Environment.GetEnvironmentVariable("dataSource");
                    }
                    else
                    {
                        dataSource = "localdb";
                    }
                 
                        
                   
                    
                    var connetionString = configRoot.GetConnectionString(dataSource);

                    options.UseMySQL(connetionString);


                });
            services.AddControllersWithViews();
            services.AddRazorPages().AddMvcOptions(options =>
            {
                var policy = new AuthorizationPolicyBuilder()
                              .RequireAuthenticatedUser()
                              .Build();
                options.Filters.Add(new AuthorizeFilter(policy));
            });
            string[] initialScopes = configRoot.GetValue<string>("DownstreamApi:Scopes")?.Split(' ');

            services.AddAuthentication(OpenIdConnectDefaults.AuthenticationScheme)
.AddMicrosoftIdentityWebApp(configRoot.GetSection("AzureAd")).EnableTokenAcquisitionToCallDownstreamApi(initialScopes)
           .AddMicrosoftGraph(configRoot.GetSection("DownstreamApi"))
           .AddInMemoryTokenCaches();
        
           
        }

        public void Configure(WebApplication app, IWebHostEnvironment env)
        {
            if (!app.Environment.IsDevelopment())
            {
                app.UseExceptionHandler("/Error");
                // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
                app.UseHsts();
            }

           // app.UseHttpsRedirection();
            app.UseStaticFiles();
            app.UseRouting();
            app.UseCors();
            app.UseAuthentication();
            app.UseAuthorization();
            app.MapControllerRoute(
                name: "default",
                pattern: "{controller=Home}/{action=Index}/{id?}");
            
            app.Run();
        }
    }

    public class MysqlEntityFrameworkDesignTimeServices : IDesignTimeServices
    {
        public void ConfigureDesignTimeServices(IServiceCollection serviceCollection)
        {
            serviceCollection.AddEntityFrameworkMySQL();
            new EntityFrameworkRelationalDesignServicesBuilder(serviceCollection)
                .TryAddCoreServices();
        }
    }


}
    
