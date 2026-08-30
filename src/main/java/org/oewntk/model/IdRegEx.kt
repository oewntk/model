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

// IPA blocks
// IPA Extensions (\p{InIPA_Extensions} / \u0250-\u02AF): Contains standard IPA symbols like ə, ʃ, ʒ, ŋ, θ, ð.
// Phonetic Extensions (\p{InPhonetic_Extensions} / \u1D00-\u1D7F): Additional phonetic symbols used in narrow transcriptions.
// Phonetic Extensions Supplement (\p{InPhonetic_Extensions_Supplement} / \u1D80-\u1DFC): Subscript/superscript tone and modified characters.
// Combining Diacritical Marks (\p{InCombining_Diacritical_Marks} / \u0300-\u036F): IPA diacritics for stress, nasalization, lengthening, and tone markings (e.g., ː, ̃, ̩).
// Basic Latin (\p{IsLatin} / a-z, A-Z): Standard English letters that represent direct IPA sounds like p, b, t, d, k, g.
// private val IPA_CHARS_RE = """\u0041-\u005A\u0061-\u007A\u00C0-\u00FF\u0100-\u017F\u0180-\u024F\u0250-\u02AF\u02B0-\u02FF\u1D00-\u1D7F\u1D80-\u1DFC\u0300-\u036F\u2000-\u206F\.\(\)\s"""

// Code-point range for the primary IPA and Phonetic extensions
// \u0250-\u02AF: Contains standard IPA symbols like ə, ʃ, ʒ, ŋ, θ, ð.
// \u02B0-\u02FF (Spacing Modifier Letters): Includes primary stress ˈ (U+02C8), secondary stress ˌ (U+02CC), length marks ː (U+02D0), half-length marks ˑ (U+02D1), and aspirated tone/modifier characters
// \u1D00-\u1D7F: Additional phonetic symbols used in narrow transcriptions.
// \u1D80-\u1DFC: Subscript/superscript tone and modified characters.
// \u0300-\u036F: IPA diacritics for stress, nasalization, lengthening, and tone markings (e.g., ː, ̃, ̩).
// \u00C0-\u00FF (Latin-1 Supplement): Includes standard IPA vowels and consonants like æ (U+00E6), ð (U+00F0), ø (U+00F8), and ç (U+00E7)
// a-z, A-Z: Standard English letters that represent direct IPA sounds like p, b, t, d, k, g.
const val IPA_CHARS_RE = "\\u0041-\\u005A\\u0061-\\u007A\\u00C0-\\u00FF\\u0100-\\u017F\\u0180-\\u024F\\u0250-\\u02AF\\u02B0-\\u02FF\\u0300-\\u036F\\u0370-\\u03FF\\u1D00-\\u1D7F\\u1D80-\\u1DFC\\u2000-\\u206F\\.\\(\\)\\s\\-,~"

const val IPA_RE = "[${IPA_CHARS_RE}]+"

val ipaRegex = "^$IPA_RE$".toRegex()

fun String.isIPA(): Boolean = ipaRegex.matches(this)
