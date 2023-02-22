package com.bluedragoon.barcodescannerlite.utils

enum class ScannerState(val stateName: String) {
    ENABLED(SCANNER_STATE_ENABLED),
    STAND_BY(SCANNER_STATE_STAND_BY),
    SCANNING(SCANNER_STATE_SCANNING),
    CODE_FOUND(SCANNER_STATE_CODE_FOUND),
    DISABLED(SCANNER_STATE_DISABLED)

}