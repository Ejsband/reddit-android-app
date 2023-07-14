package com.project.reddit.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.project.reddit.entity.AccessTokenData

@Dao
interface AccessTokenDataDao {

    @Query("SELECT * FROM access_token_data WHERE id=:id")
    fun getAccessTokenData(id: String): AccessTokenData

    @Insert(entity = AccessTokenData::class)
    fun saveAccessTokenData(accessTokenData: AccessTokenData)

    @Update
    fun updateAccessTokenData(accessTokenData: AccessTokenData)

    @Delete(entity = AccessTokenData::class)
    fun deleteAccessTokenData(accessTokenData: AccessTokenData)
}