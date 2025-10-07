CapeWeather App
CapeWeather is an Android weather forecasting application that provides accurate, real-time weather updates for any city or the user’s current location. The app allows users to explore weather-based activities, save favourite locations, customize settings, and securely store preferences using Firebase Realtime Database.
Introduction
This repository contains the source code for CapeWeather, developed as part of an Android development portfolio project. The app combines multiple APIs and libraries to deliver real-time weather data, user customization, and activity suggestions tailored to weather conditions.
CapeWeather was designed to offer a clean, intuitive experience while showcasing practical implementation of API communication, Firebase integration, and multi-screen navigation in Android.

## LINKS TO FOLLOW :
### GITHUB REPO : https://github.com/AaliyahAllie/CapeWeather.git
### YOUTUBE LINK :

Feedback and Development Notes
During earlier development phases, feedback emphasized:
Enhancing the visual design by incorporating a vibrant gradient background and consistent blue-themed elements.
Improving data presentation using card-based layouts and icons.
Integrating user accounts, favourites, and settings through Firebase for personalization.
All improvements have been implemented in this version.

Contributors
Aaliyah Allie (Team Lead)
Amaan Williams
Kenneth Michael Remmitz
Nicol Black

Programming Language
This application is built using Kotlin, utilising XML for UI layouts and modern Android libraries for backend and frontend development.

Colour Scheme
CapeWeather’s UI adopts a blue and white palette with gradient backgrounds (@drawable/gradient_bg) for a clean, ocean-inspired aesthetic representing the Cape’s coastal environment.

Database Storage
CapeWeather uses Firebase Realtime Database to securely store:
•	User profiles
•	Favourite cities
•	Personal settings such as temperature unit, notifications, and location preferences
Data remains available across sessions, even if the app is reinstalled or accessed on a new device.

New and Unique Features
•	Weather-Based Activities: Suggests activities based on current weather (Sunny, Cloudy, Windy, Rainy).
•	Favourites Management: Save and manage favourite cities for quick access.
•	Current Location Forecasts: Retrieves local weather data using Google Play’s Location API.
•	Notification Settings: Allows users to enable or disable alerts for weather updates.
•	Temperature Unit Switch: Toggle between Celsius and Fahrenheit.
•	Custom Settings Screen: Manage notifications, sound, vibration, and location permissions.

Technologies Used
Core Android Components
XML Layouts for dynamic, layered UI designs.
AppCompat and Material Design for a modern look and consistent user experience.
RecyclerView and ListView for displaying lists of favourite cities and search results.
ConstraintLayout and GridLayout for structuring detailed weather and activity information.
BottomNavigationView for simple navigation between main screens.
Networking
Retrofit 2 and OkHttp for making REST API calls to OpenWeatherMap and the Activities API.
GSON Converter for converting JSON responses into Kotlin objects.
Logging Interceptor for debugging and network monitoring.
Firebase Integration
Firebase Authentication for user sign-in, registration, and logout.
Firebase Realtime Database for storing user preferences and favourite locations.
Google Play Services Base for stable Firebase and location operations.
Location and Data Handling
Fused Location Provider API for accurate location retrieval.
Geocoder for converting coordinates to city names.
Kotlin Coroutines for managing asynchronous network and database operations.

Permissions and Connectivity
Required Permissions
ACCESS_FINE_LOCATION / ACCESS_COARSE_LOCATION
Required for retrieving current location weather data. If denied permanently, location-based features will be unavailable until re-enabled.
Internet Access
An active internet connection is required to fetch weather and activity data. The app displays a retry prompt if no connection is available.

App Functionality Overview
Splash Screen
Displays the app logo with a progress bar while initializing Firebase and loading resources.
Login and Registration
Handles user authentication through Firebase. Users can register or log in using an email and password, with options for remembering login credentials or resetting a password.
Home and Search Screen
Allows searching for weather by city or using the device’s current location. Displays temperature, weather description, and details such as pressure, wind speed, humidity, sunrise, sunset, and visibility. Users can add searched cities to favourites.
Activities Screen
Provides activity recommendations based on the weather conditions of the selected city. Data is retrieved from the following API:
https://activities-api-s8eq.onrender.com/activities/{city}/{weather}
Favourites Screen
Displays saved cities in a ListView. Allows adding and managing favourite locations, with a loading indicator while fetching data.
Profile Screen
Allows users to update their personal details, default city, and preferred temperature unit (Celsius or Fahrenheit). Includes options to save settings or log out.
Settings Screen
Provides toggles for enabling or disabling notifications, location access, temperature unit preferences, and sound or vibration alerts.
Menu Screen
Acts as the main navigation hub with links to the Activities, Profile, and Settings screens.

Visual Design and Layout
*****************************
Don’t have in-depth access to that 

Known Issues
*****************************
These are issue I experienced on the copied logic in my project is it the same on yours?
If location permission is permanently denied, location-based features remain disabled until manually re-enabled.
Firebase synchronization may temporarily lag in low-connectivity environments.

Future Enhancements
Push notifications for extreme weather conditions.
Integration with Google Maps for location-based visualization. (Radar)
Dark mode and improved accessibility support.


Setup and Installation
Clone the repository:
git clone https://github.com/yourusername/CapeWeather.git
cd CapeWeather
Add your OpenWeatherMap API key to the application code or local.properties.
Configure Firebase by adding your google-services.json file inside the app/ directory.
Sync Gradle and run the project on an Android emulator or physical device.

Conclusion
CapeWeather was built with creativity and care to make checking the weather simple and enjoyable. By combining modern Android tools and a clean design, we hope it helps users stay ready for whatever the day brings.
