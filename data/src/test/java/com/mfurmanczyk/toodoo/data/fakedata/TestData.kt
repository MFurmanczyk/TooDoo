package com.mfurmanczyk.toodoo.data.fakedata

import android.graphics.Color
import com.mfurmanczyk.toodoo.data.model.Category
import com.mfurmanczyk.toodoo.data.model.Step
import com.mfurmanczyk.toodoo.data.model.Task
import com.mfurmanczyk.toodoo.data.model.relationship.CategoryWithTasks
import com.mfurmanczyk.toodoo.data.model.relationship.TaskWithSteps
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Collections

internal open class TestData {

    val taskData = TaskData()
    val stepData = StepData()
    val categoryData = CategoryData()
    val tasksWithStepsData = TasksWithStepsData()
    val categoriesWithTasksData = CategoriesWithTasksData()

    class TaskData {

        private val task_1 = Task(
            id = 1,
            categoryId = 1,
            name = "Test_1",
            description = null,
            createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
            completedOn = LocalDateTime.of(2010, 1, 2, 12, 0, 0),
            dueDate = LocalDate.of(2010, 1, 3),
            isDone = false
        )

        private val task_2 = Task(
            id = 2,
            categoryId = 2,
            name = "Test_2",
            description = null,
            createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
            completedOn = LocalDateTime.of(2010, 1, 2, 12, 0, 0),
            dueDate = LocalDate.of(2010, 1, 3),
            isDone = false
        )

        private val task_3 = Task(
            id = 3,
            categoryId = 2,
            name = "Test_3",
            description = "Test_desc",
            createdOn = LocalDateTime.of(2011, 1, 1, 12, 0, 0),
            completedOn = LocalDateTime.of(2011, 1, 2, 12, 0, 0),
            dueDate = LocalDate.of(2011, 1, 3),
            isDone = false
        )

        private val task_4 = Task(
            id = 4,
            categoryId = 2,
            name = "Test_4",
            description = "Test_desc_4",
            createdOn = LocalDateTime.of(2011, 1, 1, 12, 0, 0),
            completedOn = LocalDateTime.of(2011, 1, 2, 12, 0, 0),
            dueDate = LocalDate.of(2011, 1, 3),
            isDone = true
        )

        private val task_5 = Task(
            id = 5,
            categoryId = 2,
            name = "Test_5",
            description = "Test_desc_5",
            createdOn = LocalDateTime.of(2009, 1, 1, 12, 0, 0),
            completedOn = LocalDateTime.of(2009, 1, 2, 12, 0, 0),
            dueDate = LocalDate.of(2009, 1, 3),
            isDone = true
        )

        private val task_6 = Task(
            id = 6,
            categoryId = null,
            name = "Test_6",
            description = "Test_desc_6",
            createdOn = LocalDateTime.of(2009, 1, 1, 12, 0, 0),
            completedOn = LocalDateTime.of(2009, 1, 2, 12, 0, 0),
            dueDate = LocalDate.of(2009, 1, 3),
            isDone = true
        )

        val task_7_insert = Task(
            id = 7,
            categoryId = 1,
            name = "Test_7",
            description = "Test_desc_7",
            createdOn = LocalDateTime.of(2012, 1, 1, 12, 0, 0),
            completedOn = LocalDateTime.of(2012, 1, 2, 12, 0, 0),
            dueDate = LocalDate.of(2012, 1, 3),
            isDone = false
        )

        val task_6_update = Task(
            id = 6,
            categoryId = 1,
            name = "Test_6_update",
            description = "Test_desc_6",
            createdOn = LocalDateTime.of(2012, 1, 1, 12, 0, 0),
            completedOn = LocalDateTime.of(2012, 1, 2, 12, 0, 0),
            dueDate = LocalDate.of(2012, 1, 3),
            isDone = false
        )

        val tasks = mutableListOf(task_1, task_2, task_3, task_4, task_5, task_6)

        fun first() = task_1
        fun last() = task_6

        fun addTask() {
            tasks.add(task_7_insert)
        }

        fun updateTask() {
            Collections.replaceAll(tasks, task_6, task_6_update)
        }

        fun deleteFirst() {
            tasks.removeFirst()
        }

        fun deleteFirstAndLast() {
            tasks.removeFirst()
            tasks.removeLast()
        }
    }

    class StepData {

        private val step_1 = Step(
            id = 1,
            taskId = 1,
            description = "test_step_1",
            createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
            isDone = false
        )

        private val step_2 = Step(
            id = 2,
            taskId = 1,
            description = "test_step_2",
            createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
            isDone = false
        )

        private val step_3 = Step(
            id = 3,
            taskId = 2,
            description = "test_step_3",
            createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
            isDone = true
        )

        private val step_4 = Step(
            id = 4,
            taskId = 2,
            description = "test_step_4",
            createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
            isDone = false
        )

        private val step_5 = Step(
            id = 5,
            taskId = 2,
            description = "test_step_5",
            createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
            isDone = true
        )

        private val step_6 = Step(
            id = 6,
            taskId = 3,
            description = "test_step_6",
            createdOn = LocalDateTime.of(2011, 1, 1, 12, 0, 0),
            isDone = false
        )

        private val step_7 = Step(
            id = 7,
            taskId = 4,
            description = "test_step_7",
            createdOn = LocalDateTime.of(2011, 1, 1, 12, 0, 0),
            isDone = false
        )

