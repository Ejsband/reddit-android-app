package com.project.reddit.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.reddit.entity.UserCommentData

@Dao
interface UserCommentDataDao {

    @Query("SELECT * FROM user_comment_data")
    fun getUserCommentData(): List<UserCommentData>

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = UserCommentData::class)
    fun saveUserCommentData(userCommentData: UserCommentData)

    @Query("DELETE FROM user_comment_data")
    fun deleteUserCommentData()
}