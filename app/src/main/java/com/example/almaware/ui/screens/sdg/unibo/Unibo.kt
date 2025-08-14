package com.example.almaware.ui.screens.sdg.unibo

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.almaware.R
import com.example.almaware.data.model.HomeCard
import com.example.almaware.ui.composables.AppBar
import com.example.almaware.ui.composables.BottomNavigationBar
import com.example.almaware.ui.screens.sdg.SdgViewModel
import com.example.almaware.utils.getDrawableForSdg
import org.koin.androidx.compose.koinViewModel

val VigaFontFamily = FontFamily(
    Font(R.font.viga)
)

@Composable
fun UniboScreen(
    navController: NavController,
    item: HomeCard,
    sdgViewModel: SdgViewModel = koinViewModel(),
    uniboViewModel: UniboViewModel = koinViewModel()
) {
    val sdg = sdgViewModel.sdg.value
    val projectCount by uniboViewModel.projectCount.observeAsState(0)

    LaunchedEffect(Unit) {
        sdgViewModel.loadSdgById(item.id)
        uniboViewModel.loadProjectCountForSdg(item.id.toString())
    }

    Scaffold(
        topBar = {
            AppBar("Prove", navController)
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = painterResource(getDrawableForSdg(item.id)),
                    contentDescription = "Bubble${sdg?.id?.plus(1)}",
                    alignment = Alignment.TopStart,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .offset(
                            x = 45.dp,
                            y = (-60).dp
                        )
                        .size(150.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.group2),
                    contentDescription = "Decorazione superiore",
                    alignment = Alignment.TopStart,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(200.dp)
                        .offset(x = (-145).dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "What Unibo does?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    fontFamily = VigaFontFamily,
                    letterSpacing = 1.sp
                )

                Text(
                    text = "Discover what Unibo do since 2016. If you want to see more about the number, tap on the case",
                    fontSize = 18.sp,
                    fontFamily = VigaFontFamily,
                    modifier = Modifier.padding(
                        start = 24.dp,
                        end = 24.dp,
                        bottom = 32.dp
                    ),
                    textAlign = TextAlign.Center
                )

                // Prima riga
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Card Course Units
                    DashboardCard(
                        modifier = Modifier
                            .weight(1f),
                        iconRes = R.drawable.courseunits,
                        value = sdg?.course_units.toString(),
                        label = "course units",
                        onClick = {  },
                        item = item
                    )

                    // Card Publications
                    DashboardCard(
                        modifier = Modifier
                            .weight(1f),
                        iconRes = R.drawable.publication,
                        value = sdg?.publications_unibo.toString(),
                        label = "publications",
                        onClick = {  },
                        item = item
                    )
                }

                // Seconda riga
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Card Project
                    DashboardCard(
                        modifier = Modifier
                            .weight(1f),
                        iconRes = R.drawable.project,
                        value = projectCount.toString(),
                        label = if (projectCount == 1) "project" else "projects",
                        onClick = {  },
                        item = item
                    )

                    Log.d("Projects", "Project count for SDG ${item.id}: $projectCount")

                    // Card Water Consumption
                    DashboardCard(
                        modifier = Modifier
                            .weight(1f),
                        iconRes = R.drawable.key,
                        value = "TODO",
                        label = "of water\nconsumption",
                        onClick = {  },
                        item = item
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardCard(
    modifier: Modifier = Modifier,
    iconRes: Int,
    value: String,
    label: String,
    item: HomeCard,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .height(150.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(item.borderColor)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Icona info in alto a destra
            Image(
                painter = painterResource(id = R.drawable.info),
                contentDescription = "Info",
                modifier = Modifier
                    .size(18.dp)
                    .align(Alignment.TopEnd)
                    .clickable {  },
                colorFilter = ColorFilter.tint(Color.White)
            )

            // Contenuto centrale
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Icona principale
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Valore
                Text(
                    text = value,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = VigaFontFamily
                )

                // Label
                Text(
                    text = label,
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    fontFamily = VigaFontFamily
                )
            }
        }
    }
}