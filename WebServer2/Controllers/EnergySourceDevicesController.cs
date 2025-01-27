using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using WebServer.Models;

namespace WebServer.Controllers
{
    public class EnergySourceDevicesController : Controller
    {
        private readonly ProjectDbContext _context;

        public EnergySourceDevicesController(ProjectDbContext context)
        {
            _context = context;
        }

        // GET: EnergySourceDevices
        public async Task<IActionResult> Index()
        {
              return View(await _context.EnergySourceDevice.ToListAsync());
        }

        // GET: EnergySourceDevices/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null || _context.EnergySourceDevice == null)
            {
                return NotFound();
            }

            var energySourceDevice = await _context.EnergySourceDevice
                .FirstOrDefaultAsync(m => m.Id == id);
            if (energySourceDevice == null)
            {
                return NotFound();
            }

            return View(energySourceDevice);
        }

        // GET: EnergySourceDevices/Create
        public IActionResult Create()
        {
            return View();
        }

        // POST: EnergySourceDevices/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,title,specifications")] EnergySourceDevice energySourceDevice)
        {
            Debug.WriteLine(energySourceDevice.Id);
            Debug.WriteLine(energySourceDevice.title);
            Debug.WriteLine(energySourceDevice.specifications);



            if (ModelState.IsValid)
            {
                _context.Add(energySourceDevice);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            return View(energySourceDevice);
        }

        // GET: EnergySourceDevices/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null || _context.EnergySourceDevice == null)
            {
                return NotFound();
            }

            var energySourceDevice = await _context.EnergySourceDevice.FindAsync(id);
            if (energySourceDevice == null)
            {
                return NotFound();
            }
            return View(energySourceDevice);
        }

        // POST: EnergySourceDevices/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,title,specifications")] EnergySourceDevice energySourceDevice)
        {
            if (id != energySourceDevice.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(energySourceDevice);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!EnergySourceDeviceExists(energySourceDevice.Id))
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
            return View(energySourceDevice);
        }

        // GET: EnergySourceDevices/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null || _context.EnergySourceDevice == null)
            {
                return NotFound();
            }

            var energySourceDevice = await _context.EnergySourceDevice
                .FirstOrDefaultAsync(m => m.Id == id);
            if (energySourceDevice == null)
            {
                return NotFound();
            }

            return View(energySourceDevice);
        }

        // POST: EnergySourceDevices/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            if (_context.EnergySourceDevice == null)
            {
                return Problem("Entity set 'ProjectDbContext.EnergySourceDevice'  is null.");
            }
            var energySourceDevice = await _context.EnergySourceDevice.FindAsync(id);
            if (energySourceDevice != null)
            {
                _context.EnergySourceDevice.Remove(energySourceDevice);
            }
            
            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool EnergySourceDeviceExists(int id)
        {
          return _context.EnergySourceDevice.Any(e => e.Id == id);
        }
    }
}
