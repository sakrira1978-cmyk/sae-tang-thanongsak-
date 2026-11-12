package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Loupe
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.AiConsultSheet
import com.example.ui.screens.CollectionScreen
import com.example.ui.screens.DigitalLoupeScreen
import com.example.ui.screens.InspectionGuideScreen
import com.example.ui.screens.MasterEncyclopediaScreen
import com.example.ui.screens.ScannerScreen
import com.example.ui.theme.AntiqueGoldBright
import com.example.ui.theme.AntiqueGoldPrimary
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldBorderBright
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.PatinaVerdigris
import com.example.ui.theme.TextPrimaryGold
import com.example.ui.theme.TextSecondaryMuted
import com.example.ui.viewmodel.AmuletViewModel

enum class MainTab(val title: String) {
    SCANNER("สแกนพระ"),
    LOUPE("กล้องส่อง 50x"),
    ENCYCLOPEDIA("พระองค์ครู"),
    GUIDE("5 เสาหลัก"),
    COLLECTION("รังพระ")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppRoot()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppRoot(viewModel: AmuletViewModel = viewModel()) {
    var currentTab by remember { mutableStateOf(MainTab.SCANNER) }
    var showAiChatSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val toastMessage by viewModel.toastMessage.collectAsState()

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearToastMessage()
        }
    }

    // BackHandler: Return to scanner tab if on secondary tab
    BackHandler(enabled = currentTab != MainTab.SCANNER) {
        currentTab = MainTab.SCANNER
    }

    if (showAiChatSheet) {
        AiConsultSheet(
            viewModel = viewModel,
            onDismiss = { showAiChatSheet = false }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(AntiqueGoldPrimary.copy(alpha = 0.2f))
                                .border(1.dp, AntiqueGoldPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = AntiqueGoldBright,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "สแกนพระหล่อโบราณ AI",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = AntiqueGoldBright
                        )
                    }
                },
                actions = {
                    // Button to open AI specialist chat
                    IconButton(
                        onClick = { showAiChatSheet = true },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceElevated)
                            .border(1.dp, GoldBorderBright, CircleShape)
                            .testTag("open_ai_consult_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "ปรึกษาเซียนพระ AI",
                            tint = AntiqueGoldBright,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DarkSurface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                tonalElevation = 6.dp,
                modifier = Modifier.border(
                    width = 0.5.dp,
                    color = GoldBorder,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
            ) {
                NavigationBarItem(
                    selected = currentTab == MainTab.SCANNER,
                    onClick = { currentTab = MainTab.SCANNER },
                    icon = { Icon(Icons.Default.CameraAlt, contentDescription = "สแกนพระ") },
                    label = { Text("สแกนพระ", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ObsidianBg,
                        selectedTextColor = AntiqueGoldBright,
                        indicatorColor = AntiqueGoldPrimary,
                        unselectedIconColor = TextSecondaryMuted,
                        unselectedTextColor = TextSecondaryMuted
                    ),
                    modifier = Modifier.testTag("nav_scanner")
                )
                NavigationBarItem(
                    selected = currentTab == MainTab.LOUPE,
                    onClick = { currentTab = MainTab.LOUPE },
                    icon = { Icon(Icons.Default.Loupe, contentDescription = "กล้องส่อง 50x") },
                    label = { Text("กล้องส่อง", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ObsidianBg,
                        selectedTextColor = AntiqueGoldBright,
                        indicatorColor = AntiqueGoldPrimary,
                        unselectedIconColor = TextSecondaryMuted,
                        unselectedTextColor = TextSecondaryMuted
                    ),
                    modifier = Modifier.testTag("nav_loupe")
                )
                NavigationBarItem(
                    selected = currentTab == MainTab.ENCYCLOPEDIA,
                    onClick = { currentTab = MainTab.ENCYCLOPEDIA },
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = "พระองค์ครู") },
                    label = { Text("องค์ครู", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ObsidianBg,
                        selectedTextColor = AntiqueGoldBright,
                        indicatorColor = AntiqueGoldPrimary,
                        unselectedIconColor = TextSecondaryMuted,
                        unselectedTextColor = TextSecondaryMuted
                    ),
                    modifier = Modifier.testTag("nav_encyclopedia")
                )
                NavigationBarItem(
                    selected = currentTab == MainTab.GUIDE,
                    onClick = { currentTab = MainTab.GUIDE },
                    icon = { Icon(Icons.Default.School, contentDescription = "5 เสาหลัก") },
                    label = { Text("5 เสาหลัก", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ObsidianBg,
                        selectedTextColor = AntiqueGoldBright,
                        indicatorColor = AntiqueGoldPrimary,
                        unselectedIconColor = TextSecondaryMuted,
                        unselectedTextColor = TextSecondaryMuted
                    ),
                    modifier = Modifier.testTag("nav_guide")
                )
                NavigationBarItem(
                    selected = currentTab == MainTab.COLLECTION,
                    onClick = { currentTab = MainTab.COLLECTION },
                    icon = { Icon(Icons.Default.Bookmark, contentDescription = "รังพระ") },
                    label = { Text("รังพระ", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ObsidianBg,
                        selectedTextColor = AntiqueGoldBright,
                        indicatorColor = AntiqueGoldPrimary,
                        unselectedIconColor = TextSecondaryMuted,
                        unselectedTextColor = TextSecondaryMuted
                    ),
                    modifier = Modifier.testTag("nav_collection")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                MainTab.SCANNER -> ScannerScreen(
                    viewModel = viewModel,
                    onNavigateToLoupe = { currentTab = MainTab.LOUPE }
                )
                MainTab.LOUPE -> DigitalLoupeScreen(
                    viewModel = viewModel
                )
                MainTab.ENCYCLOPEDIA -> MasterEncyclopediaScreen(
                    onSelectForCompare = { master ->
                        viewModel.setCompareMaster(master)
                        currentTab = MainTab.LOUPE
                    }
                )
                MainTab.GUIDE -> InspectionGuideScreen()
                MainTab.COLLECTION -> CollectionScreen(
                    viewModel = viewModel,
                    onNavigateToScan = { currentTab = MainTab.SCANNER }
                )
            }
        }
    }
}
