package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Inspection detail for specific focal points on an ancient cast amulet.
 */
data class InspectionPoint(
    val title: String,
    val location: String, // e.g. "พระพักตร์/ใบหน้า", "ริ้วจีวร", "คราบเบ้าตามซอกแขน", "รอยตะไบแต่งข้าง", "รอยตัดก้านชนวนใต้ฐาน"
    val isAuthenticTrait: Boolean,
    val score: Int, // 0 - 100
    val explanation: String
)

/**
 * Comprehensive analysis result returned by the Phra Lor Boran AI Engine.
 */
data class AmuletScanResult(
    val id: String = System.currentTimeMillis().toString(),
    val amuletName: String, // e.g. "รูปหล่อหลวงพ่อเงิน วัดบางคลาน พิมพ์นิยม"
    val templeAndMaker: String, // e.g. "หลวงพ่อเงิน พุทธโชติ วัดบางคลาน จ.พิจิตร"
    val estimatedEra: String, // e.g. "พ.ศ. 2460 (ประมาณ 109 ปี)"
    val alloyClassification: String, // e.g. "เนื้อโลหะทองผสมแก่ทองเหลืองวรรณะเหลืองอมเขียว"
    val castingMethod: String, // e.g. "หล่อโบราณเทแบบเบ้าประกบ (หล่อช่อ/ก้านชนวนก้น)"
    val overallAuthenticityScore: Int, // 0 - 100
    val conditionGrade: String, // e.g. "สภาพสวยแชมป์ ผิวเดิมไม่ผ่านการล้าง"
    val metalAgingScore: Int, // 1. เนื้อโลหะ & ความเก่า
    val clayResidueScore: Int, // 2. คราบเบ้าดินไทย & เม็ดผด
    val filingMarksScore: Int, // 3. รอยตะไบแต่งโบราณ
    val sprueCutScore: Int, // 4. รอยตัดชนวน & ก้น
    val shrinkageScore: Int, // 5. รอยยุบตัว & พิมพ์ทรง
    val keyInspectionPoints: List<InspectionPoint>,
    val estimatedMarketValue: String, // e.g. "4,500,000 - 8,000,000 บาท"
    val conclusionSummary: String,
    val expertAdvice: String,
    val certificateCode: String,
    val frontImageUri: String? = null,
    val backImageUri: String? = null,
    val baseImageUri: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Room entity to save scanned amulets locally in the user's collection.
 */
@Entity(tableName = "amulet_scans")
data class ScanRecordEntity(
    @PrimaryKey val id: String,
    val amuletName: String,
    val templeAndMaker: String,
    val estimatedEra: String,
    val alloyClassification: String,
    val castingMethod: String,
    val overallAuthenticityScore: Int,
    val conditionGrade: String,
    val metalAgingScore: Int,
    val clayResidueScore: Int,
    val filingMarksScore: Int,
    val sprueCutScore: Int,
    val shrinkageScore: Int,
    val estimatedMarketValue: String,
    val conclusionSummary: String,
    val certificateCode: String,
    val frontImageUri: String? = null,
    val backImageUri: String? = null,
    val baseImageUri: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val userNote: String = ""
)

/**
 * Master piece encyclopedia entry.
 */
data class MasterAmulet(
    val id: String,
    val name: String,
    val temple: String,
    val creator: String,
    val era: String,
    val alloyType: String,
    val keyPoints: List<String>,
    val secretChecklist: List<String>,
    val marketPriceRange: String,
    val rarityLevel: String,
    val imageDrawableName: String = "img_hero_phralor"
)

/**
 * One of the 5 sacred pillars of Phra Lor Boran evaluation.
 */
data class InspectionPillar(
    val id: Int,
    val title: String,
    val subtitle: String,
    val iconName: String,
    val description: String,
    val authenticCharacteristics: List<String>,
    val fakeCharacteristics: List<String>,
    val expertProTip: String
)

/**
 * AI Consultation Chat Message.
 */
data class ChatMessage(
    val id: String = System.currentTimeMillis().toString(),
    val sender: String, // "user" or "ai"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
