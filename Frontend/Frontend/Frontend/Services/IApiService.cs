using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace Frontend.Services
{
    public interface IApiService
    {
        string GetDebug();

        Task<List<string>> Get();

        bool Post();
    }
}
