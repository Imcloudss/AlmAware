package com.example.almaware.ui.screens.awards

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.almaware.R
import com.example.almaware.ui.composables.AppBar
import com.example.almaware.ui.composables.BottomNavigationBar
import com.example.almaware.ui.screens.sdg.VigaFontFamily
import com.example.almaware.ui.screens.sdg.student.StudentViewModel
import com.example.almaware.ui.screens.sdg.student.BadgeWithUserStatus
import org.koin.androidx.compose.koinViewModel

@Composable
fun AwardsScreen(
    navController: NavController,
    viewModel: StudentViewModel = koinViewModel()
) {
    var showDropdown by remember { mutableStateOf(false) }

    val filteredBadges by viewModel.filteredBadges.collectAsState()
    val currentFilter by viewModel.currentFilter.collectAsState()
    val allBadges by viewModel.badges.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadBadges()
    }

    Scaffold(
        topBar = {
            AppBar("Prove", navController)
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.group36),
                contentDescription = "Decorazione superiore sx",
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.TopStart),
                contentScale = ContentScale.Crop
            )

            Image(
                painter = painterResource(id = R.drawable.group37),
                contentDescription = "Decorazione superiore dx",
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.TopEnd),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Badges",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = VigaFontFamily,
                    modifier = Modifier.padding(top = 60.dp, bottom = 24.dp)
                )

                // Filtri
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    // Filtro "Completed"
                    FilterChip(
                        onClick = { viewModel.setCompletedFilter() },
                        label = {
                            Text(
                                "Completed",
                                color = if (viewModel.isCompletedSelected()) Color.White else Color.Black,
                                fontSize = 16.sp
                            )
                        },
                        selected = viewModel.isCompletedSelected(),
                        enabled = true,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color.Black,
                            containerColor = Color.White
                        )
                    )

                    // Filtro "To complete"
                    FilterChip(
                        onClick = { viewModel.setToCompleteFilter() },
                        label = {
                            Text(
                                "To complete",
                                color = if (viewModel.isToCompleteSelected()) Color.White else Color.Black,
                                fontSize = 16.sp
                            )
                        },
                        selected = viewModel.isToCompleteSelected(),
                        enabled = true,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color.Black,
                            containerColor = Color.White
                        )
                    )

                    Box {
                        FilterChip(
                            onClick = { showDropdown = !showDropdown },
                            label = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = if (viewModel.isSdgSelected()) {
                                            currentFilter.getDisplayText()
                                        } else {
                                            "SDGs"
                                        },
                                        color = if (viewModel.isSdgSelected()) Color.White else Color.Black,
                                        fontSize = 16.sp
                                    )
                                    Icon(
                                        Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Dropdown",
                                        tint = if (viewModel.isSdgSelected()) Color.White else Color.Black,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            },
                            selected = viewModel.isSdgSelected(),
                            enabled = true,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color.Black,
                                containerColor = Color.White
                            )
                        )

                        DropdownMenu(
                            expanded = showDropdown,
                            onDismissRequest = { showDropdown = false }
                        ) {
                            // Opzione per deselezionare tutti gli SDG
                            if (viewModel.isSdgSelected()) {
                                DropdownMenuItem(
                                    text = { Text("Tutti i badge") },
                                    onClick = {
                                        viewModel.clearFilter()
                                        showDropdown = false
                                    }
                                )
                            }
                            for (i in 1..17) {
                                DropdownMenuItem(
                                    text = { Text("SDG $i") },
                                    onClick = {
                                        viewModel.setSdgFilter(i)
                                        showDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filteredBadges.size) { index ->
                        val badgeWithStatus = filteredBadges[index]
                        // Calcola l'indice originale del badge nella lista completa
                        val originalIndex = allBadges.indexOf(badgeWithStatus.badge)

                        BadgeItem(
                            badgeWithStatus = badgeWithStatus,
                            onClick = {
                                // Gestisci click sul badge - puoi navigare o mostrare dettagli
                                println("Clicked badge: ${badgeWithStatus.badge.badgeName}")
                                // Opzionalmente, puoi anche toggleare lo stato qui:
//                                if (originalIndex >= 0) {
//                                    viewModel.toggleBadgeCompletion(originalIndex)
//                                }
                            }
                        )
                    }
                }

                // Mostra numero di badge trovati
                if (filteredBadges.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "${filteredBadges.size} badge${if (filteredBadges.size != 1) "s" else ""} found",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun BadgeItem(
    badgeWithStatus: BadgeWithUserStatus,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(90.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        val badge = badgeWithStatus.badge
        // Usa lo stato reale dell'utente invece del campo checkbox del badge
        val imageUrl = if (badgeWithStatus.isCompleted) badge.validate else badge.unvalidate

        AsyncImage(
            model = imageUrl,
            contentDescription = badge.badgeName,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}