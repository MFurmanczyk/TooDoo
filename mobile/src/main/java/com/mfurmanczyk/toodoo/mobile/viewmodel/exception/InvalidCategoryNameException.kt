package com.mfurmanczyk.toodoo.mobile.viewmodel.exception

import com.mfurmanczyk.toodoo.mobile.R

class InvalidCategoryNameException(
    override val displayMessage: Int = R.string.category_name_warning
) : TooDooException("Category name cannot be empty!")