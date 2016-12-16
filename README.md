# Companies House Application #

Companies House does have an application, but it is outdated and doesn't contain all the features from their newer API.

### How do I get set up? ###

* You need Android Studio and you are good to go
* Also available on Google Play (soon)

### App screen levels ###

* List of companies (One of Search/Recent Searches/Favourites; starts with empty search screen)
* Company screen
* Specialized screens (Filing History/Charges/Insolvency/Officers/Persons with Significant Control)
* Specialized detail screens (Not implemented yet)
* Officer appointments (opens from the Officer Details screen; Not implemented yet)

### Technology highlights ###

* Retrofit with RxJava is used for networking.
* MVP architecture for easy testing.
* Large part of the presenters are unit tested.
* New ConstraintLayout is used on Details screens.

### Roadmap ###

* UI tests with Espresso.
* Animations.
    * Search/Delete action buttons appearance/disappearance.
    * Activity transactions.