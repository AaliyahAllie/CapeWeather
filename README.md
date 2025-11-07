#### Cape Weather App
CapeWeather is an Android weather forecasting application that provides accurate, real-time weather updates for any city or the user’s current location. The app allows users to explore weather-based activities, save favourite locations, customize settings, and securely store preferences using Firebase Realtime Database.
Introduction
This repository contains the source code for CapeWeather, developed as part of an Android development portfolio project. The app combines multiple APIs and libraries to deliver real-time weather data, user customization, and activity suggestions tailored to weather conditions.
CapeWeather was designed to offer a clean, intuitive experience while showcasing practical implementation of API communication, Firebase integration, and multi-screen navigation in Android.

#### Contributors
•	AALIYAH ALLIE – ST10212542
•	NICOL BLACK – ST10391534
•	Amaan Williams – ST10396397
•	Kenneth Remmitz – ST10403075

#### LINKS TO FOLLOW :
•	GITHUB REPO : https://github.com/AaliyahAllie/CapeWeather.git
•	YOUTUBE LINK Part 2: https://youtu.be/SZBBdiXeoZE
•	YOUTUBE LINK PART 3 (FINAL POE) : https://youtu.be/tGZxiM5of-Q
Feedback and Development Notes
During earlier development phases, feedback emphasized:
Enhancing the visual design by incorporating a vibrant gradient background and consistent, blue-themed elements.
Improving data presentation using card-based layouts and icons.
Integrating user accounts, favourites, and settings through Firebase for personalization.
All improvements have been implemented in this version.
Programming Language
This application is built using Kotlin, utilising XML for UI layouts and modern Android libraries for backend and frontend development.
As well as the use of JavaScript for the development of our own API.

#### Colour Scheme
CapeWeather’s UI adopts a blue and white palette with gradient backgrounds (@drawable/gradient_bg) for a clean, ocean-inspired aesthetic representing the Cape’s coastal environment.
Database Storage
CapeWeather uses Firebase Realtime Database to securely store:
•	User profiles
•	Favourite cities
•	Personal settings such as temperature unit, notifications, and location preferences
Data remains available across sessions, even if the app is reinstalled or accessed on a new device.

#### New and Unique Features
•	Weather-Based Activities: Suggests activities based on current weather (Sunny, Cloudy, Windy, Rainy).
•	Favourites Management: Save and manage favourite cities for quick access.
•	Current Location Forecasts: Retrieves local weather data using Google Play’s Location API.
•	Notification Settings: Allows users to enable or disable alerts for weather updates.
•	Temperature Unit Switch: Toggle between Celsius and Fahrenheit.
•	Custom Settings Screen: Manage notifications, sound, vibration, and location permissions.

#### Technologies Used
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

#### Permissions and Connectivity
Required Permissions
ACCESS_FINE_LOCATION / ACCESS_COARSE_LOCATION
Required for retrieving current location weather data. If denied permanently, location-based features will be unavailable until re-enabled.
Internet Access
An active internet connection is required to fetch weather and activity data. The app displays a retry prompt if no connection is available.

#### App Functionality Overview
Splash Screen
Displays the app logo with a progress bar while initializing Firebase and loading resources.
 
Login and Registration
Handles user authentication through Firebase. Users can register or log in using an email and password, with options for remembering login credentials or resetting a password. Updated to now host a single sign on feature using google single sign on.
   
Home and Search Screen
Allows searching for weather by city or using the device’s current location. Displays temperature, weather description, and details such as pressure, wind speed, humidity, sunrise, sunset, and visibility. Users can add searched cities to favourites.
   
Activities Screen
Provides activity recommendations based on the weather conditions of the selected city. Data is retrieved from the following API:
 
Favourites Screen
Displays saved cities in a ListView. Allows adding and managing favourite locations, with a loading indicator while fetching data.
 
Profile Screen
Allows users to update their personal details, default city, and preferred temperature unit (Celsius or Fahrenheit). Includes options to save settings or log out.
 
Settings Screen
Provides toggles for enabling or disabling notifications, location access, temperature unit preferences, and sound or vibration alerts. Also implements a multi-language feature for uses to select their preferred language.
 
Menu Screen
Acts as the main navigation hub with links to the Activities, Profile, and Settings screens.
 
#### Known Issues
In the development of part 3, we faced challenges with the multi-language feature, when language is changed only a few features are update of the user interface to the new language picked and it is still a feature we need to improve on as we could not implement it to 100% capacity.

#### Setup and Installation
Clone the repository:
git clone https://github.com/yourusername/CapeWeather.git
cd CapeWeather
Add your OpenWeatherMap API key to the application code or local.properties.
Configure Firebase by adding your google-services.json file inside the app/ directory.
Sync Gradle and run the project on an Android emulator or physical device.
Firebase:
 
Render: 
 
Render API GitHub Link: 
https://github.com/AaliyahAllie/activities-api.git
Git Commits:
 
 
#### PART 3 UPDATES TO APPLICATION
In part 3 we have implemented new features to our application to account for the requirements of the POE.
New login/register feature:
Google Single Sign on. Users can now sign in with their google account and instantly access the application.
 
 
New notification features:
Notifications go off when a user logs in as well as if a user updates their temperature units of the application
 
New multi-language features:
Updates users’ language preferences for 3 different language options, English, Afrikaans and Xhosa.
      
Offline Feature:
Allows users to still see their last captured weather data when they are offline

