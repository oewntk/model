/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */
package org.oewntk.model

import org.oewntk.model.Key.W_P_A
import java.io.Serializable
import java.util.*

/**
 * Lexical item/unit/entry
 * The basic container of senses.
 * Can be thought of as the pair of a key and value (k, senses).
 * The value is the set of senses while the key is made up of member elements, depending on the key.
 * @property lemma         lemma
 * @property source        source
 * @property type          type ss_type {n, v, a, r, s}
 * @property partOfSpeech  type ss_type {n, v, a, r}
 * @property lCLemma       lower-cased lemma
 * @param    code          code
 *
 */
class Lex(
    /**
     * Lemma written form
     */
    @JvmField val lemma: String,

    /**
     * Code
     */
    code: String,

    /**
     * Source file
     */
    val source: String?

) : Serializable //, Comparable<Lex>
{
    /**
     * Lemma lower-cased written form
     */
    val lCLemma: String
        get() = lemma.lowercase()

    /**
     * Whether lemma has uppercase
     */
    val isCased: Boolean
        get() = lemma != lCLemma

    /**
     * Synset type ss_type {n, v, a, r, s}
     */
    @JvmField
    val type: Char = code[0]

    /**
     * Part-of-speech (sme as part-of-speech except for satellite adjective)
     */
    val partOfSpeech: Char
        get() = if (this.type == 's') 'a' else this.type

    /**
     * Discriminant amongst same-type lexes appended to type that distinguishes same-type lexes (because of pronunciation or morphological forms )
     * Current values are '-1','-2'
     */
    @JvmField
    val discriminant: String? = if (code.length > 1) code.substring(1) else null

    /**
     * Senses
     */
    lateinit var senses: MutableList<Sense>

    /**
     * Set senses
     *
     * @param senses senses
     */
    fun setSenses(senses: Array<Sense>) {
        this.senses.addAll(listOf(*senses))
    }

    /**
     * Add sense to the list fo senses
     *
     * @param sense sense
     */
    fun addSense(sense: Sense) {
        senses.add(sense)
    }

    /**
     * Senses as a set
     */
    val sensesAsSet: Set<Sense>
        get() = LinkedHashSet(senses)

    /**
     * Morphological forms
     */
    var forms: Array<out String>? = null
        private set

    /**
     * Set morphological forms
     *
     * @param forms morphological forms
     * @return this
     */
    fun setForms(vararg forms: String): Lex {
        this.forms = forms
        return this
    }

    /**
     * Pronunciations
     */
    var pronunciations: Array<Pronunciation>? = null
        private set

    /**
     * Set pronunciations
     *
     * @param pronunciations pronunciations
     * @return this
     */
    fun setPronunciations(pronunciations: Array<Pronunciation>?): Lex {
        this.pronunciations = pronunciations
        return this
    }

    // stringify

    override fun toString(): String {
        val pronunciationsStr = Formatter.join(pronunciations, ",")
        val sensesStr = Formatter.join(senses, ",")
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

    companion object {

        // ordering, this is used when sorted maps are made

        val comparatorByKeyOEWN: Comparator<Lex> = Comparator { thisLex: Lex, thatLex: Lex ->
            val thisKey = W_P_A.of_t(thisLex)
            val thatKey = W_P_A.of_t(thatLex)
            thisKey.compareTo(thatKey)
        }
    }
}
