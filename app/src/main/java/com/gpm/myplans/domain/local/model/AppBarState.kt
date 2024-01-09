package com.gpm.myplans.domain.local.model

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

data class AppBarState(
        val title: String = "",
        val navIcon: @Composable () -> Unit= {},
        val actions: (@Composable RowScope.() -> Unit)? = null
    )
