package de.sh.todoapp.ui.screen

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import de.sh.todoapp.data.Controller
import de.sh.todoapp.data.TodoDataClass
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Todo screen composable function for displaying and managing todo items.
 * This function is responsible for rendering the UI for managing todo items, including adding, editing, and deleting them.
 * @param context The application context.
 * @param navController The navigation controller for handling navigation between screens.
 */
@Composable
fun TodoScreen(
    context: Context,
    navController: NavHostController
) {

    // Initialize the Controller
    val todoController = Controller(context)

    // State variables
    var todos by remember { mutableStateOf(todoController.getAllTodos()) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedTodo by remember { mutableStateOf<TodoDataClass?>(null) }

    // Callback to toggle completion status
    val onCompleteToggle: (TodoDataClass) -> Unit = { todo ->
        // Toggle the completion status
        todo.is_completed = !todo.is_completed
        todoController.updateTodo(todo) // Update the todo in the database
        todos = todoController.getAllTodos() // Refresh the todo list
    }

    Column(modifier = Modifier.background(MaterialTheme.colorScheme.background).padding(16.dp)) {

        // Header with back button and title "Todo List"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Back button to navigate back to the dashboard screen
            IconButton(onClick = {
                navController.navigate("dashboard") {
                    popUpTo("dashboard") { inclusive = true }
                }
            }) {

                // Icon for the back button
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Dashboard",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = "Todo List",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (todos.isEmpty()) {
            Text("No todos found.", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.error)
        }
        // Lazy column to display the todo items
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(todos) { todo ->
                // Expandable todo card for each todo item
                ExpandableTodoCard(
                    todo = todo,
                    onEditClick = {
                        selectedTodo = todo
                        showEditDialog = true
                    },
                    onCompleteToggle = onCompleteToggle // Pass the callback
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Add todo button
        Button(
            onClick = {
                selectedTodo = null
                showEditDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Todo", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    // Show the edit dialog if selectedTodo is not null
    if (showEditDialog) {
        EditTodoDialog(
            todo = selectedTodo,
            onDismiss = { showEditDialog = false },
            onSave = { todo ->
                if (todo.id == 0) {
                    todoController.insertTodo(todo)
                } else {
                    todoController.updateTodo(todo)
                }
                todos = todoController.getAllTodos()
                showEditDialog = false
            },
            onDelete = { todo ->
                todoController.deleteTodo(todo.id)
                todos = todoController.getAllTodos()
                showEditDialog = false
            }
        )
    }
}


/**
 * Expandable todo card composable function for displaying and managing todo items.
 *
 * @param todo The todo item to display.
 * @param onEditClick Callback to handle edit button click.
 * @param onCompleteToggle Callback to toggle completion status.
 * @receiver The todo item to display.
 * @receiver Callback to handle edit button click.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpandableTodoCard(
    todo: TodoDataClass,
    onEditClick: () -> Unit,
    onCompleteToggle: (TodoDataClass) -> Unit // New callback to toggle completion status
) {

    // State variables and remember functions
    var expanded by remember { mutableStateOf(false) }

    // Format the deadline timestamp into a readable date string
    val formattedDeadline = remember(todo.deadline) {
        java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(todo.deadline))
    }

    // Convert numeric priority to string representation
    val (priorityLabel, backgroundColor) = when (todo.priority) {
        0 -> "LOW" to MaterialTheme.colorScheme.secondaryContainer // Light Green
        1 -> "MEDIUM" to MaterialTheme.colorScheme.tertiaryContainer // Light Yellow
        2 -> "HIGH" to MaterialTheme.colorScheme.errorContainer // Light Red
        else -> "LOW" to MaterialTheme.colorScheme.secondaryContainer // Default Background
    }

    // Card for displaying the todo item with expandable content
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { expanded = !expanded },
                onLongClick = { onEditClick() }
            ),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = MaterialTheme.shapes.medium,
        border = null,

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Row for displaying the todo name and expand/collapse button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = todo.name,
                    style = MaterialTheme.typography.titleLarge,
                    textDecoration = if (todo.is_completed) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }

                )
                // Expand/collapse button with corresponding icon based on expanded state
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }

            // Expanded content
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = "Priority: $priorityLabel",
                        style = MaterialTheme.typography.bodyMedium,
                        textDecoration = if (todo.is_completed) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        }
                    )
                    Text(
                        text = "Deadline: $formattedDeadline",
                        style = MaterialTheme.typography.bodyMedium,
                        textDecoration = if (todo.is_completed) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        }
                    )
                    Text(
                        text = todo.description,
                        style = MaterialTheme.typography.bodyMedium,
                        textDecoration = if (todo.is_completed) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        }
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (todo.is_completed) "Status: Completed" else "Status: Pending",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Checkbox(
                            checked = todo.is_completed,
                            onCheckedChange = {
                                // Toggle completion status
                                onCompleteToggle(todo)
                            }
                        )
                    }
                }
            }
        }
    }
}


/**
 * Edit todo dialog composable function for displaying and managing todo items.
 *
 * @param todo The todo item to edit.
 * @param onDismiss Callback to dismiss the dialog.
 * @param onSave Callback to save the edited todo.
 * @param onDelete Callback to delete the todo.
 * @receiver The todo item to edit.
 * @receiver Callback to dismiss the dialog.
 * @receiver Callback to save the edited todo.
 */
@Composable
fun EditTodoDialog(
    todo: TodoDataClass?,
    onDismiss: () -> Unit,
    onSave: (TodoDataClass) -> Unit,
    onDelete: (TodoDataClass) -> Unit
) {
    var name by remember { mutableStateOf(todo?.name ?: "") }
    var priority by remember { mutableStateOf(todo?.priority?.toString() ?: "0") }
    var deadline by remember { mutableLongStateOf(todo?.deadline ?: System.currentTimeMillis()) }
    var description by remember { mutableStateOf(todo?.description ?: "") }
    var isCompleted by remember { mutableStateOf(todo?.is_completed ?: false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance().apply {
        timeInMillis = deadline
    }
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // Convert numeric priority to string representation
    val priorityLabel = when (priority.toIntOrNull() ?: 0) {
        0 -> "LOW"
        1 -> "MEDIUM"
        2 -> "HIGH"
        else -> "LOW" // Default to LOW for invalid input
    }



    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = if (todo == null) "Add New Todo" else "Edit Todo")
        },
        text = {
            val scrollState = androidx.compose.foundation.rememberScrollState()
            Column(modifier = Modifier.verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    maxLines = 1,
                    isError = name.isBlank(),
                    supportingText = {
                        if (name.isBlank()) {
                            Text("Name cannot be empty")
                        }
                    },
                    trailingIcon = {
                        if (name.isNotBlank()) {
                            IconButton(onClick = { name = "" }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Clear Name")
                            }
                        }
                    }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Priority: $priorityLabel",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Column {
                        Button(onClick = { priority = "0" }) {
                            Text("LOW")
                        }
                        Button(onClick = { priority = "1" }) {
                            Text("MEDIUM")
                        }
                        Button(onClick = { priority = "2" }) {
                            Text("HIGH")
                        }
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Deadline: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                            Date(deadline)
                        )}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Button(onClick = {
                        android.app.DatePickerDialog(
                            context,
                            { _, selectedYear, selectedMonth, selectedDay ->
                                // Update the deadline with the selected date
                                calendar.set(selectedYear, selectedMonth, selectedDay)
                                deadline = calendar.timeInMillis
                            },
                            year, month, day
                        ).show()
                    }) {
                        Text("🗓️")
                    }
                }
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    maxLines = 1,
                    isError = description.isBlank(),
                    supportingText = {
                        if (description.isBlank()) {
                            Text("Description cannot be empty")
                        }

                        },
                    trailingIcon = {
                        if (description.isNotBlank()) {
                            IconButton(onClick = { description = "" }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Clear Description")
                            }
                        }
                    }

                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Completed")
                    Checkbox(
                        checked = isCompleted,
                        onCheckedChange = { isCompleted = it }
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (name.isBlank()) {
                    Toast.makeText(context, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                }
                else if(description.isBlank()){
                    Toast.makeText(context, "Description cannot be empty", Toast.LENGTH_SHORT).show()
                }
                else{
                    val updatedTodo = TodoDataClass(
                        id = todo?.id ?: 0,
                        name = name,
                        priority = priority.toIntOrNull() ?: 0, // Ensure it's 0, 1, or 2
                        deadline = deadline,
                        description = description,
                        is_completed = isCompleted // Updated completion status
                    )
                    onSave(updatedTodo)
                }

            }) {
                Text("Save")
            }
        },
        dismissButton = {
            if (todo != null) {
                Button(onClick = { onDelete(todo) }) {
                    Text("Delete")
                }
            }
        }
    )
}