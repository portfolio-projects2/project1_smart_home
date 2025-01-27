using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using Microsoft.Identity.Web;
using WebServer.Models;

namespace WebServer.Controllers
{

    public class DevicesController : Controller
    {
        private readonly DownStreamCaller _callClient;
        private readonly ProjectDbContext _context;

        public DevicesController(ProjectDbContext context, DownStreamCaller callClient)
        {
            _context = context;
            _callClient = callClient;
        }

        // GET: Devices
        public async Task<IActionResult> Index()
        {
            var projectDbContext = _context.Device.Include(d => d.room);
            return View(await projectDbContext.ToListAsync());
        }

        // GET: Devices/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null || _context.Device == null)
            {
                return NotFound();
            }

            var device = await _context.Device
                .Include(d => d.room)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (device == null)
            {
                return NotFound();
            }

            return View(device);
        }

        // GET: Devices/Create
        public IActionResult Create()
        {
            List<Room> rooms = _context.Room.ToList();
            ViewBag.rooms = rooms;
            return View();
        }

        // POST: Devices/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,deviceName,roomId,communicationProvider")] Device device)
        {
           
                _context.Add(device);
                await _context.SaveChangesAsync();
            _callClient.AddDevice(device);
               
                return RedirectToAction(nameof(Index));

            
            
        }

        // GET: Devices/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null || _context.Device == null)
            {
                return NotFound();
            }

            var device = await _context.Device.FindAsync(id);
            if (device == null)
            {
                return NotFound();
            }
            List<Room> rooms = _context.Room.ToList();
            ViewBag.rooms = rooms;
            ViewData["roomId"] = new SelectList(_context.Room, "Id", "Id", device.roomId);
            return View(device);
        }

        // POST: Devices/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,deviceName,roomId,communicationProvider")] Device device)
        {
            if (id != device.Id)
            {
                return NotFound();
            }

           
                try
                {
                    _context.Update(device);
                    await _context.SaveChangesAsync();
                _callClient.EditDevice(device);
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!DeviceExists(device.Id))
                    {
                        return NotFound();
                    }
                    else
                    {
                        throw;
                    }
                }
                return RedirectToAction(nameof(Index));
           
        }

        // GET: Devices/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null || _context.Device == null)
            {
                return NotFound();
            }

            var device = await _context.Device
                .Include(d => d.room)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (device == null)
            {
                return NotFound();
            }

            return View(device);
        }

        // POST: Devices/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            if (_context.Device == null)
            {
                return Problem("Entity set 'ProjectDbContext.Device'  is null.");
            }
            var device =  _context.Device.Where(a=>a.Id==id).Include(a=>a.deviceAutomations).ToList().Last();
            _context.Device.Remove(device);
            try
            {
                foreach(Automation a in device.deviceAutomations)
                {
                    Debug.WriteLine("autDevicesCount"+a.automationDevices.Count());
                    if (a.automationDevices.ToList().Count() == 1)
                    {
                        _context.Automation.Remove(a);
                        _callClient.RemoveAutomation(a);
                    }


                }
            }
            catch { }


            if (device != null)
            {
                _callClient.RemoveDevice(device);
                    
            }
            
            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool DeviceExists(int id)
        {
          return _context.Device.Any(e => e.Id == id);
        }


        [HttpGet]
         public async Task<IActionResult> stopDeviceManually(int id)
        {
            HttpClient client = _callClient.GetCallClient().Result;
            HttpContent content = new StringContent("{\"databaseName\":\"database1\"}", Encoding.UTF8, "application/json");


            // Send the request to the specified URL
        
                HttpResponseMessage response = await client.PostAsync($"http://host.docker.internal:8080/stopDevice/{id}", content);
           



            return Json(Response.StatusCode);
        }

        [HttpGet]
        public async Task<IActionResult> startDeviceManually(int id)
        {
            HttpClient client = _callClient.GetCallClient().Result;
            HttpContent content = new StringContent("{\"databaseName\":\"database1\"}", Encoding.UTF8, "application/json");


            // Send the request to the specified URL

                HttpResponseMessage response = await client.PostAsync($"http://host.docker.internal:8080/startDevice/{id}", content);
            


            return Json(Response.StatusCode);
        }

        [HttpGet]
        public async Task<IActionResult> stopManualControl(int id)
        {
            HttpClient client = _callClient.GetCallClient().Result;
            HttpContent content = new StringContent("{"+$"\"databaseName\":\"database1\",\"deviceId\":{id}"+"}", Encoding.UTF8, "application/json");


            // Send the request to the specified URL

            HttpResponseMessage response = await client.PostAsync($"http://host.docker.internal:8080/stopManualControl", content);



            return Json(Response.StatusCode);
        }

    }


    

}
