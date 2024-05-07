/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.oewntk.model.Key.*

/**
 * Keys extended with a functional interface that extract data R from model
 *
 * @param R result
 */
interface KeyF<R> : Key, (CoreModel) -> R {

    /**
     * Interface for single value output
     */
    interface MonoValued : KeyF<Lex>

    /**
     * Interface for multiple values (array) output
     */
    interface MultiValued : KeyF<Array<Lex>>

    /**
     * (Word, PosOrType)
     *
     * @param word    word: lemma or LC lemma
     * @param pos pos type: part-of-speech or type
     * @property wordExtractor word extractor function from lex
     * @property posExtractor part-of-speech or type extractor function from lex
     */
    open class F_W_P private constructor(
        override var word: LemmaType,
        override var pos: PosType,
        val wordExtractor: (Lex) -> LemmaType,
        val posExtractor: (Lex) -> PosType,

        ) : W_P(word, pos) {

        override fun toLongString(): String = "KEYF ${javaClass.simpleName} WP_${toWordExtractorString(wordExtractor)}_${toPosTypeExtractorString(posExtractor)} $this"

        /**
         * Functional part that yields single value
         */
        class Mono private constructor(
            word: LemmaType,
            pos: PosType,
            wordExtractor: (Lex) -> LemmaType,
            posExtractor: (Lex) -> PosType,
        ) : F_W_P(word, pos, wordExtractor, posExtractor), MonoValued {

            override fun invoke(model: CoreModel): Lex {
                return Finder.getLexesHaving(model, word, pos, wordExtractor, posExtractor)
                    .firstOrNull() ?: throw IllegalArgumentException()
            }

            companion object {

                fun of(
                    wordExtractor: (Lex) -> LemmaType,
                    posExtractor: (Lex) -> PosType,
                    lex: Lex,
                ): Mono {
                    return Mono(
                        wordExtractor.invoke(lex),
                        posExtractor.invoke(lex),
                        wordExtractor,
                        posExtractor
                    )
                }

                fun from(
                    wordExtractor: (Lex) -> LemmaType,
                    posExtractor: (Lex) -> PosType,
                    word: LemmaType,
                    pos: PosType,
                ): Mono {
                    return Mono(word, pos, wordExtractor, posExtractor)
                }
            }
        }

        /**
         * Functional part that yields multiple values (array)
         */
        class Multi private constructor(
            word: LemmaType,
            pos: PosType,
            wordExtractor: (Lex) -> LemmaType,
            posExtractor: (Lex) -> PosType,
        ) : F_W_P(word, pos, wordExtractor, posExtractor), MultiValued {

            override fun invoke(model: CoreModel): Array<Lex> {
                return Finder.getLexesHaving(model, word, pos, wordExtractor, posExtractor)
                    .toList()
                    .toTypedArray()
            }

            companion object {

                fun of(
                    wordExtractor: (Lex) -> LemmaType,
                    posExtractor: (Lex) -> PosType,
                    lex: Lex,
                ): Multi {
                    return Multi(
                        wordExtractor.invoke(lex),
                        posExtractor.invoke(lex),
                        wordExtractor,
                        posExtractor
                    )
                }

                fun from(
                    wordExtractor: (Lex) -> LemmaType,
                    posExtractor: (Lex) -> PosType,
                    word: LemmaType,
                    pos: PosType,
                ): Multi {
                    return Multi(word, pos, wordExtractor, posExtractor)
                }
            }
        }

        companion object {

            fun of(
                lex: Lex,
                wordExtractor: (Lex) -> LemmaType,
                posExtractor: (Lex) -> PosType,
            ): F_W_P {
                return F_W_P(
                    wordExtractor.invoke(lex),
                    posExtractor.invoke(lex),
                    wordExtractor,
                    posExtractor
                )
            }
        }
    }

