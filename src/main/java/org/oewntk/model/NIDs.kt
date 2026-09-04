package org.oewntk.model

import org.oewntk.model.Lex.Companion.lexIdComparator
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets

object NIDs {

    private const val SENSEKEYS_WORDS_SYNSETS_FILE = "sensekeys_words_synsets"

    // M A K E

    /**
     * Make lex-to-NID map
     */
    fun makeLexesNIDs(lexes: Collection<Lex>): Map<LexId, Int> {
        return lexes
            .asSequence()
            .map { it.key }
            .sortedWith(lexIdComparator)
            .withIndex()
            .associate { it.value to it.index + 1 } // map(lexId, nid)
    }

    /**
     * Make synset id-to-nid map
     *
     * @param synsets synsets
     * @return id-to-nid map
     */
    fun makeSynsetNIDs(synsets: Collection<Synset>): Map<SynsetId, Int> {
        return synsets
            .asSequence()
            .map { it.synsetId }
            .sorted()
            .withIndex()
            .associate { it.value to it.index + 1 }
    }

    /**
     * Make synset id string-to-nid map
     *
     * @param synsets synsets
     * @return synset id string-to-nid map
     */
    fun makeStrSynsetNIDs(synsets: Collection<Synset>): Map<String, Int> {
        return synsets
            .asSequence()
            .map { it.synsetId.id }
            .sorted()
            .withIndex()
            .associate { it.value to it.index + 1 }
    }

    /**
     * Make sense id-to-nid map
     *
     * @param senses senses
     * @return sense key-to-nid map
     */
    fun makeSenseNIDs(senses: Collection<Sense>): Map<SenseKey, Int> {
        return senses
            .asSequence()
            .map(Sense::senseKey)
            .distinct()
            .sorted()
            .withIndex()
            .associate { it.value to it.index + 1 }
    }

    /**
     * Make sense key string-to-nid map
     *
     * @param senses senses
     * @return sense key string-to-nid map
     */
    fun makeStrSenseNIDs(senses: Collection<Sense>): Map<String, Int> {
        return senses
            .asSequence()
            .map { it.senseKey.id }
            .distinct()
            .sorted()
            .withIndex()
            .associate { it.value to it.index + 1 }
    }

    /**
     * Make sense_lemma id-to-nid map
     *
     * @param senses senses
     * @return id-to-nid map
     */
    fun makeSenseWordNIDs(senses: Collection<Sense>): Map<String, Int> {
        return senses
            .asSequence()
            .map(Sense::uniqueId)
            .distinct()
            .sorted()
            .withIndex()
            .associate { it.value to it.index + 1 }
    }

    /**
     * Make word -to-NID map
     *
     * @param lexes lexes
     * @return word-to-nid map
     */
    fun makeWordNIDs(lexes: Collection<Lex>): Map<Lemma, Int> {
        // stream of words
        val map = lexes
            .asSequence()
            .map { it.lemma.lCLemma }
            .distinct()
            .sorted()
            .withIndex()
            .associate { it.value to it.index + 1 }
        assert(map.values.none { it == 0 })
        return map
    }

    /**
     * Make word string-to-NID map
     *
     * @param lexes lexes
     * @return word string-to-nid map
     */
    fun makeStrWordNIDs(lexes: Collection<Lex>): Map<String, Int> {
        // stream of words
        val map = lexes
            .asSequence()
            .map { it.lemma.lCLemma.form }
            .distinct()
            .sorted()
            .withIndex()
            .associate { it.value to it.index + 1 }
        assert(map.values.none { it == 0 })
        return map
    }

    /**
     * Make cased_word-to-NID map
     *
     * @param lexes lexes
     * @return cased_word-to-nid map
     */
    fun makeCasedWordNIDs(lexes: Collection<Lex>): Map<Lemma, Int> {
        val map = lexes
            .asSequence()
            .filter(Lex::isCased)
            .map { it.lemma }
            .distinct()
            .sorted()
            .withIndex()
            .associate { it.value to it.index + 1 }
        assert(map.values.none { it == 0 })
        return map
    }

