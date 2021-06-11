# Samples
app for testing new Android features and tools in more applicable case scenarios.

** Architecture **
The project directories are divided up into 4 main dirs: core, data, ui, util

- Core: holds directories/classes associated with core functionality of app like room, hilt, datastore
- Data: holds models, storage, and network classes
- UI: holds all UI elements including ViewModels
- Util: holds helper classes and extracted classes for specific features

Data is separated by data type (storage, model, network) and then by feature
ui is separated by type (custom views or screens) adn then by feature
util is separated by feature

ui and util are divided more similarly than data

- App is single activity architecture and uses navgraph to navigate throughout
- storage is done with DataStore Proto & Preferences
- Room DB is used for sqlite storage
- Hilt is used for dependency injection
- Retrofit is used for network calls

** FEATURES **
Bart:
 - Experiment a bit with ViewModel + DataBinding
 - use ViewModel to pass data between fragments
 - Flow + LiveData to pass data from bart api down to fragments
 - use new ViewPager2 to switch between Station and Trips fragments
 - use SafeArgs to pass data between destinations using Navigation Components

Camerax:
- Test new Camerax library
- use Google Machine Learning kit (ML kit) to analyze camera preview and read a barcode
- use ML Kit to analyze camera preview and read image & convert to text
- same as above but analyze a picture taken and convert the picture to text
- store picture to storage

Coffee:
- Simple State Machine in the form of Coffee Machine to test StateFlow + LiveData
- This module used to explore StateFlows and Hot Flows to perform actions even after app is finished potentially
- more demonstration of DataBinding + ViewModel
- (still working on) Notification Builder can send notifications to device when coffee is complete

Goal:
- Test Drag & Drop functionality similar to Jira or Trello board

MockHue:
- MockHue meant to be mock representation of Philips Hue Lights controller
- Test Navigation graph within a navigation graph. Issue arose when trying to handle fragments in childFragmentManager. is it better to just do childFragmentManager commits or create another embedded navigation graph

News:
- test Hot and Cold stream flows using a mock News API
- Similar to what is achieved in Bart but is a bit more easily manipulatable for testing different things
- tests working with marquees and layout animations

Venue:
- Testing connecting to local wifi and various other types of connections (wifi, nfc, bluetooth, BLE, etc...) 
- see what's viable replacement for currently used systems. popular ones being geofencing/location
