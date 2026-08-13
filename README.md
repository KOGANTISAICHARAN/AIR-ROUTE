
# 🌿 AIRROUTE

### Plan smarter. Breathe better.

AIRROUTE is an intelligent outdoor planning platform that combines **real-time air quality, PM2.5, weather, location and route information** to help people make better decisions about when and where to go outside.

Instead of simply showing pollution numbers, AIRROUTE converts environmental data into practical recommendations.

> **"Should I go now, wait, or choose another route?"**

---

##  Why AIRROUTE?

Air-quality applications usually tell users:

> "The AQI is 85."

But most people don't know what they should actually do with that information.

AIRROUTE transforms complex environmental information into simple actions.

For example:

>  **Planning a run?**  
> AIRROUTE analyzes the current air quality, forecast, weather and activity duration to suggest a better time.

Or:

>  **Going somewhere?**  
> AIRROUTE can compare available routes and highlight routes with lower estimated pollution exposure.

The goal is to make air-quality information **actionable rather than informational**.

---

# ✨ Core Features

## 🌿 Real-Time Air Quality

AIRROUTE retrieves live environmental information for the user's location.

Displays:

- AQI
- PM2.5
- Pollution status
- Dominant pollutants
- Last updated time
- Data source

No hardcoded pollution values are used in production mode.

---

##  Air Quality Forecast

View upcoming air-quality conditions using real forecast data.

Users can see:

- Hourly AQI
- PM2.5
- Pollution status
- Forecast trend
- Correct local date and time

The application automatically converts timestamps into the user's local timezone.

---

## 📊 Historical Air Quality

AIRROUTE provides historical air-quality information where supported by the configured provider.

Users can analyze:

- 24-hour history
- 7-day trends
- 30-day history
- Average pollution
- Maximum pollution
- Minimum pollution
- Improving/worsening trends

---

## Real-Time Weather

AIRROUTE combines air quality with weather conditions.

Weather information includes:

- Temperature
- Feels-like temperature
- Humidity
- Wind speed
- Wind direction
- Weather condition
- Rain probability
- Other available weather information

---

# Smart Air-Aware Maps

AIRROUTE uses an interactive map to help users understand environmental conditions around them.

Users can:

- View their current location
- Search for locations
- Select destinations
- View AQI information
- View pollution markers
- View route options
- Compare estimated pollution exposure

Map technology:

**Leaflet**

---

# 🚗 Air-Aware Route Planning

One of AIRROUTE's main features is intelligent route comparison.

A user can enter:

```text
Current Location
        ↓
Destination
````

AIRROUTE retrieves real routes and analyzes environmental conditions along the available routes where data coverage permits.

Example:

```text
Route A
22 min
Higher estimated exposure

Route B
25 min
Lower estimated exposure

Route C
20 min
Moderate estimated exposure
```

AIRROUTE can recommend a route with:

> **Lower estimated pollution exposure**

The system does not claim that any route is guaranteed to be safe.

---

# 🏃 Activity Planner

AIRROUTE doesn't stop at showing AQI.

Users can select an activity such as:

* 🏃 Running
* 🚶 Walking
* 🚴 Cycling
* 🌳 Outdoor Work
* 🏞️ Park Visit
* 🚗 Outdoor Travel
* ☀️ General Outdoor Activity

Users can specify:

* Activity
* Duration
* Start time
* Location
* Destination

AIRROUTE then analyzes:

* Current AQI
* PM2.5
* Air-quality forecast
* Weather
* Activity duration
* Location

and recommends a suitable outdoor window.

Example:

```text
🏃 RUNNING

Recommended Window
4:00 PM – 5:00 PM

AQI
LIVE VALUE

PM2.5
LIVE VALUE

Weather
LIVE VALUE

