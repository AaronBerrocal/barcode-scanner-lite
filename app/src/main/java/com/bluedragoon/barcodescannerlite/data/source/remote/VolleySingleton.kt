package com.bluedragoon.barcodescannerlite.data.source.remote

import android.content.Context
import com.bluedragoon.barcodescannerlite.utils.GENERIC_VOLLEY_TAG
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import javax.inject.Inject

class VolleySingleton @Inject constructor(
    context: Context
) {

    val requestQueue: RequestQueue by lazy{
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>){
        req.tag = GENERIC_VOLLEY_TAG
        requestQueue.add(req)
    }

    fun <T> addToRequestQueue(req: Request<T>, tag: String?){
        req.tag = tag ?: GENERIC_VOLLEY_TAG
        requestQueue.add(req)
    }

    fun cancelPendingRequests(tag: Any){
        requestQueue.cancelAll(tag)
    }

//    val future = RequestFuture.newFuture<JSONObject>()

}