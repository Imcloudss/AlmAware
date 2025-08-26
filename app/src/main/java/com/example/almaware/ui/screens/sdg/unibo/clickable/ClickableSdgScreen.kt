package com.example.almaware.ui.screens.sdg.unibo.clickable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.almaware.data.model.HomeCard
import com.example.almaware.ui.composables.AppBar
import com.example.almaware.ui.composables.ComposeBubble
import com.example.almaware.ui.screens.sdg.VigaFontFamily
import com.example.almaware.ui.screens.sdg.unibo.UniboViewModel
import com.example.almaware.utils.caption
import com.example.almaware.utils.filterProjectsBySdgId
import org.koin.androidx.compose.koinViewModel

@Composable
fun ClickableSdgScreen(
    navController: NavController,
    item: HomeCard,
    string: String,
    value: Int,
    uniboViewModel: UniboViewModel = koinViewModel()
) {
    val projects by uniboViewModel.projects.collectAsState()
    LaunchedEffect(Unit) {
        uniboViewModel.loadProjects()
    }

    val filterProjects = filterProjectsBySdgId(projects, item.id.toString())

    Scaffold(
        topBar = {
            AppBar(
                "Prove",
                navController
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ComposeBubble(
                    item,
                    string
                )

                Text(
                    text = value.toString(),
                    fontFamily = VigaFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 40.sp,
                    modifier = Modifier.padding(
                        top = 10.dp,
                        bottom = 6.dp)
                )

                Text(
                    text = caption(string),
                    fontFamily = VigaFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(bottom = 14.dp)
                )

                if (string == "course" || string == "publication") {
                    Text(
                        if (string == "course") {
                            "This data came from a survey investigating the link between the single course unit of a study programme and the U.N. Sustainable Development Goals. The survey was conducted asking to all teachers the link for each their course units using a web procedure."
                        } else {
                            "Research regarding the number of publications was taken from the Scopus database, considering all articles since 2016 which contain a specific sequence of keywords and an author affiliated with University of Bologna."
                        },
                        fontFamily = VigaFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(15.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filterProjects.size) { index ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 100.dp)
                                    .clickable {
                                        navController.navigate("project/${filterProjects[index].id}/${item.id}")
                                    },
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(2.dp, Color(item.borderColor)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = filterProjects[index].name.ifEmpty { "Name" },
                                        fontFamily = VigaFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 25.sp,
                                        color = Color(item.borderColor)
                                    )
                                    Text(
                                        text = filterProjects[index].link.ifEmpty { "Link" },
                                        fontSize = 21.sp,
                                        fontFamily = VigaFontFamily
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
