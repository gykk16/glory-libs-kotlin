package io.glory.coremvc.aop.transaction

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    @Column(nullable = false)
    val name: String,
) {

    constructor(name: String) : this(null, name)
}