        private val step_8 = Step(
            id = 8,
            taskId = 4,
            description = "test_step_8",
            createdOn = LocalDateTime.of(2011, 1, 1, 12, 0, 0),
            isDone = true
        )

        private val step_9 = Step(
            id = 9,
            taskId = 4,
            description = "test_step_9",
            createdOn = LocalDateTime.of(2011, 1, 1, 12, 0, 0),
            isDone = false
        )

        val step_10_insert = Step(
            id = 10,
            taskId = 4,
            description = "test_step_10",
            createdOn = LocalDateTime.of(2011, 1, 1, 12, 0, 0),
            isDone = true
        )

        val step_2_update = Step(
            id = 2,
            taskId = 1,
            description = "test_step_2_update",
            createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
            isDone = true
        )

        val steps =
            mutableListOf(step_1, step_2, step_3, step_4, step_5, step_6, step_7, step_8, step_9)

        fun first() = step_1
        fun last() = step_9

        fun addStep() {
            steps.add(step_10_insert)
        }

        fun updateStep() {
            Collections.replaceAll(steps, step_2, step_2_update)
        }

        fun deleteFirst() {
            steps.removeFirst()
        }

        fun deleteFirstAndLast() {
            steps.removeFirst()
            steps.removeLast()
        }

    }

    class CategoryData {

        private val category_1 = Category(
            id = 1,
            name = "category_1",
            color = Color()
        )

        private val category_2 = Category(
            id = 2,
            name = "category_2",
            color = Color()
        )

        val category_3_insert = Category(
            name = "category_3",
            color = Color()
        )

        val category_2_update = Category(
            id = 2,
            name = "category_2_update",
            color = Color()
        )

        val categories = mutableListOf(category_1, category_2)

        fun first() = category_1
        fun last() = category_2

        fun addCategory() {
            categories.add(category_3_insert)
        }

        fun updateCategory() {
            Collections.replaceAll(categories, category_2, category_2_update)
        }

        fun deleteFirst() {
            categories.removeFirst()
        }

        fun deleteFirstAndLast() {
            categories.removeFirst()
            categories.removeLast()
        }

    }

    class TasksWithStepsData {
        val tasksWithSteps = listOf(
            TaskWithSteps(
                task = Task(
                    id = 1,
                    categoryId = 1,
                    name = "Test_1",
                    description = null,
                    createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
                    completedOn = LocalDateTime.of(2010, 1, 2, 12, 0, 0),
                    dueDate = LocalDate.of(2010, 1, 3),
                    isDone = false
                ),
                steps = listOf(
                    Step(
                        id = 1,
                        taskId = 1,
                        description = "test_step_1",
                        createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
                        isDone = false
                    ),
                    Step(
                        id = 2,
                        taskId = 1,
                        description = "test_step_2",
                        createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
                        isDone = false
                    )
                )
            ),
            TaskWithSteps(
                task = Task(
                    id = 2,
                    categoryId = 2,
                    name = "Test_2",
                    description = null,
                    createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
                    completedOn = LocalDateTime.of(2010, 1, 2, 12, 0, 0),
                    dueDate = LocalDate.of(2010, 1, 3),
                    isDone = false
                ),
                steps = listOf(
                    Step(
                        id = 2,
                        taskId = 1,
                        description = "test_step_2",
                        createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
                        isDone = false
                    ), Step(
                        id = 3,
                        taskId = 2,
                        description = "test_step_3",
                        createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
                        isDone = true
                    ),
                    Step(
                        id = 4,
                        taskId = 2,
                        description = "test_step_4",
                        createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
                        isDone = false
                    ),
                    Step(
                        id = 5,
                        taskId = 2,
                        description = "test_step_5",
                        createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
                        isDone = true
                    )
                )
            )
        )
    }

    class CategoriesWithTasksData {
        val categoriesWithTasks = listOf(
            CategoryWithTasks(
                category = Category(
                    id = 1,
                    name = "category_1",
                    color = Color()
                ),
                tasks = listOf(
                    Task(
                        id = 1,
                        categoryId = 1,
                        name = "Test_1",
                        description = null,
                        createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2010, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2010, 1, 3),
                        isDone = false
                    )
                )
            ),
            CategoryWithTasks(
                category = Category(
                    id = 2,
                    name = "category_2",
                    color = Color()
                ),
                tasks = listOf(
                    Task(
                        id = 2,
                        categoryId = 2,
                        name = "Test_2",
                        description = null,
                        createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2010, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2010, 1, 3),
                        isDone = false
                    ), Task(
                        id = 3,
                        categoryId = 2,
                        name = "Test_3",
                        description = "Test_desc",
                        createdOn = LocalDateTime.of(2011, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2011, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2011, 1, 3),
                        isDone = false
                    ), Task(
                        id = 4,
                        categoryId = 2,
                        name = "Test_4",
                        description = "Test_desc_4",
                        createdOn = LocalDateTime.of(2011, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2011, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2011, 1, 3),
                        isDone = true
                    ), Task(
                        id = 5,
                        categoryId = 2,
                        name = "Test_5",
                        description = "Test_desc_5",
                        createdOn = LocalDateTime.of(2009, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2009, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2009, 1, 3),
                        isDone = true
                    )
                )
            )
        )
    }
}
