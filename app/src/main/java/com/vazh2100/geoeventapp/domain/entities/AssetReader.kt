package com.vazh2100.geoeventapp.domain.entities

import android.content.Context

class AssetReader {
    fun readJsonFromAsset(context: Context, fileName: String = "events.json"): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText().trim() }
    }
}