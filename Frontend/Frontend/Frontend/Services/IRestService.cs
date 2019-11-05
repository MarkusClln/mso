using System;
using System.Collections.Generic;
using System.Text;

namespace Frontend.Services
{
    public interface IRestService
    {
        string Get();

        bool Post();
    }
}
