package com.bluedragoon.barcodescannerlite.utils

enum class RestResponseProperties(val keyName: String){
    STATUS(REST_PROPERTY_STATUS),
    MESSAGE(REST_PROPERTY_MESSAGE),
    DETAILS(REST_PROPERTY_DETAILS),
    RECORDS(REST_PROPERTY_RECORDS)
}