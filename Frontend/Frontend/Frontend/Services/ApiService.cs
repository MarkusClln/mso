using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using Xamarin.Forms;

[assembly: Dependency(typeof(Frontend.Services.ApiService))]
namespace Frontend.Services
{
    public class ApiService : IApiService
    {
        HttpClient client;

        public async Task<List<string>> Get()
        {
            var list = new List<string>();

            client = new HttpClient();

            //set url of the nodejs api with parameter e.g. "localhost:1234/pins/get/id"
            var uri = new Uri("");

            //use getasync to get what you want
            var response = await client.GetAsync(uri);

            if (response.IsSuccessStatusCode)
            {
                //Read json response as string and convert it to your desired datatype
                var content = await response.Content.ReadAsStringAsync();
                list = JsonConvert.DeserializeObject<List<string>>(content);
            }

            return list;
        }

        public string GetDebug()
        {
            return "Hallo du grausame Welt";
        }

        public bool Post()
        {
            throw new NotImplementedException();
        }
    }
}
