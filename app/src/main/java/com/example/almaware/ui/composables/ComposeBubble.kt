package com.example.almaware.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.almaware.R
import com.example.almaware.data.model.HomeCard

private fun selectDrawable(string: String): Int {
    return when (string) {
        "course" -> R.drawable.courseunits
        "project" -> R.drawable.project
        "publication" -> R.drawable.publication
        else -> R.drawable.info
    }
}

@Composable
fun ComposeBubble(
    item: HomeCard,
    string: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(200.dp)
            .padding(bottom = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.bubble),
            contentDescription = "Bubble",
            tint = Color(item.borderColor),
            modifier = Modifier.fillMaxSize()
        )

        Image(
            painter = painterResource(id = R.drawable.around),
            contentDescription = "Around bubble",
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 9.dp, x = 3.dp)
        )

        Image(
            painter = painterResource(id = selectDrawable(string)),
            contentDescription = "Clickable image",
            modifier = Modifier.size(90.dp)
        )
    }
}