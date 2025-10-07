## CapeWeather App
CapeWeather is an Android weather forecasting application that provides accurate, real-time weather updates for any city or the user’s current location. The app allows users to explore weather-based activities, save favourite locations, customize settings, and securely store preferences using Firebase Realtime Database.
## Introduction
This repository contains the source code for CapeWeather, developed as part of an Android development portfolio project. The app combines multiple APIs and libraries to deliver real-time weather data, user customization, and activity suggestions tailored to weather conditions.
CapeWeather was designed to offer a clean, intuitive experience while showcasing practical implementation of API communication, Firebase integration, and multi-screen navigation in Android.

## LINKS TO FOLLOW :
### GITHUB REPO : https://github.com/AaliyahAllie/CapeWeather.git
### YOUTUBE LINK : https://youtu.be/SZBBdiXeoZE

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

## App Functionality Overview
## Splash Screen
Displays the app logo with a progress bar while initializing Firebase and loading resources.
![Splash Screen](![WhatsApp Image 2025-10-07 at 13 43 45_df00eed8](https://github.com/user-attachments/assets/68ce3561-0c8c-4f78-85ce-ae25c06a60d4)
## Login and Registration
Handles user authentication through Firebase. Users can register or log in using an email and password, with options for remembering login credentials or resetting a password.
![WhatsApp Image 2025-10-07 at 13 43 45_df00eed8](https://github.com/user-attachments/assets/df2b6d55-05fb-47b1-9b46-2b1a15487317)
![WhatsApp Image 2025-10-07 at 13 43 46_c75d17a7](https://github.com/user-attachments/assets/0b4a2d8c-ab86-4b0b-a54b-4b69f1fe7a78)
![WhatsApp Image 2025-10-07 at 13 43 46_e088e600](https://github.com/user-attachments/assets/9534c603-97d9-48b1-ace1-87856dbfa08a)
## Home and Search Screen
Allows searching for weather by city or using the device’s current location. Displays temperature, weather description, and details such as pressure, wind speed, humidity, sunrise, sunset, and visibility. Users can add searched cities to favourites.
![WhatsApp Image 2025-10-07 at 13 43 44_13075c6e](https://github.com/user-attachments/assets/f1dea629-a866-48fa-8302-585155fc0afa)
![WhatsApp Image 2025-10-07 at 13 43 44_c6eee300](https://github.com/user-attachments/assets/e826485c-cc6e-4a6a-9b2b-6d00c4d95ec2)
![WhatsApp Image 2025-10-07 at 13 43 44_3d52822e](https://github.com/user-attachments/assets/d6ae0707-1627-436c-a5f3-f3bedfa11376)

## Activities Screen
Provides activity recommendations based on the weather conditions of the selected city. Data is retrieved from the following API:
https://activities-api-s8eq.onrender.com/activities/{city}/{weather}
![WhatsApp Image 2025-10-07 at 13 58 51_8c25d09e](https://github.com/user-attachments/assets/c9edb0f0-2d0a-42c6-b132-25a8a9c61d2e)

## Favourites Screen
Displays saved cities in a ListView. Allows adding and managing favourite locations, with a loading indicator while fetching data.
![WhatsApp Image 2025-10-07 at 13 43 45_77e5fc16](https://github.com/user-attachments/assets/26cdd059-bb8f-4107-b42b-03fe5906356f)

## Profile Screen
Allows users to update their personal details, default city, and preferred temperature unit (Celsius or Fahrenheit). Includes options to save settings or log out.
![WhatsApp Image 2025-10-07 at 13 59 34_7ec47b39](https://github.com/user-attachments/assets/0c36ce26-1b30-4ea7-964b-fcedbb9e8319)

## Settings Screen
Provides toggles for enabling or disabling notifications, location access, temperature unit preferences, and sound or vibration alerts.
![WhatsApp Image 2025-10-07 at 13 43 44_610ecbd3](https://github.com/user-attachments/assets/8f0b4bfa-5eca-4109-8472-6d2ee3feeca5)
## Menu Screen
Acts as the main navigation hub with links to the Activities, Profile, and Settings screens.
![WhatsApp Image 2025-10-07 at 13 43 45_3ae4d99d](https://github.com/user-attachments/assets/9fb5b47c-cfb9-4e1b-ab37-6524eb5f1ce5)

Known Issues
*****************************
These are issue I experienced on the copied logic in my project is it the same on yours?
If location permission is permanently denied, location-based features remain disabled until manually re-enabled.
Firebase synchronization may temporarily lag in low-connectivity environments.

Future Enhancements
Push notifications for extreme weather conditions.
Integration with Google Maps for location-based visualization. (Radar) (SET IN PART 3)
Dark mode and improved accessibility support.


## Setup and Installation
Clone the repository:
git clone https://github.com/yourusername/CapeWeather.git
cd CapeWeather
Add your OpenWeatherMap API key to the application code or local.properties.
Configure Firebase by adding your google-services.json file inside the app/ directory.
Sync Gradle and run the project on an Android emulator or physical device.

## Firebase:
<img width="1918" height="1017" alt="image" src="https://github.com/user-attachments/assets/79799bf3-98a2-4f1d-ac9c-cb3ebed7d451" />

## Render: 
<img width="1918" height="1027" alt="image" src="https://github.com/user-attachments/assets/4b219f83-edf7-4e01-b39c-81b56b9b6f04" />
## Render API GitHub Link: https://github.com/AaliyahAllie/activities-api.git
## Conclusion
CapeWeather was built with creativity and care to make checking the weather simple and enjoyable. By combining modern Android tools and a clean design, we hope it helps users stay ready for whatever the day brings.
