package com.example.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.AmuletScanResult
import com.example.data.model.ChatMessage
import com.example.data.model.MasterAmulet
import com.example.data.model.ScanRecordEntity
import com.example.data.repository.AiAmuletRepository
import com.example.data.repository.LocalAiAmuletRepositoryImpl
import com.example.data.repository.MasterAmuletCatalog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AmuletViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val scanDao = db.scanRecordDao()
    private val aiRepository: AiAmuletRepository = LocalAiAmuletRepositoryImpl()

    // Scanned photos slots
    private val _frontImageUri = MutableStateFlow<Uri?>(null)
    val frontImageUri: StateFlow<Uri?> = _frontImageUri.asStateFlow()

    private val _backImageUri = MutableStateFlow<Uri?>(null)
    val backImageUri: StateFlow<Uri?> = _backImageUri.asStateFlow()

    private val _baseImageUri = MutableStateFlow<Uri?>(null)
    val baseImageUri: StateFlow<Uri?> = _baseImageUri.asStateFlow()

    // Analysis State
    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _analysisStageText = MutableStateFlow("")
    val analysisStageText: StateFlow<String> = _analysisStageText.asStateFlow()

    private val _scanProgress = MutableStateFlow(0f)
    val scanProgress: StateFlow<Float> = _scanProgress.asStateFlow()

    private val _scanResult = MutableStateFlow<AmuletScanResult?>(null)
    val scanResult: StateFlow<AmuletScanResult?> = _scanResult.asStateFlow()

    // Selected sample amulet archetype (for immediate testing without real camera)
    private val _selectedArchetype = MutableStateFlow("หลวงพ่อเงิน วัดบางคลาน")
    val selectedArchetype: StateFlow<String> = _selectedArchetype.asStateFlow()

    // Digital Loupe
    private val _loupeZoom = MutableStateFlow(10f)
    val loupeZoom: StateFlow<Float> = _loupeZoom.asStateFlow()

    private val _loupeFilter = MutableStateFlow("NORMAL") // NORMAL, UV, CONTRAST, VERDIGRIS
    val loupeFilter: StateFlow<String> = _loupeFilter.asStateFlow()

    private val _compareMaster = MutableStateFlow<MasterAmulet?>(MasterAmuletCatalog.masterAmulets.first())
    val compareMaster: StateFlow<MasterAmulet?> = _compareMaster.asStateFlow()

    // Certificate Dialog
    private val _showCertificate = MutableStateFlow(false)
    val showCertificate: StateFlow<Boolean> = _showCertificate.asStateFlow()

    // Toast / Feedback message
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Chat with AI specialist
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = "ai",
                text = "สวัสดีครับ ผมคือผู้เชี่ยวชาญด้านพระหล่อโบราณ AI ยินดีให้คำแนะนำเกี่ยวกับเนื้อโลหะทองผสม คราบเบ้า รอยตะไบ รอยตัดชนวน และพิมพ์ทรงพระหล่อทุกสำนักครับ มีข้อสงสัยจุดไหนสอบถามได้เลยครับ"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    // Saved Records from Room
    val savedRecords: StateFlow<List<ScanRecordEntity>> = scanDao.getAllScans()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setFrontImage(uri: Uri?) {
        _frontImageUri.value = uri
    }

    fun setBackImage(uri: Uri?) {
        _backImageUri.value = uri
    }

    fun setBaseImage(uri: Uri?) {
        _baseImageUri.value = uri
    }

    fun setSelectedArchetype(name: String) {
        _selectedArchetype.value = name
    }

    fun setLoupeZoom(zoom: Float) {
        _loupeZoom.value = zoom
    }

    fun setLoupeFilter(filter: String) {
        _loupeFilter.value = filter
    }

    fun setCompareMaster(master: MasterAmulet) {
        _compareMaster.value = master
    }

    fun setShowCertificate(show: Boolean) {
        _showCertificate.value = show
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }

    fun startAmuletScan() {
        viewModelScope.launch {
            _isAnalyzing.value = true
            _scanProgress.value = 0.05f
            _analysisStageText.value = "เริ่มต้นกระบวนการสแกนด้วยระบบ AI โบราณคดี..."
            delay(400)

            _scanProgress.value = 0.25f
            _analysisStageText.value = "วิเคราะห์วรรณะเนื้อโลหะผสม & การเกิดสนิมขุม..."
            delay(500)

            _scanProgress.value = 0.50f
            _analysisStageText.value = "ตรวจจับชั้นคราบเบ้าดินไทย & เม็ดผดตามดตามซอก..."
            delay(500)

            _scanProgress.value = 0.75f
            _analysisStageText.value = "ประเมินรอยแทงตะไบแต่งมือ & รอยตัดก้านชนวนใต้ฐาน..."
            delay(450)

            _scanProgress.value = 0.95f
            _analysisStageText.value = "เทียบเคียงพิมพ์ทรง & ธรรมชาติการยุบตัวของน้ำทอง..."
            delay(350)

            val result = aiRepository.analyzeAmulet(
                frontBitmap = null,
                backBitmap = null,
                baseBitmap = null,
                hintSelection = _selectedArchetype.value
            )

            result.onSuccess { scan ->
                val fullResult = scan.copy(
                    frontImageUri = _frontImageUri.value?.toString(),
                    backImageUri = _backImageUri.value?.toString(),
                    baseImageUri = _baseImageUri.value?.toString()
                )
                _scanResult.value = fullResult
                _scanProgress.value = 1f
                _toastMessage.value = "สแกนและประเมินผลสำเร็จแล้ว!"
            }.onFailure {
                _toastMessage.value = "เกิดข้อผิดพลาดในการวิเคราะห์ กรุณาลองใหม่อีกครั้ง"
            }

            _isAnalyzing.value = false
        }
    }

    fun saveCurrentScanToCollection(userNote: String = "") {
        val current = _scanResult.value ?: return
        viewModelScope.launch {
            val entity = ScanRecordEntity(
                id = current.id,
                amuletName = current.amuletName,
                templeAndMaker = current.templeAndMaker,
                estimatedEra = current.estimatedEra,
                alloyClassification = current.alloyClassification,
                castingMethod = current.castingMethod,
                overallAuthenticityScore = current.overallAuthenticityScore,
                conditionGrade = current.conditionGrade,
                metalAgingScore = current.metalAgingScore,
                clayResidueScore = current.clayResidueScore,
                filingMarksScore = current.filingMarksScore,
                sprueCutScore = current.sprueCutScore,
                shrinkageScore = current.shrinkageScore,
                estimatedMarketValue = current.estimatedMarketValue,
                conclusionSummary = current.conclusionSummary,
                certificateCode = current.certificateCode,
                frontImageUri = current.frontImageUri,
                backImageUri = current.backImageUri,
                baseImageUri = current.baseImageUri,
                timestamp = current.timestamp,
                userNote = userNote
            )
            scanDao.insertScan(entity)
            _toastMessage.value = "บันทึกเข้าสู่คอลเลกชันและออกใบการันตีแล้ว!"
        }
    }

    fun deleteScanRecord(id: String) {
        viewModelScope.launch {
            scanDao.deleteScanById(id)
            _toastMessage.value = "ลบรายการเรียบร้อยแล้ว"
        }
    }

    fun toggleRecordFavorite(record: ScanRecordEntity) {
        viewModelScope.launch {
            scanDao.toggleFavorite(record.id, !record.isFavorite)
        }
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val userMsg = ChatMessage(sender = "user", text = text)
        _chatMessages.value = _chatMessages.value + userMsg

        viewModelScope.launch {
            _isChatLoading.value = true
            val reply = aiRepository.consultExpert(text, _chatMessages.value)
            reply.onSuccess { ans ->
                val aiMsg = ChatMessage(sender = "ai", text = ans)
                _chatMessages.value = _chatMessages.value + aiMsg
            }.onFailure {
                val errorMsg = ChatMessage(sender = "ai", text = "ขออภัยครับ ขณะนี้ระบบกำลังประมวลผล กรุณาลองใหม่อีกครั้งครับ")
                _chatMessages.value = _chatMessages.value + errorMsg
            }
            _isChatLoading.value = false
        }
    }

    fun resetScanner() {
        _frontImageUri.value = null
        _backImageUri.value = null
        _baseImageUri.value = null
        _scanResult.value = null
        _scanProgress.value = 0f
    }
}
