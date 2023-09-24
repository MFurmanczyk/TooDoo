package com.mfurmanczyk.toodoo.mobile.view.screen

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mfurmanczyk.toodoo.data.model.Category
import com.mfurmanczyk.toodoo.data.model.ColorHolder
import com.mfurmanczyk.toodoo.data.model.Task
import com.mfurmanczyk.toodoo.data.model.relationship.CategoryWithTasks
import com.mfurmanczyk.toodoo.mobile.view.component.TaskTile
import com.mfurmanczyk.toodoo.mobile.view.screen.pagernavigation.AddCategoryTile
import com.mfurmanczyk.toodoo.mobile.view.screen.pagernavigation.CategoryTile
import com.mfurmanczyk.toodoo.mobile.view.screen.pagernavigation.DashboardScreen
import com.mfurmanczyk.toodoo.mobile.viewmodel.DashboardScreenUIState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class DashboardScreenTest {

    private val TEST_TAG = "Test"

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    @Throws(Exception::class)
    fun taskTile_displaysTaskName() {

        val TASK_NAME = "Task test name"
        rule.setContent {
            val task by remember {
                mutableStateOf(
                    Task(
                        name = TASK_NAME,
                        description = null,
                        createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2010, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2010, 1, 3),
                        isDone = false
                    )
                )
            }

            TaskTile(
                onClick = {},
                onCheckboxClick = {_, _ -> },
                task = task,
                modifier = Modifier.semantics { testTag = TEST_TAG }
            )
        }
        rule.onNodeWithTag(TEST_TAG)
            .assertIsDisplayed()
            .assert(hasText(TASK_NAME))
    }

    @Test
    @Throws(Exception::class)
    fun taskTile_checkboxClicked_checkboxCheckedAfterClick() {
        val TASK_NAME = "Task test name"

        val task = mutableStateOf(
                Task(
                    name = TASK_NAME,
                    description = null,
                    createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
                    completedOn = LocalDateTime.of(2010, 1, 2, 12, 0, 0),
                    dueDate = LocalDate.of(2010, 1, 3),
                    isDone = false
                )
            )

        rule.setContent {


            TaskTile(
                onClick = {},
                onCheckboxClick = {task1, checked ->
                    task.value = task1.copy(isDone = checked)
                },
                task = task.value,
                modifier = Modifier.semantics { testTag = TEST_TAG }
            )
        }

        rule.onNodeWithTag(TEST_TAG).assertIsDisplayed().assert(hasText(TASK_NAME))
        rule.onNode(isToggleable()).assertIsOff().performClick().assertIsOn()
        assertTrue(task.value.isDone)
    }

    @Test
    @Throws(Exception::class)
    fun taskTile_clickPerformed_clickMethodTriggered() {
        val TASK_NAME = "Task test name"

        var counter = 0

        rule.setContent {
            val task by remember {
                mutableStateOf(
                    Task(
                        name = TASK_NAME,
                        description = null,
                        createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2010, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2010, 1, 3),
                        isDone = false
                    )
                )
            }

            TaskTile(
                onClick = {
                    counter++
                },
                onCheckboxClick = {_, _ ->

                },
                task = task,
                modifier = Modifier.semantics { testTag = TEST_TAG }
            )
        }

        rule.onNodeWithTag(TEST_TAG).assertIsDisplayed().performClick()
        assertEquals(1, counter)
    }

    @Test
    @Throws(Exception::class)
    fun categoryTile_displaysCategoryName() {
        val CATEGORY_NAME = "Test"

        rule.setContent {

            val category by remember {
                mutableStateOf(
                    Category(
                        name = CATEGORY_NAME,
                        color = ColorHolder(255, 255, 255, 255),
                    )
                )
            }

            CategoryTile(
                onClick = {},
                category = category,
                progress = 0.5f,
                modifier = Modifier.semantics { testTag = TEST_TAG }
            )
        }

        rule.onNodeWithTag(TEST_TAG).assertIsDisplayed().assertHasClickAction()
        rule.onNodeWithText(CATEGORY_NAME).assertIsDisplayed()
    }

    @Test
    @Throws(Exception::class)
    fun categoryTile_clickPerformed_clickMethodTriggered() {
        val CATEGORY_NAME = "Test"

        val counter = mutableStateOf(0)

        rule.setContent {

            val category by remember {
                mutableStateOf(
                    Category(
                        name = CATEGORY_NAME,
                        color = ColorHolder(255, 255, 255, 255),
                    )
                )
            }

            CategoryTile(
                onClick = {
                          counter.value++
                },
                category = category,
                progress = 0.5f,
                modifier = Modifier.semantics { testTag = TEST_TAG }
            )
        }

        rule.onNodeWithTag(TEST_TAG).performClick()

        assertEquals(1, counter.value)
    }

    @Test
    @Throws(Exception::class)
    fun addCategoryTile_clickPerformed_clickMethodTriggered() {
        val counter = mutableStateOf(0)

        rule.setContent {

            AddCategoryTile(onClick = { counter.value++ }, modifier = Modifier.semantics { testTag = TEST_TAG  })
        }

        rule.onNodeWithTag(TEST_TAG).performClick()
        assertEquals(1, counter.value)
    }

    @Test
    @Throws(Exception::class)
    fun dashboardScreen_categoryDisplayed() {

        val CATEGORY_NAME = "Test"

        rule.setContent {

            val uiState by remember {
                mutableStateOf(
                    DashboardScreenUIState(
                        listOf(
                            CategoryWithTasks(
                                category = Category(name = CATEGORY_NAME, color = ColorHolder(1,1,1,1)), tasks = listOf()
                            )
                        ),
                        listOf(),
                        listOf()
                    )
                )
            }

            DashboardScreen(uiState = uiState)

        }

        rule.onNodeWithText(CATEGORY_NAME).assertIsDisplayed()

    }

    @Test
    @Throws(Exception::class)
    fun dashboardScreen_categoryClicked_clickMethodTriggered() {
        val CATEGORY_NAME = "Test"

        val counter = mutableStateOf(0)

        rule.setContent {

            val uiState by remember {
                mutableStateOf(
                    DashboardScreenUIState(
                        listOf(
                            CategoryWithTasks(
                                category = Category(name = CATEGORY_NAME, color = ColorHolder(1,1,1,1)), tasks = listOf()
                            )
                        ),
                        listOf(),
                        listOf()
                    )
                )
            }

            DashboardScreen(uiState = uiState, onCategoryClick = {counter.value++})

        }

        rule.onNodeWithText(CATEGORY_NAME).assertIsDisplayed().performClick()
        assertEquals(1, counter.value)
    }

    @Test
    @Throws(Exception::class)
    fun dashboardScreen_addCategoryClicked_clickMethodTriggered() {
        val CATEGORY_NAME = "Test"

        val counter = mutableStateOf(0)

        rule.setContent {

            val uiState by remember {
                mutableStateOf(
                    DashboardScreenUIState(
                        listOf(
                            CategoryWithTasks(
                                category = Category(name = CATEGORY_NAME, color = ColorHolder(1,1,1,1)), tasks = listOf()
                            )
                        ),
                        listOf(),
                        listOf()
                    )
                )
            }

            DashboardScreen(uiState = uiState, onAddCategoryClick = {counter.value++})

        }

        rule.onNodeWithText("Add", substring = true, ignoreCase = true).assertIsDisplayed().performClick()
        assertEquals(1, counter.value)
    }

    @Test
    @Throws(Exception::class)
    fun dashboardScreen_taskClicked_clickMethodTriggered() {
        val TASK_NAME = "Test"

        val counter = mutableStateOf(0)

        rule.setContent {

            val uiState by remember {
                mutableStateOf(
                    DashboardScreenUIState(
                        listOf(),
                        listOf(
                            Task(
                                name = TASK_NAME,
                                description = null,
                                createdOn = LocalDateTime.now(),
                                completedOn = LocalDateTime.now(),
                                dueDate = LocalDate.now(),
                                isDone = true
                            )
                        ),
                        listOf()
                    )
                )
            }

            DashboardScreen(uiState = uiState, onTaskClick = {counter.value++})

        }

        rule.onNodeWithText(TASK_NAME).assertIsDisplayed().performClick()
        assertEquals(1, counter.value)
    }
}