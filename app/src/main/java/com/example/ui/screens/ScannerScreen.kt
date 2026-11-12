package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Loupe
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.ui.components.AiNoticeHeader
import com.example.ui.components.CertificateDialog
import com.example.ui.components.PillarScoreBar
import com.example.ui.components.ScannerHudOverlay
import com.example.ui.components.ScoreGauge
import com.example.ui.theme.AntiqueGoldBright
import com.example.ui.theme.AntiqueGoldDark
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
fun ScannerScreen(
    viewModel: AmuletViewModel,
    onNavigateToLoupe: () -> Unit,
    modifier: Modifier = Modifier
) {
    val frontUri by viewModel.frontImageUri.collectAsState()
    val backUri by viewModel.backImageUri.collectAsState()
    val baseUri by viewModel.baseImageUri.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val stageText by viewModel.analysisStageText.collectAsState()
    val progress by viewModel.scanProgress.collectAsState()
    val scanResult by viewModel.scanResult.collectAsState()
    val selectedArchetype by viewModel.selectedArchetype.collectAsState()
    val showCert by viewModel.showCertificate.collectAsState()

    // Photo Pickers
    var activePhotoSlot by remember { mutableStateOf("front") }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            when (activePhotoSlot) {
                "front" -> viewModel.setFrontImage(uri)
                "back" -> viewModel.setBackImage(uri)
                "base" -> viewModel.setBaseImage(uri)
            }
        }
    }

    if (showCert && scanResult != null) {
        CertificateDialog(
            result = scanResult!!,
            onDismiss = { viewModel.setShowCertificate(false) }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("scanner_screen")
    ) {
        // App Header Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, GoldBorderBright, RoundedCornerShape(14.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_phralor),
                contentDescription = "พระหล่อโบราณยอดนิยม",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                ObsidianBg.copy(alpha = 0.92f),
                                ObsidianBg.copy(alpha = 0.75f),
                                Color.Transparent
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = AntiqueGoldBright,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ระบบ AI ตรวจสอบพระหล่อโบราณ",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        ),
                        color = AntiqueGoldBright
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "วิเคราะห์ 5 เสาหลัก: เนื้อสัมฤทธิ์ทองผสม คราบเบ้าดินไทย รอยตะไบ รอยตัดชนวน และพิมพ์ทรง",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = TextPrimaryGold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        AiNoticeHeader()

        Spacer(modifier = Modifier.height(14.dp))

        // Archetype quick chooser
        Text(
            text = "เลือกเทียบเคียงพระองค์ครูยอดนิยม:",
            style = MaterialTheme.typography.labelMedium,
            color = TextPrimaryGold
        )
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val archetypes = listOf(
                "หลวงพ่อเงิน วัดบางคลาน",
                "พระพุทธชินราช อินโดจีน",
                "พระกริ่งปวเรศ วัดบวร",
                "หลวงพ่อไปล่ วัดกำแพง",
                "หลวงพ่อทา วัดพะเนียงแตก",
                "หลวงพ่อเงิน พิมพ์ขี้ตา",
                "หลวงพ่อโบ้ย วัดมะนาว"
            )
            items(archetypes) { item ->
                val isSelected = selectedArchetype == item
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.setSelectedArchetype(item) },
                    label = {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AntiqueGoldPrimary,
                        selectedLabelColor = ObsidianBg,
                        containerColor = DarkSurfaceElevated,
                        labelColor = TextPrimaryGold
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isSelected) AntiqueGoldBright else GoldBorder,
                        enabled = true,
                        selected = isSelected
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Multi-Angle Photo Capture Cards
        Text(
            text = "ถ่ายภาพหรือเลือกรูปพระเครื่อง (ครบ 3 มุมยิ่งแม่นยำ):",
            style = MaterialTheme.typography.labelMedium,
            color = TextPrimaryGold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Angle 1: Front (ด้านหน้า)
            PhotoSlotCard(
                label = "ด้านหน้า (จำเป็น)",
                imageUri = frontUri,
                isScanning = isAnalyzing,
                onPickPhoto = {
                    activePhotoSlot = "front"
                    photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                onClear = { viewModel.setFrontImage(null) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("front_photo_slot")
            )

            // Angle 2: Back (ด้านหลัง)
            PhotoSlotCard(
                label = "ด้านหลัง",
                imageUri = backUri,
                isScanning = isAnalyzing,
                onPickPhoto = {
                    activePhotoSlot = "back"
                    photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                onClear = { viewModel.setBackImage(null) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("back_photo_slot")
            )

            // Angle 3: Base (ก้น/รอยตัดชนวน)
            PhotoSlotCard(
                label = "ใต้ฐาน/รอยตัด",
                imageUri = baseUri,
                isScanning = isAnalyzing,
                onPickPhoto = {
                    activePhotoSlot = "base"
                    photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                onClear = { viewModel.setBaseImage(null) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("base_photo_slot")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Scan Action Button
        Button(
            onClick = { viewModel.startAmuletScan() },
            enabled = !isAnalyzing,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("start_scan_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = AntiqueGoldPrimary,
                contentColor = ObsidianBg
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isAnalyzing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = ObsidianBg,
                    strokeWidth = 2.5.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "ระบบ AI กำลังประมวลผล...",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "เริ่มการสแกนด้วยระบบ AI",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp)
                )
            }
        }

        // Live Scanning Progress Indicator
        AnimatedVisibility(visible = isAnalyzing) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = AntiqueGoldPrimary,
                    trackColor = DarkSurfaceElevated
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stageText,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = PatinaVerdigris,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Analysis Results Card
        AnimatedVisibility(
            visible = scanResult != null && !isAnalyzing,
            enter = fadeIn() + expandVertically()
        ) {
            scanResult?.let { result ->
                Column(modifier = Modifier.padding(top = 20.dp)) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("scan_result_card"),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldBorderBright),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            // Header badge
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = null,
                                        tint = AntiqueGoldBright,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "ผลการตรวจวิเคราะห์ระดับลึก",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = AntiqueGoldBright
                                    )
                                }
                                Text(
                                    text = result.certificateCode,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondaryMuted
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Divider(color = GoldBorder, thickness = 0.5.dp)
                            Spacer(modifier = Modifier.height(14.dp))

                            // Score gauge & Identity Summary
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ScoreGauge(
                                    score = result.overallAuthenticityScore,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1.3f)) {
                                    Text(
                                        text = result.amuletName,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        ),
                                        color = TextPrimaryGold
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = result.templeAndMaker,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                        color = TextSecondaryMuted
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "ยุค: ${result.estimatedEra}",
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                        color = AntiqueGoldPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "ประเมินมูลค่า: ${result.estimatedMarketValue}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = PatinaVerdigris
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // 5 Pillars Breakdown
                            Text(
                                text = "คะแนนประเมิน 5 เสาหลักพระหล่อโบราณ:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = AntiqueGoldPrimary
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            PillarScoreBar(title = "1. เนื้อโลหะ & ความเก่าสนิมขุม", score = result.metalAgingScore)
                            Spacer(modifier = Modifier.height(8.dp))
                            PillarScoreBar(title = "2. คราบเบ้าดินไทย & เม็ดผดตามด", score = result.clayResidueScore)
                            Spacer(modifier = Modifier.height(8.dp))
                            PillarScoreBar(title = "3. รอยแทงตะไบโบราณแต่งข้าง", score = result.filingMarksScore)
                            Spacer(modifier = Modifier.height(8.dp))
                            PillarScoreBar(title = "4. รอยตัดชนวน & ก้นองค์พระ", score = result.sprueCutScore)
                            Spacer(modifier = Modifier.height(8.dp))
                            PillarScoreBar(title = "5. พิมพ์ทรง & ธรรมชาติการยุบตัว", score = result.shrinkageScore)

                            Spacer(modifier = Modifier.height(18.dp))
                            Divider(color = GoldBorder, thickness = 0.5.dp)
                            Spacer(modifier = Modifier.height(14.dp))

                            // Key Inspection Points (จุดชี้ขาด)
                            Text(
                                text = "จุดชี้ขาดและตำหนิสำคัญที่ตรวจพบ (${result.keyInspectionPoints.size} จุด):",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimaryGold
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            result.keyInspectionPoints.forEach { point ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(DarkSurfaceElevated)
                                        .border(0.5.dp, GoldBorder, RoundedCornerShape(8.dp))
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = PatinaVerdigris,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = point.title,
                                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                                    color = TextPrimaryGold
                                                )
                                            }
                                            Text(
                                                text = "${point.score}%",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = AntiqueGoldBright
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "ตำแหน่ง: ${point.location}",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                            color = AntiqueGoldDark
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = point.explanation,
                                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                            color = TextSecondaryMuted
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Expert Advice
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DarkSurfaceVariant)
                                    .padding(12.dp)
                            ) {
                                Row {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = AntiqueGoldPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "คำแนะนำจากเซียนพระ:",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = AntiqueGoldPrimary
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = result.expertAdvice,
                                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                            color = TextSecondaryMuted
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // Action Buttons Row: Certificate & Save
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { viewModel.setShowCertificate(true) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("view_cert_button"),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = AntiqueGoldPrimary
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, AntiqueGoldPrimary),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "ใบรับรอง AI",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }

                                Button(
                                    onClick = { viewModel.saveCurrentScanToCollection() },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("save_collection_button"),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PatinaBronze,
                                        contentColor = TextPrimaryGold
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.BookmarkAdd,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "บันทึกในรัง",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Go to Digital Loupe Inspection
                            Button(
                                onClick = onNavigateToLoupe,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("navigate_loupe_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DarkSurfaceElevated,
                                    contentColor = AntiqueGoldBright
                                ),
                                border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorderBright),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Loupe,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "ส่องกล้องขยาย 40x และเทียบกับองค์ครู",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PhotoSlotCard(
    label: String,
    imageUri: Uri?,
    isScanning: Boolean,
    onPickPhoto: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(0.85f)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onPickPhoto),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
        border = androidx.compose.foundation.BorderStroke(
            width = if (imageUri != null) 1.5.dp else 1.dp,
            color = if (imageUri != null) AntiqueGoldPrimary else GoldBorder
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = label,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Clear button
                IconButton(
                    onClick = onClear,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(24.dp)
                        .background(ObsidianBg.copy(alpha = 0.7f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "ลบรูป",
                        tint = TextPrimaryGold,
                        modifier = Modifier.size(14.dp)
                    )
                }
            } else {
                // Default slot when no user photo: show sample antique amulet watermark
                Image(
                    painter = painterResource(id = R.drawable.ic_app_icon_src),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alpha = 0.35f,
                    modifier = Modifier.fillMaxSize()
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(AntiqueGoldPrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = "เพิ่มรูป",
                            tint = AntiqueGoldBright,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = TextPrimaryGold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "แตะเพื่อเลือกรูป",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                        color = TextSecondaryMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // HUD targeting lines overlay
            ScannerHudOverlay(
                isScanning = isScanning && imageUri != null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
