Got it! Since you're using Jetpack Compose, we can adjust the `README.md` to reflect this. Here's the updated version, taking Jetpack Compose into account.

# TodoApp

A simple Todo application for managing tasks using Jetpack Compose and SQLite database in Android. This app allows users to create, read, update, and delete Todo items with features like priority, deadline, and completion status.

## Features

- Add new Todo items.
- Edit Todo details (Name, Priority, Description, Deadline, Completion status).
- Delete Todo items.
- View all Todos stored in the SQLite database.
- Built using Jetpack Compose for the UI.

## Architecture

The app follows a simple **Model-View-Controller** (MVC) pattern:

- **Model**: `TodoDataClass` represents the Todo data.
- **View**: Composable functions using Jetpack Compose for displaying and interacting with the Todo items.
- **Controller**: `Controller` class manages CRUD operations (Create, Read, Update, Delete) for Todo items by interacting with the SQLite database.

## Prerequisites

- Android Studio (latest stable version)
- Kotlin 1.5 or higher
- Jetpack Compose dependencies
- Android SDK

## Setup Instructions

Follow these steps to set up the project on your local machine:

### 1. Clone the repository
```bash
git clone https://github.com/yourusername/todoapp.git
```

### 2. Open the project in Android Studio

1. Launch Android Studio.
2. Click on **Open an existing project**.
3. Select the folder where you cloned the repository.

### 3. Install dependencies

The project uses Jetpack Compose, so ensure that your project has the necessary dependencies in `build.gradle`:

```gradle
dependencies {
    implementation "androidx.compose.ui:ui:$compose_version"
    implementation "androidx.compose.material3:material3:$compose_version"
    implementation "androidx.navigation:navigation-compose:$nav_version"
    implementation "androidx.activity:activity-compose:$activity_compose_version"
    // Other dependencies
}
```

### 4. Run the project

1. Connect your Android device or start an emulator.
2. Click on the **Run** button in Android Studio (the green triangle).
3. The app should launch on your device/emulator.

## File Structure



## Usage

1. **Adding a Todo:**
    - Tap on the "Add Todo" button to create a new Todo item.
    - Fill out the details including Name, Priority, Deadline, and Description.
    - Save the Todo item.

2. **Editing a Todo:**
    - Tap on a Todo item to edit its details.
    - Update the Name, Priority, Deadline, Description, or Completion status.

3. **Deleting a Todo:**
    - Swipe left or right on a Todo item to delete it.

## Database Structure

The app uses a local SQLite database with the following table schema:

```sql
CREATE TABLE todos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT,
    priority INTEGER,
    description TEXT,
    deadline INTEGER,
    isCompleted INTEGER
);
```

- `id`: Unique identifier for each Todo item.
- `name`: The name/title of the Todo item.
- `priority`: An integer representing the priority level of the Todo item (e.g., 1 for high priority, 2 for medium, etc.).
- `description`: A detailed description of the Todo item.
- `deadline`: Timestamp for the Todo item's deadline.
- `isCompleted`: A flag indicating whether the Todo item is completed (`1` for true, `0` for false).

## Troubleshooting

- **Database not copied correctly**: Ensure that the `todo.db` file is placed in the `assets` folder, as it will be copied during the app's first run.
- **Database schema issues**: If you encounter schema mismatch errors, ensure that the version of the database is properly handled when upgrading.


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgements

- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose) for building modern UIs in Android.
- [Android Documentation](https://developer.android.com/docs) for providing useful resources and API documentation.
- SQLite for providing lightweight, serverless database storage.
