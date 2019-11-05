using Frontend.Common;
using Frontend.Services;
using System;
using System.Collections.Generic;
using System.Text;
using System.Windows.Input;
using Xamarin.Forms;

namespace Frontend.ViewModels
{
    public class NewItemViewModel : BaseViewModel
    {
        private bool isVisible;
        private string name;
        private string description;

        public string Name
        {
            get { return name; }
            set { SetProperty(ref this.name, value); }
        }
        public string Description
        {
            get { return description; }
            set { SetProperty(ref this.description, value); }
        }

        public bool IsVisible 
        {
            get { return isVisible; }
            set { SetProperty(ref this.isVisible, value); }
        }

        public NewItemViewModel()
        {
            var test = DIContainer.ApiService;

            this.Name = "Hallooo";
            this.Description = test.GetDebug();
            this.IsVisible = false;
            this.TestCommand = new Command(Test);
        }

        public ICommand TestCommand { get; }

        public void Test()
        {
            this.IsVisible = true;
        }
    }
}