Estimated Exposure
LOWER
```

---

# 🤖 AIRROUTE AI

AIRROUTE uses AI to translate environmental information into simple human language.

Instead of:

> PM2.5 = 74 µg/m³

the application can explain:

> "Pollution is expected to increase later this evening. If you're planning an outdoor activity, an earlier window may have lower estimated exposure."

The AI does not generate environmental measurements itself.

It receives structured information from the application's data services and explains it to the user.

---

# 🔔 Smart Android Notifications

AIRROUTE supports real Android notifications using **Firebase Cloud Messaging (FCM)**.

Notifications can be triggered by:

* High AQI
* Rising PM2.5
* Unhealthy forecast
* Planned activity window
* Destination air-quality changes
* Significant weather changes

Users can configure notification preferences such as:

* Every 1 hour
* Every 2 hours
* Every 3 hours
* Every 6 hours
* Every 12 hours
* Daily
* Off

Example:

> ⚠️ **AIRROUTE — Pollution Rising**
> Air quality near you has worsened. Consider reducing prolonged outdoor exposure.

---

# 🔐 Authentication

AIRROUTE supports real user authentication.

Planned/implemented authentication includes:

* Google Sign-In
* Email/password authentication
* Persistent login
* Logout
* Password visibility toggle
* Authentication error handling

Android Google authentication is implemented using Firebase Authentication and the appropriate Google Android sign-in flow.

---

# 👤 User Profile

Users can manage:

* Name
* Email
* Profile photo
* Location
* Saved locations
* Preferred activities
* Notification preferences
* Account settings
* Sign out

---

# 🏗️ Architecture

```text
                    ┌──────────────────────┐
                    │      AIRROUTE        │
                    │   Mobile / Web UI    │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │      FastAPI         │
                    │      Backend         │
                    └──────────┬───────────┘
                               │
          ┌────────────────────┼────────────────────┐
          │                    │                    │
          ▼                    ▼                    ▼
 ┌────────────────┐   ┌────────────────┐   ┌────────────────┐
 │ Air Quality    │   │ Weather API    │   │ Routing API    │
 │ API            │   │                │   │                │
 └────────────────┘   └────────────────┘   └────────────────┘
          │                    │                    │
          └────────────────────┼────────────────────┘
                               ▼
                    ┌──────────────────────┐
                    │ AIRROUTE Engine      │
                    │                      │
                    │ Recommendation       │
                    │ Activity Planner     │
                    │ Exposure Estimation  │
                    │ Route Analysis       │
                    └──────────┬───────────┘
                               │
                ┌──────────────┴──────────────┐
                ▼                             ▼
       ┌─────────────────┐          ┌─────────────────┐
       │ Firebase        │          │ Android FCM     │
       │ Authentication  │          │ Notifications   │
       └─────────────────┘          └─────────────────┘
```

---

# 🔄 Data Flow

```text
User Location
      ↓
Latitude + Longitude
      ↓
AIRROUTE Backend
      ↓
┌──────────────┬──────────────┬──────────────┐
│              │              │              │
▼              ▼              ▼              ▼
Air Quality   Weather       Routing       Location
API           API           API           Services
│              │              │              │
▼              ▼              ▼              ▼
AQI/PM2.5     Weather       Routes        Coordinates
Forecast      Forecast      ETA            Address
History
      └──────────────┬──────────────┘
                     ▼
             AIRROUTE Engine
                     ↓
        ┌────────────┼────────────┐
        ▼            ▼            ▼
    Best Time    Activity      Route
    Window       Advice        Recommendation
        │            │            │
        └────────────┼────────────┘
                     ▼
                 User
```

---

# 🛠️ Technology Stack

## Frontend

* React
* TypeScript
* Tailwind CSS
* Responsive UI
* Charting library
* Leaflet

## Backend

* Python
* FastAPI
* REST APIs

## Authentication

* Firebase Authentication
* Google Sign-In

## Notifications

* Firebase Cloud Messaging (FCM)

## Maps & Routing

* Leaflet
* OpenStreetMap-compatible map tiles
* Google Routes API / configured routing provider

## Air Quality

* Google Air Quality API

## Weather

* OpenWeather API

## Database

Depending on deployment configuration:

* Firebase
* PostgreSQL
* Supabase

---

# 🔌 API Architecture

The frontend communicates with our backend rather than directly depending on multiple external services.

Suggested endpoints:

```text
GET  /api/location/current

GET  /api/air-quality/current

GET  /api/air-quality/forecast

GET  /api/air-quality/history

GET  /api/weather/current

GET  /api/weather/forecast

POST /api/routes

POST /api/route-air-quality

GET  /api/recommendation

GET  /api/activity-advisor

POST /api/notifications/preferences

GET  /api/notifications