#### ChatGPT reference:
Artificial Intelligence (AI) could greatly enhance the Cape Weather App by making it smarter, more interactive, and more accurate in predicting weather conditions.
API CREATION
We used ai in our application to help us create our own API, ChatGPT helped us step by step as well as using YouTube to understand and implement our own API features using JavaScript and Render for publishing of API.
When hosting the backend on Render (a cloud hosting platform), I could have used AI to guide me through the steps — such as connecting the weather API, deploying a Flask or Node.js server, or fixing deployment errors.
Example (Node.js Server):
app.get("/weather", async (req, res) => {
  const city = req.query.city;
  const response = await fetch(`https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=API_KEY`);
  const data = await response.json();
  res.json(data);
});
AI could help write this backend code, explain how to deploy it on Render, and even debug common issues like missing environment variables or build errors.
General Code Assistance
General Code Assistance for connecting to the open weather and render API’s
suspend fun getWeather(city: String): String {
    val apiKey = "your_api_key"
    val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey&units=metric"

    val connection = URL(url).openConnection() as HttpURLConnection
    val data = connection.inputStream.bufferedReader().readText()
    return data
}
I didn’t quite understand where and how to get and generate the api keys.
. Debugging and Problem Solving

When the app displayed errors such as “NullPointerException” or “Network on Main Thread Exception,” AI could have helped me identify the cause and provided instant fixes.
Example Fix:
GlobalScope.launch(Dispatchers.IO) {
    val data = getWeather("Cape Town")
    withContext(Dispatchers.Main) {
        txtReport.text = data
    }
}
Instead of manually searching for solutions, AI can explain what caused the error (e.g., network call on main thread) and how to solve it using coroutines. This shortens debugging time and improves coding habits.
3. Designing the User Interface
AI could have assisted in designing the app layout — suggesting color schemes, proper alignment, and accessibility features. Tools like Figma AI can automatically create design components, and ChatGPT can generate the XML structure.
Example:
<TextView
    android:id="@+id/txtCity"
    android:text="Cape Town"
    android:textSize="24sp"
    android:textStyle="bold"
    android:gravity="center"/>
AI could also suggest consistent styles for better readability and user experience.

####Publishing our application to google playstore
Guide to Publishing an Android Application on the Google Play Store
Before publishing an application to the Google Play Store, the application must be fully developed, tested thoroughly on multiple devices, and confirmed to have no major errors or crashes. The application must have a unique package name, and it must be built in a signed release format as an Android App Bundle (.aab). The version code and version name must be correctly assigned in the project configuration, as the Play Store uses these values to differentiate updates.
  
To begin publishing, a Google Play Console developer account must be created. During account creation, we must select whether the account is for personal use or for an organisation. We are then asked to provide a developer display name, verify our identity, and link a payment profile. A one-time registration fee is required. Once the developer account is fully registered and verified, we can proceed to create a new application.
 
 
 
Inside the Play Console, the user selects “Create App” and enters the required initial information, including the application name, the default language, whether the application is classified as an app or a game, and whether it will be distributed as free or paid. 
 
After the app shell is created, we must complete the Store Listing, which determines how the application will appear to users on the Play Store. The Store Listing includes the application name, a short description of up to eighty characters, and a full description of up to four thousand characters that explains the application’s purpose and features. The listing also requires promotional graphics. The mandatory graphics include the application icon in a 512 by 512 pixel format not exceeding 1 MB, and a feature graphic sized 1024 by 500 pixels not exceeding 15 MB. A promotional video may be provided if desired, using a public or unlisted YouTube link.
To publish an app on the Google Play Store, developers must upload screenshots of the application’s interface. At least two and up to eight screenshots are required for each supported device type (phone, 7-inch tablet, 10-inch tablet, or Chromebook). These must be in PNG or JPEG format, follow a 16:9 or 9:16 aspect ratio, and meet specific pixel size requirements (320–3840 pixels for phones/tablets, 1080–7680 for Chromebooks).
After completing the Store Listing, developers must fill in several policy and compliance sections, including the Privacy Policy URL, ad content, target age group, content rating, and a detailed data safety declaration explaining what information the app collects, uses, or shares. These must be accurate, as incorrect information can lead to rejection.
Next, developers prepare the release by uploading the signed .aab file, adding a release name, and writing release notes describing updates or features. It is recommended to start with internal or closed testing, adding testers by email or opt-in link. Google requires meaningful testing—with testers using the app for about 14 days—before allowing a full release.
Once testing is complete and issues are fixed, the app can be submitted for production release. Google then reviews it for policy compliance. If approved, the app becomes publicly available; if not, corrections must be made before resubmission. After publishing, developers should monitor performance, including crash rates and user feedback, through the Play Console, and continue to provide updates to maintain quality and compliance.

#### Signed APK steps
Go to Build > Generate Signed Bundle/APK.
Select APK and click Next.
Click Create new to generate a new keystore file (.jks) and a key alias for it.
Fill in the required details for the keystore and key, such as the path, passwords, and alias.
Choose the release build variant and select the desired signature versions (e.g., v1 and v2).
Click Finish to generate the signed APK file in the specified destination folder.
This will prepare the application for release on google play store.

#### Conclusion
CapeWeather was built with creativity and care to make checking the weather simple and enjoyable. By combining modern Android tools and a clean design, we hope it helps users stay ready for whatever the day brings.
