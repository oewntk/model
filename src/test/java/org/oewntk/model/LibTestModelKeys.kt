/*
 * Copyright (c) 2024-2024. Bernard Bou.
 */

package org.oewntk.model

import org.oewntk.model.Finder.getLcLexes
import org.oewntk.model.KeyF.MonoValued
import org.oewntk.model.KeyF.MultiValued
import org.oewntk.model.Pronunciation.Companion.ipa
import java.io.PrintStream
import java.util.Locale

object LibTestModelKeys {
    // M O B I L E

    fun testMobile(model: CoreModel, ps: PrintStream): IntArray {
        val pGB = ipa("ˈməʊbaɪl", "GB")
        val pUS = ipa("ˈmoʊbil", "US")
        val pronunciations = arrayOf(pGB, pUS)
        return testWordMulti(model, ps, "Mobile", pronunciations, Category.N)
    }

    fun testMobileNoPronunciation(model: CoreModel, ps: PrintStream): IntArray {
        return testWordNoPronunciationMulti(model, ps, "Mobile", Category.N)
    }

    // E A R T H (case)

    fun testEarthMulti(model: CoreModel, ps: PrintStream): IntArray {
        return testWordNoPronunciationMulti(model, ps, "Earth", Category.N)
    }

    fun testEarthMono(model: CoreModel, ps: PrintStream): IntArray {
        return testWordNoPronunciationMono(model, ps, "Earth", Category.N)
    }

    // B A R O Q U E (case)

    fun testBaroqueMulti(model: CoreModel, ps: PrintStream): IntArray {
        return testWordNoPronunciationMulti(model, ps, "Baroque", Category.A, Category.S)
    }

    fun testBaroqueMono(model: CoreModel, ps: PrintStream): IntArray {
        return testWordNoPronunciationMono(model, ps, "Baroque", Category.A, Category.S)
    }

    // C R I T I C A L (part-of-speech/type)

    fun testCriticalMulti(model: CoreModel, ps: PrintStream): IntArray {
        return testWordNoPronunciationMulti(model, ps, "critical", Category.A, Category.S)
    }

    fun testCriticalMono(model: CoreModel, ps: PrintStream): IntArray {
        return testWordNoPronunciationMono(model, ps, "critical", Category.A, Category.S)
    }

    // R O W

    fun testRowDeep(model: CoreModel, ps: PrintStream): IntArray {
        return testPronunciations(model, ps, "row", Category.N, ipa("ɹəʊ"), ipa("ɹaʊ"))
    }

    fun testRowShallow(model: CoreModel, ps: PrintStream): IntArray {
        return testShallow(model, ps, "row", Category.N, "-1", "-2")
    }

    fun testRowNoPronunciationDeep(model: CoreModel, ps: PrintStream): IntArray {
        return testPronunciations(model, ps, "row", Category.N)
    }

    fun testRowNoPronunciationShallow(model: CoreModel, ps: PrintStream): IntArray {
        return testShallow(model, ps, "row", Category.N)
    }

    // B A S S

    fun testBassDeep(model: CoreModel, ps: PrintStream): IntArray {
        return testPronunciations(model, ps, "bass", Category.N, ipa("beɪs"), ipa("bæs"))
    }

    fun testBassShallow(model: CoreModel, ps: PrintStream): IntArray {
        return testShallow(model, ps, "bass", Category.N, "-1", "-2")
    }

    fun testBassNoPronunciationDeep(model: CoreModel, ps: PrintStream): IntArray {
        return testPronunciations(model, ps, "bass", Category.N)
    }

    fun testBassNoPronunciationShallow(model: CoreModel, ps: PrintStream): IntArray {
        return testShallow(model, ps, "bass", Category.N)
    }

    // W O R D    T E S T S

