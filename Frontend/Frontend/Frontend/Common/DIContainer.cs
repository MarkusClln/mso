using Autofac;
using Frontend.Services;
using System;
using System.Collections.Generic;
using System.Text;

namespace Frontend.Common
{
    /// <summary>
    /// Dependency Injection Container
    /// Used for registering services, so they can be used anywhere you need a specific service
    /// </summary>
    public static class DIContainer
    {
        private static IContainer _container;

        //create a public property which just returns a specific service
        public static IApiService ApiService { get { return _container.Resolve<IApiService>(); } }
       
        //gets called at application start
        public static void Initialize()
        {
            if (_container == null)
            {
                var builder = new ContainerBuilder();

                //register your service 
                builder.RegisterType<ApiService>().As<IApiService>().SingleInstance();

                _container = builder.Build();
            }
        }
    }
}
