package com.project.reddit.domain

import com.project.reddit.entity.AccessTokenData
import com.project.reddit.repository.RoomRepository
import javax.inject.Inject

class AccessTokenDataUseCase @Inject constructor(private val roomRepository: RoomRepository) {

    fun getAccessToken(id: String): AccessTokenData {
        return roomRepository.getAccessTokenDataDao().getAccessTokenData(id)
    }

    fun saveAccessToken(accessTokenData: AccessTokenData) {
        roomRepository.getAccessTokenDataDao().saveAccessTokenData(accessTokenData)
    }

    fun updateAccessToken(accessTokenData: AccessTokenData) {
        roomRepository.getAccessTokenDataDao().updateAccessTokenData(accessTokenData)
    }

    fun deleteAccessToken(accessTokenData: AccessTokenData) {
        roomRepository.getAccessTokenDataDao().deleteAccessTokenData(accessTokenData)
    }
}