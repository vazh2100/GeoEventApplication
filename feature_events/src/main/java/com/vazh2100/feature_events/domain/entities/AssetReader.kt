package com.vazh2100.feature_events.domain.entities

import android.content.Context

class AssetReader(private val context: Context) {
    fun readJsonFromAsset(fileName: String = "events.json"): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText().trim() }
    }
}
