package com.example.gastronomad2.models.entities


enum class Screen {
    SignUp,
    SignIn,
    Splash,
    Home,
    ProfilePicture,
    Loading,
    TestNotification,
    Profile,
    CreateRestaurant,
    AddRestaurantMapLocation,
    MyRestaurants,
    RestaurantsDetails,
    Filter,
    FilteredRestaurants
}

enum class RestaurantType {
    ITALIAN,         // Italijanski restoran
    CHINESE,         // Kineski restoran
    MEXICAN,         // Meksički restoran
    JAPANESE,        // Japanski restoran
    INDIAN,          // Indijski restoran
    AMERICAN,        // Američki restoran
    FRENCH,          // Francuski restoran
    MEDITERRANEAN,   // Mediteranski restoran
    VEGAN,           // Veganski restoran
    SEAFOOD,         // Restoran sa morskim plodovima
    FAST_FOOD,       // Brza hrana
    STEAKHOUSE,      // Restoran sa biftecima
    BUFFET,          // Restoran sa bifeom
    CAFE,            // Kafić
    BAKERY,          // Pekara
    BAR              // Bar
}