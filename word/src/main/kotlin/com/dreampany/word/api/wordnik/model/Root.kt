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
package com.dreampany.word.api.wordnik.model


/**
 *
 * @param categories
 * @param id
 * @param name
 */
data class Root(
        val id: Long,
        val categories: Array<String>? = null,
        val name: String? = null
)

