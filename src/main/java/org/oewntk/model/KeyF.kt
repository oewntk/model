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
     * @param posType pos type: part-of-speech or type
     * @property wordExtractor word extractor function from lex
     * @property posTypeExtractor part-of-speech or type extractor function from lex
     */
    open class F_W_P private constructor(
        word: String,
        posType: Char,
        val wordExtractor: (Lex) -> String,
        val posTypeExtractor: (Lex) -> Char,
    ) : W_P(word, posType) {

        override fun toLongString(): String {
            return String.format(
                "KEYF %s WPD_%s_%s %s",
                javaClass.simpleName,
                Utils.toWordExtractorString(wordExtractor),
                Utils.toPosTypeExtractorString(posTypeExtractor),
                this
            )
        }

        /**
         * Functional part that yields single value
         */
        class Mono private constructor(
            word: String,
            posType: Char,
            wordExtractor: (Lex) -> String,
            posTypeExtractor: (Lex) -> Char,
        ) : F_W_P(word, posType, wordExtractor, posTypeExtractor), MonoValued {

            override fun invoke(model: CoreModel): Lex {
                return Finder.getLexesHaving(model, word, posType, wordExtractor, posTypeExtractor)
                    .firstOrNull() ?: throw IllegalArgumentException()
            }

            companion object {

                fun of(
                    wordExtractor: (Lex) -> String,
                    posTypeExtractor: (Lex) -> Char,
                    lex: Lex,
                ): Mono {
                    return Mono(
                        wordExtractor.invoke(lex),
                        posTypeExtractor.invoke(lex),
                        wordExtractor,
                        posTypeExtractor
                    )
                }

                fun from(
                    wordExtractor: (Lex) -> String,
                    posTypeExtractor: (Lex) -> Char,
                    word: String,
                    posType: Char,
                ): Mono {
                    return Mono(word, posType, wordExtractor, posTypeExtractor)
                }
            }
        }

        /**
         * Functional part that yields multiple values (array)
         */
        class Multi private constructor(
            word: String,
            posType: Char,
            wordExtractor: (Lex) -> String,
            posTypeExtractor: (Lex) -> Char,
        ) : F_W_P(word, posType, wordExtractor, posTypeExtractor), MultiValued {

            override fun invoke(model: CoreModel): Array<Lex> {
                return Finder.getLexesHaving(model, word, posType, wordExtractor, posTypeExtractor)
                    .toList()
                    .toTypedArray()
            }

            companion object {

                fun of(
                    wordExtractor: (Lex) -> String,
                    posTypeExtractor: (Lex) -> Char,
                    lex: Lex,
                ): Multi {
                    return Multi(
                        wordExtractor.invoke(lex),
                        posTypeExtractor.invoke(lex),
                        wordExtractor,
                        posTypeExtractor
                    )
                }

                fun from(
                    wordExtractor: (Lex) -> String,
                    posTypeExtractor: (Lex) -> Char,
                    word: String,
                    posType: Char,
                ): Multi {
                    return Multi(word, posType, wordExtractor, posTypeExtractor)
                }
            }
        }

        companion object {

            fun of(
                lex: Lex,
                wordExtractor: (Lex) -> String,
                posTypeExtractor: (Lex) -> Char,
            ): F_W_P {
                return F_W_P(
                    wordExtractor.invoke(lex),
                    posTypeExtractor.invoke(lex),
                    wordExtractor,
                    posTypeExtractor
                )
            }
        }
    }

    /**
     * (Word, PosOrType, Pronunciations)
     *
     * @param word    word: lemma or LC lemma
     * @param posType pos type: part-of-speech or type
     * @param pronunciations pronunciations
     * @property wordExtractor word extractor function from lex
     * @property posTypeExtractor part-of-speech or type extractor function from lex
     */
    open class F_W_P_A private constructor(
        word: String,
        posType: Char,
        pronunciations: Array<Pronunciation>?,
        val wordExtractor: (Lex) -> String,
        val posTypeExtractor: (Lex) -> Char,
    ) : W_P_A(word, posType, pronunciations) {

        override fun toLongString(): String {
            return String.format(
                "KEYF %s WPA_%s_%s %s", this.javaClass.simpleName, Utils.toWordExtractorString(
                    this.wordExtractor
                ), Utils.toPosTypeExtractorString(this.posTypeExtractor), this
            )
        }

        /**
         * Functional part that yields single value
         */
        class Mono private constructor(
            word: String,
            posType: Char,
            pronunciations: Array<Pronunciation>?,
            wordExtractor: (Lex) -> String,
            posTypeExtractor: (Lex) -> Char,
        ) : F_W_P_A(word, posType, pronunciations, wordExtractor, posTypeExtractor), MonoValued {

            override fun invoke(model: CoreModel): Lex {
                return Finder.getLexesHavingPronunciations(
                    Finder.getLexesHaving(
                        model,
                        word,
                        posType,
                        wordExtractor,
                        posTypeExtractor
                    ), pronunciations
                ).firstOrNull() ?: throw IllegalArgumentException()
            }

            companion object {

                fun of(
                    wordExtractor: (Lex) -> String,
                    posTypeExtractor: (Lex) -> Char,
                    lex: Lex,
                ): Mono {
                    return Mono(
                        wordExtractor.invoke(lex),
                        posTypeExtractor.invoke(lex),
                        lex.pronunciations,
                        wordExtractor,
                        posTypeExtractor
                    )
                }

                fun from(
                    wordExtractor: (Lex) -> String,
                    posTypeExtractor: (Lex) -> Char,
                    word: String,
                    posType: Char,
                    pronunciations: Array<Pronunciation>,
                ): Mono {
                    return Mono(word, posType, pronunciations, wordExtractor, posTypeExtractor)
                }
            }
        }

        /**
         * Functional part that yields multiple values (array)
         */
        class Multi private constructor(
            word: String,
            posType: Char,
            pronunciations: Array<Pronunciation>?,
            wordExtractor: (Lex) -> String,
            posTypeExtractor: (Lex) -> Char,
        ) : F_W_P_A(word, posType, pronunciations, wordExtractor, posTypeExtractor), MultiValued {

            override fun invoke(model: CoreModel): Array<Lex> {
                return Finder.getLexesHavingPronunciations(
                    Finder.getLexesHaving(
                        model,
                        word,
                        posType,
                        wordExtractor,
                        posTypeExtractor
                    ), pronunciations
                )
                    .toList()
                    .toTypedArray()
            }

            companion object {

                fun of(
                    wordExtractor: (Lex) -> String,
                    posTypeExtractor: (Lex) -> Char,
                    lex: Lex,
                ): Multi {
                    return Multi(
                        wordExtractor.invoke(lex),
                        posTypeExtractor.invoke(lex),
                        lex.pronunciations,
                        wordExtractor,
                        posTypeExtractor
                    )
                }

                fun from(
                    wordExtractor: (Lex) -> String,
                    posTypeExtractor: (Lex) -> Char,
                    word: String,
                    posType: Char,
                    pronunciations: Array<Pronunciation>,
                ): Multi {
                    return Multi(word, posType, pronunciations, wordExtractor, posTypeExtractor)
                }
            }
        }

        companion object {

            fun of(
                lex: Lex,
                wordExtractor: (Lex) -> String,
                posTypeExtractor: (Lex) -> Char,
            ): F_W_P_A {
                return F_W_P_A(
                    wordExtractor.invoke(lex),
                    posTypeExtractor.invoke(lex),
                    lex.pronunciations,
                    wordExtractor,
                    posTypeExtractor
                )
            }
        }
    }

    /**
     * (Word, PosOrType, Discriminant) - Shallow key
     *
     * @param word    word: lemma or LC lemma
     * @param posType pos type: part-of-speech or type
     * @param discriminant discriminat
     * @property wordExtractor word extractor function from lex
     * @property posTypeExtractor part-of-speech or type extractor function from lex
     */
    open class F_W_P_D private constructor(
        word: String,
        posType: Char,
        discriminant: String?,
        val wordExtractor: (Lex) -> String,
        val posTypeExtractor: (Lex) -> Char,
    ) : W_P_D(word, posType, discriminant) {

        override fun toLongString(): String {
            return String.format(
                "KEYF %s WPD_%s_%s %s", this.javaClass.simpleName, Utils.toWordExtractorString(
                    this.wordExtractor
                ), Utils.toPosTypeExtractorString(this.posTypeExtractor), this
            )
        }

        /**
         * Functional part that yields single value
         */
        class Mono private constructor(
            word: String,
            posType: Char,
            discriminant: String,
            wordExtractor: (Lex) -> String,
            posTypeExtractor: (Lex) -> Char,
        ) : F_W_P_D(word, posType, discriminant, wordExtractor, posTypeExtractor), MonoValued {

            override fun invoke(model: CoreModel): Lex {
                return Finder.getLexesHavingDiscriminant(
                    Finder.getLexesHaving(
                        model,
                        word,
                        posType,
                        wordExtractor,
                        posTypeExtractor
                    ), discriminant
                )
                    .firstOrNull() ?: throw IllegalArgumentException()
            }

            companion object {

                fun from(
                    wordExtractor: (Lex) -> String,
                    posTypeExtractor: (Lex) -> Char,
                    word: String,
                    posType: Char,
                    discriminant: String,
                ): Mono {
                    return Mono(word, posType, discriminant, wordExtractor, posTypeExtractor)
                }
            }
        }

        /**
         * Functional part that yields multiple values (array)
         */
        class Multi private constructor(
            word: String,
            posType: Char,
            discriminant: String,
            wordExtractor: (Lex) -> String,
            posTypeExtractor: (Lex) -> Char,
        ) : F_W_P_D(word, posType, discriminant, wordExtractor, posTypeExtractor), MultiValued {

            override fun invoke(model: CoreModel): Array<Lex> {
                return Finder.getLexesHavingDiscriminant(
                    Finder.getLexesHaving(
                        model,
                        word,
                        posType,
                        wordExtractor,
                        posTypeExtractor
                    ), discriminant
                )
                    .toList()
                    .toTypedArray()
            }

            companion object {

                fun from(
                    wordExtractor: (Lex) -> String,
                    posTypeExtractor: (Lex) -> Char,
                    word: String,
                    posType: Char,
                    discriminant: String,
                ): Multi {
                    return Multi(word, posType, discriminant, wordExtractor, posTypeExtractor)
                }
            }
        }

        companion object {

            fun of(
                lex: Lex,
                wordExtractor: (Lex) -> String,
                posTypeExtractor: (Lex) -> Char,
            ): F_W_P_D {
                return F_W_P_D(
                    wordExtractor.invoke(lex),
                    posTypeExtractor.invoke(lex),
                    lex.discriminant,
                    wordExtractor,
                    posTypeExtractor
                )
            }
        }
    }
}
