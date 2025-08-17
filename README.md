# 📚 Readify

Readify is a clean-architecture Android app that helps you discover, track, and manage your reading journey.  
Search books from the **Google Books API**, log in with **Firebase Authentication**, save your favorite books, and mark them as read.

---

## ✨ Features

- **🔐 Firebase Authentication**
  - Email & Password sign-up/sign-in
  - Google Sign-in
  - Password reset

- **📖 Google Books Search**
  - Search books by title, author, or ISBN
  - View detailed book information

- **💾 Personal Bookshelf**
  - Save books to your reading list
  - Mark books as "Read" or "To Read"
  - Remove books from your list

- **☁️ Cloud Storage**
  - All user data is stored in Firebase Firestore
  - Synced across devices

---

## 🛠 Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose, Material 3
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt
- **Backend**: Firebase Authentication & Firestore
- **API**: Google Books API
- **Networking**: Retrofit + OkHttp
- **Async**: Kotlin Coroutines + Flow

---

## 📂 Project Structure

- data
  - BookState.kt             (Sealed class for handling book loading states)
  - models/                  (Data classes for app entities (Book, User, etc.))
  - repository/              (Repository classes for fetching and updating data)

- screens
  - home/                  (Home screen & related components)
  - stats/                 (Stats screen & related components)
  - update/                (Update book screen & ViewModel)
  - splash/                (Splash screen (if any))

- components               (Reusable composables (AppBar, Loading, etc.))
- utils                    (Utility functions (date formatting, etc.))
- navigation               (Navigation setup (NavController, routes))
- viewmodel                (Shared or screen-specific ViewModels)
- di                       (Dependency injection modules (Hilt))



### 📌 Explanation
- **data**: Handles models, repository, and state classes for MVVM flow.
- **screens**: Contains all UI screens and their Compose functions. Each screen can have its own folder for organization.
- **components**: Reusable UI elements like buttons, cards, app bars, loading indicators.
- **utils**: General-purpose helper functions used across the app.
- **navigation**: Centralized navigation setup using Jetpack Compose navigation.
- **viewmodel**: All ViewModels managing UI state, interacting with repositories.
- **di**: Hilt dependency injection modules.

## 📸 Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/b8e6abd1-80e1-4645-9a29-2ecf50dec624" alt="Screenshot 1" width="250"/>
  <img src="https://github.com/user-attachments/assets/3ba4e065-686d-422c-9639-978efc5aea8d" alt="Screenshot 2" width="250"/>
  <img src="https://github.com/user-attachments/assets/4fb5eead-8e51-4337-88f9-5872900c8d9c" alt="Screenshot 3" width="250"/>
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/84944789-3348-4b54-967d-202fc2586247" alt="Screenshot 4" width="250"/>
  <img src="https://github.com/user-attachments/assets/6c56cdf2-3999-4148-8ec6-bdcd6a800c95" alt="Screenshot 5" width="250"/>
  <img src="https://github.com/user-attachments/assets/882d4be6-c2d6-4357-afa9-d59411062654" alt="Screenshot 6" width="250"/>
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/460ef4f9-e9f9-4fed-8874-9a37ad0f27c5" alt="Screenshot 7" width="250"/>
  <img src="https://github.com/user-attachments/assets/2f0fef83-1e0a-4607-936b-a3d28223671f" alt="Screenshot 8" width="250"/>
</p>







