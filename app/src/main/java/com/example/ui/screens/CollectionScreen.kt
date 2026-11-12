package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.AmuletScanResult
import com.example.data.model.InspectionPoint
import com.example.data.model.ScanRecordEntity
import com.example.ui.components.CertificateDialog
import com.example.ui.theme.AntiqueGoldBright
import com.example.ui.theme.AntiqueGoldDark
import com.example.ui.theme.AntiqueGoldPrimary
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldBorderBright
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.PatinaBronze
import com.example.ui.theme.PatinaVerdigris
import com.example.ui.theme.TextPrimaryGold
import com.example.ui.theme.TextSecondaryMuted
import com.example.ui.viewmodel.AmuletViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CollectionScreen(
    viewModel: AmuletViewModel,
    onNavigateToScan: () -> Unit,
    modifier: Modifier = Modifier
) {
    val records by viewModel.savedRecords.collectAsState()
    var selectedRecordForCert by remember { mutableStateOf<ScanRecordEntity?>(null) }

    if (selectedRecordForCert != null) {
        val rec = selectedRecordForCert!!
        val dummyResult = AmuletScanResult(
            id = rec.id,
            amuletName = rec.amuletName,
            templeAndMaker = rec.templeAndMaker,
            estimatedEra = rec.estimatedEra,
            alloyClassification = rec.alloyClassification,
            castingMethod = rec.castingMethod,
            overallAuthenticityScore = rec.overallAuthenticityScore,
            conditionGrade = rec.conditionGrade,
            metalAgingScore = rec.metalAgingScore,
            clayResidueScore = rec.clayResidueScore,
            filingMarksScore = rec.filingMarksScore,
            sprueCutScore = rec.sprueCutScore,
            shrinkageScore = rec.shrinkageScore,
            keyInspectionPoints = listOf(
                InspectionPoint("วรรณะเนื้อโลหะ", "ทั่วองค์", true, rec.metalAgingScore, "เนื้อหาถึงยุคตามมาตรฐาน"),
                InspectionPoint("คราบเบ้าดินไทย", "ตามซอกลึก", true, rec.clayResidueScore, "คราบเบ้าธรรมชาติเกาะแน่น")
            ),
            estimatedMarketValue = rec.estimatedMarketValue,
            conclusionSummary = rec.conclusionSummary,
            expertAdvice = "เก็บรักษาในสภาพเดิมเพื่อรักษามูลค่า",
            certificateCode = rec.certificateCode,
            frontImageUri = rec.frontImageUri,
            timestamp = rec.timestamp
        )
        CertificateDialog(
            result = dummyResult,
            onDismiss = { selectedRecordForCert = null }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .padding(16.dp)
            .testTag("collection_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = null,
                        tint = AntiqueGoldBright,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "รังพระของฉัน & ใบรับรอง AI",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = AntiqueGoldBright
                    )
                }
                Text(
                    text = "ประวัติการสแกน บันทึกพระเครื่อง และใบเซอร์ดิจิทัล",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = TextSecondaryMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Stats summary bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(
                title = "พระในรัง",
                value = "${records.size} องค์",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "แท้ระดับแชมป์",
                value = "${records.count { it.overallAuthenticityScore >= 90 }} องค์",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "ออกใบเซอร์แล้ว",
                value = "${records.size} ใบ",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (records.isEmpty()) {
            // Empty State
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = AntiqueGoldPrimary,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "ยังไม่มีพระเครื่องที่บันทึกไว้",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimaryGold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "เริ่มต้นสแกนพระหล่อโบราณเพื่อบันทึกประวัติและสร้างใบรับรอง AI ดิจิทัล",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = TextSecondaryMuted,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onNavigateToScan,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AntiqueGoldPrimary,
                            contentColor = ObsidianBg
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "สแกนพระองค์แรก",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(records, key = { it.id }) { record ->
                    SavedRecordCard(
                        record = record,
                        onViewCert = { selectedRecordForCert = record },
                        onToggleFavorite = { viewModel.toggleRecordFavorite(record) },
                        onDelete = { viewModel.deleteScanRecord(record.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                ),
                color = AntiqueGoldBright
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                color = TextSecondaryMuted
            )
        }
    }
}

@Composable
private fun SavedRecordCard(
    record: ScanRecordEntity,
    onViewCert: () -> Unit,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit
) {
    val dateStr = remember(record.timestamp) {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        sdf.format(Date(record.timestamp))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("saved_record_${record.id}"),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Amulet photo
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, GoldBorderBright, RoundedCornerShape(8.dp))
                ) {
                    if (record.frontImageUri != null) {
                        AsyncImage(
                            model = record.frontImageUri,
                            contentDescription = record.amuletName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_app_icon_src),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = record.amuletName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        ),
                        color = TextPrimaryGold
                    )
                    Text(
                        text = record.templeAndMaker,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = TextSecondaryMuted
                    )
                    Text(
                        text = "สแกนเมื่อ: $dateStr",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        color = AntiqueGoldDark
                    )
                }

                // Authenticity Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(PatinaBronze.copy(alpha = 0.3f))
                        .border(0.5.dp, PatinaVerdigris, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${record.overallAuthenticityScore}%",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        ),
                        color = PatinaVerdigris
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onViewCert,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AntiqueGoldBright),
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, AntiqueGoldPrimary),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "เปิดใบรับรอง (${record.certificateCode.take(12)}...)",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    )
                }

                Row {
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (record.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "รายการโปรด",
                            tint = if (record.isFavorite) AntiqueGoldBright else TextSecondaryMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "ลบ",
                            tint = TextSecondaryMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
