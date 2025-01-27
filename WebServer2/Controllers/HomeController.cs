using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Identity.Web;
using System.Diagnostics;
using System.Net.Http.Headers;
using WebServer.Models;

namespace WebServer.Controllers
{
    //[Authorize("admin")]
    [AuthorizeForScopes(Scopes = new[] { "api://e36cc531-383a-40e9-ac46-7616943ce93c/aaa" })]
    public class HomeController : Controller
    {
        string[] scopes = new string[] { "api://e36cc531-383a-40e9-ac46-7616943ce93c/aaa" };
        private readonly ILogger<HomeController> _logger;
        private readonly ProjectDbContext _context;
        readonly ITokenAcquisition _tokenAcquisition;
        public HomeController(ILogger<HomeController> logger, ProjectDbContext context, ITokenAcquisition tokenAcquisition)
        {
            _tokenAcquisition = tokenAcquisition;
            _context = context;
            _logger = logger;
        }



       

        public async Task<IActionResult> IndexAsync()
        {
            
            string accessToken = await  _tokenAcquisition.GetAccessTokenForUserAsync(scopes);

            Debug.WriteLine("helloo" + accessToken);
           
            ViewBag.context = _context;
           
            HttpClient client = new HttpClient();
            client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", accessToken);
            ViewBag.token = accessToken;

           
            return View();
        }





        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
        }
    }
}