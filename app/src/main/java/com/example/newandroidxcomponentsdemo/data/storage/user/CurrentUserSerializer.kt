package com.example.newandroidxcomponentsdemo.data.storage.user

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.newandroidxcomponentsdemo.CurrentUser
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object CurrentUserSerializer : Serializer<CurrentUser> {
    override val defaultValue: CurrentUser
        get() = CurrentUser.getDefaultInstance()

    val Context.userDataStore: DataStore<CurrentUser> by dataStore(
        fileName = "user.pb",
        serializer = CurrentUserSerializer
    )

    override suspend fun readFrom(input: InputStream): CurrentUser {
        try {
            return CurrentUser.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: CurrentUser, output: OutputStream) = t.writeTo(output)
}