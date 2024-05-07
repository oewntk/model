/*
 * Copyright (c) 2024-2024. Bernard Bou.
 */

package org.oewntk.model

import org.oewntk.model.Finder.getLcLexes
import org.oewntk.model.KeyF.MonoValued
import org.oewntk.model.KeyF.MultiValued
import org.oewntk.model.Pronunciation.Companion.ipa
import java.io.PrintStream

object LibTestModelKeys {
    // M O B I L E

    fun testMobile(model: CoreModel, ps: PrintStream): IntArray {
        val pGB = ipa("ˈməʊbaɪl", "GB")
        val pUS = ipa("ˈmoʊbil", "US")
        val pronunciations = arrayOf(pGB, pUS)
        return testWordMulti(model, ps, "Mobile", pronunciations, 'n')
    }

    fun testMobileNoPronunciation(model: CoreModel, ps: PrintStream): IntArray {
        return testWordNoPronunciationMulti(model, ps, "Mobile", 'n')
    }

    // E A R T H (case)

    fun testEarthMulti(model: CoreModel, ps: PrintStream): IntArray {
        return testWordNoPronunciationMulti(model, ps, "Earth", 'n')
    }

    fun testEarthMono(model: CoreModel, ps: PrintStream): IntArray {
        return testWordNoPronunciationMono(model, ps, "Earth", 'n')
    }

    // B A R O Q U E (case)

    fun testBaroqueMulti(model: CoreModel, ps: PrintStream): IntArray {
        return testWordNoPronunciationMulti(model, ps, "Baroque", 'a', 's')
    }

    fun testBaroqueMono(model: CoreModel, ps: PrintStream): IntArray {
        return testWordNoPronunciationMono(model, ps, "Baroque", 'a', 's')
    }

    // C R I T I C A L (part-of-speech/type)

    fun testCriticalMulti(model: CoreModel, ps: PrintStream): IntArray {
        return testWordNoPronunciationMulti(model, ps, "critical", 'a', 's')
    }

    fun testCriticalMono(model: CoreModel, ps: PrintStream): IntArray {
        return testWordNoPronunciationMono(model, ps, "critical", 'a', 's')
    }

    // R O W

    fun testRowDeep(model: CoreModel?, ps: PrintStream): IntArray {
        return testPronunciations(model, ps, "row", 'n', ipa("ɹəʊ"), ipa("ɹaʊ"))
    }

    fun testRowShallow(model: CoreModel?, ps: PrintStream): IntArray {
        return testShallow(model, ps, "row", 'n', "-1", "-2")
    }

    fun testRowNoPronunciationDeep(model: CoreModel?, ps: PrintStream): IntArray {
        return testPronunciations(model, ps, "row", 'n')
    }

    fun testRowNoPronunciationShallow(model: CoreModel?, ps: PrintStream): IntArray {
        return testShallow(model, ps, "row", 'n')
    }

    // B A S S

    fun testBassDeep(model: CoreModel?, ps: PrintStream): IntArray {
        return testPronunciations(model, ps, "bass", 'n', ipa("beɪs"), ipa("bæs"))
    }

    fun testBassShallow(model: CoreModel?, ps: PrintStream): IntArray {
        return testShallow(model, ps, "bass", 'n', "-1", "-2")
    }

    fun testBassNoPronunciationDeep(model: CoreModel?, ps: PrintStream): IntArray {
        return testPronunciations(model, ps, "bass", 'n')
    }

    fun testBassNoPronunciationShallow(model: CoreModel?, ps: PrintStream): IntArray {
        return testShallow(model, ps, "bass", 'n')
    }

    // W O R D    T E S T S