POST /api/user/preferences
```

This architecture makes it easier to replace external providers without rebuilding the frontend.

---

# 📁 Project Structure

```text
AIRROUTE/
│
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── hooks/
│   │   ├── services/
│   │   ├── utils/
│   │   └── types/
│   │
│   └── package.json
│
├── backend/
│   ├── app/
│   │   ├── api/
│   │   ├── services/
│   │   ├── models/
│   │   ├── schemas/
│   │   └── main.py
│   │
│   └── requirements.txt
│
├── android/
│
├── .env.example
├── README.md
└── LICENSE
```

---

# ⚙️ Environment Variables

Create a `.env` file using `.env.example`.

Example:

```env
GOOGLE_MAPS_API_KEY=
GOOGLE_AIR_QUALITY_API_KEY=
OPENWEATHER_API_KEY=

FIREBASE_PROJECT_ID=
FIREBASE_API_KEY=
FIREBASE_APP_ID=
FIREBASE_MESSAGING_SENDER_ID=
```

Never commit real API keys or secrets to GitHub.

---

# 🚀 Getting Started

## 1. Clone the repository

```bash
git clone YOUR_REPOSITORY_URL
cd AIRROUTE
```

## 2. Install frontend dependencies

```bash
cd frontend
npm install
```

## 3. Configure environment variables

Create:

```text
.env
```

and add the required API keys.

## 4. Start frontend

```bash
npm run dev
```

## 5. Start backend

```bash
cd backend

python -m venv venv
```

Windows:

```bash
venv\Scripts\activate
```

Install dependencies:

```bash
pip install -r requirements.txt
```

Run FastAPI:

```bash
uvicorn app.main:app --reload
```

---

# 🔑 Required API Services

To run AIRROUTE with live data, configure the required services:

### Air Quality

Google Air Quality API

Provides:

* Current air quality
* AQI
* Pollutants
* PM2.5 information
* Forecast
* Historical information
* Air-quality map/heatmap capabilities where supported

### Weather

OpenWeather API

Provides real weather and forecast information.

### Maps

Leaflet + configured map tile provider.

### Routing

Google Routes API or another configured real routing provider.

### Authentication

Firebase Authentication.

### Android Notifications

Firebase Cloud Messaging.

---

# 🔒 Security

AIRROUTE follows these principles:

* No API secrets committed to GitHub
* Environment variables for sensitive configuration
* Backend API layer
* Authentication
* Input validation
* Secure user data handling
* Clear data-source attribution

---

#  Data & Safety

AIRROUTE is an environmental planning and information platform.

It does not provide medical diagnosis or guarantee safety.

Recommendations are based on available environmental data and should be treated as estimates.

The application uses wording such as:

> "Lower estimated pollution exposure"

rather than:

> "Guaranteed safe route"

---

# Real-World Use Cases

AIRROUTE can be useful for:

### Individuals

Planning:

* Running
* Walking
* Cycling
* Outdoor recreation
* Daily travel

### Students

Choosing better times for:

* Walking to college
* Sports
* Outdoor activities

### Outdoor Workers

Planning outdoor work around changing environmental conditions.

### Construction

Helping teams identify periods with higher pollution and plan outdoor operations accordingly.

### Fitness Communities

Planning group runs and cycling activities.

### Travelers

Checking environmental conditions before traveling to a destination.

---

#  What Makes AIRROUTE Different?

Traditional applications:

```text
AQI → Number
```

AIRROUTE:

```text
AQI
+
PM2.5
+
Weather
+
Forecast
+
Activity
+
Location
+
Route
        ↓
ACTIONABLE RECOMMENDATION
```

AIRROUTE focuses on:

> **"What should I do with this information?"**

rather than simply:

> **"What is the AQI?"**

---

#  Future Improvements

Potential future development:

* More cities and countries
* More air-quality providers
* Advanced pollution-exposure modelling
* Personalized activity recommendations
* Wearable integration
* Smartwatch support
* Community pollution reports
* More advanced route optimization
* Environmental trend prediction
* Personalized notification intelligence

---

#  Development Philosophy

AIRROUTE is designed around five principles:

1. **Real Data**
2. **Simple Decisions**
3. **Useful Recommendations**
4. **Transparent Sources**
5. **Human-Friendly Design**

The goal is to turn environmental data into meaningful everyday decisions.

---

# Built With Purpose

AIRROUTE was created from a simple observation:

Air pollution is often discussed through numbers and dashboards, but people ultimately need to know:

> **"What should I do right now?"**

AIRROUTE attempts to bridge that gap by combining environmental intelligence with everyday outdoor planning.

---

# License

This project is currently under development.

Add your preferred open-source license before public distribution.