    @Suppress("SameParameterValue")
    private fun testWordMulti(
        model: CoreModel,
        ps: PrintStream,
        cased: Lemma,
        p: Array<Pronunciation>,
        vararg categories: Category,
    ): IntArray {
        val lc = cased.lowercase(Locale.ENGLISH)
        val isCased = lc != cased
        val pSet = p.toSet()
        val keys: MutableList<MultiValued> = ArrayList()
        for (category in categories) {
            keys.add(KeyF.FuncFromLemmaCategoryPronunciation.Multi.from(Lex::lemma, { it.type.toCategory() }, cased, category, pSet))
            if (p.size > 1) {
                keys.add(KeyF.FuncFromLemmaCategoryPronunciation.Multi.from(Lex::lemma, { it.type.toCategory() }, cased, category, pSet))
            }
            if (isCased) {
                keys.add(KeyF.FuncFromLemmaCategoryPronunciation.Multi.from(Lex::lemma, { it.type.toCategory() }, lc, category, pSet))
                keys.add(KeyF.FuncFromLemmaCategoryPronunciation.Multi.from(Lex::lCLemma, { it.type.toCategory() }, lc, category, pSet))
                keys.add(KeyF.FuncFromLemmaCategoryPronunciation.Multi.from(Lex::lCLemma, { it.type.toCategory() }, lc, category, pSet))
            }
            if (category == Category.S || category == Category.A) {
                keys.add(KeyF.FuncFromLemmaCategoryPronunciation.Multi.from(Lex::lemma, { it.partOfSpeech.toCategory() }, cased, category, pSet))
                if (isCased) {
                    keys.add(KeyF.FuncFromLemmaCategoryPronunciation.Multi.from(Lex::lemma, { it.partOfSpeech.toCategory() }, lc, category, pSet))
                    keys.add(KeyF.FuncFromLemmaCategoryPronunciation.Multi.from(Lex::lCLemma, { it.partOfSpeech.toCategory() }, lc, category, pSet))
                    keys.add(KeyF.FuncFromLemmaCategoryPronunciation.Multi.from(Lex::lCLemma, { it.partOfSpeech.toCategory() }, lc, category, pSet))
                }
            }
        }
        return testKeysMulti(
            model,
            ps,
            cased,
            *keys.toTypedArray<MultiValued>()
        )
    }

    private fun testWordNoPronunciationMulti(
        model: CoreModel,
        ps: PrintStream,
        cased: Lemma,
        vararg categories: Category,
    ): IntArray {
        val lc = cased.lowercase(Locale.ENGLISH)
        val isCased = lc != cased

        val keys: MutableList<MultiValued> = ArrayList()
        for (category in categories) {
            keys.add(KeyF.FuncFromLemmaCategory.Multi.from(Lex::lemma, { it.type.toCategory() }, cased, category))
            if (isCased) {
                keys.add(KeyF.FuncFromLemmaCategory.Multi.from(Lex::lemma, { it.type.toCategory() }, lc, category))
                keys.add(KeyF.FuncFromLemmaCategory.Multi.from(Lex::lCLemma, { it.type.toCategory() }, lc, category))
                keys.add(KeyF.FuncFromLemmaCategory.Multi.from(Lex::lCLemma, { it.type.toCategory() }, lc, category))
            }
            if (category == Category.S || category == Category.A) {
                keys.add(KeyF.FuncFromLemmaCategory.Multi.from(Lex::lemma, { it.partOfSpeech.toCategory() }, cased, category))
                if (isCased) {
                    keys.add(KeyF.FuncFromLemmaCategory.Multi.from(Lex::lemma, { it.partOfSpeech.toCategory() }, cased.lowercase(Locale.ENGLISH), category))
                    keys.add(KeyF.FuncFromLemmaCategory.Multi.from(Lex::lCLemma, { it.partOfSpeech.toCategory() }, cased.lowercase(Locale.ENGLISH), category))
                    keys.add(KeyF.FuncFromLemmaCategory.Multi.from(Lex::lCLemma, { it.partOfSpeech.toCategory() }, cased.lowercase(Locale.ENGLISH), category))
                }
            }
        }
        return testKeysMulti(
            model,
            ps,
            cased,
            *keys.toTypedArray<MultiValued>()
        )
    }

