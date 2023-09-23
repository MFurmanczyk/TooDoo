package com.mfurmanczyk.toodoo.mobile.view.screen.pagernavigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mfurmanczyk.toodoo.data.model.Category
import com.mfurmanczyk.toodoo.data.model.ColorHolder
import com.mfurmanczyk.toodoo.data.model.Task
import com.mfurmanczyk.toodoo.data.model.relationship.CategoryWithTasks
import com.mfurmanczyk.toodoo.mobile.R
import com.mfurmanczyk.toodoo.mobile.util.NavigationDestination
import com.mfurmanczyk.toodoo.mobile.util.getCompletedTasksRatio
import com.mfurmanczyk.toodoo.mobile.util.toColorHolder
import com.mfurmanczyk.toodoo.mobile.util.toComposeColor
import com.mfurmanczyk.toodoo.mobile.view.component.TaskTile
import com.mfurmanczyk.toodoo.mobile.view.screen.theme.TooDooTheme
import com.mfurmanczyk.toodoo.mobile.view.screen.theme.spacing
import com.mfurmanczyk.toodoo.mobile.viewmodel.DashboardScreenUIState
import java.time.LocalDate
import java.time.LocalDateTime
object DashboardDestination : NavigationDestination(
    displayedTitle = R.string.dashboard,
    route = "dashboard",
    Icons.TwoTone.Home
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DashboardScreen(
    uiState: DashboardScreenUIState,
    modifier: Modifier = Modifier,
    onCategoryClick: (Category) -> Unit = {},
    onAddCategoryClick: () -> Unit = {},
    onTaskClick: (Task) -> Unit = {},
    onTaskCheckedChanged: (Task, Boolean) -> Unit = { _, _ -> }
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = MaterialTheme.spacing.small)
        ) {
            stickyHeader {
                Text(
                    text = stringResource(R.string.categories).uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .padding(start = MaterialTheme.spacing.small)
                        .padding(bottom = MaterialTheme.spacing.small)
                )
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.small),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    modifier = Modifier.padding(bottom = MaterialTheme.spacing.small)
                ) {
                    items(uiState.categoryList) {
                        CategoryTile(
                            onClick = onCategoryClick,
                            category = it.category,
                            progress = it.getCompletedTasksRatio()
                        )
                    }
                    item {
                        AddCategoryTile(onClick = onAddCategoryClick)
                    }
                }
            }
            stickyHeader {
                Text(
                    text = stringResource(R.string.today_tasks).uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .padding(start = MaterialTheme.spacing.small)
                        .padding(bottom = MaterialTheme.spacing.small)
                )
            }
            if (uiState.todaysTasks.isNotEmpty()) {
                items(uiState.todaysTasks) { task ->
                    AnimatedContent(
                        targetState = !task.isDone, label = "Task done animation"
                    ) {
                        if(it) {
                            TaskTile(
                                modifier = Modifier
                                    .padding(horizontal = MaterialTheme.spacing.small)
                                    .padding(bottom = MaterialTheme.spacing.small),
                                onClick = onTaskClick,
                                onCheckboxClick = onTaskCheckedChanged,
                                task = task
                            )
                        }
                    }
                }
            } else {
                item {
                    KudosTile()
                }
            }
        }
    }
}

@Composable
fun CategoryTile(
    onClick: (Category) -> Unit,
    category: Category,
    progress: Float,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .size(
                width = 220.dp,
                height = 120.dp
            ),
        onClick = {
            onClick(category)
        },
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = MaterialTheme.spacing.medium,
                    vertical = MaterialTheme.spacing.large
                )
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.SpaceBetween
        ) {
            val animatedProgress by animateFloatAsState(targetValue = progress, label = "progress value animation")
            Text(text = category.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
            LinearProgressIndicator(progress = animatedProgress, color = category.color.toComposeColor())
        }
    }
}

