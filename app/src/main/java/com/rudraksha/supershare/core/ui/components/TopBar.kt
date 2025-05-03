package com.rudraksha.supershare.core.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperShareTopBar(
    title: String = "",
    showBackButton: Boolean = false,
    showSearchBar: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    searchBar: @Composable (() -> Unit)? = null,
    centerTitle: Boolean = searchBar == null && title.length <= 10,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val navigationIcon: @Composable (() -> Unit)? = if (showBackButton) {
        {
            ActionIcon(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Nav Icon",
                onClick = onBackClick ?: {}
            )
        }
    } else null

    val colors = if (centerTitle)
        TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    else
        TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )

    if (searchBar != null && showSearchBar) {
        TopAppBar(
            title = { searchBar() },
            navigationIcon = navigationIcon ?: {},
            actions = actions,
            colors = colors,
            modifier = Modifier.defaultMinSize(minHeight = 96.dp)
        )
    } else {
        if (centerTitle) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineLarge,
                    )
                },
                modifier = Modifier.padding(horizontal = 0.dp),
                navigationIcon = navigationIcon ?: {},
                actions = actions,
                colors = colors,
            )
        } else {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineLarge,
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 0.dp)
                    .defaultMinSize(minHeight = 96.dp)
                    .height(96.dp),
                navigationIcon = navigationIcon ?: {},
                actions = actions,
                colors = colors,
            )
        }
    }
}
