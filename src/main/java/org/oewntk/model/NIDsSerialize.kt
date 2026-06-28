package org.oewntk.model

import java.io.*

/**
 * Serialize ID to Numeric IDs maps
 */
object SerializeNIDs {

    const val NID_PREFIX: String = "nid_"

    private const val SENSEKEYS_WORDS_SYNSETS_FILE = "sensekeys_words_synsets"

    /**
     * Serialize words id-to-nid map
     *
     * @param os    output stream
     * @param lexes lexes
     * @throws java.io.IOException io exception
     */
    @Throws(IOException::class)
    fun serializeWordNIDs(os: OutputStream, lexes: Collection<Lex>) {
        val wordToNID = NIDs.makeWordNIDs(lexes)
        serialize(os, wordToNID)
    }

    /**
     * Serialize cased words id-to-nid map
     *
     * @param os    output stream
     * @param lexes lexes
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    fun serializeCasedWordNIDs(os: OutputStream, lexes: Collection<Lex>) {
        val casedToNID = NIDs.makeCasedWordNIDs(lexes)
        serialize(os, casedToNID)
    }

    /**
     * Serialize morphs id-to-nid map
     *
     * @param os    output stream
     * @param lexes lexes
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    fun serializeMorphNIDs(os: OutputStream, lexes: Collection<Lex>) {
        val morphToNID = NIDs.makeMorphNIDs(lexes)
        serialize(os, morphToNID)
    }

    /**
     * Serialize pronunciations id-to-nid map
     *
     * @param os    output stream
     * @param lexes lexes
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    fun serializePronunciationNIDs(os: OutputStream, lexes: Collection<Lex>) {
        val pronunciationValueToNID = NIDs.makeMorphNIDs(lexes)
        serialize(os, pronunciationValueToNID)
    }

    /**
     * Serialize senses id-to-nid map
     *
     * @param os     output stream
     * @param senses senses
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    private fun serializeSensesNIDs(os: OutputStream, senses: Collection<Sense>) {
        val senseToNID = NIDs.makeSenseNIDs(senses)
        serialize(os, senseToNID)
    }

    /**
     * Serialize id-to-nid map
     *
     * @param os      output stream
     * @param synsets synsets
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    fun serializeSynsetNIDs(os: OutputStream, synsets: Collection<Synset>) {
        val synsetIdToNID = NIDs.makeSynsetNIDs(synsets)
        serialize(os, synsetIdToNID)
    }

    /**
     * Serialize object
     *
     * @param os     output stream
     * @param thing object
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    private fun serialize(os: OutputStream, thing: Any) {
        ObjectOutputStream(os)
            .use { it.writeObject(thing) }
    }

    /**
     * Serialize sensekey to wordnid-synsetnid
     * Does not use Kotlin pairs.
     *
     * @param os    output stream
     * @param model model
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    private fun serializeSensekeysWordsSynsetsNIDs(os: OutputStream, model: CoreModel) {
        val wordToNID = NIDs.makeWordNIDs(model.lexes)
        val synsetIdToNID = NIDs.makeSynsetNIDs(model.synsets)
        val m = model.senses
            .associate { it.senseKey to (wordToNID[it.lCLemma] to synsetIdToNID[it.synsetId]) } // (sensekey, (lemma,synsetId))
        serialize(os, m)
    }

    /**
     * Serialize sensekey to wordnid-synsetnid
     *
     * @param os    output stream
     * @param model model
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    private fun kserializeSensekeysWordsSynsetsNIDs(os: OutputStream, model: CoreModel) {
        val wordToNID = NIDs.makeWordNIDs(model.lexes)
        val synsetIdToNID = NIDs.makeSynsetNIDs(model.synsets)
        val m = model.senses
            .associate { it.senseKey to (wordToNID[it.lCLemma] to synsetIdToNID[it.synsetId]) } // (sensekey, (lemma,synsetId))
        serialize(os, m)
    }

    private fun serialize(outDir: File, baseName: String, printFunction: (FileOutputStream) -> Unit) {
        FileOutputStream(File(outDir, "$baseName.ser"))
            .use { printFunction.invoke(it) }
    }

    /**
     * Serialize all id-to-nid maps
     *
     * @param model  model
     * @param outDir output dir
     * @param model  model
     * @param outDir out dir
     * @param wordsFile words
     * @param casedWordsFile cased wods
     * @param morphsFile  morphs
     * @param pronunciationsFile pronunciations
     * @param synsetsFile synsets
     * @param sensesFile senses     * @throws IOException io exception
     */
    @Throws(IOException::class)
    fun serializeNIDs(
        model: CoreModel, outDir: File,
        wordsFile: String = "words",
        casedWordsFile: String = "casedwords",
        morphsFile: String = "morphs",
        pronunciationsFile: String = "pronunciations",
        synsetsFile: String = "synsets",
        sensesFile: String = "senses",
    ) {
        serialize(outDir, "$NID_PREFIX$wordsFile") { serializeWordNIDs(it, model.lexes) }
        serialize(outDir, "$NID_PREFIX$casedWordsFile") { serializeCasedWordNIDs(it, model.lexes) }
        serialize(outDir, "$NID_PREFIX$morphsFile") { serializeMorphNIDs(it, model.lexes) }
        serialize(outDir, "$NID_PREFIX$pronunciationsFile") { serializePronunciationNIDs(it, model.lexes) }
        serialize(outDir, "$NID_PREFIX$sensesFile") { serializeSensesNIDs(it, model.senses) }
        serialize(outDir, "$NID_PREFIX$synsetsFile") { serializeSynsetNIDs(it, model.synsets) }
        serialize(outDir, SENSEKEYS_WORDS_SYNSETS_FILE) { serializeSensekeysWordsSynsetsNIDs(it, model) }
    }
}