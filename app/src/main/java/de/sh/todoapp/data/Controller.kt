package de.sh.todoapp.data

import android.content.ContentValues
import android.content.Context
import android.util.Log

/**
 * Controller class for managing ToDo items.
 *
 * @constructor Creates a new Controller instance.
 *
 * @param context The application context.
 */
class Controller(context: Context) {
    private val dbHelper = DbHelper(context)

    // Insert a ToDo item
    /**
     * Insert todo into the database.
     *
     * @param todo The todo to insert.
     * @return True if the insertion was successful, false otherwise.
     */
    fun insertTodo(todo: TodoDataClass): Boolean {
        // Log the todo being inserted for debugging purposes
        Log.d("Controller", "Inserting todo: $todo")

        // Get a writable database connection from the helper
        val db = dbHelper.writableDatabase
        // Try to insert the todo into the database
        return try {
            val values = ContentValues().apply {
                put("name", todo.name)
                put("priority", todo.priority)
                put("description", todo.description)
                put("deadline", todo.deadline)
                put("isCompleted", if (todo.is_completed) 1 else 0)
            }
            val result = db.insert("todos", null, values)
            result != -1L
        } catch (e: Exception) {
            Log.e("Controller", "Insert failed", e)
            false
        } finally {
            db.close()
        }
    }

    // Update a ToDo item
    /**
     * Update todo in the database.
     *
     * @param todo The todo to update.
     * @return True if the update was successful, false otherwise.
     */
    fun updateTodo(todo: TodoDataClass): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val values = ContentValues().apply {
                put("name", todo.name)
                put("priority", todo.priority)
                put("description", todo.description)
                put("deadline", todo.deadline)
                put("isCompleted", if (todo.is_completed) 1 else 0)
            }
            val result = db.update("todos", values, "id = ?", arrayOf(todo.id.toString()))
            Log.d("Controller", "Update result: $result, ToDo ID: ${todo.id}")
            result > 0
        } catch (e: Exception) {
            Log.e("Controller", "Update failed", e)
            false
        } finally {
            db.close()
        }
    }

    // Delete a ToDo item
    /**
     * Delete todo by id from the database.
     *
     * @param todoId The id of the todo to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    fun deleteTodo(todoId: Int): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val result = db.delete("todos", "id = ?", arrayOf(todoId.toString()))
            result > 0
        } catch (e: Exception) {
            Log.e("Controller", "Delete failed", e)
            false
        } finally {
            db.close()
        }
    }

    // Retrieve all ToDo items
    /**
     * Get all todos from the database.
     *
     * @return A list of TodoDataClass objects representing all todos.
     */
    fun getAllTodos(): List<TodoDataClass> {
        val db = dbHelper.readableDatabase
        val todos = mutableListOf<TodoDataClass>()
        val cursor = db.rawQuery("SELECT * FROM todos", null)
        try {
            if (cursor.moveToFirst()) {
                do {
                    val todo = TodoDataClass(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        priority = cursor.getInt(cursor.getColumnIndexOrThrow("priority")),
                        description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        deadline = cursor.getLong(cursor.getColumnIndexOrThrow("deadline")),
                        is_completed = cursor.getInt(cursor.getColumnIndexOrThrow("isCompleted")) == 1
                    )
                    todos.add(todo)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("Controller", "Fetching todos failed", e)
        } finally {
            cursor.close()
            db.close()
        }
        return todos
    }
}
