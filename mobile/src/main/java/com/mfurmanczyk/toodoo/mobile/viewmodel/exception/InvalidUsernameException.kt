package com.mfurmanczyk.toodoo.mobile.viewmodel.exception

import com.mfurmanczyk.toodoo.mobile.R

class InvalidUsernameException : TooDooException(displayMessage = R.string.username_empty_warning, message = "Username must not be empty!")