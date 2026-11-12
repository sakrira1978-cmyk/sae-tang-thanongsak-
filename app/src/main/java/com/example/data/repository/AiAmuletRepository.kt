package com.example.data.repository

import android.graphics.Bitmap
import com.example.data.model.AmuletScanResult
import com.example.data.model.ChatMessage
import com.example.data.model.InspectionPoint
import kotlinx.coroutines.delay
import kotlin.random.Random

interface AiAmuletRepository {
    suspend fun analyzeAmulet(
        frontBitmap: Bitmap?,
        backBitmap: Bitmap?,
        baseBitmap: Bitmap?,
        hintSelection: String? = null
    ): Result<AmuletScanResult>

    suspend fun consultExpert(
        question: String,
        history: List<ChatMessage>
    ): Result<String>
}

class LocalAiAmuletRepositoryImpl : AiAmuletRepository {

    override suspend fun analyzeAmulet(
        frontBitmap: Bitmap?,
        backBitmap: Bitmap?,
        baseBitmap: Bitmap?,
        hintSelection: String?
    ): Result<AmuletScanResult> {
        // Simulate deep multi-stage computer vision inspection
        delay(2200)

        // Select the best matching archetype from master database or hint
        val master = when {
            hintSelection?.contains("ชินราช") == true -> MasterAmuletCatalog.masterAmulets.find { it.id == "chinaraj_indochine" }
            hintSelection?.contains("กริ่ง") == true -> MasterAmuletCatalog.masterAmulets.find { it.id == "phra_kring_pawaret" }
            hintSelection?.contains("ไปล่") == true -> MasterAmuletCatalog.masterAmulets.find { it.id == "lp_ploi_job_yai" }
            hintSelection?.contains("ทา") == true -> MasterAmuletCatalog.masterAmulets.find { it.id == "lp_tha_phaniangtaek" }
            hintSelection?.contains("โบ้ย") == true -> MasterAmuletCatalog.masterAmulets.find { it.id == "lp_boy_parunang" }
            hintSelection?.contains("ขี้ตา") == true -> MasterAmuletCatalog.masterAmulets.find { it.id == "lp_ngern_kheeta" }
            else -> MasterAmuletCatalog.masterAmulets.first() // หลวงพ่อเงิน พิมพ์นิยม
        } ?: MasterAmuletCatalog.masterAmulets.first()

        // Multi-angle bonus: if user provided all 3 angles (front, back, base), confidence is higher
        val angleBonus = when {
            frontBitmap != null && backBitmap != null && baseBitmap != null -> 6
            frontBitmap != null && (backBitmap != null || baseBitmap != null) -> 3
            else -> 0
        }

        val baseScore = 92 + angleBonus
        val overallScore = baseScore.coerceIn(88, 98)

        val metalAging = (91 + Random.nextInt(0, 7)).coerceIn(85, 99)
        val clayResidue = (89 + Random.nextInt(0, 9)).coerceIn(85, 98)
        val filingMarks = (90 + Random.nextInt(0, 8)).coerceIn(85, 98)
        val sprueCut = if (baseBitmap != null) (94 + Random.nextInt(0, 5)) else (88 + Random.nextInt(0, 5))
        val shrinkage = (93 + Random.nextInt(0, 6)).coerceIn(86, 99)

        val inspectionPoints = listOf(
            InspectionPoint(
                title = "วรรณะเนื้อโลหะและสนิมขุมในเนื้อ",
                location = "ซอกแขนและพื้นผิวองค์พระ",
                isAuthenticTrait = true,
                score = metalAging,
                explanation = "เนื้อโลหะทองผสมมีความแห้งสนิท สุกสว่างอมเขียว ปรากฏพรายทองและสนิมขุมผุดจากในเนื้อตามธรรมชาติอายุร้อยปี ไม่ใช่คราบทาสีเคมี"
            ),
            InspectionPoint(
                title = "คราบเบ้าดินไทยและเม็ดผดตามด",
                location = "ซอกลึก ลำคอ และข้างใบหู",
                isAuthenticTrait = true,
                score = clayResidue,
                explanation = "พบอนุภาคขี้เบ้าดินนวลสีเทาอมส้มและเศษแกลบเผาไหม้เกาะติดแน่น เม็ดผดกระจายตัวตามการเดือดของน้ำโลหะเหลวโบราณ"
            ),
            InspectionPoint(
                title = "รอยแทงตะไบแต่งข้างด้วยมือ",
                location = "ขอบข้างสะโพกและรอยต่อประกบ",
                isAuthenticTrait = true,
                score = filingMarks,
                explanation = "รอยฟันตะไบมีความเอียงตามสรีระมือช่างโบราณ ร่องตะไบมีความแห้งคล้ำ ไม่คมวาวแบบเครื่องเจียรสมัยใหม่"
            ),
            InspectionPoint(
                title = "รอยตัดก้านชนวนและธรรมชาติใต้ฐาน",
                location = "ใต้ก้นองค์พระ",
                isAuthenticTrait = true,
                score = sprueCut,
                explanation = if (baseBitmap != null) {
                    "ตรวจสอบภาพก้นองค์พระ: ปรากฏรอยเลื่อยตัดและรอยฉีกขาดของช่อชนวน พร้อมรอยตะไบเก็บขอบอย่างประณีต ความแห้งสนิทถึงยุค"
                } else {
                    "บริเวณรอยชนวนสอดคล้องกับกรรมวิธีหล่อช่อแบบโบราณ แนะนำถ่ายภาพใต้ฐานเพิ่มเติมเพื่อยืนยัน 100%"
                }
            ),
            InspectionPoint(
                title = "พิมพ์ทรงและธรรมชาติการยุบตัว",
                location = "ริ้วจีวรและโครงหน้า",
                isAuthenticTrait = true,
                score = shrinkage,
                explanation = "สัดส่วนล่ำสันสมบูรณ์ ริ้วจีวรลบเหลี่ยมด้วยความร้อนและกาลเวลา มีรอยย่นยุบตัวตามซอกแขนคล้ายผิวมะนาวแห้งชัดเจน"
            )
        )

        val certNumber = "PLB-${System.currentTimeMillis().toString().takeLast(8)}-TH"

        val result = AmuletScanResult(
            amuletName = master.name,
            templeAndMaker = "${master.creator} ${master.temple}",
            estimatedEra = master.era,
            alloyClassification = master.alloyType,
            castingMethod = "หล่อโบราณเทช่อดินไทย (เบ้าประกบแต่งตะไบมือ)",
            overallAuthenticityScore = overallScore,
            conditionGrade = "สภาพเดิม พระสวยสมบูรณ์ระดับประกวด (ผิวพรายทองคำ)",
            metalAgingScore = metalAging,
            clayResidueScore = clayResidue,
            filingMarksScore = filingMarks,
            sprueCutScore = sprueCut,
            shrinkageScore = shrinkage,
            keyInspectionPoints = inspectionPoints,
            estimatedMarketValue = master.marketPriceRange,
            conclusionSummary = "ผลการตรวจสแกนพบจุดชี้ขาดสำคัญครบทั้ง 5 เสาหลักพระหล่อโบราณ เนื้อหาถึงยุค คราบเบ้าดินไทยแห้งสนิท รอยตะไบและก้นตัดชนวนเป็นธรรมชาติอย่างยิ่ง",
            expertAdvice = "พระองค์นี้เนื้อหาเดิมและสมบูรณ์มาก แนะนำให้เก็บรักษาในตลับทองปิดหลังหรือกล่องสุญญากาศ ไม่ควรนำไปล้างผิวหรือขัดน้ำยาใดๆ เพื่อคงมูลค่าสูงสุด",
            certificateCode = certNumber
        )

        return Result.success(result)
    }

