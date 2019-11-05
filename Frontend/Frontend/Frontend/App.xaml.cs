using System;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;
using Frontend.Services;
using Frontend.Views;
using Autofac;
using Xamarin.Forms.Internals;
using Frontend.Common;

namespace Frontend
{
    public partial class App : Application
    {
        public App()
        {
            InitializeComponent();

            DIContainer.Initialize();
            //DependencyService.Register<MockDataStore>();
            MainPage = new MainPage();
        }

        protected override void OnStart()
        {
            // Handle when your app starts
        }

        protected override void OnSleep()
        {
            // Handle when your app sleeps
        }

        protected override void OnResume()
        {
            // Handle when your app resumes
        }
    }
}
