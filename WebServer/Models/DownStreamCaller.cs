using Microsoft.AspNetCore.Mvc;
using Microsoft.Identity.Web;
using System.Diagnostics;
using System.Net.Http.Headers;
using System.Text;
using WebServer.Models;

namespace WebServer.Models
{
    [AuthorizeForScopes(Scopes = new[] { "api://e36cc531-383a-40e9-ac46-7616943ce93c/aaa" })]
    public class DownStreamCaller 
    {
        private String host= "host.docker.internal";
        string[] scopes = new string[] { "api://e36cc531-383a-40e9-ac46-7616943ce93c/aaa" };
        readonly ITokenAcquisition _tokenAcquisition;

        public DownStreamCaller( ITokenAcquisition tokenAcquisition)
        {
            _tokenAcquisition = tokenAcquisition;

        }



        public async Task<HttpClient> GetCallClient()
        {
            string accessToken = await _tokenAcquisition.GetAccessTokenForUserAsync(scopes);

            HttpClient client = new HttpClient();
            client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", accessToken);


            return client;
        }

        public async void RemoveDevice(Device device)
        {
          
                HttpClient client = GetCallClient().Result;
                string requestBody = "{"
                    + $"\"Id\":{device.Id}" + ","
                    + $"\"databaseName\":\"database1" + "\"" + "}";

                HttpContent content = new StringContent(requestBody, Encoding.UTF8, "application/json");

                // Send the request to the specified URL
                try
                {
                    HttpResponseMessage response = await client.PostAsync($"http://{host}:8080/deleteDevice", content);
                }
                catch { }

            }





        public async void RemoveAutomation(Automation automation)
        {
            HttpClient client = GetCallClient().Result;
            string requestBody = "{"
                + $"\"automationId\":{automation.Id}" + ","
                + $"\"databaseName\":\"database1\"" + "}";

            HttpContent content = new StringContent(requestBody, Encoding.UTF8, "application/json");

            // Send the request to the specified URL
            try
            {
                HttpResponseMessage response = await client.PostAsync(new Uri($"http://{host}:8080/deleteAutomation"), content);
            }
            catch { }
        }

        public async void AddDevice(Device device)
        {
            HttpClient client = GetCallClient().Result;

            string requestBody = "{" + $"\"sensorId\":{device.Id}" + ","
                + "\"databaseName\":\"database1\"" + ","
                + $"\"Id\":{device.Id}" + ","
                + $"\"deviceName\":\"{device.deviceName}\"" + ","
                + $"\"comProvider\":\"{device.communicationProvider}\"" + "}";
            HttpContent content = new StringContent(requestBody, Encoding.UTF8, "application/json");

            // Send the request to the specified URL
            try
            {
                HttpResponseMessage response = await client.PostAsync(new Uri($"http://{host}:8080/addDevice"), content);
            }
            catch (Exception e)
            {

            }
        }

        public async void AddParameter(Parameter parameter, ProjectDbContext _context)
        {
            HttpClient client = GetCallClient().Result;
            string parameterValue = "";
            if (parameter.parameterLogic.Equals("onOnly"))
            {
                parameterValue ="\""+$"{parameter.parameterValue}"+"\"";
            }
            else { parameterValue = $"{parameter.parameterValue}"; }
            string requestBody = "{" + $"\"parameterId\":{parameter.Id},"
                + "\"databaseName\":\"database1\","
                + $"\"parameterLogic\":\"{parameter.parameterLogic}\","
                + $"\"parameterValue\":{parameterValue},"
                + $"\"sensordevId\":\"{_context.Sensor.Find(parameter.sensorId).devId}\","
                + $"\"deviceId\":" + parameter.deviceId + ","
                + $"\"automationId\":" + parameter.automationId + "}";
            Debug.WriteLine("reuuuu" + requestBody);
            HttpContent content = new StringContent(requestBody, Encoding.UTF8, "application/json");

            // Send the request to the specified URL
            try
            {
                HttpResponseMessage response = await client.PostAsync(new Uri($"http://{host}:8080/addParameter"), content);
            }
            catch (Exception e)
            {

            }
        }

        public async void EditAutomation(Automation automation)
        {
            HttpClient client =GetCallClient().Result;
            string requestBody = "{"
                + $"\"automationId\":{automation.Id}" + ","
                + $"\"databaseName\":\"database1" + "\"" + ","
                + $"\"timeRange\":{ automation.automationTimeRange}" + ","
                + $"\"automationTitle\":\"{ automation.automationTitle}\"" + "}";
            HttpContent content = new StringContent(requestBody, Encoding.UTF8, "application/json");

            // Send the request to the specified URL
            try
            {
                HttpResponseMessage response = await client.PostAsync(new Uri($"http://{host}:8080/editAutomation"), content);
            }
            catch { }
        }

