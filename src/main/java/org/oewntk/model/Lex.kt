/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import java.io.Serializable
import java.util.*

/**
 * Lexical item/unit/entry
 * The basic container of senses.
 * Can be thought of as the pair of a key and value (k, senses).
 * The value is the set of senses while the key is made up of member elements, depending on the key.
 *
 * @param lemma             lemma written form
 * @param code              ss_type with a possible discriminant appended in the form '-number'
 * @param source            source
 *
 * @property lemma          lemma written form
 * @property type           type ss_type {'n', 'v', 'a', 'r', 's'}
 * @property source         source
 * @property senses         senses
 * @property forms          morphological forms
 * @property pronunciations pronunciations
 *
 * @property lCLemma        lower-cased lemma
 * @property isCased        whether lemma contains uppercase
 * @property partOfSpeech   synset part-of-speech {'n', 'v', 'a', 'r'} with ss_type 's' (satellite adj) mapped to 'a'
 * @property discriminant   discriminates same type entries
 */
class Lex(

    val lemma: String,
    code: String,
    val source: String?,

    ) : Serializable /*, Comparable<Lex> */ {

    val type: Char = code[0]
    lateinit var senses: MutableList<Sense>
    var forms: Array<out String>? = null
    var pronunciations: Array<Pronunciation>? = null

    val lCLemma: String
        get() = lemma.lowercase()
    val isCased: Boolean
        get() = lemma != lCLemma
    val partOfSpeech: Char
        get() = if (this.type == 's') 'a' else this.type
    val discriminant: String? = if (code.length > 1) code.substring(1) else null

    // stringify

    override fun toString(): String {
        val pronunciationsStr = pronunciations?.joinToString(",") ?: ""
        val sensesStr = senses.joinToString(",")
        return "$lemma $type${discriminant ?: ""} $pronunciationsStr {$sensesStr}"
    }

    // identify

    override fun equals(other: Any?): Boolean {
        throw UnsupportedOperationException("Either compare values using 'sensesAsSet' or keys using 'Key.OEWN.of', 'Key.PWN.of', etc")
    }

    override fun hashCode(): Int {
        var result = Objects.hash(lemma, type, discriminant)
        result = 31 * result + pronunciations.contentHashCode()
        return result
    }
}
