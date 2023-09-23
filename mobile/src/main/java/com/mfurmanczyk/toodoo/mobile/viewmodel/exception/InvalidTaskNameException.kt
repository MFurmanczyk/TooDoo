package com.mfurmanczyk.toodoo.mobile.viewmodel.exception

import com.mfurmanczyk.toodoo.mobile.R

class InvalidTaskNameException(
    override val displayMessage: Int = R.string.task_name_warning
) : TooDooException("Task name cannot be empty!")