    /**
     * (Word, PosOrType, Pronunciations)
     *
     * @param word    word: lemma or LC lemma
     * @param pos pos type: part-of-speech or type
     * @param pronunciations pronunciations
     * @property wordExtractor word extractor function from lex
     * @property posExtractor part-of-speech or type extractor function from lex
     */
    open class F_W_P_A private constructor(
        word: LemmaType,
        pos: PosType,
        pronunciations: Set<Pronunciation>?,
        val wordExtractor: (Lex) -> LemmaType,
        val posExtractor: (Lex) -> PosType,
    ) : W_P_A(word, pos, pronunciations) {

        override fun toLongString(): String = "KEYF ${javaClass.simpleName} WPA_${toWordExtractorString(wordExtractor)}_${toPosTypeExtractorString(posExtractor)} $this"

        /**
         * Functional part that yields single value
         */
        class Mono private constructor(
            word: LemmaType,
            pos: PosType,
            pronunciations: Set<Pronunciation>?,
            wordExtractor: (Lex) -> LemmaType,
            posExtractor: (Lex) -> PosType,
        ) : F_W_P_A(word, pos, pronunciations, wordExtractor, posExtractor), MonoValued {

            override fun invoke(model: CoreModel): Lex {
                return Finder.getLexesHavingPronunciations(
                    Finder.getLexesHaving(
                        model,
                        word,
                        pos,
                        wordExtractor,
                        posExtractor
                    ), pronunciations
                ).firstOrNull() ?: throw IllegalArgumentException()
            }

            companion object {

                fun of(
                    wordExtractor: (Lex) -> LemmaType,
                    posExtractor: (Lex) ->  PosType,
                    lex: Lex,
                ): Mono {
                    return Mono(
                        wordExtractor.invoke(lex),
                        posExtractor.invoke(lex),
                        lex.pronunciations,
                        wordExtractor,
                        posExtractor
                    )
                }

                fun from(
                    wordExtractor: (Lex) -> LemmaType,
                    posExtractor: (Lex) -> PosType,
                    word: LemmaType,
                    pos: PosType,
                    pronunciations: Set<Pronunciation>,
                ): Mono {
                    return Mono(word, pos, pronunciations, wordExtractor, posExtractor)
                }
            }
        }

        /**
         * Functional part that yields multiple values (array)
         */
        class Multi private constructor(
            word: LemmaType,
            pos: PosType,
            pronunciations: Set<Pronunciation>?,
            wordExtractor: (Lex) -> LemmaType,
            posExtractor: (Lex) -> PosType,
        ) : F_W_P_A(word, pos, pronunciations, wordExtractor, posExtractor), MultiValued {

            override fun invoke(model: CoreModel): Array<Lex> {
                return Finder.getLexesHavingPronunciations(
                    Finder.getLexesHaving(
                        model,
                        word,
                        pos,
                        wordExtractor,
                        posExtractor
                    ), pronunciations
                )
                    .toList()
                    .toTypedArray()
            }

            companion object {

                fun of(
                    wordExtractor: (Lex) -> LemmaType,
                    posExtractor: (Lex) -> PosType,
                    lex: Lex,
                ): Multi {
                    return Multi(
                        wordExtractor.invoke(lex),
                        posExtractor.invoke(lex),
                        lex.pronunciations,
                        wordExtractor,
                        posExtractor
                    )
                }

                fun from(
                    wordExtractor: (Lex) -> LemmaType,
                    posExtractor: (Lex) -> PosType,
                    word: LemmaType,
                    pos: PosType,
                    pronunciations: Set<Pronunciation>,
                ): Multi {
                    return Multi(word, pos, pronunciations, wordExtractor, posExtractor)
                }
            }
        }

        companion object {

            fun of(
                lex: Lex,
                wordExtractor: (Lex) -> LemmaType,
                posExtractor: (Lex) -> PosType,
            ): F_W_P_A {
                return F_W_P_A(
                    wordExtractor.invoke(lex),
                    posExtractor.invoke(lex),
                    lex.pronunciations,
                    wordExtractor,
                    posExtractor
                )
            }
        }
    }

