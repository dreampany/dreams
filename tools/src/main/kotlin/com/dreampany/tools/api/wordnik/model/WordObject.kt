/**
 * api.wordnik.com
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 4.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
package com.dreampany.tools.api.wordnik.model


/**
 *
 * @param canonicalForm
 * @param id
 * @param originalWord
 * @param suggestions
 * @param vulgar
 * @param word
 */
data class WordObject(
        val id: Long,
        val canonicalForm: String? = null,
        val originalWord: String? = null,
        val suggestions: Array<String>? = null,
        val vulgar: String? = null,
        val word: String? = null
)

