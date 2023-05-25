package com.aimelodies.controllers.assemblers.mocks

import org.springframework.beans.PropertyEditorRegistry
import org.springframework.validation.BindingResult
import org.springframework.validation.Errors
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import java.beans.PropertyEditor

object MockBindingResult : BindingResult {
    override fun getObjectName(): String {
        TODO("Not yet implemented")
    }

    override fun setNestedPath(nestedPath: String) {
        TODO("Not yet implemented")
    }

    override fun getNestedPath(): String {
        TODO("Not yet implemented")
    }

    override fun pushNestedPath(subPath: String) {
        TODO("Not yet implemented")
    }

    override fun popNestedPath() {
        TODO("Not yet implemented")
    }

    override fun reject(errorCode: String) {
        TODO("Not yet implemented")
    }

    override fun reject(errorCode: String, defaultMessage: String) {
        TODO("Not yet implemented")
    }

    override fun reject(errorCode: String, errorArgs: Array<out Any>?, defaultMessage: String?) {
        TODO("Not yet implemented")
    }

    override fun rejectValue(field: String?, errorCode: String) {
        TODO("Not yet implemented")
    }

    override fun rejectValue(field: String?, errorCode: String, defaultMessage: String) {
        TODO("Not yet implemented")
    }

    override fun rejectValue(field: String?, errorCode: String, errorArgs: Array<out Any>?, defaultMessage: String?) {
        TODO("Not yet implemented")
    }

    override fun addAllErrors(errors: Errors) {
        TODO("Not yet implemented")
    }

    override fun hasErrors(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getErrorCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getAllErrors(): MutableList<ObjectError> {
        TODO("Not yet implemented")
    }

    override fun hasGlobalErrors(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getGlobalErrorCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getGlobalErrors(): MutableList<ObjectError> {
        TODO("Not yet implemented")
    }

    override fun getGlobalError(): ObjectError? {
        TODO("Not yet implemented")
    }

    override fun hasFieldErrors(): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasFieldErrors(field: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getFieldErrorCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getFieldErrorCount(field: String): Int {
        TODO("Not yet implemented")
    }

    override fun getFieldErrors(): MutableList<FieldError> {
        TODO("Not yet implemented")
    }

    override fun getFieldErrors(field: String): MutableList<FieldError> {
        TODO("Not yet implemented")
    }

    override fun getFieldError(): FieldError? {
        TODO("Not yet implemented")
    }

    override fun getFieldError(field: String): FieldError? {
        TODO("Not yet implemented")
    }

    override fun getFieldValue(field: String): Any? {
        TODO("Not yet implemented")
    }

    override fun getFieldType(field: String): Class<*>? {
        TODO("Not yet implemented")
    }

    override fun getTarget(): Any? {
        TODO("Not yet implemented")
    }

    override fun getModel(): MutableMap<String, Any> {
        TODO("Not yet implemented")
    }

    override fun getRawFieldValue(field: String): Any? {
        TODO("Not yet implemented")
    }

    override fun findEditor(field: String?, valueType: Class<*>?): PropertyEditor? {
        TODO("Not yet implemented")
    }

    override fun getPropertyEditorRegistry(): PropertyEditorRegistry? {
        TODO("Not yet implemented")
    }

    override fun resolveMessageCodes(errorCode: String): Array<String> {
        TODO("Not yet implemented")
    }

    override fun resolveMessageCodes(errorCode: String, field: String): Array<String> {
        TODO("Not yet implemented")
    }

    override fun addError(error: ObjectError) {
        TODO("Not yet implemented")
    }
}