package com.project.reddit.domain

import com.project.reddit.entity.UserSubmittedPostData
import com.project.reddit.repository.RoomRepository
import javax.inject.Inject

class UserSubmittedPostDataUseCase @Inject constructor(private val roomRepository: RoomRepository) {

    fun getUserSubmittedPostData(): List<UserSubmittedPostData> {
        return roomRepository.getUserSubmittedPostDataDao().getUserSubmittedPostData()
    }

    fun saveUserSubmittedPostData(userSubmittedPostData: UserSubmittedPostData) {
        roomRepository.getUserSubmittedPostDataDao().saveUserSubmittedPostData(userSubmittedPostData)
    }

    fun deleteUserSubmittedPostData() {
        roomRepository.getUserSubmittedPostDataDao().deleteUserSubmittedPostData()
    }
}