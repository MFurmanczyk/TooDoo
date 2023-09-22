package com.mfurmanczyk.toodoo.mobile.viewmodel.exception

import androidx.annotation.StringRes

abstract class TooDooException(
    message: String,
    @StringRes open val displayMessage: Int? = null
) : Exception(message)