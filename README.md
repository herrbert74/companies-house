# Companies House Application #

Companies House does have an application, but it is outdated and doesn't contain all the features from their newer API. So I created a new application.

### How do I get set up? ###

* You need the latest Android Studio. I usually switch between beta and canary channels, so if you prefer another version you will have to change the Gradle build tools version in the project gradle file.
* Check out the BaBeStudiosBase library project to the same directory (e.g. ~HOME/projects) as this project:
https://bitbucket.org/herrbert74/babestudiosbase/src/master/
* Register an account and get an authentication code as described [here](https://developer.companieshouse.gov.uk/api/docs/index/gettingStarted.html).
* Place the authentication code into your local gradle configuration file (on windows it's under c:\Users\\[User]\\.gradle\gradle.properties by default) with the name
"CH_API_KEY", e.g. 'CH_API_KEY="YOUR API KEY HERE"'.
* Also available on Google Play (older version, only updated when I add functionality changes):
https://play.google.com/store/apps/details?id=com.babestudios.companyinfouk

### App screen levels ###

You can drill down from the main screen to the following:
1. Top level: main screen with list of companies (One of Search/Recent Searches/Favourites; starts with empty search screen).
1. Company screen.
1. Specialized screens (Filing History/Charges/Insolvency/Officers/Persons with Significant Control).
1. Specialized detail screens.
1. Only from the Officer Details screen: Officer appointments. Links to Company screen.

### Other features ###

* Set, delete and show favourites.
* Filing reports are downloadable in PDF format, what can be sent to the user's choice of reader application.
* Filter search results by company status and filing history by filing category.

### Technical highlights ###

* 100% Kotlin.
* Retrofit 2 with RxJava is used for networking and events.
* Unidirectional Data Flow architecture with MvRx from AirBnB. 
* Dagger 2 for easy testing.
* Large part of the ViewModels are unit tested.
* ConstraintLayout is used on Details screens.
* Architecture Components Navigation.
* Modularized with the following modules:
    * <i><b>BuidSrc directory</b></i> is the standard Gradle way to separate links to third party libraries.
    * <i><b>Base module</b></i> in a separate project to maintain reusability.
    * <i><b>Common module</b></i> contains project specific Kotlin extension functions and part of the model
    * <i><b>Navigation module</b></i> contains the abstraction for navigation to make navigation between features possible. This currently has to be duplicated in the app module.
    * <i><b>Data module</b></i> contains network calls (no caching at the moment) and the rest of the model
    * <i><b>Injection module</b></i> contains a Core Component and abstractions to use ti everywhere.
    * <i><b>Feature modules</b></i> contain the features.
    * <i><b>App module</b></i> sits on top, containing the final navigation, Application class and Espresso tests. 

### Roadmap ###

* Add company registers and company exemptions.
* More UI tests with Espresso.
* Check the Issues tab for more.