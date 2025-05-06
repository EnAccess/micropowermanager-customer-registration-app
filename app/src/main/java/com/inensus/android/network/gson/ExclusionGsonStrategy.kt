package com.inensus.android.network.gson

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes

class ExclusionGsonStrategy : ExclusionStrategy {
    override fun shouldSkipClass(clazz: Class<*>?) = false

    override fun shouldSkipField(f: FieldAttributes) =
        f.getAnnotation(Exclude::class.java) != null
}
