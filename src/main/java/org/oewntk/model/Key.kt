/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.model

import java.io.Serializable
import java.util.*

interface Key {

	/**
	 * (Word, PosOrType)
	 *
	 * @param word    word: lemma or LC lemma
	 * @param posType pos type: part-of-speech or type
	 */
	open class W_P(
		/**
		 * Word: Lemma or LC lemma
		 */
		@JvmField val word: String,
		/**
		 * PosType: part-of-speech or type
		 */
		@JvmField val posType: Char

	) : Key, Comparable<W_P>, Serializable {

		override fun equals(other: Any?): Boolean {
			if (this === other) {
				return true
			}
			if (other == null || javaClass != other.javaClass) {
				return false
			}
			val that = other as W_P
			if (word != that.word) {
				return false
			}
			return this.posType == that.posType
		}

		override fun hashCode(): Int {
			return Objects.hash(word, posType)
		}

		override fun compareTo(other: W_P): Int {
			if (this == other) {
				return 0
			}
			return wpComparator.compare(this, other)
		}

		override fun toString(): String {
			return String.format("(%s,%s)", word, posType)
		}

		open fun toLongString(): String {
			return "KEY WP ${javaClass.simpleName} $this"
		}

		companion object {

			fun of(
				lex: Lex,
				wordExtractor: (Lex) -> String,
				posTypeExtractor: (Lex) -> Char
			): W_P {
				return W_P(wordExtractor(lex), posTypeExtractor(lex))
			}

			@JvmStatic
			fun of_t(lex: Lex): W_P {
				return of(lex, Lex::lemma, Lex::type)
			}

			@JvmStatic
			fun of_p(lex: Lex): W_P {
				return of(lex, Lex::lemma, Lex::partOfSpeech)
			}

			@JvmStatic
			fun of_lc_t(lex: Lex): W_P {
				return of(lex, Lex::lCLemma, Lex::type)
			}

			@JvmStatic
			fun of_lc_p(lex: Lex): W_P {
				return of(lex, Lex::lCLemma, Lex::partOfSpeech)
			}

			@JvmStatic
			fun from(word: String, posType: Char): W_P {
				return W_P(word, posType)
			}

			val wpComparator: Comparator<W_P> = Comparator.comparing { obj: W_P -> obj.word } 
				.thenComparing { obj: W_P -> obj.posType }
		}
	}

	/**
	 * (Word, PosOrType, Pronunciations)
	 */
	open class W_P_A(
		word: String,
		posType: Char,
		@JvmField val pronunciations: Array<Pronunciation>?
	) : W_P(word, posType) {

		val pronunciationSet: Set<Pronunciation>
			get() = Utils.toSet(pronunciations)

		override fun equals(other: Any?): Boolean {
			if (this === other) {
				return true
			}
			if (other == null || javaClass != other.javaClass) {
				return false
			}
			val that = other as W_P_A
			if (word != that.word) {
				return false
			}
			if (this.posType != that.posType) {
				return false
			}
			return Utils.toSet(this.pronunciations) == Utils.toSet(that.pronunciations)
		}

		override fun hashCode(): Int {
			return Objects.hash(word, posType, pronunciations.contentHashCode())
		}

		override fun compareTo(other: W_P): Int {
			if (this == other) {
				return 0
			}
			return wpaComparator.compare(this, other as W_P_A)
		}

		override fun toString(): String {
			return String.format("(%s,%s,%s)", word, posType, pronunciations.contentToString())
		}

		override fun toLongString(): String {
			return String.format("KEY WPA %s %s", this.javaClass.simpleName, this)
		}

		companion object {

			fun of(
				lex: Lex,
				wordExtractor: (Lex) -> String,
				posTypeExtractor: (Lex) -> Char,
			): W_P_A {
				return W_P_A(wordExtractor(lex), posTypeExtractor(lex), lex.pronunciations)
			}

			@JvmStatic
			fun of_t(lex: Lex): W_P_A {
				return of(lex, Lex::lemma, Lex::type)
			}

			@JvmStatic
			fun of_p(lex: Lex): W_P_A {
				return of(lex, Lex::lemma, Lex::partOfSpeech)
			}

			@JvmStatic
			fun of_lc_t(lex: Lex): W_P_A {
				return of(lex, Lex::lCLemma, Lex::type)
			}

			fun of_lc_p(lex: Lex): W_P_A {
				return of(lex, Lex::lCLemma, Lex::partOfSpeech)
			}

			fun from(lemma: String, type: Char, pronunciations: Array<Pronunciation>): W_P_A {
				return W_P_A(lemma, type, pronunciations)
			}

			val wpaComparator = compareBy<W_P_A> { it.word }
				.thenBy { it.posType }
				.thenBy(nullsFirst(pronunciationsComparator)) { it.pronunciations }
		}
	}

	/**
	 * (Word, PosOrType, Discriminant) - Shallow key
	 */
	open class W_P_D(word: String, posType: Char, @JvmField val discriminant: String?) : W_P(word, posType) {

		override fun equals(other: Any?): Boolean {
			if (this === other) {
				return true
			}
			if (other == null || javaClass != other.javaClass) {
				return false
			}
			val that = other as W_P_D
			if (word != that.word) {
				return false
			}
			if (this.posType != that.posType) {
				return false
			}
			return this.discriminant == that.discriminant
		}

		override fun hashCode(): Int {
			return Objects.hash(word, posType, discriminant)
		}

		override fun compareTo(other: W_P): Int {
			if (this == other) {
				return 0
			}
			return wpdComparator.compare(this, other as W_P_D)
		}

		override fun toString(): String {
			return String.format("(%s,%s,%s)", word, posType, discriminant)
		}

		override fun toLongString(): String {
			return String.format("KEY WPD %s %s", this.javaClass.simpleName, this)
		}

		companion object {
			fun of(
				lex: Lex,
				wordExtractor: (Lex) -> String,
				posTypeExtractor: (Lex) -> Char
			): W_P_D {
				return W_P_D(wordExtractor(lex), posTypeExtractor(lex), lex.discriminant)
			}

			@JvmStatic
			fun of_t(lex: Lex): W_P_D {
				return of(lex, Lex::lemma, Lex::type)
			}

			fun of_p(lex: Lex): W_P_D {
				return of(lex, Lex::lemma, Lex::partOfSpeech)
			}

			@JvmStatic
			fun of_lc_t(lex: Lex): W_P_D {
				return of(lex, Lex::lCLemma, Lex::type)
			}

			fun of_lc_p(lex: Lex): W_P_D {
				return of(lex, Lex::lCLemma, Lex::partOfSpeech)
			}

			fun from(lemma: String, type: Char, discriminant: String?): W_P_D {
				return W_P_D(lemma, type, discriminant)
			}

			val wpdComparator: Comparator<W_P_D> = compareBy<W_P_D> { it.word }
				.thenBy { it.posType }
				.thenBy(nullsFirst(Comparator.naturalOrder())) { it.discriminant }
		}
	}

	companion object {
		val pronunciationsComparator: Comparator<Array<Pronunciation>?> =
			Comparator { pa1: Array<Pronunciation>?, pa2: Array<Pronunciation>? ->
				val ps1 = Utils.toSet(pa1)
				val ps2 = Utils.toSet(pa2)
				if (ps1 == ps2) 0 else ps1.toString().compareTo(ps2.toString())
			}
	}
}
