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
 * @param createdAt
 * @param description
 * @param id
 * @param lastActivityAt
 * @param name
 * @param numberWordsInList
 * @param permalink
 * @param type
 * @param updatedAt
 * @param userId
 * @param username
 */
data class WordList(
        val id: Long,
        val createdAt: java.time.LocalDateTime? = null,
        val description: String? = null,
        val lastActivityAt: java.time.LocalDateTime? = null,
        val name: String? = null,
        val numberWordsInList: Long? = null,
        val permalink: String? = null,
        val type: String? = null,
        val updatedAt: java.time.LocalDateTime? = null,
        val userId: Long? = null,
        val username: String? = null
)

