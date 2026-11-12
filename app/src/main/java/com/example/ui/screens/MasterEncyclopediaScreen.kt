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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.MasterAmulet
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MasterEncyclopediaScreen(
    onSelectForCompare: (MasterAmulet) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("ทั้งหมด") }
    var selectedAmuletForDetail by remember { mutableStateOf<MasterAmulet?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val categories = listOf("ทั้งหมด", "ทองผสม", "สัมฤทธิ์", "ขันลงหิน", "พระกริ่ง", "ชินตะกั่ว")

    val filteredList = remember(searchQuery, selectedCategory) {
        MasterAmuletCatalog.masterAmulets.filter { amulet ->
            val matchesQuery = amulet.name.contains(searchQuery, ignoreCase = true) ||
                    amulet.temple.contains(searchQuery, ignoreCase = true) ||
                    amulet.creator.contains(searchQuery, ignoreCase = true)
            val matchesCategory = when (selectedCategory) {
                "ทั้งหมด" -> true
                "ทองผสม" -> amulet.alloyType.contains("ทองผสม") || amulet.alloyType.contains("ทองเหลือง")
                "สัมฤทธิ์" -> amulet.alloyType.contains("สัมฤทธิ์") || amulet.alloyType.contains("นวโลหะ")
                "ขันลงหิน" -> amulet.alloyType.contains("ขันลงหิน")
                "พระกริ่ง" -> amulet.name.contains("กริ่ง")
                "ชินตะกั่ว" -> amulet.alloyType.contains("ตะกั่ว")
                else -> true
            }
            matchesQuery && matchesCategory
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .padding(16.dp)
            .testTag("master_encyclopedia_screen")
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.MenuBook,
                contentDescription = null,
                tint = AntiqueGoldBright,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "สารานุกรมพระหล่อโบราณยอดนิยม",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = AntiqueGoldBright
            )
        }
        Text(
            text = "รวมพระองค์ครูระดับตำนาน ชี้ขาดจุดลับและตำหนิแม่พิมพ์",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = TextSecondaryMuted
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("encyclopedia_search_input"),
            placeholder = {
                Text(
                    text = "ค้นหาชื่อพระ, หลวงพ่อ, หรือวัด...",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondaryMuted
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "ค้นหา",
                    tint = AntiqueGoldPrimary
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "ล้าง",
                            tint = TextSecondaryMuted
                        )
                    }
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = DarkSurfaceElevated,
                unfocusedContainerColor = DarkSurface,
                focusedIndicatorColor = AntiqueGoldPrimary,
                unfocusedIndicatorColor = GoldBorder,
                focusedTextColor = TextPrimaryGold,
                unfocusedTextColor = TextPrimaryGold
            ),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                val isSelected = selectedCategory == cat
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = cat },
                    label = {
                        Text(
                            text = cat,
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

        Spacer(modifier = Modifier.height(12.dp))

        // List of Master Amulets
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredList) { amulet ->
                MasterAmuletCard(
                    amulet = amulet,
                    onClick = { selectedAmuletForDetail = amulet }
                )
            }
        }
    }

    // Detail Bottom Sheet
    selectedAmuletForDetail?.let { amulet ->
        ModalBottomSheet(
            onDismissRequest = { selectedAmuletForDetail = null },
            sheetState = sheetState,
            containerColor = DarkSurface,
            scrimColor = ObsidianBg.copy(alpha = 0.75f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = amulet.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = AntiqueGoldBright
                        )
                        Text(
                            text = "${amulet.creator} • ${amulet.temple}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = TextSecondaryMuted
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(PatinaBronze.copy(alpha = 0.3f))
                            .border(0.5.dp, PatinaVerdigris, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = amulet.era,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = PatinaVerdigris
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = GoldBorder, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "วรรณะเนื้อหา: ${amulet.alloyType}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = TextPrimaryGold
                )
                Text(
                    text = "ประเมินมูลค่าสะสม: ${amulet.marketPriceRange}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = AntiqueGoldPrimary
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "จุดชี้ขาดสำคัญ (Key Identification Points):",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = AntiqueGoldBright
                )
                Spacer(modifier = Modifier.height(6.dp))
                amulet.keyPoints.forEach { point ->
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = PatinaVerdigris,
                            modifier = Modifier
                                .size(16.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = point,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = TextPrimaryGold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "รายการตรวจสอบลับ (Secret Checklist):",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = AntiqueGoldBright
                )
                Spacer(modifier = Modifier.height(6.dp))
                amulet.secretChecklist.forEach { item ->
                    Text(
                        text = "• $item",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = TextSecondaryMuted,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                androidx.compose.material3.Button(
                    onClick = {
                        onSelectForCompare(amulet)
                        selectedAmuletForDetail = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = AntiqueGoldPrimary,
                        contentColor = ObsidianBg
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "นำไปเปรียบเทียบในกล้องส่องพระ",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
private fun MasterAmuletCard(
    amulet: MasterAmulet,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .testTag("master_amulet_${amulet.id}"),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Amulet thumbnail
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, GoldBorderBright, RoundedCornerShape(8.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_app_icon_src),
                    contentDescription = amulet.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = amulet.name,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        ),
                        color = TextPrimaryGold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = amulet.era.take(8),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = AntiqueGoldPrimary
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${amulet.creator} • ${amulet.temple}",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = TextSecondaryMuted
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = amulet.marketPriceRange,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp
                    ),
                    color = PatinaVerdigris
                )
            }
        }
    }
}
