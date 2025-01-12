package com.example.ridesharing.utils

object Constants {
    // Yandex Geocoding API
     const val YANDEX_GEOCODING_URL = "https://geocode-maps.yandex.ru/1.x/"
    const val YANDEX_GEOCODING_API_KEY = "53ae3d79-d6cf-40f4-9e12-468d7495bea6" // Replace with your actual key

    // Shared Preferences keys
    const val PREF_SEARCH_RADIUS = "search_radius"
    
    // Location Settings
    const val LOCATION_REQUEST_INTERVAL = 10000L // 10 seconds
    const val LOCATION_FASTEST_INTERVAL = 5000L // 5 seconds
    
    // Trip Settings
    const val DEFAULT_SEARCH_RADIUS = 10 // Default search radius in km
    
    //Firebase
    const val NOTIFICATION_CHANNEL_ID = "default"
}