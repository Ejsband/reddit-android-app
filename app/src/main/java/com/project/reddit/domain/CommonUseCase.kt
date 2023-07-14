package com.project.reddit.domain

import com.project.reddit.entity.User
import com.project.reddit.repository.CommonRetrofitRepository
import javax.inject.Inject

class CommonUseCase @Inject constructor(private val commonRetrofitRepository: CommonRetrofitRepository) {

    suspend fun getUser(accessToken: String): User {
        return commonRetrofitRepository.getUser(accessToken)
    }
}