    private fun testWordMono(
        model: CoreModel,
        ps: PrintStream,
        cased: Lemma,
        p: Array<Pronunciation>,
        vararg categories: Category,
    ): IntArray {
        val lc = cased.lowercase(Locale.ENGLISH)
        val isCased = lc != cased
        val pSet = p.toSet()

        val keys: MutableList<MonoValued> = ArrayList()
        for (category in categories) {
            keys.add(KeyF.FuncFromLemmaCategoryPronunciation.Mono.from(Lex::lemma, { it.type.toCategory() }, cased, category, pSet))
            if (p.size > 1) {
                keys.add(KeyF.FuncFromLemmaCategoryPronunciation.Mono.from(Lex::lemma, { it.type.toCategory() }, cased, category, pSet))
            }
            if (isCased) {
                keys.add(KeyF.FuncFromLemmaCategoryPronunciation.Mono.from(Lex::lemma, { it.type.toCategory() }, cased.lowercase(Locale.ENGLISH), category, pSet))
                keys.add(KeyF.FuncFromLemmaCategoryPronunciation.Mono.from(Lex::lCLemma, { it.type.toCategory() }, cased.lowercase(Locale.ENGLISH), category, pSet))
                keys.add(KeyF.FuncFromLemmaCategoryPronunciation.Mono.from(Lex::lCLemma, { it.type.toCategory() }, cased.lowercase(Locale.ENGLISH), category, pSet))
            }

            if (category == Category.S || category == Category.A) {
                keys.add(KeyF.FuncFromLemmaCategoryPronunciation.Mono.from(Lex::lemma, { it.partOfSpeech.toCategory() }, cased, category, pSet))
                if (isCased) {
                    keys.add(KeyF.FuncFromLemmaCategoryPronunciation.Mono.from(Lex::lemma, { it.partOfSpeech.toCategory() }, cased.lowercase(Locale.ENGLISH), category, pSet))
                    keys.add(KeyF.FuncFromLemmaCategoryPronunciation.Mono.from(Lex::lCLemma, { it.partOfSpeech.toCategory() }, cased.lowercase(Locale.ENGLISH), category, pSet))
                    keys.add(KeyF.FuncFromLemmaCategoryPronunciation.Mono.from(Lex::lCLemma, { it.partOfSpeech.toCategory() }, cased.lowercase(Locale.ENGLISH), category, pSet))
                }
            }
        }
        return testKeysMono(
            model,
            ps,
            cased,
            *keys.toTypedArray<MonoValued>()
        )
    }

    private fun testWordNoPronunciationMono(
        model: CoreModel,
        ps: PrintStream,
        cased: Lemma,
        vararg categories: Category,
    ): IntArray {
        val lc = cased.lowercase(Locale.ENGLISH)
        val isCased = lc != cased

        val keys: MutableList<MonoValued> = ArrayList()
        for (category in categories) {
            keys.add(KeyF.FuncFromLemmaCategory.Mono.from(Lex::lemma, { it.type.toCategory() }, cased, category))
            if (isCased) {
                keys.add(KeyF.FuncFromLemmaCategory.Mono.from(Lex::lemma, { it.type.toCategory() }, cased.lowercase(Locale.ENGLISH), category))
                keys.add(KeyF.FuncFromLemmaCategory.Mono.from(Lex::lCLemma, { it.type.toCategory() }, cased.lowercase(Locale.ENGLISH), category))
                keys.add(KeyF.FuncFromLemmaCategory.Mono.from(Lex::lCLemma, { it.type.toCategory() }, cased.lowercase(Locale.ENGLISH), category))
            }

            if (category == Category.S || category == Category.A) {
                keys.add(KeyF.FuncFromLemmaCategory.Mono.from(Lex::lemma, { it.partOfSpeech.toCategory() }, cased, category))
                if (isCased) {
                    keys.add(KeyF.FuncFromLemmaCategory.Mono.from(Lex::lemma, { it.partOfSpeech.toCategory() }, cased.lowercase(Locale.ENGLISH), category))
                    keys.add(KeyF.FuncFromLemmaCategory.Mono.from(Lex::lCLemma, { it.partOfSpeech.toCategory() }, cased.lowercase(Locale.ENGLISH), category))
                    keys.add(KeyF.FuncFromLemmaCategory.Mono.from(Lex::lCLemma, { it.partOfSpeech.toCategory() }, cased.lowercase(Locale.ENGLISH), category))
                }
            }
        }
        return testKeysMono(
            model,
            ps,
            cased,
            *keys.toTypedArray<MonoValued>()
        )
    }

