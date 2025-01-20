package de.sh.todoapp.data

/**
 * Todo data class for representing a todo item.
 *
 * @property id The unique identifier for the todo item.
 * @property name The name of the todo item.
 * @property priority The priority of the todo item.
 * @property deadline The deadline of the todo item.
 * @property description The description of the todo item.
 * @property is_completed A flag indicating if the todo item is completed.
 * @constructor Create empty Todo data class for representing a todo item.
 */
data class TodoDataClass(
    val id: Int,
    val name: String,
    val priority: Int,
    val deadline: Long,
    val description: String,
    var is_completed: Boolean
)
