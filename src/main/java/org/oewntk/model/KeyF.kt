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

    fun toLongString(): String

    /**
     * Interface for single value output
     */
    interface MonoValued : KeyF<Lex>

    /**
     * Interface for multiple values (array) output
     */
    interface MultiValued : KeyF<Array<Lex>>

    /**
     * (Lemma, Category)
     *
     * @param lemma                 lemma or LC lemma
     * @param category              category: part-of-speech or type
     * @property lemmaExtractor     lemma extractor function from lex
     * @property categoryExtractor  category extractor function from lex
     */
    open class FuncFromLemmaCategory private constructor(
        override var lemma: Lemma,
        override var category: Category,
        val lemmaExtractor: (Lex) -> Lemma,
        val categoryExtractor: (Lex) -> Category,

        ) : FromLemmaCategory(lemma, category) {

        override fun toLongString(): String = "KEYF ${javaClass.simpleName} LC_${lemmaExtractor.toCaseSensitiveOrLowerCased()}_${categoryExtractor.toTypeOrPos()} $this"

        /**
         * Functional part that yields single value
         */
        class Mono private constructor(
            lemma: Lemma,
            category: Category,
            lemmaExtractor: (Lex) -> Lemma,
            categoryExtractor: (Lex) -> Category,
        ) : FuncFromLemmaCategory(lemma, category, lemmaExtractor, categoryExtractor), MonoValued {

            override fun invoke(model: CoreModel): Lex {
                return Finder.getLexesHaving(model, lemma, category, lemmaExtractor, categoryExtractor)
                    .firstOrNull() ?: throw IllegalArgumentException()
            }

            companion object {

                fun of(
                    lemmaExtractor: (Lex) -> Lemma,
                    categoryExtractor: (Lex) -> Category,
                    lex: Lex,
                ): Mono {
                    return Mono(
                        lemmaExtractor.invoke(lex),
                        categoryExtractor.invoke(lex),
                        lemmaExtractor,
                        categoryExtractor
                    )
                }

                fun from(
                    lemmaExtractor: (Lex) -> Lemma,
                    categoryExtractor: (Lex) -> Category,
                    lemma: Lemma,
                    category: Category,
                ): Mono {
                    return Mono(lemma, category, lemmaExtractor, categoryExtractor)
                }
            }
        }

        /**
         * Functional part that yields multiple values (array)
         */
        class Multi private constructor(
            lemma: Lemma,
            category: Category,
            lemmaExtractor: (Lex) -> Lemma,
            categoryExtractor: (Lex) -> Category,
        ) : FuncFromLemmaCategory(lemma, category, lemmaExtractor, categoryExtractor), MultiValued {

            override fun invoke(model: CoreModel): Array<Lex> {
                return Finder.getLexesHaving(model, lemma, category, lemmaExtractor, categoryExtractor)
                    .toList()
                    .toTypedArray()
            }

            companion object {

                fun of(
                    lemmaExtractor: (Lex) -> Lemma,
                    categoryExtractor: (Lex) -> Category,
                    lex: Lex,
                ): Multi {
                    return Multi(
                        lemmaExtractor.invoke(lex),
                        categoryExtractor.invoke(lex),
                        lemmaExtractor,
                        categoryExtractor
                    )
                }

                fun from(
                    lemmaExtractor: (Lex) -> Lemma,
                    categoryExtractor: (Lex) -> Category,
                    lemma: Lemma,
                    category: Category,
                ): Multi {
                    return Multi(lemma, category, lemmaExtractor, categoryExtractor)
                }
            }
        }

        companion object {

            fun of(
                lex: Lex,
                lemmaExtractor: (Lex) -> Lemma,
                categoryExtractor: (Lex) -> Category,
            ): FuncFromLemmaCategory {
                return FuncFromLemmaCategory(
                    lemmaExtractor.invoke(lex),
                    categoryExtractor.invoke(lex),
                    lemmaExtractor,
                    categoryExtractor
                )
            }
        }
    }

    /**
     * (Lemma, Category, Pronunciations)
     *
     * @param lemma                lemma or LC lemma
     * @param category             category: part-of-speech or type
     * @param pronunciations       pronunciations
     * @property lemmaExtractor    lemma extractor function from lex
     * @property categoryExtractor category extractor function from lex
     */
    open class FuncUsingPronunciation private constructor(
        lemma: Lemma,
        category: Category,
        pronunciations: Set<Pronunciation>?,
        val lemmaExtractor: (Lex) -> Lemma,
        val categoryExtractor: (Lex) -> Category,
    ) : UsingPronunciation(lemma, category, pronunciations) {

        override fun toLongString(): String = "KEYF ${javaClass.simpleName} LCP_${lemmaExtractor.toCaseSensitiveOrLowerCased()}_${categoryExtractor.toTypeOrPos()} $this"

        /**
         * Functional part that yields single value
         */
        class Mono private constructor(
            lemma: Lemma,
            category: Category,
            pronunciations: Set<Pronunciation>?,
            lemmaExtractor: (Lex) -> Lemma,
            categoryExtractor: (Lex) -> Category,
        ) : FuncUsingPronunciation(lemma, category, pronunciations, lemmaExtractor, categoryExtractor), MonoValued {

            override fun invoke(model: CoreModel): Lex {
                return Finder.getLexesHavingPronunciations(
                    Finder.getLexesHaving(
                        model,
                        lemma,
                        category,
                        lemmaExtractor,
                        categoryExtractor
                    ), pronunciations
                ).firstOrNull() ?: throw IllegalArgumentException()
            }

            companion object {

                fun of(
                    lemmaExtractor: (Lex) -> Lemma,
                    categoryExtractor: (Lex) -> Category,
                    lex: Lex,
                ): Mono {
                    return Mono(
                        lemmaExtractor.invoke(lex),
                        categoryExtractor.invoke(lex),
                        lex.pronunciations,
                        lemmaExtractor,
                        categoryExtractor
                    )
                }

                fun from(
                    lemmaExtractor: (Lex) -> Lemma,
                    categoryExtractor: (Lex) -> Category,
                    lemma: Lemma,
                    category: Category,
                    pronunciations: Set<Pronunciation>,
                ): Mono {
                    return Mono(lemma, category, pronunciations, lemmaExtractor, categoryExtractor)
                }
            }
        }

        /**
         * Functional part that yields multiple values (array)
         */
        class Multi private constructor(
            lemma: Lemma,
            category: Category,
            pronunciations: Set<Pronunciation>?,
            lemmaExtractor: (Lex) -> Lemma,
            categoryExtractor: (Lex) -> Category,
        ) : FuncUsingPronunciation(lemma, category, pronunciations, lemmaExtractor, categoryExtractor), MultiValued {

            override fun invoke(model: CoreModel): Array<Lex> {
                return Finder.getLexesHavingPronunciations(
                    Finder.getLexesHaving(
                        model,
                        lemma,
                        category,
                        lemmaExtractor,
                        categoryExtractor
                    ), pronunciations
                )
                    .toList()
                    .toTypedArray()
            }

            companion object {

                fun of(
                    lemmaExtractor: (Lex) -> Lemma,
                    categoryExtractor: (Lex) -> Category,
                    lex: Lex,
                ): Multi {
                    return Multi(
                        lemmaExtractor.invoke(lex),
                        categoryExtractor.invoke(lex),
                        lex.pronunciations,
                        lemmaExtractor,
                        categoryExtractor
                    )
                }

                fun from(
                    lemmaExtractor: (Lex) -> Lemma,
                    categoryExtractor: (Lex) -> Category,
                    lemma: Lemma,
                    category: Category,
                    pronunciations: Set<Pronunciation>,
                ): Multi {
                    return Multi(lemma, category, pronunciations, lemmaExtractor, categoryExtractor)
                }
            }
        }

        companion object {

            fun of(
                lex: Lex,
                lemmaExtractor: (Lex) -> Lemma,
                categoryExtractor: (Lex) -> Category,
            ): FuncUsingPronunciation {
                return FuncUsingPronunciation(
                    lemmaExtractor.invoke(lex),
                    categoryExtractor.invoke(lex),
                    lex.pronunciations,
                    lemmaExtractor,
                    categoryExtractor
                )
            }
        }
    }

    /**
     * (Lemma, Category, Discriminant) - Shallow key
     *
     * @param lemma             lemma or LC lemma
     * @param category          category: part-of-speech or type
     * @param discriminant      discriminant
     * @property lemmaExtractor lemma extractor function from lex
     * @property categoryExtractor category extractor function from lex
     */
    open class FuncUsingDiscriminant private constructor(
        lemma: Lemma,
        category: Category,
        discriminant: Discriminant?,
        val lemmaExtractor: (Lex) -> Lemma,
        val categoryExtractor: (Lex) -> Category,
    ) : UsingDiscriminant(lemma, category, discriminant) {

        override fun toLongString(): String = "KEYF ${javaClass.simpleName} LCD_${lemmaExtractor.toCaseSensitiveOrLowerCased()}_${categoryExtractor.toTypeOrPos()} $this"

        /**
         * Functional part that yields single value
         */
        @Suppress("unused")
        class Mono private constructor(
            lemma: Lemma,
            category: Category,
            discriminant: Discriminant?,
            lemmaExtractor: (Lex) -> Lemma,
            categoryExtractor: (Lex) -> Category,
        ) : FuncUsingDiscriminant(lemma, category, discriminant, lemmaExtractor, categoryExtractor), MonoValued {

            override fun invoke(model: CoreModel): Lex {
                return Finder.getLexesHavingDiscriminant(
                    Finder.getLexesHaving(
                        model,
                        lemma,
                        category,
                        lemmaExtractor,
                        categoryExtractor
                    ), discriminant
                )
                    .firstOrNull() ?: throw IllegalArgumentException()
            }

            companion object {

                fun from(
                    lemmaExtractor: (Lex) -> Lemma,
                    categoryExtractor: (Lex) -> Category,
                    lemma: Lemma,
                    category: Category,
                    discriminant: Discriminant?,
                ): Mono {
                    return Mono(lemma, category, discriminant, lemmaExtractor, categoryExtractor)
                }
            }
        }

        /**
         * Functional part that yields multiple values (array)
         */
        class Multi private constructor(
            lemma: Lemma,
            category: Category,
            discriminant: Discriminant?,
            lemmaExtractor: (Lex) -> Lemma,
            categoryExtractor: (Lex) -> Category,
        ) : FuncUsingDiscriminant(lemma, category, discriminant, lemmaExtractor, categoryExtractor), MultiValued {

            override fun invoke(model: CoreModel): Array<Lex> {
                return Finder.getLexesHavingDiscriminant(
                    Finder.getLexesHaving(
                        model,
                        lemma,
                        category,
                        lemmaExtractor,
                        categoryExtractor
                    ), discriminant
                )
                    .toList()
                    .toTypedArray()
            }

            companion object {

                fun from(
                    lemmaExtractor: (Lex) -> Lemma,
                    categoryExtractor: (Lex) -> Category,
                    lemma: Lemma,
                    category: Category,
                    discriminant: Discriminant?,
                ): Multi {
                    return Multi(lemma, category, discriminant, lemmaExtractor, categoryExtractor)
                }
            }
        }

        companion object {

            fun of(
                lex: Lex,
                lemmaExtractor: (Lex) -> Lemma,
                categoryExtractor: (Lex) -> Category,
            ): FuncUsingDiscriminant {
                return FuncUsingDiscriminant(
                    lemmaExtractor.invoke(lex),
                    categoryExtractor.invoke(lex),
                    lex.discriminant,
                    lemmaExtractor,
                    categoryExtractor
                )
            }
        }
    }

    companion object {

        private const val DUMMY_UPPER = "CASE"

        private val dummyLex = Lex(DUMMY_UPPER, Category.S.toString())

        /**
         * Name a lemma extractor (by applying dummy data)
         *
         * @receiver lemma extractor
         * @return name
         */
        fun ((Lex) -> Lemma).toCaseSensitiveOrLowerCased(): String {
            return if (this.invoke(dummyLex) == DUMMY_UPPER) "cs" else "lc"
        }

        /**
         * Name a pos/type category extractor (by applying dummy data)
         *
         * @receiver category extractor
         * @return name
         */
        fun ((Lex) -> Category).toTypeOrPos(): String {
            return if (this.invoke(dummyLex) == Category.S) "t" else "p"
        }
    }
}
