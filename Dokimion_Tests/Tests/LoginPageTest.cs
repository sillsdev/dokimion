﻿using Dokimion.Interactions;
using Dokimion.Pages;
using Boa.Constrictor.Screenplay;
using Boa.Constrictor.Selenium;
using OpenQA.Selenium.Chrome;
using OpenQA.Selenium;

namespace Dokimion.Tests
{
    internal class LoginPageTest
    {


        private IActor Actor;
        UserActions userActions;

        [OneTimeSetUp]
        public void Setup()
        {

            userActions = new UserActions();
            userActions.LogConsoleMessage("In one time Set up :" + TestContext.CurrentContext.Test.ClassName);
            userActions.LogConsoleMessage("Register Driver & Open the Dokimion website");

            Actor = new Actor(name: userActions.ActorName, logger: new NoOpLogger());
            ChromeDriver driver = new ChromeDriver(userActions.GetChromeOptions());
            driver.Manage().Timeouts().PageLoad = TimeSpan.FromSeconds(300);

            ICapabilities capabilities = driver.Capabilities;
            var browserName =     capabilities.GetCapability("browserName");
            var browserVersion = capabilities.GetCapability("browserVersion");
            var SeleniumWebDriverVersion = (capabilities.GetCapability("chrome") as Dictionary<string, object>)!["chromedriverVersion"];

            userActions.LogConsoleMessage("BrowserName : " + browserName);
            userActions.LogConsoleMessage("browserVersion : " + browserVersion);
            userActions.LogConsoleMessage("ChromeDriver : " + driver.GetType().ToString());
            userActions.LogConsoleMessage("SeleniumWebDriverVersion " + SeleniumWebDriverVersion);

            var count = 1;

            try
            {
                Actor.Can(BrowseTheWeb.With(driver));
                Actor.AttemptsTo(Navigate.ToUrl(userActions.DokimionUrl));
                //Page is redirected after initial URL
                Actor.AttemptsTo(WaitAndRefresh.For(LoginPage.NameInput));
            }
            catch (Exception ex)
            {
                Actor.AttemptsTo(WaitAndRefresh.For(LoginPage.NameInput).ForAnAdditional(3));
                count++;
                userActions.LogConsoleMessage("Unable to load page : retried with addtionatime on " + count + " " + ex.ToString());
                
            }

            Actor.WaitsUntil(Appearance.Of(LoginPage.LoginPageWelcomeMsg), IsEqualTo.True());
            var welcomeMessage = Actor.AskingFor(Text.Of(LoginPage.LoginPageWelcomeMsg));
            userActions.LogConsoleMessage("Login Page is loaded successfully on count " + count + " " + welcomeMessage);
        }


        [OneTimeTearDown]
        public void QuitBrowser()
        {
            Actor.AttemptsTo(QuitWebDriver.ForBrowser());
        }

        [TearDown]
        public void TearDownAfterTestCase()
        {
            userActions.TearDownAfterTestcase();
        }

        [Test]
        public void TC1LoginValidPage()
        {
            userActions.LogConsoleMessage(TestContext.CurrentContext.Test.MethodName!);
            userActions.LogConsoleMessage("Set Up : ");

            userActions.LogConsoleMessage("Action steps : ");
            userActions.LogConsoleMessage("Enter the Username : ");


            Actor.AttemptsTo(Clear.On(LoginPage.NameInput));
            Actor.AttemptsTo(SendKeys.To(LoginPage.NameInput, userActions.Username).ThenHitEnter());

            userActions.LogConsoleMessage("Enter the password : ");
            Actor.WaitsUntil(Appearance.Of(LoginPage.PasswordInput), IsEqualTo.True());
            Actor.AttemptsTo(Clear.On(LoginPage.PasswordInput));
            Actor.AttemptsTo(SendKeys.To(LoginPage.PasswordInput, userActions.Password).ThenHitEnter());

            userActions.LogConsoleMessage("Click Sign in button");
            Actor.AttemptsTo(Click.On(LoginPage.SingInButton));


            try
            {
                userActions.LogConsoleMessage("Verify : Username is on top right menu");
                Actor.AttemptsTo(Refresh.Browser());
                //Actor.AttemptsTo(WaitAndRefresh.For(Header.UserInfo));
                Actor.WaitsUntil(Text.Of(Header.UserInfo),ContainsSubstring.Text(userActions.DisplayUserName), 
                    timeout:60) ;

            }
            finally
            {
                userActions.LogConsoleMessage("Clean up : Logout User");
                try
                {
                    Actor.AttemptsTo(Refresh.Browser());
                    // Actor.AttemptsTo(WaitAndRefresh.For(Header.UserInfo));
                }
                catch (Exception e)
                {
                    userActions.LogConsoleMessage("Added Additional time to find User Name " + e.Source);
                    Actor.AttemptsTo(WaitAndRefresh.For(Header.UserInfo).ForAnAdditional(5));
                }

                Actor.AttemptsTo(Logout.For());
            }

        }


        [Test]
        public void TC2LoginInvalidUser()
        {
            userActions.LogConsoleMessage(TestContext.CurrentContext.Test.MethodName!);
            userActions.LogConsoleMessage("Set Up : ");

            userActions.LogConsoleMessage("Action steps : ");
            userActions.LogConsoleMessage("Enter the Invalid Username : ");
            userActions.LogConsoleMessage("Enter the Invalid password : ");
            userActions.LogConsoleMessage("Click Sign in button");

            Actor.AttemptsTo(LoginUser.For(userActions.InvalidUser!, userActions.InvalidPassword!));

            userActions.LogConsoleMessage("Verify : Error message is displayed ");
            Actor.WaitsUntil(Text.Of(LoginPage.ErrorMessageLocator), ContainsSubstring.Text(LoginPage.UnableToLoginError));

            userActions.LogConsoleMessage("Clean up :");

        }

    }
}
