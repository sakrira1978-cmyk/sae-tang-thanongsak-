package com.example

import com.example.data.repository.LocalAiAmuletRepositoryImpl
import com.example.data.repository.MasterAmuletCatalog
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testFivePillarsCompleteness() {
        val pillars = MasterAmuletCatalog.fivePillars
        assertEquals("ต้องมี 5 เสาหลักครบถ้วน", 5, pillars.size)
        pillars.forEach { pillar ->
            assertTrue("ต้องมีข้อสังเกตพระแท้", pillar.authenticCharacteristics.isNotEmpty())
            assertTrue("ต้องมีข้อสังเกตพระเก๊", pillar.fakeCharacteristics.isNotEmpty())
            assertTrue("ต้องมีเคล็ดลับเซียน", pillar.expertProTip.isNotBlank())
        }
    }

    @Test
    fun testMasterAmuletsArchive() {
        val masterList = MasterAmuletCatalog.masterAmulets
        assertTrue("ต้องมีรายการพระองค์ครู", masterList.isNotEmpty())
        val lpNgern = masterList.find { it.id == "lp_ngern_niyom" }
        assertNotNull("ต้องมีหลวงพ่อเงินพิมพ์นิยม", lpNgern)
        assertTrue("ต้องมีจุดชี้ขาดแม่พิมพ์", lpNgern!!.keyPoints.isNotEmpty())
    }

    @Test
    fun testAiAnalysisEngine() = runBlocking {
        val repository = LocalAiAmuletRepositoryImpl()
        val result = repository.analyzeAmulet(null, null, null, "หลวงพ่อเงิน")
        assertTrue("การวิเคราะห์ต้องสำเร็จ", result.isSuccess)
        val data = result.getOrNull()
        assertNotNull(data)
        assertTrue("คะแนนความแท้ต้องอยู่ระหว่าง 0-100", data!!.overallAuthenticityScore in 80..100)
        assertTrue("รหัสใบรับรองต้องขึ้นต้นด้วย PLB", data.certificateCode.startsWith("PLB-"))
        assertEquals("ต้องมีจุดชี้ขาด 5 ตำแหน่ง", 5, data.keyInspectionPoints.size)
    }

    @Test
    fun testAiExpertConsultation() = runBlocking {
        val repository = LocalAiAmuletRepositoryImpl()
        val response = repository.consultExpert("คราบเบ้าดูอย่างไร", emptyList())
        assertTrue(response.isSuccess)
        val answer = response.getOrNull()
        assertNotNull(answer)
        assertTrue("คำตอบต้องมีเนื้อหาดินไทย", answer!!.contains("ดินไทย"))
    }
}
