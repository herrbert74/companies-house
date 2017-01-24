# Companies House Application #

Companies House does have an application, but it is outdated and doesn't contain all the features from their newer API. So I created a new application.

### How do I get set up? ###

* You need Android Studio and you are good to go.
* Also available on Google Play as a Beta Version:
play.google.com/apps/testing/com.babestudios.companyinfouk

### App screen levels ###

* List of companies (One of Search/Recent Searches/Favourites; starts with empty search screen).
* Company screen.
* Specialized screens (Filing History/Charges/Insolvency/Officers/Persons with Significant Control).
* Specialized detail screens.
* Officer appointments. Opens from the Officer Details screen. Link to Company screen will be added soon.

### Other features ###

* Set, delete and show favourites.
* PDF reports are downloadable and can be sent to the user's choice of reader application.

### Technology highlights ###

* Retrofit with RxJava is used for networking.
* MVP architecture for easy testing.
* Large part of the presenters are unit tested.
* New ConstraintLayout is used on Details screens.

### Roadmap ###

* More UI tests with Espresso.
* Animations.
    * Search/Delete action buttons appearance/disappearance.
    * Activity transactions.