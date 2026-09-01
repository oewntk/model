package org.oewntk.model

import java.io.Serializable

/**
* Example
*/
@kotlinx.serialization.Serializable
data class Example(val text: String, val source: String? = null) : Serializable