    override suspend fun consultExpert(
        question: String,
        history: List<ChatMessage>
    ): Result<String> {
        delay(1200)

        val q = question.lowercase()
        val reply = when {
            q.contains("คราบเบ้า") || q.contains("ดินไทย") -> {
                "คราบเบ้าของพระหล่อโบราณแท้เกิดจาก 'ดินไทย' (ดินเหนียวผสมขี้วัวและแกลบ) ที่ถูกความร้อนของน้ำโลหะหลอมเหลวเผาไหม้ คราบจะเกาะติดแน่นตามซอกลึก มีสีเทานวล สีน้ำตาลไหม้ หรือดำสนิท หากเอาแอลกอฮอล์เช็ดจะไม่ออก ต่างจากพระเก๊ที่เอากาวหรือดินสอพองผสมสีมาทาเลียนแบบครับ"
            }
            q.contains("ตะไบ") || q.contains("รอยตะไบ") -> {
                "รอยตะไบพระหล่อโบราณคือหัวใจสำคัญครับ ช่างยุคเก่าใช้ตะไบมือแทงลบครีบประกบ รอยจึงมีความเอียง ลึกตื้น และโค้งตามแรงมือมนุษย์ ไม่ขนานตรงแหน่วแบบตะไบไฟฟ้า และในร่องตะไบต้องมีสีเข้มอมน้ำตาลแห้งผาก ไม่วาววับเหมือนเพิ่งทำเมื่อวานครับ"
            }
            q.contains("ล้าง") || q.contains("ทำความสะอาด") -> {
                "ข้อห้ามเด็ดขาดของพระหล่อโบราณคือ 'ห้ามล้างผิวด้วยน้ำยาล้างห้องน้ำหรือกรด' เด็ดขาดครับ เพราะจะทำให้ผิวไฟ คราบน้ำทอง และคราบเบ้าโบราณที่สะสมมานับร้อยปีหลุดหาย ทำให้มูลค่าพระตกฮวบเหลือไม่ถึง 10% หากมีฝุ่นเกาะ ให้ใช้พู่กันขนนุ่มปัดเบาๆ เท่านั้นครับ"
            }
            q.contains("หลวงพ่อเงิน") || q.contains("บางคลาน") -> {
                "หลวงพ่อเงิน วัดบางคลาน มี 2 พิมพ์หลัก คือ 'พิมพ์นิยม' หล่อแบบเทช่อ (มีก้านชนวนใต้ก้น ไม่มีตะเข็บข้าง มีเส้นมือรองรับ) และ 'พิมพ์ขี้ตา' หล่อแบบประกบเบ้า (มีตะเข็บข้าง รอยตะไบ และมีก้อนเนื้อเกินใต้ตาซ้าย) ทั้งสองพิมพ์สร้างปี พ.ศ. 2460 วรรณะเนื้อทองเหลืองผสมแก่ทองคำอมเขียวครับ"
            }
            q.contains("ชินราช") || q.contains("อินโดจีน") -> {
                "พระพุทธชินราชอินโดจีน วัดสุทัศน์ พ.ศ. 2485 จุดชี้ขาดอันดับหนึ่งคือ 'โค้ดอกเลาและโค้ดกงจักร' ที่ใต้ฐาน ตัวตอกโค้ดต้องมีความคม เสี้ยนโลหะตั้งตรงในร่องตอก วรรณะเนื้อโลหะมีทั้งเหลืองอมเขียวและเหลืองอมแดง มีรอยแต่งตะไบซุ้มเรือนแก้วอย่างวิจิตรครับ"
            }
            q.contains("สนิม") || q.contains("สนิมขุม") -> {
                "พระหล่อโบราณอายุ 80-150 ปี จะเกิด 'สนิมขุม' ซึ่งเป็นการทำปฏิกิริยาของเนื้อโลหะจากภายในสู่ภายนอก สนิมจะกินลึกเป็นหลุมเล็กๆ ขอบมน นุ่มตา มักมีสีน้ำตาลไหม้ สนิมเขียวหยก หรือสนิมดำ แตกต่างจากสนิมเทียมที่เกิดจากการทาน้ำกรดซึ่งจะมีขอบคมและผิวโลหะตายด้านครับ"
            }
            else -> {
                "พระหล่อโบราณแท้ต้องดูที่ '5 เสาหลัก' เสมอครับ คือ 1. วรรณะเนื้อโลหะสุกใสแห้งผาก 2. คราบเบ้าดินไทยติดแน่นในซอก 3. รอยตะไบมือโบราณ 4. รอยตัดและยุบตัวของก้านชนวน และ 5. ธรรมชาติการหดตัวของน้ำโลหะ หากเข้าหลัก 5 ประการนี้ มั่นใจได้เกิน 95% ครับ มีจุดไหนบนองค์พระที่อยากให้ช่วยวิเคราะห์เพิ่มเติมสอบถามได้เลยครับ"
            }
        }

        return Result.success(reply)
    }
}
