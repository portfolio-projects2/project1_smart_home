using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using WebServer.Models;

namespace WebServer.Controllers
{
    public class RoomsController : Controller
    {
        private readonly ProjectDbContext _context;
        private readonly DownStreamCaller _callClient;

        public RoomsController(ProjectDbContext context, DownStreamCaller callClient)
        {
            _context = context;
            _callClient = callClient;
        }

            // GET: Rooms
            public async Task<IActionResult> Index()
        {
              return View(await _context.Room.ToListAsync());
        }

        // GET: Rooms/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null || _context.Room == null)
            {
                return NotFound();
            }

            var room = await _context.Room
                .FirstOrDefaultAsync(m => m.Id == id);
            if (room == null)
            {
                return NotFound();
            }

            return View(room);
        }

        // GET: Rooms/Create
        public IActionResult Create()
        {
            return View();
        }

        // POST: Rooms/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
       // [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,roomTitle")] Room room)
        { 
                _context.Add(room);
               
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            
          
        }

        // GET: Rooms/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null || _context.Room == null)
            {
                return NotFound();
            }

            var room = await _context.Room.FindAsync(id);
            if (room == null)
            {
                return NotFound();
            }
            return View(room);
        }

        // POST: Rooms/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,roomTitle")] Room room)
        {
            if (id != room.Id)
            {
                return NotFound();
            }

          
                try
                {
                    _context.Update(room);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!RoomExists(room.Id))
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

        // GET: Rooms/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null || _context.Room == null)
            {
                return NotFound();
            }

            var room = await _context.Room
                .FirstOrDefaultAsync(m => m.Id == id);
            if (room == null)
            {
                return NotFound();
            }

            return View(room);
        }

        // POST: Rooms/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {

            if (_context.Room == null)
            {
                return Problem("Entity set 'ProjectDbContext.Room'  is null.");
            }
            var room =  _context.Room.Where(a=>a.Id==id).Include(a=>a.roomAutomations).Include(a=>a.roomDevices).Include(a=>a.roomSensors).ToList().Last();
            if (room != null)
            {

               

                _context.Room.Remove(room);

            }
            
            await _context.SaveChangesAsync();
            List<Sensor> sensors = room.roomSensors.ToList();
            List<Device> devices = room.roomDevices.ToList();
            List<Automation> automations = room.roomAutomations.ToList();
            foreach (Device d in devices)
            {
                _callClient.RemoveDevice(d);
            }
            foreach (Sensor s in sensors)
            {
                _callClient.RemoveSensor(s);
            }
            foreach (Automation a in automations)
            {
                _callClient.RemoveAutomation(a);
            }
            return RedirectToAction(nameof(Index));
        }

        private bool RoomExists(int id)
        {
          return _context.Room.Any(e => e.Id == id);
        }
    }
}
