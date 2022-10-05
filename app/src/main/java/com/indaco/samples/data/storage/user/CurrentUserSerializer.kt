package com.indaco.samples.data.storage.user

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.indaco.samples.CurrentUser
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object CurrentUserSerializer : Serializer<com.indaco.samples.CurrentUser> {
    override val defaultValue: com.indaco.samples.CurrentUser
        get() = com.indaco.samples.CurrentUser.getDefaultInstance()

    val Context.userDataStore: DataStore<com.indaco.samples.CurrentUser> by dataStore(
        fileName = "user.pb",
        serializer = CurrentUserSerializer
    )

    override suspend fun readFrom(input: InputStream): com.indaco.samples.CurrentUser {
        try {
            return com.indaco.samples.CurrentUser.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: com.indaco.samples.CurrentUser, output: OutputStream) = t.writeTo(output)
}