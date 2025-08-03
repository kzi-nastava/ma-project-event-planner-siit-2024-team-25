# 📱 Event Planner - Android App

This is the **Android mobile client** for the _Event Planner_ project, developed during the 2024/2025 academic year for coursework in Mobile Applications, Server-side Engineering, Software Testing, and other related subjects.

The app allows users to register, plan and manage events, search for services and products, and communicate with others. It supports multiple user roles: guests, organizers, service/product providers, and administrators.

---

## 🛠 Tech Stack

- **Language:** Java
- **Architecture:** MVVM
- **UI Design:** Material Design 3
- **Network:** Retrofit
- **Notifications:** Android Notification Manager (with backend support)
- **Map Support:** OpenStreetMap

---

## 🚀 Getting Started

### 1. Clone the Repository

# Mobile App

git clone https://github.com/kzi-nastava/ma-project-event-planner-siit-2024-team-25.git

# Back-End

git clone https://github.com/kzi-nastava/iss-project-event-planner-siit-2024-team-25

### 2. Open the projects

Open the backend project using IntelliJ IDEA Ultimate.
Open the mobile app project using Android Studio

### 3. Configure local.properties

In the mobile app project root, create a file named local.properties and add the following:

sdk.dir=YOUR_ANDROID_SDK_PATH
ip_address=http://YOUR_IP_ADDRESS:8080

# Finding Your IP Address on Windows

1. Open Command Prompt (cmd)
2. Run: ipconfig
3. Locate your network adapter and copy the IPv4 Address
4. Paste it into the ip_address field in local.properties

# Notes

- Ensure both the mobile app and backend are on the same network.
- The backend must be running and accessible at the specified IP address for full app functionality.
- Notifications and map features require proper permissions and network access.

# Authors

Developed by Team 25, SIIT 2024/2025

1. Nikola Jolović SV9/2022
2. Miloš Mrđa SV34/2022
3. Stefan Stjepić SV40/2022
