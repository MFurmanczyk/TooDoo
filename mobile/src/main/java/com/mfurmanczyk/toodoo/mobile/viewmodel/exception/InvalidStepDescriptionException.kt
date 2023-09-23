package com.mfurmanczyk.toodoo.mobile.viewmodel.exception

import com.mfurmanczyk.toodoo.mobile.R

class InvalidStepDescriptionException(
    override val displayMessage: Int = R.string.step_description_warning
) : TooDooException("Step description cannot be empty!")