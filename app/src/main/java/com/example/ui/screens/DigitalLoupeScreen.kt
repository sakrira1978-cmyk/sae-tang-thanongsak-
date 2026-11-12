package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Loupe
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.repository.MasterAmuletCatalog
import com.example.ui.theme.AntiqueGoldBright
import com.example.ui.theme.AntiqueGoldPrimary
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldBorderBright
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.PatinaBronze
import com.example.ui.theme.PatinaVerdigris
import com.example.ui.theme.TextPrimaryGold
import com.example.ui.theme.TextSecondaryMuted
import com.example.ui.viewmodel.AmuletViewModel

@Composable
fun DigitalLoupeScreen(
    viewModel: AmuletViewModel,
    modifier: Modifier = Modifier
) {
    val frontUri by viewModel.frontImageUri.collectAsState()
    val zoomLevel by viewModel.loupeZoom.collectAsState()
    val activeFilter by viewModel.loupeFilter.collectAsState()
    val compareMaster by viewModel.compareMaster.collectAsState()

    var isCompareMode by remember { mutableStateOf(false) }

    // Pan & Zoom transformations
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val transformState = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale = (scale * zoomChange).coerceIn(1f, 8f)
        offset += offsetChange
    }

    // Color matrix based on selected inspection filter
    val colorFilter = when (activeFilter) {
        "UV" -> {
            // Emphasize blue/violet and high contrast to expose fake paint & real clay
            val matrix = ColorMatrix().apply {
                setToSaturation(0.6f)
                timesAssign(
                    ColorMatrix(
                        floatArrayOf(
                            0.7f, 0f, 0.4f, 0f, 20f,
                            0f, 0.9f, 0.3f, 0f, 20f,
                            0.2f, 0.4f, 1.8f, 0f, 50f,
                            0f, 0f, 0f, 1f, 0f
                        )
                    )
                )
            }
            ColorFilter.colorMatrix(matrix)
        }
        "VERDIGRIS" -> {
            // Emphasize green/jade verdigris patina and red rust
            val matrix = ColorMatrix().apply {
                setToSaturation(1.6f)
                timesAssign(
                    ColorMatrix(
                        floatArrayOf(
                            1.2f, 0f, 0f, 0f, 10f,
                            0f, 1.5f, 0f, 0f, 15f,
                            0f, 0.4f, 0.8f, 0f, 0f,
                            0f, 0f, 0f, 1f, 0f
                        )
                    )
                )
            }
            ColorFilter.colorMatrix(matrix)
        }
        "CONTRAST" -> {
            // High contrast to highlight filing teeth marks and sprue edges
            val matrix = ColorMatrix().apply {
                setToSaturation(0.2f) // semi monochrome for metallurgical edge detection
            }
            ColorFilter.colorMatrix(matrix)
        }
        "BRONZE" -> {
            // Warm golden brass/bronze highlight
            val matrix = ColorMatrix().apply {
                setToSaturation(1.3f)
                timesAssign(
                    ColorMatrix(
                        floatArrayOf(
                            1.4f, 0f, 0f, 0f, 25f,
                            0f, 1.2f, 0f, 0f, 15f,
                            0f, 0f, 0.5f, 0f, 0f,
                            0f, 0f, 0f, 1f, 0f
                        )
                    )
                )
            }
            ColorFilter.colorMatrix(matrix)
        }
        else -> null
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("digital_loupe_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Loupe,
                        contentDescription = null,
                        tint = AntiqueGoldBright,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "กล้องส่องพระดิจิทัล (10x - 50x)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = AntiqueGoldBright
                    )
                }
                Text(
                    text = "ระบบส่องเจาะลึกเนื้อโลหะ คราบเบ้า และรอยตะไบโบราณ",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = TextSecondaryMuted
                )
            }

            // Toggle compare mode
            IconButton(
                onClick = { isCompareMode = !isCompareMode },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isCompareMode) AntiqueGoldPrimary else DarkSurfaceElevated)
                    .testTag("toggle_compare_mode_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Compare,
                    contentDescription = "เทียบกับองค์ครู",
                    tint = if (isCompareMode) ObsidianBg else AntiqueGoldBright
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Digital Loupe Viewport Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.Black)
                .border(2.dp, GoldBorderBright, RoundedCornerShape(14.dp))
        ) {
            if (isCompareMode) {
                // Split comparison screen
                Row(modifier = Modifier.fillMaxSize()) {
                    // Left: User Amulet
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .border(0.5.dp, GoldBorder)
                    ) {
                        AmuletViewport(
                            imageUri = frontUri,
                            scale = scale,
                            offset = offset,
                            colorFilter = colorFilter,
                            modifier = Modifier
                                .fillMaxSize()
                                .transformable(state = transformState)
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(6.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(ObsidianBg.copy(alpha = 0.8f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "พระของท่าน",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                color = AntiqueGoldBright
                            )
                        }
                    }

                    // Right: Master Amulet (พระองค์ครู)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .border(0.5.dp, GoldBorder)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_app_icon_src),
                            contentDescription = "พระองค์ครู",
                            contentScale = ContentScale.Crop,
                            colorFilter = colorFilter,
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer(
                                    scaleX = scale,
                                    scaleY = scale,
                                    translationX = offset.x,
                                    translationY = offset.y
                                )
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(6.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(PatinaBronze.copy(alpha = 0.85f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "องค์ครู (แท้ระดับแชมป์)",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                color = TextPrimaryGold
                            )
                        }
                    }
                }
            } else {
                // Full single loupe viewport
                AmuletViewport(
                    imageUri = frontUri,
                    scale = scale,
                    offset = offset,
                    colorFilter = colorFilter,
                    modifier = Modifier
                        .fillMaxSize()
                        .transformable(state = transformState)
                )
            }

            // Loupe Reticle Overlay & Zoom Badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(ObsidianBg.copy(alpha = 0.85f))
                    .border(0.5.dp, AntiqueGoldPrimary, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${(scale * zoomLevel).toInt()}X MAGNIFIED",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    ),
                    color = AntiqueGoldBright
                )
            }

            // Reset zoom button
            IconButton(
                onClick = {
                    scale = 1f
                    offset = Offset.Zero
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(ObsidianBg.copy(alpha = 0.75f))
            ) {
                Icon(
                    imageVector = Icons.Default.RestartAlt,
                    contentDescription = "รีเซ็ตการซูม",
                    tint = TextPrimaryGold,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Magnification Preset Buttons
        Text(
            text = "ระดับกำลังขยายเลนส์:",
            style = MaterialTheme.typography.labelMedium,
            color = TextPrimaryGold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val zooms = listOf(10f, 20f, 30f, 40f, 50f)
            zooms.forEach { z ->
                val isSelected = zoomLevel == z
                Button(
                    onClick = { viewModel.setLoupeZoom(z) },
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) AntiqueGoldPrimary else DarkSurfaceElevated,
                        contentColor = if (isSelected) ObsidianBg else TextPrimaryGold
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${z.toInt()}x",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Optical Inspection Filters
        Text(
            text = "ฟิลเตอร์แสงส่องเนื้อพระทางวิทยาศาสตร์:",
            style = MaterialTheme.typography.labelMedium,
            color = TextPrimaryGold
        )
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val filters = listOf(
                Pair("NORMAL", "แสงธรรมชาติ (Normal)"),
                Pair("UV", "ส่องคราบเบ้า (UV Light)"),
                Pair("VERDIGRIS", "ขับสนิมเขียว (Patina)"),
                Pair("CONTRAST", "รอยแทงตะไบ (Contrast)"),
                Pair("BRONZE", "พรายทองคำ (Gold Glow)")
            )
            items(filters) { (key, label) ->
                val isSelected = activeFilter == key
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.setLoupeFilter(key) },
                    label = {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PatinaBronze,
                        selectedLabelColor = TextPrimaryGold,
                        containerColor = DarkSurfaceElevated,
                        labelColor = TextSecondaryMuted
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isSelected) PatinaVerdigris else GoldBorder,
                        enabled = true,
                        selected = isSelected
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Compare piece chooser
        if (isCompareMode) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "เลือกพระองค์ครูสำหรับเปรียบเทียบ:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = AntiqueGoldBright
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(MasterAmuletCatalog.masterAmulets) { amulet ->
                            val isSelected = compareMaster?.id == amulet.id
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) AntiqueGoldPrimary.copy(alpha = 0.2f) else DarkSurfaceElevated)
                                    .border(1.dp, if (isSelected) AntiqueGoldBright else GoldBorder, RoundedCornerShape(8.dp))
                                    .clickable { viewModel.setCompareMaster(amulet) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = amulet.name,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = if (isSelected) AntiqueGoldBright else TextPrimaryGold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    compareMaster?.let { master ->
                        Text(
                            text = "จุดชี้ขาดของ ${master.name}:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = PatinaVerdigris
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        master.keyPoints.take(3).forEach { point ->
                            Text(
                                text = "• $point",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = TextSecondaryMuted,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AmuletViewport(
    imageUri: android.net.Uri?,
    scale: Float,
    offset: Offset,
    colorFilter: ColorFilter?,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "พระหล่อขยายสูง",
                contentScale = ContentScale.Crop,
                colorFilter = colorFilter,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_app_icon_src),
                contentDescription = "ภาพตัวอย่างพระหล่อโบราณ",
                contentScale = ContentScale.Crop,
                colorFilter = colorFilter,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
            )
        }
    }
}
