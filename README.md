# MessageApp_droid Setup Guide

MessageApp_droid is an Android application that enables users to register, log in, and chat one-on-one with other registered users using the MessageApp REST API. Key features include user registration, automatic login, individual chat functionality, and a contacts view for managing registered contacts.

---

## Project Features

- **User Registration**: Allows new users to register by providing essential details.
- **Automatic Login**: Logs users in automatically if they are registered, bypassing the manual login process.
- **One-on-One Chat**: Enables direct messaging with other registered users.
- **Contacts View**: Displays registered contacts, allowing users to start a chat with them.

---

## Setup Instructions

### Prerequisites

Before starting, ensure you have the following installed:

- **Android Studio** (latest version recommended)
- **JDK 11** - [Amazon Corretto 11](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html)
- **Gradle 7.3.3**

---

### Project Configuration

1. **Set Gradle Version**
    - Open **Android Studio**.
    - Go to **File > Project Structure**.
    - In the **Project** settings, set **Gradle Version** to `7.3.3`.

2. **Configure JDK for Gradle Build**
    - In the **Project Structure** window, go to **Build Tools**.
    - Set the **Gradle JDK** to **Corretto-11**.

Once these settings are configured, proceed to sync and build the project. This should ensure compatibility with the required dependencies and tools.

---

# Setting Up Firebase in MessageApp_droid

Integrating Firebase into the MessageApp_droid project allows for enhanced features such as user authentication, real-time database functionality, and cloud storage. Follow the steps below to set up Firebase in your project.

---

## Step-by-Step Guide to Firebase Setup

### 1. Create a Firebase Project

1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Click on **Add project**.
3. Enter a project name (e.g., "MessageApp_droid").
4. (Optional) Enable Google Analytics if you want to use it for your project.
5. Click **Create Project** and wait for it to finish.

### 2. Register Your App with Firebase

1. In the Firebase Console, select your newly created project.
2. Click on the **Android** icon to add an Android app.
3. Enter your app's package name (e.g., `hr.vsite.messageapp`).
4. (Optional) Enter the app nickname and the debug signing certificate SHA-1.
5. Click **Register App**.

### 3. Download the `google-services.json` File

1. After registering your app, click **Download google-services.json**.
2. Place the `google-services.json` file in the `app/` directory of your Android project.