    private fun testWordMulti(
        model: CoreModel,
        ps: PrintStream,
        cased: String,
        p: Array<Pronunciation>,
        vararg poses: PosId,
    ): IntArray {
        val lc = cased.lowercase()
        val isCased = lc != cased
        val pSet = p.toSet()
        val keys: MutableList<MultiValued> = ArrayList()
        for (pos in poses) {
            keys.add(KeyF.F_W_P_A.Multi.from(Lex::lemma, Lex::type, cased, pos, pSet))
            if (p.size > 1) {
                keys.add(KeyF.F_W_P_A.Multi.from(Lex::lemma, Lex::type, cased, pos, pSet))
            }
            if (isCased) {
                keys.add(KeyF.F_W_P_A.Multi.from(Lex::lemma, Lex::type, lc, pos, pSet))
                keys.add(KeyF.F_W_P_A.Multi.from(Lex::lCLemma, Lex::type, lc, pos, pSet))
                keys.add(KeyF.F_W_P_A.Multi.from(Lex::lCLemma, Lex::type, lc, pos, pSet))
            }
            if (pos == 's' || pos == 'a') {
                keys.add(KeyF.F_W_P_A.Multi.from(Lex::lemma, Lex::partOfSpeech, cased, pos, pSet))
                if (isCased) {
                    keys.add(KeyF.F_W_P_A.Multi.from(Lex::lemma, Lex::partOfSpeech, lc, pos, pSet))
                    keys.add(KeyF.F_W_P_A.Multi.from(Lex::lCLemma, Lex::partOfSpeech, lc, pos, pSet))
                    keys.add(KeyF.F_W_P_A.Multi.from(Lex::lCLemma, Lex::partOfSpeech, lc, pos, pSet))
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
        cased: String,
        vararg poses: PosId,
    ): IntArray {
        val lc = cased.lowercase()
        val isCased = lc != cased

        val keys: MutableList<MultiValued> = ArrayList()
        for (pos in poses) {
            keys.add(KeyF.F_W_P.Multi.from(Lex::lemma, Lex::type, cased, pos))
            if (isCased) {
                keys.add(KeyF.F_W_P.Multi.from(Lex::lemma, Lex::type, lc, pos))
                keys.add(KeyF.F_W_P.Multi.from(Lex::lCLemma, Lex::type, lc, pos))
                keys.add(KeyF.F_W_P.Multi.from(Lex::lCLemma, Lex::type, lc, pos))
            }
            if (pos == 's' || pos == 'a') {
                keys.add(KeyF.F_W_P.Multi.from(Lex::lemma, Lex::partOfSpeech, cased, pos))
                if (isCased) {
                    keys.add(KeyF.F_W_P.Multi.from(Lex::lemma, Lex::partOfSpeech, cased.lowercase(), pos))
                    keys.add(KeyF.F_W_P.Multi.from(Lex::lCLemma, Lex::partOfSpeech, cased.lowercase(), pos))
                    keys.add(KeyF.F_W_P.Multi.from(Lex::lCLemma, Lex::partOfSpeech, cased.lowercase(), pos))
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
        cased: String,
        p: Array<Pronunciation>,
        vararg poses: PosId,
    ): IntArray {
        val lc = cased.lowercase()
        val isCased = lc != cased
        val pset = p.toSet()

        val keys: MutableList<MonoValued> = ArrayList()
        for (pos in poses) {
            keys.add(KeyF.F_W_P_A.Mono.from(Lex::lemma, Lex::type, cased, pos, pset))
            if (p.size > 1) {
                keys.add(KeyF.F_W_P_A.Mono.from(Lex::lemma, Lex::type, cased, pos, pset))
            }
            if (isCased) {
                keys.add(KeyF.F_W_P_A.Mono.from(Lex::lemma, Lex::type, cased.lowercase(), pos, pset))
                keys.add(KeyF.F_W_P_A.Mono.from(Lex::lCLemma, Lex::type, cased.lowercase(), pos, pset))
                keys.add(KeyF.F_W_P_A.Mono.from(Lex::lCLemma, Lex::type, cased.lowercase(), pos, pset))
            }

            if (pos == 's' || pos == 'a') {
                keys.add(KeyF.F_W_P_A.Mono.from(Lex::lemma, Lex::partOfSpeech, cased, pos, pset))
                if (isCased) {
                    keys.add(KeyF.F_W_P_A.Mono.from(Lex::lemma, Lex::partOfSpeech, cased.lowercase(), pos, pset))
                    keys.add(KeyF.F_W_P_A.Mono.from(Lex::lCLemma, Lex::partOfSpeech, cased.lowercase(), pos, pset))
                    keys.add(KeyF.F_W_P_A.Mono.from(Lex::lCLemma, Lex::partOfSpeech, cased.lowercase(), pos, pset))
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
        cased: String,
        vararg poses: PosId,
    ): IntArray {
        val lc = cased.lowercase()
        val isCased = lc != cased

        val keys: MutableList<MonoValued> = ArrayList()
        for (pos in poses) {
            keys.add(KeyF.F_W_P.Mono.from(Lex::lemma, Lex::type, cased, pos))
            if (isCased) {
                keys.add(KeyF.F_W_P.Mono.from(Lex::lemma, Lex::type, cased.lowercase(), pos))
                keys.add(KeyF.F_W_P.Mono.from(Lex::lCLemma, Lex::type, cased.lowercase(), pos))
                keys.add(KeyF.F_W_P.Mono.from(Lex::lCLemma, Lex::type, cased.lowercase(), pos))
            }

            if (pos == 's' || pos == 'a') {
                keys.add(KeyF.F_W_P.Mono.from(Lex::lemma, Lex::partOfSpeech, cased, pos))
                if (isCased) {
                    keys.add(KeyF.F_W_P.Mono.from(Lex::lemma, Lex::partOfSpeech, cased.lowercase(), pos))
                    keys.add(KeyF.F_W_P.Mono.from(Lex::lCLemma, Lex::partOfSpeech, cased.lowercase(), pos))
                    keys.add(KeyF.F_W_P.Mono.from(Lex::lCLemma, Lex::partOfSpeech, cased.lowercase(), pos))
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

    private fun testShallow(
        model: CoreModel?,
        ps: PrintStream,
        lemma: String,
        type: Char,
        vararg discriminants: String?,
    ): IntArray {
        val keys: MutableList<MultiValued> = ArrayList()
        for (d in discriminants) {
            keys.add(KeyF.F_W_P_D.Multi.from(Lex::lemma, Lex::type, lemma, type, d!!))
        }
        keys.add(KeyF.F_W_P.Multi.from(Lex::lemma, Lex::type, lemma, type))
        return testKeysMulti(model, ps, lemma, *keys.toTypedArray<MultiValued>())
    }

    private fun testPronunciations(
        model: CoreModel?,
        ps: PrintStream,
        lemma: String,
        type: Char,
        vararg pronunciations: Pronunciation?,
    ): IntArray {
        val keys: MutableList<MultiValued> = ArrayList()
        for (p in pronunciations) {
            keys.add(KeyF.F_W_P_A.Multi.from(Lex::lemma, Lex::partOfSpeech, lemma, type, setOf(p!!)))
        }
        keys.add(KeyF.F_W_P.Multi.from(Lex::lemma, Lex::type, lemma, type))
        return testKeysMulti(model, ps, lemma, *keys.toTypedArray<MultiValued>())
    }

    // M O N O / M U L T I    T E S T S

    @SafeVarargs
    fun testKeysMono(
        model: CoreModel?,
        ps: PrintStream,
        lemma: String,
        vararg keys: MonoValued,
    ): IntArray {
        val r = IntArray(keys.size)
        for ((i, k) in keys.withIndex()) {
            ps.printf("%s%n", k.toLongString())
            try {
                val lex: Lex = k(model!!)
                ps.println("\t" + lex)
                r[i] = 1
            } catch (e: Exception) {
                r[i] = 0
            }
        }
        dumpContext(lemma, model, ps)
        return r
    }

    @SafeVarargs
    fun testKeysMulti(
        model: CoreModel?,
        ps: PrintStream,
        lemma: String,
        vararg keys: MultiValued,
    ): IntArray {
        val r = IntArray(keys.size)
        for ((i, k) in keys.withIndex()) {
            ps.printf("%s%n", k.toLongString())
            val lexes: Array<Lex> = k(model!!)
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
        model: CoreModel?,
        ps: PrintStream,
    ) {
        ps.println("----------")
        ps.println("ALL LEMMAS IGNORE CASE $lemma")
        getLcLexes(model!!, lemma)?.forEach { lex: Lex -> ps.println("\t" + lex) }
    }
}