    // P R O N U N C I A T I O N   / S H A L L O W    T E S T S

    @Suppress("SameParameterValue")
    private fun testShallow(
        model: CoreModel,
        ps: PrintStream,
        lemma: Lemma,
        category: Category,
        vararg discriminants: Discriminant?,
    ): IntArray {
        val keys: MutableList<MultiValued> = ArrayList()
        for (discriminant in discriminants) {
            keys.add(KeyF.FuncFromLemmaCategoryDiscriminant.Multi.from(Lex::lemma, { it.type.toCategory() }, lemma, category, discriminant!!))
        }
        keys.add(KeyF.FuncFromLemmaCategory.Multi.from(Lex::lemma, { it.type.toCategory() }, lemma, category))
        return testKeysMulti(model, ps, lemma, *keys.toTypedArray<MultiValued>())
    }

    @Suppress("SameParameterValue")
    private fun testPronunciations(
        model: CoreModel,
        ps: PrintStream,
        lemma: String,
        category: Category,
        vararg pronunciations: Pronunciation?,
    ): IntArray {
        val keys: MutableList<MultiValued> = ArrayList()
        for (p in pronunciations) {
            keys.add(KeyF.FuncFromLemmaCategoryPronunciation.Multi.from(Lex::lemma, { it.partOfSpeech.toCategory() }, lemma, category, setOf(p!!)))
        }
        keys.add(KeyF.FuncFromLemmaCategory.Multi.from(Lex::lemma, { it.type.toCategory() }, lemma, category))
        return testKeysMulti(model, ps, lemma, *keys.toTypedArray<MultiValued>())
    }

    // M O N O / M U L T I    T E S T S

    @SafeVarargs
    fun testKeysMono(
        model: CoreModel,
        ps: PrintStream,
        lemma: Lemma,
        vararg keys: MonoValued,
    ): IntArray {
        val r = IntArray(keys.size)
        for ((i, k) in keys.withIndex()) {
            ps.printf("%s%n", k.toLongString())
            try {
                val lex: Lex = k(model)
                ps.println("\t" + lex)
                r[i] = 1
            } catch (_: Exception) {
                r[i] = 0
            }
        }
        dumpContext(lemma, model, ps)
        return r
    }

    @SafeVarargs
    fun testKeysMulti(
        model: CoreModel,
        ps: PrintStream,
        lemma: String,
        vararg keys: MultiValued,
    ): IntArray {
        val r = IntArray(keys.size)
        for ((i, k) in keys.withIndex()) {
            ps.printf("%s%n", k.toLongString())
            val lexes: Array<Lex> = k(model)
            r[i] = lexes.size
            for (lex in lexes) {
                ps.println("\t" + lex)
            }
        }
        dumpContext(lemma, model, ps)
        return r
    }

    // C O N T E X T

    private fun dumpContext(
        lemma: String,
        model: CoreModel,
        ps: PrintStream,
    ) {
        ps.println("----------")
        ps.println("ALL LEMMAS IGNORE CASE $lemma")
        getLcLexes(model, lemma)?.forEach { lex: Lex -> ps.println("\t" + lex) }
    }
}
