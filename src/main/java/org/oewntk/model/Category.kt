package org.oewntk.model

/**
 * Category, a convenience class to subsume both PartOfSpeech or SynsetType.
 * As these are e"nums they cannot inherit from category.
 **/
enum class Category(val value: Char) {
    N('n') {
        override fun toPartOfSpeech(): PartOfSpeech = PartOfSpeech.N
    },
    V('v') {
        override fun toPartOfSpeech(): PartOfSpeech = PartOfSpeech.V
    },
    A('a') {
        override fun toPartOfSpeech(): PartOfSpeech = PartOfSpeech.A
    },
    R('r') {
        override fun toPartOfSpeech(): PartOfSpeech = PartOfSpeech.R
    },
    S('s') {
        override fun toPartOfSpeech(): PartOfSpeech = PartOfSpeech.A
    };

    abstract fun toPartOfSpeech(): PartOfSpeech
}