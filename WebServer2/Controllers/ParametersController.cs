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
    public class ParametersController : Controller
    {
        private readonly ProjectDbContext _context;
        private readonly DownStreamCaller _callClient;

        public ParametersController(ProjectDbContext context, DownStreamCaller callClient)
        {
            _callClient = callClient;
            _context = context;
        }

        // GET: Parameters
        public async Task<IActionResult> Index()
        {
            var projectDbContext = _context.Parameter.Include(p => p.automation).Include(p => p.device).Include(p => p.sensor);
            ViewBag.automations = _context.Automation.Include(a => a.automationDevices).Include(a=>a.AutomationParameters).ToList();


            return View(await projectDbContext.ToListAsync());
        }

        // GET: Parameters/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null || _context.Parameter == null)
            {
                return NotFound();
            }

            var parameter = await _context.Parameter
                .Include(p => p.automation)
                .Include(p => p.device)
                .Include(p => p.sensor)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (parameter == null)
            {
                return NotFound();
            }



            return View(parameter);
        }

        // GET: Parameters/Create
        public IActionResult Create(int id)
        {

            ViewBag.context = _context;
            ViewBag.automationId = id;
            
           
            //ViewData["automationId"] = new SelectList(_context.Automation, "Id", "Id");
            //ViewData["deviceId"] = new SelectList(_context.Device, "Id", "Id");
            //ViewData["sensorId"] = new SelectList(_context.Sensor, "Id", "Id");
            return View();
        }

        // POST: Parameters/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        
        public async Task<IActionResult> AddParameter([FromBody] ViewsModel vm)
        {
            foreach (ParameterObject ob in vm.parametersList)
            {
                Parameter parameter = new Parameter();

                parameter.automationId = ob.automationId;
                parameter.deviceId = ob.relatedDeviceId;
                parameter.parameterLogic = ob.parameterLogic;
                parameter.sensorId = ob.sensorId;
                parameter.parameterValue = ob.parameterValue;
                parameter.parameterTitle = ob.parameterTitle;
                _context.Add(parameter);
                _context.SaveChanges();
                _callClient.AddParameter(parameter, _context);

            }
            return Json(new { url = $"/" });
        }

        // GET: Parameters/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null || _context.Parameter == null)
            {
                return NotFound();
            }

            var parameter = await _context.Parameter.FindAsync(id);
            if (parameter == null)
            {
                return NotFound();
            }
            ViewData["automationId"] = new SelectList(_context.Automation, "Id", "Id", parameter.automationId);
            ViewData["deviceId"] = new SelectList(_context.Device, "Id", "Id", parameter.deviceId);
            ViewData["sensorId"] = new SelectList(_context.Sensor, "Id", "Id", parameter.sensorId);
            return View(parameter);
        }

        // POST: Parameters/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,parameterTitle,parameterLogic,parameterValue,sensorId,deviceId,automationId")] Parameter parameter)
        {
            if (id != parameter.Id)
            {
                return NotFound();
            }

                try
                {
                    _context.Update(parameter);
                    await _context.SaveChangesAsync();
                _callClient.EditParameter(parameter,_context);


                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!ParameterExists(parameter.Id))
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

        // GET: Parameters/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null || _context.Parameter == null)
            {
                return NotFound();
            }

            var parameter = await _context.Parameter
                .Include(p => p.automation)
                .Include(p => p.device)
                .Include(p => p.sensor)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (parameter == null)
            {
                return NotFound();
            }

            return View(parameter);
        }

        // POST: Parameters/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            if (_context.Parameter == null)
            {
                return Problem("Entity set 'ProjectDbContext.Parameter'  is null.");
            }
            var parameter = await _context.Parameter.FindAsync(id);
            if (parameter != null)
            {
                _context.Parameter.Remove(parameter);
            }
            
            await _context.SaveChangesAsync();
            _callClient.RemoveParameter(parameter);

        


            return RedirectToAction(nameof(Index));
        }

        private bool ParameterExists(int id)
        {
          return _context.Parameter.Any(e => e.Id == id);
        }
    }
}
