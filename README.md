# Companies House Application #

Companies House does have an application, but it is outdated and doesn't contain all the features from their newer API. So I created a new application.

### How do I get set up? ###

* You need the latest Android Studio. I usually switch between beta and canary channels, so if you prefer another version you will have to change the Gradle build tools version in the project gradle file.
* Register an account and get an authentication code as described [here](https://developer.companieshouse.gov.uk/api/docs/index/gettingStarted.html).
* OR add this test API key in the next step: '-zPqEtWGqxMG7BZoxyjigSEarXFjTRU0UGqQT-S8'.
* Place the authentication code into your local gradle configuration file (on Windows it's under c:\Users\\[User]\\.gradle\gradle.
  properties by default) with the name "companiesHouseApiKey", e.g. 'companiesHouseApiKey="YOUR API KEY HERE"'.
* Also available on Google Play (older version, only updated when I add functionality changes):
https://play.google.com/store/apps/details?id=com.babestudios.companyinfouk

### App screen levels ###

You can drill down from the main screen to the following:

1. Top level: main screen with list of companies (One of Search/Recent Searches/Favourites; starts with empty search screen).

2. Company screen.

3. Specialized screens (Filing History/Charges/Insolvency/Officers/Persons with Significant Control).

4. Specialized detail screens.

5. Only from the Officer Details screen: Officer appointments. Links to Company screen.

### Other features ###

* Set, delete and show favourites.
* Filing reports are downloadable in PDF format, what can be sent to the user's choice of reader application.
* Filter search results by company status and filing history by filing category.

### Technical highlights ###

* **Kotlin Multiplatform** with Android and iOS apps.
* **Koin** for dependency injection.
* **Ktor** with Ktorfit is used for networking.
* **Coroutines** is used for networking and events.
* **Compose** and **SwiftUI** is used for UI.
* Unidirectional Data Flow architecture with **MVIKotlin**.
* Routing functionality and pluggable UI with **Decompose**.
* **Version catalog** is used for dependency management.
* Extensively unit tested with **JUnit**, but there is no full coverage. Some **Espresso test** are also present.
* **Kotlin Gradle DSL** with Feature and Android plugins in buildSrc to avoid duplicate declarations.
* Modularized with the following modules:
  * The **shared** module has the domain classes (api and model), dependency injection, the data (network) implementations, and 
    **Decompose** components.
  * The **buildSrc directory** is the standard Gradle way to separate links to third party libraries.
  * **Base modules** are in a separate project to maintain reusability (BaBeStudios Base).
  * **Common module** contains project specific resources, Composables and the design elements.
  * **Feature modules** contain the UI features: charges, filings, companies, insolvencies, officers and persons. Main feature is the 
    entry point to the app: that holds the single Activity and the UI tests.
  * The **app module** sits on top, containing only the Application class.
  * **iOSApp module** contains the iOS app.
