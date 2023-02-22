package com.bluedragoon.barcodescannerlite.data.source.remote

import org.json.JSONObject

interface VolleyRestCallbacks {

    fun onSuccess(response: JSONObject?, tag: String)

    fun onError(error: String, tag: String)

}