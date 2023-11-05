package tech.witkor.services.web.utilities.validation

import io.konform.validation.ValidationError
import io.konform.validation.ValidationResult
import tech.witkor.services.web.entities.exceptions.ValidationException

class ValidationHelper(result: ValidationResult<*>?) {
    private val violations: MutableMap<String, String> = HashMap()
    init {
        result!!.errors.forEach {
            this.violation(it)
        }
    }
    private fun violation(validationError: ValidationError) {
        violations[validationError.dataPath.replace(".", "")] = validationError.message
    }
    fun throwError() {
        if (violations.isNotEmpty()) throw ValidationException(violations)
    }
}