    /**
     * (Word, PosOrType, Discriminant) - Shallow key
     *
     * @param word word lemma or LC lemma
     * @param pos pos part-of-speech or type
     * @param discriminant discriminant
     * @property wordExtractor word extractor function from lex
     * @property posExtractor part-of-speech or type extractor function from lex
     */
    open class F_W_P_D private constructor(
        word: LemmaType,
        pos: PosType,
        discriminant: String?,
        val wordExtractor: (Lex) -> LemmaType,
        val posExtractor: (Lex) -> PosType,
    ) : W_P_D(word, pos, discriminant) {

        override fun toLongString(): String = "KEYF ${javaClass.simpleName} WPD_${toWordExtractorString(wordExtractor)}_${toPosTypeExtractorString(posExtractor)} $this"

        /**
         * Functional part that yields single value
         */
        class Mono private constructor(
            word: LemmaType,
            pos: PosType,
            discriminant: String,
            wordExtractor: (Lex) -> LemmaType,
            posExtractor: (Lex) -> PosType,
        ) : F_W_P_D(word, pos, discriminant, wordExtractor, posExtractor), MonoValued {

            override fun invoke(model: CoreModel): Lex {
                return Finder.getLexesHavingDiscriminant(
                    Finder.getLexesHaving(
                        model,
                        word,
                        pos,
                        wordExtractor,
                        posExtractor
                    ), discriminant
                )
                    .firstOrNull() ?: throw IllegalArgumentException()
            }

            companion object {

                fun from(
                    wordExtractor: (Lex) -> LemmaType,
                    posExtractor: (Lex) -> PosType,
                    word: LemmaType,
                    pos: PosType,
                    discriminant: String,
                ): Mono {
                    return Mono(word, pos, discriminant, wordExtractor, posExtractor)
                }
            }
        }

        /**
         * Functional part that yields multiple values (array)
         */
        class Multi private constructor(
            word: LemmaType,
            pos: PosType,
            discriminant: String,
            wordExtractor: (Lex) -> LemmaType,
            posExtractor: (Lex) -> PosType,
        ) : F_W_P_D(word, pos, discriminant, wordExtractor, posExtractor), MultiValued {

            override fun invoke(model: CoreModel): Array<Lex> {
                return Finder.getLexesHavingDiscriminant(
                    Finder.getLexesHaving(
                        model,
                        word,
                        pos,
                        wordExtractor,
                        posExtractor
                    ), discriminant
                )
                    .toList()
                    .toTypedArray()
            }

            companion object {

                fun from(
                    wordExtractor: (Lex) -> LemmaType,
                    posExtractor: (Lex) -> PosType,
                    word: LemmaType,
                    pos: PosType,
                    discriminant: String,
                ): Multi {
                    return Multi(word, pos, discriminant, wordExtractor, posExtractor)
                }
            }
        }

        companion object {

            fun of(
                lex: Lex,
                wordExtractor: (Lex) -> LemmaType,
                posExtractor: (Lex) -> PosType,
            ): F_W_P_D {
                return F_W_P_D(
                    wordExtractor.invoke(lex),
                    posExtractor.invoke(lex),
                    lex.discriminant,
                    wordExtractor,
                    posExtractor
                )
            }
        }
    }

    companion object {
        // Name for extractor

        private const val DUMMY_UPPER = "CASE"

        private const val DUMMY_SATELLITE = 's'

        private val dummyLex = Lex(DUMMY_UPPER, DUMMY_SATELLITE.toString())

        /**
         * Name a word extractor (by applying dummy data)
         *
         * @param wordExtractor word extractor
         * @return name
         */
        fun toWordExtractorString(wordExtractor: (Lex) -> LemmaType): LemmaType {
            return if (wordExtractor.invoke(dummyLex) == DUMMY_UPPER) "cs" else "lc"
        }

        /**
         * Name a pos/type extractor (by applying dummy data)
         *
         * @param posExtractor word extractor
         * @return name
         */
        fun toPosTypeExtractorString(posExtractor: (Lex) -> PosType): String {
            return if (posExtractor.invoke(dummyLex) == DUMMY_SATELLITE) "t" else "p"
        }
    }
}
