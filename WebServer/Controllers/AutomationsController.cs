using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Diagnostics;
using System.Globalization;
using System.Linq;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.ModelBinding;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using Microsoft.Identity.Web;
using Nancy.Json;
using Newtonsoft.Json.Linq;
using WebServer.Models;

namespace WebServer.Controllers
{

    public class AutomationsController : Controller
    {
        private readonly DownStreamCaller _callClient;

        private readonly ProjectDbContext _context;

        public AutomationsController(ProjectDbContext context, DownStreamCaller callClient)
        {
            _context = context;
            _callClient = callClient;

        }

        // GET: Automations
        public async Task<IActionResult> Index()
        {
              return View(await _context.Automation.ToListAsync());
        }

        // GET: Automations/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            ViewBag.automation = _context.Automation.Include(a => a.automationDevices).Include(a=>a.automationSensors).Include(a=>a.automationRooms).ToList().Find(a=>a.Id==id);
            ViewBag.context = _context;
            
            if (id == null || _context.Automation == null)
            {
                return NotFound();
            }

            var automation = await _context.Automation
                .FirstOrDefaultAsync(m => m.Id == id);
            if (automation == null)
            {
                return NotFound();
            }

            return View(automation);
        }

        // GET: Automations/Create
        public IActionResult Create()
        {
            ViewBag.context = _context;

            return View();
        }

        // POST: Automations/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]

        public async Task<IActionResult> Create([FromBody] ViewsModel vm)
        {
            //Debug.WriteLine("jjjjjjjjjjjjjjjjjj" + vm.energySource);
            // get energySource instance
            EnergySourceDevice energySourceDevice = _context.EnergySourceDevice.Find(vm.energySource);
            // get time range for this automation 
            string autTimeRange = "{\"start\":\"" + vm.automationTimeRange.start + "\",\"off\":\"" + vm.automationTimeRange.off + "\"}";
            DateTime autStartTime = DateTime.ParseExact(vm.automationTimeRange.start, "dd-MM-yyyy-HH-mm",
                                        CultureInfo.InvariantCulture);
            DateTime autOffTime = DateTime.ParseExact(vm.automationTimeRange.off, "dd-MM-yyyy-HH-mm",
                                CultureInfo.InvariantCulture);
            // validating time range
            foreach (var d in vm.devices)
            {
                Device device = _context.Device.Where(a => a.Id == d).Include(a => a.deviceAutomations).ToList().Last();

                foreach(Automation a in device.deviceAutomations)
                {
                    JObject timeRange = JObject.Parse(a.automationTimeRange);
                    Debug.WriteLine(timeRange.GetValue("start"));
                    
                    DateTime startTimeDatabase = DateTime.ParseExact(timeRange.GetValue("start").ToString(), "dd-MM-yyyy-HH-mm",
                                        CultureInfo.InvariantCulture);
                    DateTime offTimeDatabase = DateTime.ParseExact(timeRange.GetValue("off").ToString(), "dd-MM-yyyy-HH-mm",
                                        CultureInfo.InvariantCulture);
                    if((autStartTime>startTimeDatabase&&autStartTime<offTimeDatabase)|| (autOffTime > startTimeDatabase && autOffTime < offTimeDatabase)||
                        (startTimeDatabase > autStartTime && startTimeDatabase < autOffTime) || (offTimeDatabase > autStartTime && offTimeDatabase < autOffTime))
                    {
                            return StatusCode(400, $"The {device.deviceName} is resereved by atutomation process {a.automationTitle} between {startTimeDatabase} and {offTimeDatabase} please choose another time ");

                    }




                }

            }



            // create a new automation object
            Debug.WriteLine("Valll" + ModelState.IsValid);
          
                Automation aut = new Automation()
                {

                    energySourceDeviceId = energySourceDevice.Id, // Ensure it's correctly assigned
                    automationTitle = vm.automationTitle,
                    active = vm.automationState,
                    automationTimeRange = autTimeRange                                    // other properties

                };
                _context.Add(aut);
                _context.SaveChanges();
            
            int automationId = _context.Automation.Where(a => (a.automationTitle.Equals(vm.automationTitle))).ToList().Last().Id;
            
            List<Room> automationRooms = new List<Room>();
            List<Sensor> automationSensors = new List<Sensor>();
            List<Device> automationDevices = new List<Device>();

            foreach (var sensorId in vm.sensors)
            {
                
                Sensor automationSensor = _context.Sensor.Find(sensorId);

                automationSensors.Add(automationSensor);
      

            }
            foreach (var deviceId in vm.devices)
            {

                Device automationDevice = _context.Device.Find(deviceId);
                Room automationRoom = _context.Room.Find(automationDevice.roomId);

                if (!automationRooms.Contains(automationRoom))
                {
                    automationRooms.Add(automationRoom);
                }
                automationDevices.Add(automationDevice);

            }

            Automation automation = _context.Automation.Find(automationId);
            automation.automationRooms = automationRooms;
            automation.automationSensors = automationSensors;
            automation.automationDevices=automationDevices;
            ViewBag.rooms = automationRooms;
            ViewBag.sensors = automationSensors;
            ViewBag.devices = automationDevices;
            ViewBag.automationId = automationId;
            ViewBag.context = _context;
            foreach(var a in automation.automationSensors)
            {
                Debug.WriteLine(a.sensorName);
            }
            _context.Update(automation);
            _context.SaveChanges();

            _callClient.AddAutomation(automation);





            return Json(new { url = $"../parameters/create/{automationId}" });
         
        }



        // GET: Automations/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null || _context.Automation == null)
            {
                return NotFound();
            }

            var automation = await _context.Automation.FindAsync(id);
            if (automation == null)
            {
                return NotFound();
            }
            ViewBag.energySources = _context.EnergySourceDevice.ToList();

            return View(automation);
        }

        // POST: Automations/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,active,automationTitle,automationTimeRange,energySourceDeviceId")] Automation automation)
        {
              Debug.WriteLine("reach edit1");

            if (id != automation.Id)
            {
              

                return NotFound();
            }

            
                    _context.Update(automation);
                    await _context.SaveChangesAsync();
            _callClient.EditAutomation(automation);

                return RedirectToAction(nameof(Index));
            
        }

        // GET: Automations/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null || _context.Automation == null)
            {
                return NotFound();
            }

            var automation = await _context.Automation
                .FirstOrDefaultAsync(m => m.Id == id);
            if (automation == null)
            {
                return NotFound();
            }

            return View(automation);
        }

        // POST: Automations/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            if (_context.Automation == null)
            {
                return Problem("Entity set 'ProjectDbContext.Automation'  is null.");
            }
            var automation = await _context.Automation.FindAsync(id);
            if (automation != null)
            {
                _context.Automation.Remove(automation);
                await _context.SaveChangesAsync();
                _callClient.RemoveAutomation(automation);

            }
            
            
            return RedirectToAction(nameof(Index));
        }

        private bool AutomationExists(int id)
        {
          return _context.Automation.Any(e => e.Id == id);
        }
    }
}
