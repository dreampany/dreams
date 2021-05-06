package com.dreampany.pos

import android.util.SparseArray
import com.starmicronics.starioextension.StarIoExt.Emulation
import com.starmicronics.starioextension.StarIoExt.LedModel

/**
 * Created by roman on 5/6/21
 * Copyright (c) 2021 bjit. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class ModelCapability {

    companion object {
        const val NONE = -1
        const val MPOP = 0
        const val FVP10 = 1
        const val TSP100 = 2
        const val TSP650II = 3
        const val TSP700II = 4
        const val TSP800II = 5
        const val SP700 = 6
        const val SM_S210I = 7
        const val SM_S220I = 8
        const val SM_S230I = 9
        const val SM_T300I_T300 = 10
        const val SM_T400I = 11
        const val SM_L200 = 12
        const val BSC10 = 13
        const val SM_S210I_StarPRNT = 14
        const val SM_S220I_StarPRNT = 15
        const val SM_S230I_StarPRNT = 16
        const val SM_T300I_T300_StarPRNT = 17
        const val SM_T400I_StarPRNT = 18

        // V5.3.0
        const val SM_L300 = 19

        // V5.6.0
        const val MC_PRINT2 = 20
        const val MC_PRINT3 = 21

        // V5.11.0
        const val TUP500 = 22

        // V5.12.0
        const val SK1_211_221_V211 = 23
        const val SK1_211_221_V211_Presenter = 24
        const val SK1_311_321_V311 = 25
        const val SK1_311_V311_Presenter = 26

        private val capabilities: SparseArray<ModelCapability.ModelInfo> =
            object : SparseArray<ModelCapability.ModelInfo>() {
                init {
                    put(
                        MC_PRINT2, ModelCapability.ModelInfo(
                            "mC-Print2", arrayOf( // modelNameArray
                                "MCP20 (STR-001)",  // <-LAN interface
                                "MCP21 (STR-001)",
                                "mC-Print2-",  // <-Bluetooth interface
                                "mC-Print2"
                            ),  // <-USB interface
                            Emulation.StarPRNT,  // Emulation
                            "",  // Default portSettings
                            Constants.PAPER_SIZE_TWO_INCH,  // Default paper size
                            true,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            true,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            true,  // canPrintCjk
                            false,  // canUseBlackMark
                            false,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            true,  // canUseCashDrawer
                            true,  // canUseBarcodeReader
                            true,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            false,  // canUseLed
                            LedModel.None,  // ledModel
                            false,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            true,  // canGetProductSerialNumber
                            16,  // settableUsbSerialNumberLength
                            true,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            true // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        MC_PRINT3, ModelCapability.ModelInfo(
                            "mC-Print3", arrayOf( // modelNameArray
                                "MCP31 (STR-001)",  // <-LAN interface
                                "mC-Print3-",  // <-Bluetooth interface
                                "mC-Print3"
                            ),  // <-USB interface
                            Emulation.StarPRNT,  // Emulation
                            "",  // Default portSettings
                            Constants.PAPER_SIZE_THREE_INCH,  // Default paper size
                            true,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            true,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            true,  // canPrintCjk
                            false,  // canUseBlackMark
                            false,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            true,  // canUseCashDrawer
                            true,  // canUseBarcodeReader
                            true,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            false,  // canUseLed
                            LedModel.None,  // ledModel
                            false,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            true,  // canGetProductSerialNumber
                            16,  // settableUsbSerialNumberLength
                            true,  // isUsbSerialNumberEnabledByDefault
                            true,  // canUseMelodySpeaker
                            0,  // defaultSoundNumber
                            12,  // defaultVolume
                            15,  // volumeMax
                            0,  // volumeMin
                            true // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        MPOP, ModelCapability.ModelInfo(
                            "mPOP", arrayOf( // modelNameArray
                                "STAR mPOP-",  // <-Bluetooth interface
                                "mPOP"
                            ),  // <-USB interface
                            Emulation.StarPRNT,  // Emulation
                            "",  // Default portSettings
                            Constants.PAPER_SIZE_TWO_INCH,  // Default paper size
                            false,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            true,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            false,  // canUseBlackMark
                            false,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            true,  // canUseCashDrawer
                            true,  // canUseBarcodeReader
                            true,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            false,  // canUseLed
                            LedModel.None,  // ledModel
                            false,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            true,  // canGetProductSerialNumber
                            8,  // settableUsbSerialNumberLength
                            false,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        FVP10, ModelCapability.ModelInfo(
                            "FVP10", arrayOf( // modelNameArray
                                "FVP10 (STR_T-001)",  // <-LAN interface
                                "Star FVP10"
                            ),  // <-USB interface
                            Emulation.StarLine,  // Emulation
                            "",  // Default portSettings
                            Constants.PAPER_SIZE_THREE_INCH,  // Default paper size
                            true,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            true,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            true,  // canUseBlackMark
                            true,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            true,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            false,  // canUseLed
                            LedModel.None,  // ledModel
                            false,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            8,  // settableUsbSerialNumberLength
                            false,  // isUsbSerialNumberEnabledByDefault
                            true,  // canUseMelodySpeaker
                            1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        TSP100, ModelCapability.ModelInfo(
                            "TSP100", arrayOf( // modelNameArray
                                "TSP113", "TSP143",  // <-LAN model
                                "TSP100-",  // <-Bluetooth model
                                "Star TSP113", "Star TSP143"
                            ),  // <-USB model
                            Emulation.StarGraphic,  // Emulation
                            "",  // Default portSettings
                            Constants.PAPER_SIZE_THREE_INCH,  // Default paper size
                            true,  // canSetDrawerOpenStatus
                            false,  // canPrintTextReceiptSample
                            false,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            false,  // canUseBlackMark
                            false,  // canUseBlackMarkDetection
                            false,  // canUsePageMode
                            true,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            true,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            false,  // canUseLed
                            LedModel.None,  // ledModel
                            false,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            true,  // canGetProductSerialNumber
                            8,  // settableUsbSerialNumberLength
                            false,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        TSP650II, ModelCapability.ModelInfo(
                            "TSP650II", arrayOf( // modelNameArray
                                "TSP654II (STR_T-001)",  // Only LAN model->
                                "TSP654 (STR_T-001)",
                                "TSP651 (STR_T-001)"
                            ),
                            Emulation.StarLine,  // Emulation
                            "",  // Default portSettings
                            Constants.PAPER_SIZE_THREE_INCH,  // Default paper size
                            true,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            true,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            true,  // canPrintCjk
                            false,  // canUseBlackMark
                            false,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            true,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            false,  // canUseLed
                            LedModel.None,  // ledModel
                            false,  // canUseBlinkLed
                            true,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            8,  // settableUsbSerialNumberLength
                            false,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        TSP700II, ModelCapability.ModelInfo(
                            "TSP700II", arrayOf( // modelNameArray
                                "TSP743II (STR_T-001)",
                                "TSP743 (STR_T-001)"
                            ),
                            Emulation.StarLine,  // Emulation
                            "",  // Default portSettings
                            Constants.PAPER_SIZE_THREE_INCH,  // Default paper size
                            true,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            true,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            true,  // canUseBlackMark
                            true,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            true,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            false,  // canUseLed
                            LedModel.None,  // ledModel
                            false,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            8,  // settableUsbSerialNumberLength
                            false,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        TSP800II, ModelCapability.ModelInfo(
                            "TSP800II", arrayOf( // modelNameArray
                                "TSP847II (STR_T-001)",
                                "TSP847 (STR_T-001)"
                            ),  // <-Only LAN model
                            Emulation.StarLine,  // Emulation
                            "",  // Default portSettings
                            Constants.PAPER_SIZE_FOUR_INCH,  // Default paper size
                            true,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            true,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            true,  // canUseBlackMark
                            true,  // canUseBlackMarkDetection
                            false,  // canUsePageMode
                            true,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            false,  // canUseLed
                            LedModel.None,  // ledModel
                            false,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            8,  // settableUsbSerialNumberLength
                            false,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        TUP500, ModelCapability.ModelInfo(
                            "TUP500", arrayOf( // modelNameArray
                                "TUP592 (STR_T-001)",
                                "TUP542 (STR_T-001)"
                            ),
                            Emulation.StarLine,  // Emulation
                            "",  // Default portSettings
                            Constants.PAPER_SIZE_THREE_INCH,  // Default paper size
                            false,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            false,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            true,  // canUseBlackMark
                            true,  // canUseBlackMarkDetection
                            false,  // canUsePageMode
                            false,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            true,  // canUsePresenter
                            true,  // canUseLed
                            LedModel.Star,  // ledModel
                            true,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            8,  // settableUsbSerialNumberLength
                            false,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        SP700, ModelCapability.ModelInfo(
                            "SP700", arrayOf( // modelNameArray
                                "SP712 (STR-001)",  // Only LAN model
                                "SP717 (STR-001)",
                                "SP742 (STR-001)",
                                "SP747 (STR-001)"
                            ),
                            Emulation.StarDotImpact,  // Emulation
                            "",  // Default portSettings
                            Constants.PAPER_SIZE_DOT_THREE_INCH,  // Default paper size
                            true,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            true,  // canPrintUtf8EncodedText
                            false,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            true,  // canUseBlackMark
                            true,  // canUseBlackMarkDetection
                            false,  // canUsePageMode
                            true,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            false,  // canUseLed
                            LedModel.None,  // ledModel
                            false,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            false,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            8,  // settableUsbSerialNumberLength
                            false,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        SM_S210I, ModelCapability.ModelInfo(
                            "SM-S210i", arrayOf<String>(),  // modelNameArray
                            Emulation.EscPosMobile,  // Emulation
                            "mini",  // Default portSettings
                            Constants.PAPER_SIZE_TWO_INCH,  // Default paper size
                            false,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            false,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            false,  // canUseBlackMark
                            false,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            false,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            false,  // canUseLed
                            LedModel.None,  // ledModel
                            false,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            0,  // settableUsbSerialNumberLength
                            false,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        SM_S220I, ModelCapability.ModelInfo(
                            "SM-S220i", arrayOf<String>(),  // modelNameArray
                            Emulation.EscPosMobile,  // Emulation
                            "mini",  // Default portSettings
                            Constants.PAPER_SIZE_TWO_INCH,  // Default paper size
                            false,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            false,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            false,  // canUseBlackMark
                            false,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            false,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            false,  // canUseLed
                            LedModel.None,  // ledModel
                            false,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            0,  // settableUsbSerialNumberLength
                            false,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        SM_S230I, ModelCapability.ModelInfo(
                            "SM-S230i", arrayOf<String>(),  // modelNameArray
                            Emulation.EscPosMobile,  // Emulation
                            "mini",  // Default portSettings
                            Constants.PAPER_SIZE_TWO_INCH,  // Default paper size
                            false,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            false,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            false,  // canUseBlackMark
                            false,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            false,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            false,  // canUseLed
                            LedModel.None,  // ledModel
                            false,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            0,  // settableUsbSerialNumberLength
                            false,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        SM_T300I_T300, ModelCapability.ModelInfo(
                            "SM-T300i/T300", arrayOf<String>(),  // modelNameArray
                            Emulation.EscPosMobile,  // Emulation
                            "mini",  // Default portSettings
                            Constants.PAPER_SIZE_THREE_INCH,  // Default paper size
                            false,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            false,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            true,  // canUseBlackMark
                            false,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            false,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            false,  // canUseLed
                            LedModel.None,  // ledModel
                            false,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            0,  // settableUsbSerialNumberLength
                            false,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        SM_T400I, ModelCapability.ModelInfo(
                            "SM-T400i", arrayOf<String>(),  // modelNameArray
                            Emulation.EscPosMobile,  // Emulation
                            "mini",  // Default portSettings
                            Constants.PAPER_SIZE_FOUR_INCH,  // Default paper size
                            false,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            false,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            true,  // canUseBlackMark
                            false,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            false,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            false,  // canUseLed
                            LedModel.None,  // ledModel
                            false,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            0,  // settableUsbSerialNumberLength
                            false,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        SM_L200, ModelCapability.ModelInfo(
                            "SM-L200", arrayOf( // modelNameArray
                                "STAR L200-",
                                "STAR L204-"
                            ),  // <-Bluetooth interface
                            Emulation.StarPRNT,  // Emulation
                            "Portable",  // Default portSettings
                            Constants.PAPER_SIZE_TWO_INCH,  // Default paper size
                            false,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            false,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            true,  // canUseBlackMark
                            true,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            false,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            false,  // canUseLed
                            LedModel.None,  // ledModel
                            false,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            0,  // settableUsbSerialNumberLength
                            false,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        SM_L300, ModelCapability.ModelInfo(
                            "SM-L300", arrayOf( // modelNameArray
                                "STAR L300-",
                                "STAR L304-"
                            ),  // <-Bluetooth interface
                            Emulation.StarPRNTL,  // Emulation
                            "Portable",  // Default portSettings
                            Constants.PAPER_SIZE_THREE_INCH,  // Default paper size
                            false,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            false,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            true,  // canUseBlackMark
                            false,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            false,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            false,  // canUseLed
                            LedModel.None,  // ledModel
                            false,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            0,  // settableUsbSerialNumberLength
                            false,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        BSC10, ModelCapability.ModelInfo(
                            "BSC10", arrayOf( // modelNameArray
                                "BSC10",  // <-LAN model
                                "Star BSC10"
                            ),  // <-USB model
                            Emulation.EscPos,  // Emulation
                            "escpos",  // Default portSettings
                            Constants.PAPER_SIZE_ESCPOS_THREE_INCH,  // Default paper size
                            true,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            false,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            false,  // canUseBlackMark
                            false,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            true,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            false,  // canUseLed
                            LedModel.None,  // ledModel
                            false,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            8,  // settableUsbSerialNumberLength
                            false,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        SM_S210I_StarPRNT, ModelCapability.ModelInfo(
                            "SM-S210i StarPRNT", arrayOf<String>(),  // modelNameArray
                            Emulation.StarPRNT,  // Emulation
                            "Portable",  // Default portSettings
                            Constants.PAPER_SIZE_TWO_INCH,  // Default paper size
                            false,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            false,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            false,  // canUseBlackMark
                            false,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            false,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            false,  // canUseLed
                            LedModel.None,  // ledModel
                            false,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            0,  // settableUsbSerialNumberLength
                            false,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        SM_S220I_StarPRNT, ModelCapability.ModelInfo(
                            "SM-S220i StarPRNT", arrayOf<String>(),  // modelNameArray
                            Emulation.StarPRNT,  // Emulation
                            "Portable",  // Default portSettings
                            Constants.PAPER_SIZE_TWO_INCH,  // Default paper size
                            false,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            false,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            false,  // canUseBlackMark
                            false,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            false,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            false,  // canUseLed
                            LedModel.None,  // ledModel
                            false,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            0,  // settableUsbSerialNumberLength
                            false,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        SM_S230I_StarPRNT, ModelCapability.ModelInfo(
                            "SM-S230i StarPRNT", arrayOf<String>(),  // modelNameArray
                            Emulation.StarPRNT,  // Emulation
                            "Portable",  // Default portSettings
                            Constants.PAPER_SIZE_TWO_INCH,  // Default paper size
                            false,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            false,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            false,  // canUseBlackMark
                            false,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            false,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            false,  // canUseLed
                            LedModel.None,  // ledModel
                            false,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            8,  // settableUsbSerialNumberLength
                            false,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        SM_T300I_T300_StarPRNT,
                        ModelCapability.ModelInfo(
                            "SM-T300i StarPRNT", arrayOf<String>(),  // modelNameArray
                            Emulation.StarPRNT,  // Emulation
                            "Portable",  // Default portSettings
                            Constants.PAPER_SIZE_THREE_INCH,  // Default paper size
                            false,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            false,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            true,  // canUseBlackMark
                            true,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            false,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            false,  // canUseLed
                            LedModel.None,  // ledModel
                            false,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            0,  // settableUsbSerialNumberLength
                            false,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        SM_T400I_StarPRNT, ModelCapability.ModelInfo(
                            "SM-T400i StarPRNT", arrayOf<String>(),  // modelNameArray
                            Emulation.StarPRNT,  // Emulation
                            "Portable",  // Default portSettings
                            Constants.PAPER_SIZE_FOUR_INCH,  // Default paper size
                            false,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            false,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            true,  // canUseBlackMark
                            true,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            false,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            false,  // canUseLed
                            LedModel.None,  // ledModel
                            false,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            0,  // settableUsbSerialNumberLength
                            false,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        SK1_211_221_V211, ModelCapability.ModelInfo(
                            "SK1-211/221/V211", arrayOf( // modelNameArray
                                "SK1-211_221"
                            ),  // <-USB interface
                            Emulation.StarPRNT,  // Emulation
                            "",  // Default portSettings
                            Constants.PAPER_SIZE_SK1_TWO_INCH,  // Default paper size
                            false,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            true,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            true,  // canUseBlackMark
                            true,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            false,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            true,  // canUseLed
                            LedModel.SK,  // ledModel
                            false,  // canUseBlinkLed
                            true,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            16,  // settableUsbSerialNumberLength
                            true,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        SK1_211_221_V211_Presenter,
                        ModelCapability.ModelInfo(
                            "SK1-211/221/V211 Presenter", arrayOf( // modelNameArray
                                "SK1-211_221 Presenter"
                            ),  // <-USB interface
                            Emulation.StarPRNT,  // Emulation
                            "",  // Default portSettings
                            Constants.PAPER_SIZE_SK1_TWO_INCH,  // Default paper size
                            false,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            true,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            true,  // canUseBlackMark
                            true,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            false,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            true,  // canUsePresenter
                            true,  // canUseLed
                            LedModel.SK,  // ledModel
                            true,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            16,  // settableUsbSerialNumberLength
                            true,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        SK1_311_321_V311, ModelCapability.ModelInfo(
                            "SK1-311/321/V311", arrayOf( // modelNameArray
                                "SK1-311_321"
                            ),  // <-USB interface
                            Emulation.StarPRNT,  // Emulation
                            "",  // Default portSettings
                            Constants.PAPER_SIZE_THREE_INCH,  // Default paper size
                            false,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            true,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            true,  // canUseBlackMark
                            true,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            false,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            false,  // canUsePresenter
                            true,  // canUseLed
                            LedModel.SK,  // ledModel
                            false,  // canUseBlinkLed
                            true,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            16,  // settableUsbSerialNumberLength
                            true,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                    put(
                        SK1_311_V311_Presenter,
                        ModelCapability.ModelInfo(
                            "SK1-311/V311 Presenter", arrayOf( // modelNameArray
                                "SK1-311 Presenter"
                            ),  // <-USB interface
                            Emulation.StarPRNT,  // Emulation
                            "",  // Default portSettings
                            Constants.PAPER_SIZE_THREE_INCH,  // Default paper size
                            false,  // canSetDrawerOpenStatus
                            true,  // canPrintTextReceiptSample
                            true,  // canPrintUtf8EncodedText
                            true,  // canPrintRasterReceiptSample
                            false,  // canPrintCjk
                            true,  // canUseBlackMark
                            true,  // canUseBlackMarkDetection
                            true,  // canUsePageMode
                            false,  // canUseCashDrawer
                            false,  // canUseBarcodeReader
                            false,  // canUseCustomerDisplay
                            true,  // canUsePresenter
                            true,  // canUseLed
                            LedModel.SK,  // ledModel
                            true,  // canUseBlinkLed
                            false,  // canUsePaperPresentStatus
                            true,  // canUseAllReceipt
                            false,  // canGetProductSerialNumber
                            16,  // settableUsbSerialNumberLength
                            true,  // isUsbSerialNumberEnabledByDefault
                            false,  // canUseMelodySpeaker
                            -1,  // defaultSoundNumber
                            -1,  // defaultVolume
                            -1,  // volumeMax
                            -1,  // volumeMin
                            false // canUseAutoSwitchInterface
                        )
                    )
                }
            }


        fun getModelTitle(model: Int): String? {
            return ModelCapability.capabilities.get(model).modelTitle
        }

        fun getEmulation(model: Int): Emulation? {
            return ModelCapability.capabilities.get(model).emulation
        }

        fun getPortSettings(model: Int): String? {
            return ModelCapability.capabilities.get(model).defaultPortSettings
        }

        fun canSetDrawerOpenStatus(model: Int): Boolean {
            return ModelCapability.capabilities.get(model).canSetDrawerOpenStatus
        }

        fun canPrintTextReceiptSample(model: Int): Boolean {
            return ModelCapability.capabilities.get(model).canPrintTextReceiptSample
        }

        fun canPrintUtf8EncodedText(model: Int): Boolean {
            return ModelCapability.capabilities.get(model).canPrintUtf8EncodedText
        }

        fun canPrintRasterReceiptSample(model: Int): Boolean {
            return ModelCapability.capabilities.get(model).canPrintRasterReceiptSample
        }

        fun canPrintCjk(model: Int): Boolean {
            return ModelCapability.capabilities.get(model).canPrintCjk
        }

        fun canUseBlackMark(model: Int): Boolean {
            return ModelCapability.capabilities.get(model).canUseBlackMark
        }

        fun canUseBlackMarkDetection(model: Int): Boolean {
            return ModelCapability.capabilities.get(model).canUseBlackMarkDetection
        }

        fun canUsePageMode(model: Int): Boolean {
            return ModelCapability.capabilities.get(model).canUsePageMode
        }

        fun canUseCashDrawer(model: Int): Boolean {
            return ModelCapability.capabilities.get(model).canUseCashDrawer
        }

        fun canUseBarcodeReader(model: Int): Boolean {
            return ModelCapability.capabilities.get(model).canUseBarcodeReader
        }

        fun canUseCustomerDisplay(model: Int, modelName: String): Boolean {
            var canUse: Boolean =
                ModelCapability.capabilities.get(model).canUseCustomerDisplay
            if (model == TSP100) {
                canUse =
                    modelName == ModelCapability.capabilities.get(TSP100).modelTitle || modelName == "Star TSP143IIIU" // Support TSP100IIIU.
                // Not support TSP100LAN, TSP100U, TSP100GT, TSP100IIU, TSP100IIILAN, TSP100IIIW and TSP100IIIBI.
            }
            return canUse
        }

        fun canUsePresenter(model: Int): Boolean {
            return ModelCapability.capabilities.get(model).canUsePresenter
        }

        fun canUseLed(model: Int): Boolean {
            return ModelCapability.capabilities.get(model).canUseLed
        }

        fun getLedModel(model: Int): LedModel? {
            return ModelCapability.capabilities.get(model).ledModel
        }

        fun canUseBlinkLed(model: Int): Boolean {
            return ModelCapability.capabilities.get(model).canUseBlinkLed
        }

        fun canUsePaperPresentStatus(model: Int): Boolean {
            return ModelCapability.capabilities.get(model).canUsePaperPresentStatus
        }

        fun canUseAllReceipt(model: Int): Boolean {
            return ModelCapability.capabilities.get(model).canUseAllReceipt
        }

        fun canGetProductSerialNumber(
            model: Int,
            modelName: String,
            isBluetoothInterface: Boolean
        ): Boolean {
            var canGet: Boolean =
                ModelCapability.capabilities.get(model).canGetProductSerialNumber
            if (model == TSP100) {
                canGet =
                    modelName == ModelCapability.capabilities.get(TSP100).modelTitle || modelName == "TSP143IIILAN (STR_T-001)" || modelName == "TSP143IIIW (STR_T-001)" ||  // Support TSP100IIIW.
                            isBluetoothInterface || modelName == "Star TSP143IIIU" // Support TSP100IIIU.
                // Not support TSP100LAN, TSP100U, TSP100GT and TSP100IIU.
            }
            return canGet
        }

        fun settableUsbSerialNumberLength(
            model: Int,
            modelName: String,
            isUsbInterface: Boolean
        ): Int {
            var length: Int =
                ModelCapability.capabilities.get(model).settableUsbSerialNumberLength
            if (model == TSP100) {
                if (!isUsbInterface) {
                    return 0
                }
                length =
                    if (modelName == ModelCapability.capabilities.get(TSP100).modelTitle || modelName == "Star TSP143IIIU") {                          // TSP100IIIU supports 16digits USB-ID.
                        16
                    } else {                                                              // TSP100U, TSP100GT and TSP100IIU support 8digits USB-ID.
                        8
                    }
            }
            if (model == BSC10 && !isUsbInterface) {                                // It is useless to set a USB serial number to BSC10LAN.
                length = 0
            }
            return length
        }

        fun isUsbSerialNumberEnabledByDefault(model: Int): Boolean {
            return ModelCapability.capabilities.get(model).isUsbSerialNumberEnabledByDefault
        }

        fun canUseMelodySpeaker(model: Int): Boolean {
            return ModelCapability.capabilities.get(model).canUseMelodySpeaker
        }

        fun getDefaultSoundNumber(model: Int): Int {
            return ModelCapability.capabilities.get(model).defaultSoundNumber
        }

        fun getDefaultVolume(model: Int): Int {
            return ModelCapability.capabilities.get(model).defaultVolume
        }

        fun getVolumeMax(model: Int): Int {
            return ModelCapability.capabilities.get(model).volumeMax
        }

        fun getVolumeMin(model: Int): Int {
            return ModelCapability.capabilities.get(model).volumeMin
        }

        fun canUseAutoSwitchInterface(model: Int): Boolean {
            return ModelCapability.capabilities.get(model).canUseAutoSwitchInterface
        }

        /**
         * Get a model index from model name string that can be got by
         * PortInfo.getModelName() or PortInfo.getPortName();
         */
        fun getModel(modelNameSrc: String): Int {
            // Perfect match
            for (i in 0 until ModelCapability.capabilities.size()) {
                for (modelName in ModelCapability.capabilities.valueAt(i).modelNameArray) {
                    if (modelNameSrc == modelName) {
                        return ModelCapability.capabilities.keyAt(i)
                    }
                }
            }

            // Partial match from the head
            for (i in 0 until ModelCapability.capabilities.size()) {
                for (modelName in ModelCapability.capabilities.valueAt(i).modelNameArray) {
                    if (modelNameSrc.startsWith(modelName)) {
                        return ModelCapability.capabilities.keyAt(i)
                    }
                }
            }
            return NONE
        }
    }

    internal class ModelInfo(
        var modelTitle: String,
        var modelNameArray: Array<String>,
        var emulation: Emulation,
        var defaultPortSettings: String,
        var defaultPaperSize: Int,
        var canSetDrawerOpenStatus: Boolean,
        var canPrintTextReceiptSample: Boolean,
        var canPrintUtf8EncodedText: Boolean,
        var canPrintRasterReceiptSample: Boolean,
        var canPrintCjk: Boolean,
        var canUseBlackMark: Boolean,
        var canUseBlackMarkDetection: Boolean,
        var canUsePageMode: Boolean,
        var canUseCashDrawer: Boolean,
        var canUseBarcodeReader: Boolean,
        var canUseCustomerDisplay: Boolean,
        var canUsePresenter: Boolean,
        var canUseLed: Boolean,
        var ledModel: LedModel,
        var canUseBlinkLed: Boolean,
        var canUsePaperPresentStatus: Boolean,
        var canUseAllReceipt: Boolean,
        var canGetProductSerialNumber: Boolean,
        var settableUsbSerialNumberLength: Int,
        var isUsbSerialNumberEnabledByDefault: Boolean,
        var canUseMelodySpeaker: Boolean,
        var defaultSoundNumber: Int,
        var defaultVolume: Int,
        var volumeMax: Int,
        var volumeMin: Int,
        var canUseAutoSwitchInterface: Boolean
    )
}