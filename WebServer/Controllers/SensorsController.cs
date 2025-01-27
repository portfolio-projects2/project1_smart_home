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
    public class SensorsController : Controller
    {
        private readonly ProjectDbContext _context;
        private readonly DownStreamCaller _callClient;

        public SensorsController(ProjectDbContext context, DownStreamCaller callClient)
        {
            _callClient = callClient;
            _context = context;
        }

        // GET: Sensors
        public async Task<IActionResult> Index()
        {
            var projectDbContext = _context.Sensor.Include(s => s.room);
            return View(await projectDbContext.ToListAsync());
        }

        // GET: Sensors/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null || _context.Sensor == null)
            {
                return NotFound();
            }

            var sensor = await _context.Sensor
                .Include(s => s.room)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (sensor == null)
            {
                return NotFound();
            }

            return View(sensor);
        }

        // GET: Sensors/Create
        public IActionResult Create()
        {
            List<Room> rooms = _context.Room.ToList();
            ViewBag.rooms = rooms;
            return View();
        }

        // POST: Sensors/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,sensorName,devId,roomId")] Sensor sensor)
        {
           
                ViewBag.context = _context;
                _context.Add(sensor);
            _callClient.AddSensor(sensor);
                await _context.SaveChangesAsync();
            
            ViewData["roomId"] = new SelectList(_context.Room, "Id", "Id", sensor.roomId);

                    
                    return RedirectToAction(nameof(Index));
            
        }

        // GET: Sensors/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null || _context.Sensor == null)
            {
                return NotFound();
            }

            var sensor = await _context.Sensor.FindAsync(id);
            if (sensor == null)
            {
                return NotFound();
            }
            ViewBag.rooms = _context.Room.ToList();
            return View(sensor);
        }

        // POST: Sensors/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,sensorName,devId,roomId")] Sensor sensor)
        {
            if (id != sensor.Id)
            {
                return NotFound();
            }

           
                try
                { _context.Update(sensor);
                await _context.SaveChangesAsync();
                _callClient.EditSensor(sensor);
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!SensorExists(sensor.Id))
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

        // GET: Sensors/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null || _context.Sensor == null)
            {
                return NotFound();
            }

            var sensor = await _context.Sensor
                .Include(s => s.room)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (sensor == null)
            {
                return NotFound();
            }

            return View(sensor);
        }

        // POST: Sensors/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            if (_context.Sensor == null)
            {
                return Problem("Entity set 'ProjectDbContext.Sensor'  is null.");
            }
            var sensor = _context.Sensor.Where(a => a.Id == id).Include(a => a.SensorAutomations).ToList().Last();
            _context.Sensor.Remove(sensor);
            try
            {
                foreach (Automation a in sensor.SensorAutomations)
                {
                    Debug.WriteLine("autDevicesCount" + a.automationSensors.Count());
                    if (a.automationSensors.ToList().Count() == 0)
                    {
                        _context.Automation.Remove(a);
                        _callClient.RemoveAutomation(a);

                    }


                }
            }
            catch { }
            if (sensor != null)
            {
               
                _context.Sensor.Remove(sensor);
                _callClient.RemoveSensor(sensor);
            }
            
            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool SensorExists(int id)
        {
          return _context.Sensor.Any(e => e.Id == id);
        }
    }
}
