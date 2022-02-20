package com.mywork.loadigouser.model.locale

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

class BoardingItem(
    @DrawableRes val imageResource: Int,
    @StringRes val headerText: Int,
    @StringRes val bodyText: Int,
)