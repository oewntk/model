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
 * @property lemma          lemma written form
 * @property type           type ss_type {'n', 'v', 'a', 'r', 's'}
 * @property lexfile        source
 * @property senseKeys      senses
 * @property generated      whether the lex entry was generated (as apposed to read from data)
 *
 * @property forms          morphological forms
 * @property pronunciations pronunciations
 *
 * @property lCLemma        lower-cased lemma
 * @property isCased        whether lemma contains uppercase
 * @property partOfSpeech   synset part-of-speech {'n', 'v', 'a', 'r'} with ss_type 's' (satellite adj) mapped to 'a'
 * @property key2           unique key (at YAML level 2) made up of part-of-speech and discriminant
 * @property lexfile        the lexfile it is expected to be output to ("entries-lexfileChar.yaml")
 * @property discriminant   discriminates same type entries
 */
@kotlinx.serialization.Serializable
data class Lex(

    // key
    val lemma: Lemma,
    val type: SynsetType,
    val discriminant: Discriminant?,

    // value
    var senseKeys: List<SenseKey>,

    // state
    val generated: Boolean = false
) : Serializable {

    // properties
    var forms: Set<Morph>? = null
    var pronunciations: Set<Pronunciation>? = null

    // computed properties
    val lCLemma: LowerCasedLemma
        get() = lemma.lowercase(Locale.ENGLISH)
    val isCased: Boolean
        get() = lemma != lCLemma
    val partOfSpeech: PartOfSpeech
        get() = type.toPartOfSpeech()
    val lexfileChar: Char
        get() {
            val c = lemma[0].lowercaseChar()
            return if (c !in 'a' until 'z' + 1) '0' else c
        }
    val lexfile: String
        get() = "entries-$lexfileChar.yaml"

    // computed properties
    val value: Set<SenseKey>
        get() = senseKeys.toSet()
    val key: Triple<Lemma, Char, Discriminant?>
        get() = Triple(lemma, type.value, discriminant)
    val key2: String
        get() = if (discriminant != null) "${type.value}$discriminant" else type.value.toString()

    /**
     * Constructor
     *
     * @param lemma             lemma written form
     * @param code              ss_type with a possible discriminant appended in the form '-number'
     * @param generated         generated
     */
    constructor (
        lemma: Lemma,
        code: String,
        senseKeys: List<SenseKey> = ArrayList(),
        generated: Boolean = false
    ) : this(lemma, SynsetType.fromChar(code.lowercase(Locale.ENGLISH)[0]), if (code.length > 1) code.substring(1) else null, senseKeys, generated = generated)

    // stringify

    override fun toString(): String {
        val pronunciations = pronunciations?.joinToString(",") ?: ""
        val senses = senseKeys.joinToString(",")
        return "$lemma $key2 $pronunciations {$senses}"
    }

    // identify

    override fun equals(other: Any?): Boolean {
        // throw UnsupportedOperationException("$this / $other")
        return if (other is Lex) Objects.equals(value, other.value) else false
    }

    override fun hashCode(): Int {
        // throw UnsupportedOperationException("$this")
        return Objects.hash(value)
    }

    object Groups {

        /**
         * Hyper map
         * level 1 key=lemma
         * level 2 key=type or type-discriminant
         * lexes = map[lemma][key2]
         */
        fun Sequence<Lex>.groupByLemmaThenByKey2(): Map<Lemma, Map<Key2, Collection<Lex>>> {
            return this
                .groupBy(Lex::lemma)
                .mapValues { (_: Lemma, lexes: Collection<Lex>) ->
                    lexes.groupBy(Lex::key2)
                }
        }

        /**
         * Hyper map
         * level 1 key=lemma
         * level 2 key=type or type-discriminant
         * lexes = map[lemma][key2]
         */
        fun Sequence<Lex>.groupByLCLemmaThenByKey2(): Map<LowerCasedLemma, Map<Key2, Collection<Lex>>> {
            return this
                .groupBy(Lex::lCLemma)
                .mapValues { (_: LowerCasedLemma, lexes: Collection<Lex>) ->
                    lexes.groupBy(Lex::key2)
                }
        }

        /**
         * Resolver (hyper map that has one level 3 value)
         * lexes = map[lemma][key2]
         * lex = map[lemma][key2]
         * @param throws throws exception if result is not one lex, otherwise it silently takes the first value
         */
        fun Sequence<Lex>.lexByLemmaThenByKey2(throws: Boolean = true): Map<Lemma, Map<Key2, Lex>> {
            return this
                .groupBy(Lex::lemma)
                .mapValues { (_: Lemma, lexes: Collection<Lex>) ->
                    lexes
                        .groupBy(Lex::key2)
                        .mapValues {
                            if (throws && it.value.size != 1)
                                throw IllegalStateException(it.value.joinToString())
                            it.value.first()
                        }
                }
        }

        /**
         * Group lexes by case-sensitive lemma
         *
         * @receiver lexes
         * @return lexes grouped by (case-sensitive) lemma
         */
        fun Sequence<Lex>.groupByLemma(): Map<Lemma, Collection<Lex>> {
            return this
                .groupBy(Lex::lemma)
                .mapValues { it.value.toSet() }
        }

        /**
         * Group lexes by lower-cased lemma
         *
         * @receiver lexes
         * @return lexes grouped by lowercased lemma
         */
        fun Sequence<Lex>.groupByLCLemma(): Map<LowerCasedLemma, Collection<Lex>> {
            return this
                .groupBy(Lex::lCLemma)
                .mapValues { it.value.toSet() }
        }
    }
}
