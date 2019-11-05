using System;
using System.Collections.Generic;
using System.Text;
using Xamarin.Forms;

[assembly: Dependency(typeof(Frontend.Services.RestService))]
namespace Frontend.Services
{
    public class RestService : IRestService
    {
        public string Get()
        {
            return "Hallo du grausame Welt";
        }

        public bool Post()
        {
            throw new NotImplementedException();
        }
    }
}
