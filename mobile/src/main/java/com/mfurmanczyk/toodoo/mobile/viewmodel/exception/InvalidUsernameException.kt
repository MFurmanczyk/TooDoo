package com.mfurmanczyk.toodoo.mobile.viewmodel.exception

import com.mfurmanczyk.toodoo.mobile.R

class InvalidUsernameException(
    override val displayMessage: Int = R.string.username_empty_warning
) : TooDooException(message = "Username must not be empty!")