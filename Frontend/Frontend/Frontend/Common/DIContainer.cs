using Autofac;
using Frontend.Services;
using System;
using System.Collections.Generic;
using System.Text;

namespace Frontend.Common
{
    public static class DIContainer
    {
        private static IContainer _container;

        public static IRestService RestService { get { return _container.Resolve<IRestService>(); } }
        public static void Initialize()
        {
            if (_container == null)
            {
                var builder = new ContainerBuilder();

                builder.RegisterType<RestService>().As<IRestService>().SingleInstance();

                _container = builder.Build();
            }
        }
    }
}
