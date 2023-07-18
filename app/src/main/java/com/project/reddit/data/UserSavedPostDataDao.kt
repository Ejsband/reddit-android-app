package com.project.reddit.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.reddit.entity.UserSavedPostData

@Dao
interface UserSavedPostDataDao {

    @Query("SELECT * FROM user_saved_post_data")
    fun getUserSavedPostData(): List<UserSavedPostData>

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = UserSavedPostData::class)
    fun saveUserSavedPostData(userSavedPostData: UserSavedPostData)

    @Query("DELETE FROM user_saved_post_data")
    fun deleteUserSavedPostData()
}