@Composable
fun AddCategoryTile(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .size(
                width = 220.dp,
                height = 120.dp
            ),
        shape = MaterialTheme.shapes.medium
    ) {
        TextButton(
            onClick = onClick,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    MaterialTheme.spacing.small,
                    Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(imageVector = Icons.TwoTone.Add, contentDescription = null)
                Text(
                    text = stringResource(R.string.add_new_category).uppercase(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun KudosTile(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        modifier = modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.TwoTone.ThumbUp,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surfaceTint
        )
        Text(text = stringResource(R.string.kudos), color = MaterialTheme.colorScheme.surfaceTint)
    }
}

@Preview
@Composable
fun DashboardScreenPreview() {
    TooDooTheme {
        DashboardScreen(
            DashboardScreenUIState(
                listOf(
                    CategoryWithTasks(
                        category = Category(
                            id = 1,
                            name = "School",
                            color = ColorHolder(255, 255, 255, 0)
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
                            ),
                            Task(
                                id = 1,
                                categoryId = 1,
                                name = "Test_1",
                                description = null,
                                createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
                                completedOn = LocalDateTime.of(2010, 1, 2, 12, 0, 0),
                                dueDate = LocalDate.of(2010, 1, 3),
                                isDone = true
                            ),
                            Task(
                                id = 1,
                                categoryId = 1,
                                name = "Test_1",
                                description = null,
                                createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
                                completedOn = LocalDateTime.of(2010, 1, 2, 12, 0, 0),
                                dueDate = LocalDate.of(2010, 1, 3),
                                isDone = true
                            ),
                            Task(
                                id = 1,
                                categoryId = 1,
                                name = "Test_1",
                                description = null,
                                createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
                                completedOn = LocalDateTime.of(2010, 1, 2, 12, 0, 0),
                                dueDate = LocalDate.of(2010, 1, 3),
                                isDone = true
                            ),
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
                            id = 1,
                            name = "Home",
                            color = ColorHolder(255, 0, 127, 255)
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
                                isDone = true
                            )
                        )
                    ),
                    CategoryWithTasks(
                        category = Category(
                            id = 2,
                            name = "Business",
                            color = ColorHolder(255, 255, 0, 255)
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
                ),
                listOf(
                    Task(
                        id = 4,
                        categoryId = 2,
                        name = "Test_4",
                        description = "Test_desc_4",
                        createdOn = LocalDateTime.of(2011, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2011, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2011, 1, 3),
                        isDone = true
                    ),
                    Task(
                        id = 5,
                        categoryId = 2,
                        name = "Test_5",
                        description = "Test_desc_5",
                        createdOn = LocalDateTime.of(2009, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2009, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2009, 1, 3),
                        isDone = false
                    ),
                    Task(
                        id = 5,
                        categoryId = 2,
                        name = "Test_5",
                        description = "Test_desc_5",
                        createdOn = LocalDateTime.of(2009, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2009, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2009, 1, 3),
                        isDone = true
                    ),
                    Task(
                        id = 5,
                        categoryId = 2,
                        name = "Test_5",
                        description = "Test_desc_5",
                        createdOn = LocalDateTime.of(2009, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2009, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2009, 1, 3),
                        isDone = true
                    ),
                    Task(
                        id = 5,
                        categoryId = 2,
                        name = "Test_5",
                        description = "Test_desc_5",
                        createdOn = LocalDateTime.of(2009, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2009, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2009, 1, 3),
                        isDone = false
                    ),
                    Task(
                        id = 5,
                        categoryId = 2,
                        name = "Test_5",
                        description = "Test_desc_5",
                        createdOn = LocalDateTime.of(2009, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2009, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2009, 1, 3),
                        isDone = false
                    ),
                    Task(
                        id = 5,
                        categoryId = 2,
                        name = "Test_5",
                        description = "Test_desc_5",
                        createdOn = LocalDateTime.of(2009, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2009, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2009, 1, 3),
                        isDone = false
                    ),
                    Task(
                        id = 5,
                        categoryId = 2,
                        name = "Test_5",
                        description = "Test_desc_5",
                        createdOn = LocalDateTime.of(2009, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2009, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2009, 1, 3),
                        isDone = true
                    ),
                    Task(
                        id = 5,
                        categoryId = 2,
                        name = "Test_5",
                        description = "Test_desc_5",
                        createdOn = LocalDateTime.of(2009, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2009, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2009, 1, 3),
                        isDone = true
                    ),
                    Task(
                        id = 5,
                        categoryId = 2,
                        name = "Test_5",
                        description = "Test_desc_5",
                        createdOn = LocalDateTime.of(2009, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2009, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2009, 1, 3),
                        isDone = true
                    ),
                    Task(
                        id = 5,
                        categoryId = 2,
                        name = "Test_5",
                        description = "Test_desc_5",
                        createdOn = LocalDateTime.of(2009, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2009, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2009, 1, 3),
                        isDone = true
                    ),
                    Task(
                        id = 5,
                        categoryId = 2,
                        name = "Test_5",
                        description = "Test_desc_5",
                        createdOn = LocalDateTime.of(2009, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2009, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2009, 1, 3),
                        isDone = false
                    ),
                    Task(
                        id = 5,
                        categoryId = 2,
                        name = "Test_5",
                        description = "Test_desc_5",
                        createdOn = LocalDateTime.of(2009, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2009, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2009, 1, 3),
                        isDone = true
                    ),
                    Task(
                        id = 5,
                        categoryId = 2,
                        name = "Test_5",
                        description = "Test_desc_5",
                        createdOn = LocalDateTime.of(2009, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2009, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2009, 1, 3),
                        isDone = true
                    ),
                    Task(
                        id = 5,
                        categoryId = 2,
                        name = "Test_5",
                        description = "Test_desc_5",
                        createdOn = LocalDateTime.of(2009, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2009, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2009, 1, 3),
                        isDone = true
                    ),
                    Task(
                        id = 5,
                        categoryId = 2,
                        name = "Test_5",
                        description = "Test_desc_5",
                        createdOn = LocalDateTime.of(2009, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2009, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2009, 1, 3),
                        isDone = false
                    ),
                    Task(
                        id = 5,
                        categoryId = 2,
                        name = "Test_5",
                        description = "Test_desc_5",
                        createdOn = LocalDateTime.of(2009, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2009, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2009, 1, 3),
                        isDone = true
                    ),
                    Task(
                        id = 5,
                        categoryId = 2,
                        name = "Test_5",
                        description = "Test_desc_5",
                        createdOn = LocalDateTime.of(2009, 1, 1, 12, 0, 0),
                        completedOn = LocalDateTime.of(2009, 1, 2, 12, 0, 0),
                        dueDate = LocalDate.of(2009, 1, 3),
                        isDone = true
                    )

                ),
                listOf()
            )
        )
    }
}

@Preview
@Composable
fun DashboardScreenNoTasksPreview() {
    TooDooTheme {
        DashboardScreen(
            DashboardScreenUIState(
                listOf(
                    CategoryWithTasks(
                        category = Category(
                            id = 1,
                            name = "School",
                            color = ColorHolder(255, 255, 255, 0)
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
                            id = 1,
                            name = "Home",
                            color = ColorHolder(255, 0, 127, 255)
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
                            name = "Business",
                            color = ColorHolder(255, 255, 0, 255)
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
                ),
                listOf(),
                listOf()
            )
        )
    }
}

@Preview
@Composable
fun CategoryTilePreview() {
    TooDooTheme {
        CategoryTile(
            onClick = {

            },
            category = Category(
                name = "School",
                color = Color.Cyan.toColorHolder()
            ),
            progress = 0.72f
        )
    }
}