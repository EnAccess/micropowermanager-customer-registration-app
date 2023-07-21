package com.inensus.android.base.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.inensus.android.base.lifecycle.LiveEvent

class SessionExpireBroadcastReceiver : BroadcastReceiver() {

    val event = LiveEvent<Unit>()

    override fun onReceive(context: Context?, intent: Intent?) {
        event.postValue(Unit)
    }

    companion object {
        const val SESSION_EXPIRE_INTENT_ACTION = "sessionExpireIntentAction"
    }
}