        public async void EditDevice(Device device)
        {
            HttpClient client = GetCallClient().Result;

            string requestBody = "{"
                + $"\"Id\":{device.Id}" + ","
                + $"\"comProvider\":\"{device.communicationProvider}\"" + ","
                + $"\"databaseName\":\"database1\"" + "}";
            HttpContent content = new StringContent(requestBody, Encoding.UTF8, "application/json");

            // Send the request to the specified URL
            try
            {
                HttpResponseMessage response = await client.PostAsync(new Uri($"http://{host}:8080/editDevice"), content);
            }
            catch (Exception e) { }
        }

        public async void AddSensor(Sensor sensor)
        {
            HttpClient client = GetCallClient().Result;

            string requestBody = "{" + $"\"sensorId\":{sensor.Id}" + ","
                + "\"databaseName\":\"database1\"" + ","
                + $"\"devId\":\"{sensor.devId}" + "\"" + ","
                + $"\"name\":\"{sensor.sensorName}" + "\"}";
            HttpContent content = new StringContent(requestBody, Encoding.UTF8, "application/json");

            // Send the request to the specified URL
            try
            {
                HttpResponseMessage response = await client.PostAsync(new Uri($"http://{host}:8080/addSensor"), content);
            }
            catch { }
        }

        public async void AddAutomation(Automation automation)
        {
            HttpClient client =GetCallClient().Result;

            string requestBody = "{" + $"\"automationId\":{automation.Id},"
                + "\"databaseName\":\"database1\","
                + $"\"title\":\"{automation.automationTitle} \","
                + $"\"active\":" + automation.active + ","
                + $"\"timeRange\":{automation.automationTimeRange}" + "}";
            HttpContent content = new StringContent(requestBody, Encoding.UTF8, "application/json");

            // Send the request to the specified URL
            try
            {
                HttpResponseMessage response = await client.PostAsync(new Uri($"http://{host}:8080/addAutomation"), content);
            }
            catch (Exception e)
            {

            }
        }

        public async void EditParameter(Parameter parameter, ProjectDbContext _context)
        {
            HttpClient client = GetCallClient().Result;
            string parameterValue = "";
            if (parameter.parameterLogic.Equals("onOnly"))
            {
                parameterValue = $"\"{parameter.parameterValue}\"";
            }
            else { parameterValue = $"{parameter.parameterValue}"; }

            string requestBody = "{" + $"\"parameterId\":{parameter.Id},"
                + "\"databaseName\":\"database1\","
                + $"\"parameterLogic\":\"{parameter.parameterLogic}\","
                + $"\"parameterValue\":{parameterValue},"
                + $"\"sensordevId\":\"{_context.Sensor.Find(parameter.sensorId).devId}\","
                + $"\"deviceId\":" + parameter.deviceId + ","
                + $"\"automationId\":" + parameter.automationId + "}";
            Debug.WriteLine("rererer" + requestBody);
            HttpContent content = new StringContent(requestBody, Encoding.UTF8, "application/json");

            // Send the request to the specified URL
            try
            {
                HttpResponseMessage response = await client.PostAsync(new Uri($"http://{host}:8080/editParameter"), content);
            }
            catch (Exception e)
            {

            }
        }

        public async void RemoveSensor(Sensor sensor)
        {
            HttpClient client = GetCallClient().Result;
            string requestBody = "{"
                + $"\"devId\":\"{sensor.devId}" + "\"" + ","
                + $"\"databaseName\":\"database1" + "\"" + "}";

            HttpContent content = new StringContent(requestBody, Encoding.UTF8, "application/json");

            // Send the request to the specified URL
            try
            {
                HttpResponseMessage response = await client.PostAsync(new Uri($"http://{host}:8080/deleteSensor"), content);
            }
            catch { }
        }

        public async void EditSensor(Sensor sensor)
        {
            HttpClient client = GetCallClient().Result;
            string requestBody = "{"
                + $"\"devId\":\"{sensor.devId}" + "\"" + ","
                + $"\"name\":\"{sensor.sensorName}" + "\"}";
            HttpContent content = new StringContent(requestBody, Encoding.UTF8, "application/json");

            // Send the request to the specified URL
            try
            {
                HttpResponseMessage response = await client.PostAsync(new Uri($"http://{host}:8080/editSensor"), content);
            }
            catch { }
        }

        public async void RemoveParameter(Parameter? parameter)
        {
            HttpClient client = GetCallClient().Result;
            string requestBody = "{"
                + $"\"parameterId\":{parameter.Id}" + ","
                + $"\"databaseName\":\"database1\"" + "}";

            HttpContent content = new StringContent(requestBody, Encoding.UTF8, "application/json");

            // Send the request to the specified URL
            try
            {
                HttpResponseMessage response = await client.PostAsync(new Uri($"http://{host}:8080/deleteParameter"), content);
            }
            catch { }
        }
    }
}
