package com.project.reddit.domain

import com.project.reddit.entity.UserCommentData
import com.project.reddit.repository.RoomRepository
import javax.inject.Inject

class UserCommentDataUseCase @Inject constructor(private val roomRepository: RoomRepository) {

    fun getUserCommentData(): List<UserCommentData> {
        return roomRepository.getUserCommentDataDao().getUserCommentData()
    }

    fun saveUserCommentData(userCommentData: UserCommentData) {
        roomRepository.getUserCommentDataDao().saveUserCommentData(userCommentData)
    }

    fun deleteUserCommentData() {
        roomRepository.getUserCommentDataDao().deleteUserCommentData()
    }
}