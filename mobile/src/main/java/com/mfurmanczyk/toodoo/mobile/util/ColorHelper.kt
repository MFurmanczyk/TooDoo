package com.mfurmanczyk.toodoo.mobile.util

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.mfurmanczyk.toodoo.data.model.ColorHolder

@Stable
fun ColorHolder.toComposeColor() =
    Color(
        red = this.red,
        green = this.green,
        blue = this.blue,
        alpha = this.alpha
    )

@Stable
fun Color.toColorHolder() =
    ColorHolder(
        (this.alpha * 255).toInt(),
        (this.red * 255).toInt(),
        (this.green * 255).toInt(),
        (this.blue * 255).toInt()
    )
