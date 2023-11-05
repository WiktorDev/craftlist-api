package tech.witkor.services.web.entities.exceptions

class ConflictException(override var message: String): RuntimeException(message)
class ValidationException(violations: Map<String, String>) : Exception(violations.toString())