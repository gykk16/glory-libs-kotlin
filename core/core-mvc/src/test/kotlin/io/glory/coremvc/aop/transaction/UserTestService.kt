package io.glory.coremvc.aop.transaction

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class UserTestService(private val userTestRepository: UserTestRepository) {

    fun findById(id: Long) = userTestRepository.findById(id)

    fun findAll() = userTestRepository.findAll()

    fun save(names: List<String>): List<User> = Tx.write {
        val users = names.map(::User)
        userTestRepository.saveAll(users)

        return@write userTestRepository.findAll()
    }

    fun saveReadOnly(names: List<String>): List<User> = Tx.readOnly {
        val users = names.map(::User)
        userTestRepository.saveAll(users)

        return@readOnly userTestRepository.findAll()
    }

}