package com.project.reddit.domain

import com.project.reddit.entity.UserSavedPostData
import com.project.reddit.repository.RoomRepository
import javax.inject.Inject

class UserSavedPostDataUseCase @Inject constructor(private val roomRepository: RoomRepository) {

    fun getUserSavedPostData(): List<UserSavedPostData> {
        return roomRepository.getUserSavedPostDataDao().getUserSavedPostData()
    }

    fun saveUserSavedPostData(userSavedPostData: UserSavedPostData) {
        roomRepository.getUserSavedPostDataDao().saveUserSavedPostData(userSavedPostData)
    }

    fun deleteUserSavedPostData() {
        roomRepository.getUserSavedPostDataDao().deleteUserSavedPostData()
    }
}