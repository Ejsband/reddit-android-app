package com.project.reddit.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.reddit.entity.UserSubmittedPostData

@Dao
interface UserSubmittedPostDataDao {

    @Query("SELECT * FROM user_submitted_post_data")
    fun getUserSubmittedPostData(): List<UserSubmittedPostData>

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = UserSubmittedPostData::class)
    fun saveUserSubmittedPostData(userSubmittedPostData: UserSubmittedPostData)

    @Query("DELETE FROM user_submitted_post_data")
    fun deleteUserSubmittedPostData()
}