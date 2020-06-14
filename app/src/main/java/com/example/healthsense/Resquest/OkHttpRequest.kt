package com.example.healthsense.Resquest

import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject


class OkHttpRequest(client: OkHttpClient) {

    internal var client = OkHttpClient()

    init {
        this.client = client
    }

    fun POST(url: String, js: JSONObject , callback: Callback): Call {

        val client = OkHttpClient()
        val body: RequestBody = RequestBody.create(JSON, js.toString())

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)

        return call
    }

    fun GET(url: String,header: JSONArray , callback: Callback): Call {
        val request = Request.Builder()
            .url(url)

            if (header.length()>0){
                for (i in 0 until header.length()) {
                    val name = header.getJSONObject(i).keys().next()
                    request.addHeader(name, header.getJSONObject(i).getString(name))
                }
            }

        val call  = client.newCall(request.build())

        call.enqueue(callback)
        return call
    }

    companion object {
        val JSON = MediaType.parse("application/json; charset=utf-8")
    }
}