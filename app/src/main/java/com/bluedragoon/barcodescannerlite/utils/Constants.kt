package com.bluedragoon.barcodescannerlite.utils

//CODES
const val CAMERA_REQUEST_CODE = 101

//DATE AND TIME
const val ISO_DATE_TIME_PATTERN: String = "yyyy-MM-dd'T'HH:mm:ss"

//DATABASE
const val DATABASE_NAME: String = "barcode_scanner_lite_database"

//NETWORKING
//enum class RestResponseProperties(val keyName: String){
//    STATUS("status"),
//    MESSAGE("message"),
//    DETAILS("details"),
//    RECORDS("records")
//}

const val REST_PROPERTY_STATUS: String = "status"
const val REST_PROPERTY_MESSAGE: String = "message"
const val REST_PROPERTY_DETAILS: String = "details"
const val REST_PROPERTY_RECORDS: String = "records"

const val SCANNER_STATE_ENABLED: String = "Habilitado"
const val SCANNER_STATE_STAND_BY: String = "En espera"
const val SCANNER_STATE_SCANNING: String = "Escanéando"
const val SCANNER_STATE_CODE_FOUND: String = "Código Encontrado"
const val SCANNER_STATE_DISABLED: String = "Deshabilitado"

const val REQUEST_SUCCESSFUL_VALUE : String = "success"

const val GENERIC_VOLLEY_TAG : String = "com.bluedragoon.barcodescannerlite.GENERIC_TAG"
const val SCANNABLE_PRODUCTS_CATALOG_VOLLEY_TAG : String = "com.bluedragoon.barcodescannerlite.SCANNABLE_PRODUCTS_CATALOG_TAG"

const val SCANNABLE_PRODUCTS_CATALOG_MOCK_DATA_URL: String = "https://api.npoint.io/30ddcb8cd373a8b47411"