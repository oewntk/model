/*
 * Copyright (c) 2024-2024. Bernard Bou.
 */

package org.oewntk.model

import kotlin.text.toRegex

const val SYNSET_ID_RE = "\\d{8}-[nvars]"

const val BASE_LEMMA_CHARS_RE = "a-zA-Z\\u00C0-\\u00D6\\u00D8-\\u00F60-9\\-+.,:!/'"
const val LEMMA_CHARS_RE = "$BASE_LEMMA_CHARS_RE "
const val ESC_LEMMA_CHARS_RE = "${BASE_LEMMA_CHARS_RE}_"

const val LEMMA_RE = "[${LEMMA_CHARS_RE}]+"

const val LEXID_RE = "$LEMMA_RE,[nvar]-?[\\d]?"

const val SENSEKEY_RE = "(?!$SYNSET_ID_RE$)[${ESC_LEMMA_CHARS_RE}]+%\\d+:\\d+:\\d+:[${ESC_LEMMA_CHARS_RE}]*:\\d*"

val synsetIdRegex = "^$SYNSET_ID_RE$".toRegex()

val senseKeyRegex = "^$SENSEKEY_RE$".toRegex()

val lexIdRegex = "^$LEXID_RE$".toRegex()

val lemmaRegex = "^$LEMMA_RE$".toRegex()

fun String.isSynsetId(): Boolean = synsetIdRegex.matches(this)

fun String.isSenseKey(): Boolean = senseKeyRegex.matches(this)

fun String.isLemma(): Boolean = lemmaRegex.matches(this)

fun String.isLexId(): Boolean = lexIdRegex.matches(this)
