package io.glory.coremvc.aop.transaction;

import org.springframework.data.jpa.repository.JpaRepository

interface UserTestRepository : JpaRepository<User, Long> {
}