    /**
     * Make cased_word string-to-NID map
     *
     * @param lexes lexes
     * @return cased_word string-to-nid map
     */
    fun makeStrCasedWordNIDs(lexes: Collection<Lex>): Map<String, Int> {
        val map = lexes
            .asSequence()
            .filter(Lex::isCased)
            .map { it.lemma.form }
            .distinct()
            .sorted()
            .withIndex()
            .associate { it.value to it.index + 1 }
        assert(map.values.none { it == 0 })
        return map
    }

    /**
     * Make morphs-to-NID map
     *
     * @param lexes lexes
     * @return morph-to-nid map
     */
    fun makeMorphNIDs(lexes: Collection<Lex>): Map<Morph, Int> {
        return lexes
            .asSequence()
            .filter { it.forms != null && it.forms!!.isNotEmpty() }
            .flatMap { it.forms!!.asSequence() }
            .sorted()
            .distinct()
            .withIndex()
            .associate { it.value to it.index + 1 }
    }

    /**
     * Make pronunciation(values)-to-NID map
     *
     * @param lexes lexes
     * @return pronunciation-to-nid map
     */
    fun makePronunciationNIDs(lexes: Collection<Lex>): Map<PronunciationValue, Int> {
        return lexes
            .asSequence()
            .filter { it.pronunciations != null && it.pronunciations!!.isNotEmpty() }
            .flatMap { it.pronunciations!!.asSequence() }
            .map { it.value }
            .sorted()
            .distinct()
            .withIndex()
            .associate { it.value to it.index + 1 }
    }

    /**
     * Make pronunciation(values) string-to-NID map
     *
     * @param lexes lexes
     * @return pronunciation string-to-nid map
     */
    fun makeStrPronunciationNIDs(lexes: Collection<Lex>): Map<String, Int> {
        return lexes
            .asSequence()
            .filter { it.pronunciations != null && it.pronunciations!!.isNotEmpty() }
            .flatMap { it.pronunciations!!.asSequence() }
            .map { it.value.ipa }
            .sorted()
            .distinct()
            .withIndex()
            .associate { it.value to it.index + 1 }
    }

    // L O O K U P

    /**
     * Lookup of id of type K
     *
     * @param map map of K-integer pairs
     * @param key key
     * @param K type of key
     * @return nid
     */
    fun <K> lookup(map: Map<K, Int>, key: K): Int {
        try {
            val nid = map[key]!!
            assert(nid != 0)
            return nid
        } catch (e: Exception) {
            Tracing.psErr.println("lookup of <$key> failed")
            throw e
        }
    }

    /**
     * Lookup of lower-cased key
     *
     * @param map map
     * @param key key, already lower-cased
     * @return nid
     */
    fun lookupLC(map: Map<Lemma, Int>, key: Lemma): Int {
        assert(key.form == key.lowercased)
        return lookup(map, key)
    }

    /**
     * Look up
     *
     * @param map map
     * @param key key
     * @param K type of key
     * @return nid or "NULL"
     */
    fun <K> lookupNullable(map: Map<K, Int>, key: K): String {
        val value = map[key] ?: return "NULL"
        return value.toString()
    }

    // P R I N T

    /**
     * Print words id-to-nid map
     *
     * @param ps    print stream
     * @param lexes lexes
     */
    fun printWords(ps: PrintStream, lexes: Collection<Lex>) = print(ps, makeWordNIDs(lexes))

    /**
     * Print cased words id-to-nid map
     *
     * @param ps    print stream
     * @param lexes lexes
     */
    fun printCasedWords(ps: PrintStream, lexes: Collection<Lex>) = print(ps, makeCasedWordNIDs(lexes))

    /**
     * Print morphs id-to-nid map
     *
     * @param ps    print stream
     * @param lexes lexes
     */
    fun printMorphs(ps: PrintStream, lexes: Collection<Lex>) = print(ps, makeMorphNIDs(lexes))

    /**
     * Print pronunciations id-to-nid map
     *
     * @param ps    print stream
     * @param lexes lexes
     */
    fun printPronunciations(ps: PrintStream, lexes: Collection<Lex>) = print(ps, makePronunciationNIDs(lexes))

