package com.bluedragoon.barcodescannerlite.data.source.remote

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import java.net.HttpURLConnection
import javax.inject.Inject

class RestRequestsManager @Inject constructor() {

    fun executeGetRequest(
        url: String,
        tag: String,
        volleySingleton: VolleySingleton,
        responseCallback: VolleyRestCallbacks
    ){

        var responseStatusCode: Int = HttpURLConnection.HTTP_INTERNAL_ERROR

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener{ response ->
                when(responseStatusCode / 100){
                    2 -> {
                        responseCallback.onSuccess(response, tag)
                    }
//                    3 -> {
//                        responseCallback.onError("3xx $response", tag)
//                    }
                    4 -> {
                        responseCallback.onError("4xx $response", tag)
                    }
                    5 -> {
                        responseCallback.onError("5xx $response", tag)
                    }
                }
            },
            Response.ErrorListener{ error ->
                responseCallback.onError(error.message.toString(), tag)
            }
        ){
            override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
                responseStatusCode = response!!.statusCode
                return super.parseNetworkResponse(response)
            }
        }

        volleySingleton.addToRequestQueue(jsonObjectRequest, tag)

    }

}