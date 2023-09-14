package com.mfurmanczyk.toodoo.data.model

import androidx.annotation.IntRange
import androidx.room.Entity

/**
 * Helper color holder needs to be used to store color in database
 * because compose Color is an inline class that is not support by room yet.
 */
@Entity
data class Color (
    @IntRange(from = 0, to = 255) val alpha: Int,
    @IntRange(from = 0, to = 255) val red: Int,
    @IntRange(from = 0, to = 255) val green: Int,
    @IntRange(from = 0, to = 255) val blue: Int
) {

}
