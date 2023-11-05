package tech.witkor.services.web.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Users: IntIdTable("users") {
    val email = varchar("email", 64)
    val username = varchar("username", 64)
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object: IntEntityClass<User>(Users)
    val email by Users.email
    val username by Users.username
}