    /**
     * Print synsets id-to-nid map
     *
     * @param ps      print stream
     * @param synsets synsets
     */
    fun printSynsets(ps: PrintStream, synsets: Collection<Synset>) = print(ps, makeSynsetNIDs(synsets))

    /**
     * Print sense id-to-nid map
     *
     * @param ps     print stream
     * @param senses senses
     */
    private fun printSenses(ps: PrintStream, senses: Collection<Sense>) = print(ps, makeSenseNIDs(senses))

    /**
     * Print sense+word id-to-nid map
     *
     * @param ps     print stream
     * @param senses senses
     */
    private fun printSensesWords(ps: PrintStream, senses: Collection<Sense>) = print(ps, makeSenseWordNIDs(senses))

    /**
     * Print sensekey to wordnid-synsetnid
     * Does not use Kotlin pairs.
     *
     * @param ps    print stream
     * @param model model
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    private fun printSensesWordsSynsetsNIDs(ps: PrintStream, model: CoreModel) {
        val wordToNID = NIDs.makeWordNIDs(model.lexes)
        val synsetIdToNID = NIDs.makeSynsetNIDs(model.synsets)
        val m = model.senses
            .associate { it.senseKey.id to (wordToNID[it.lCLemma]!! to synsetIdToNID[it.synsetId]!!) } // (sensekey, (lemma,synsetId))
        print2(ps, m)
    }

    /**
     * Print id-to-nid map
     *
     * @param ps    print stream
     * @param toNID od-to-nid map
     */
    private fun <T: Comparable<T>> print(ps: PrintStream, toNID: Map<T, Int>) {
        val data = toNID.keys
            .sorted()
            .joinToString(separator = ",\n", prefix = "{\n", postfix = "\n}") { "\"$it\": ${toNID[it]}" }
        ps.println(data)
    }

     /**
     * Print id-to-nid map
     *
     * @param ps     print stream
     * @param toNIDs sod-to-nids(pair) map
     */
   private fun <T: Comparable<T>> print2(ps: PrintStream, toNIDs: Map<T, Pair<Int, Int>>) {
        val data = toNIDs.keys
            .sorted()
            .joinToString(separator = ",\n", prefix = "{\n", postfix = "\n}") { "\"$it\": ${toNIDs[it]}" }
        ps.println(data)
    }

    private fun print(outDir: File, baseName: String, printFunction: (PrintStream) -> Unit) {
        PrintStream(FileOutputStream(File(outDir, "$baseName.json")), true, StandardCharsets.UTF_8)
            .use { printFunction.invoke(it) }
    }

    /**
     * Print all id-to-nid maps for a model
     *
     * @param model  model
     * @param outDir out dir
     * @param wordsFile words
     * @param casedWordsFile cased wods
     * @param morphsFile  morphs
     * @param pronunciationsFile pronunciations
     * @param synsetsFile synsets
     * @param sensesFile senses
     * @param sensesWordsFile senses+words
     * @throws java.io.IOException io exception
     */
    @Throws(IOException::class)
    fun printNIDs(
        model: CoreModel, outDir: File,
        wordsFile: String = "words",
        casedWordsFile: String = "casedwords",
        morphsFile: String = "morphs",
        pronunciationsFile: String = "pronunciations",
        synsetsFile: String = "synsets",
        sensesFile: String = "senses",
        sensesWordsFile: String = "senseswords",
    ) {
        print(outDir, wordsFile) { printWords(it, model.lexes) }
        print(outDir, casedWordsFile) { printCasedWords(it, model.lexes) }
        print(outDir, morphsFile) { printMorphs(it, model.lexes) }
        print(outDir, pronunciationsFile) { printPronunciations(it, model.lexes) }
        print(outDir, synsetsFile) { printSynsets(it, model.synsets) }
        print(outDir, sensesFile) { printSenses(it, model.senses) }
        print(outDir, sensesWordsFile) { printSensesWords(it, model.senses) }
        print(outDir, SENSEKEYS_WORDS_SYNSETS_FILE) { printSensesWordsSynsetsNIDs(it, model) }
    }
}