# Companies House Application #

Companies House does have an application, but it is outdated and doesn't contain all the features from their newer API. So I created a new application.

### How do I get set up? ###

* You need the latest Android Studio.
* Check out the BaBeStudiosBase library project to the same directory (e.g. ~HOME/projects) as this project:
https://bitbucket.org/herrbert74/babestudiosbase/src/master/
* Register an account and get an authentication code as described [here](https://developer.companieshouse.gov.uk/api/docs/index/gettingStarted.html).
* Place the authentication code into your local gradle configuration file (on windows it's under c:\Users\\[User]\\.gradle\gradle.properties by default) with the name
"CH_API_KEY", e.g. 'CH_API_KEY="YOUR API KEY HERE"'.
* Also available on Google Play (older version):
https://play.google.com/store/apps/details?id=com.babestudios.companyinfouk

### App screen levels ###

* List of companies (One of Search/Recent Searches/Favourites; starts with empty search screen).
* Company screen.
* Specialized screens (Filing History/Charges/Insolvency/Officers/Persons with Significant Control).
* Specialized detail screens.
* Officer appointments. Opens from the Officer Details screen. Links to Company screen.

### Other features ###

* Set, delete and show favourites.
* PDF reports are downloadable and can be sent to the user's choice of reader application.
* Filter search results by company status and filing history by filing category.

### Technical highlights ###

* 100% Kotlin (not fully idiomatic, but getting there).
* Retrofit 2 with RxJava is used for networking and events.
* MVP architecture with Dagger 2 for easy testing.
* ViewModel holds the state, AutoDispose from Uber and RxLifecycle from Trello guarantees correct lifecycle handling.
* Large part of the presenters are unit tested.
* ConstraintLayout is used on Details screens.

### Roadmap ###

* Add company registers and company exemptions.
* More UI tests with Espresso.