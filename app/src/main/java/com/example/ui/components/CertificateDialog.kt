package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AmuletScanResult
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

@Composable
fun CertificateDialog(
    result: AmuletScanResult,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 24.dp)
                .testTag("certificate_dialog"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(2.dp, GoldBorderBright)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top close action
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CERTIFICATE OF INSPECTION",
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.sp),
                        color = AntiqueGoldPrimary
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("close_cert_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "ปิด",
                            tint = TextSecondaryMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Certificate Paper Container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF24201A),
                                    Color(0xFF1B1916),
                                    Color(0xFF221E19)
                                )
                            )
                        )
                        .border(1.5.dp, GoldBorderBright, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Emblem Icon
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(AntiqueGoldPrimary.copy(alpha = 0.15f))
                                .border(1.dp, AntiqueGoldPrimary, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "ตราประทับรับรอง",
                                tint = AntiqueGoldBright,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "ใบรับรองผลการตรวจวิเคราะห์พระเครื่อง",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = AntiqueGoldBright,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "PHRA LOR BORAN AI AUTHENTICITY CERTIFICATE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                letterSpacing = 1.sp
                            ),
                            color = TextSecondaryMuted,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "รหัสใบรับรอง: ${result.certificateCode}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = PatinaVerdigris
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = GoldBorder, thickness = 0.5.dp)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Details table
                        CertificateRow(label = "ชื่อพระเครื่อง:", value = result.amuletName)
                        CertificateRow(label = "วัด / ผู้สร้าง:", value = result.templeAndMaker)
                        CertificateRow(label = "ยุคสมัย / อายุ:", value = result.estimatedEra)
                        CertificateRow(label = "ประเภทเนื้อหา:", value = result.alloyClassification)
                        CertificateRow(label = "กรรมวิธีการหล่อ:", value = result.castingMethod)
                        CertificateRow(label = "สภาพพระ:", value = result.conditionGrade)
                        CertificateRow(label = "คะแนน 5 เสาหลัก:", value = "${result.overallAuthenticityScore}% (ผ่านเกณฑ์มาตรฐานสากล)")
                        CertificateRow(label = "ประเมินมูลค่า:", value = result.estimatedMarketValue)

                        Spacer(modifier = Modifier.height(14.dp))
                        Divider(color = GoldBorder, thickness = 0.5.dp)
                        Spacer(modifier = Modifier.height(14.dp))

                        // Bottom Signature & QR Seal Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // QR Mockup
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color.White)
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.QrCode2,
                                        contentDescription = "QR ตรวจสอบ",
                                        tint = Color.Black,
                                        modifier = Modifier.size(46.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "สแกนเพื่อตรวจสอบ",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = TextPrimaryGold
                                    )
                                    Text(
                                        text = "ฐานข้อมูลกลาง AI",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                        color = TextSecondaryMuted
                                    )
                                }
                            }

                            // Official Gold Seal Stamp
                            Box(
                                modifier = Modifier
                                    .size(62.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, AntiqueGoldBright, CircleShape)
                                    .background(AntiqueGoldDark.copy(alpha = 0.25f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "AI VERIFIED",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Black
                                        ),
                                        color = AntiqueGoldBright
                                    )
                                    Text(
                                        text = "แท้สมบูรณ์",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 7.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = TextPrimaryGold
                                    )
                                    Text(
                                        text = "★ ★ ★ ★ ★",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 7.sp),
                                        color = AntiqueGoldBright
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dismiss_cert_action_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AntiqueGoldPrimary,
                        contentColor = ObsidianBg
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "เสร็จสิ้น / บันทึกใบรับรอง",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
private fun CertificateRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = TextSecondaryMuted,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            ),
            color = TextPrimaryGold,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}
