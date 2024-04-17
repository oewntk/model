/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public interface Key
{
	Comparator<Pronunciation[]> pronunciationsComparator = (pa1, pa2) -> {
		var ps1 = Utils.toSet(pa1);
		var ps2 = Utils.toSet(pa2);
		return ps1.equals(ps2) ? 0 : ps1.toString().compareTo(ps2.toString());
	};

	/**
	 * (Word, PosOrType)
	 */
	class W_P implements Key, Comparable<W_P>, Serializable
	{
		public static <L extends Function<Lex, String>, P extends Function<Lex, Character>> W_P of(final Lex lex, final L wordExtractor, final P posTypeExtractor)
		{
			return new W_P(wordExtractor.apply(lex), posTypeExtractor.apply(lex));
		}

		public static W_P of_t(final Lex lex)
		{
			return W_P.of(lex, Lex::getLemma, Lex::getType);
		}

		public static W_P of_p(final Lex lex)
		{
			return W_P.of(lex, Lex::getLemma, Lex::getPartOfSpeech);
		}

		public static W_P of_lc_t(final Lex lex)
		{
			return W_P.of(lex, Lex::getLCLemma, Lex::getType);
		}

		public static W_P of_lc_p(final Lex lex)
		{
			return W_P.of(lex, Lex::getLCLemma, Lex::getPartOfSpeech);
		}

		public static W_P from(final String word, final Character posType)
		{
			return new W_P(word, posType);
		}

		/**
		 * Word: Lemma or LC lemma
		 */
		protected final String word;

		/**
		 * PosType: part-of-speech or type
		 */
		protected final Character posType;

		/**
		 * Constructor
		 *
		 * @param word    word: lemma or LC lemma
		 * @param posType pos type: part-of-speech or type
		 */
		protected W_P(final String word, final Character posType)
		{
			this.word = word;
			this.posType = posType;
		}

		/**
		 * Get word
		 *
		 * @return word
		 */
		public String getWord()
		{
			return word;
		}

		/**
		 * Get pos type
		 *
		 * @return pos type
		 */
		public Character getPosType()
		{
			return posType;
		}

		@Override
		public boolean equals(final Object o)
		{
			if (this == o)
			{
				return true;
			}
			if (o == null || getClass() != o.getClass())
			{
				return false;
			}
			W_P that = (W_P) o;
			if (!this.word.equals(that.word))
			{
				return false;
			}
			return this.posType == that.posType;
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(word, posType);
		}

		public static final Comparator<W_P> wpComparator = Comparator.comparing(W_P::getWord) //
				.thenComparing(W_P::getPosType);

		@Override
		public int compareTo(final W_P that)
		{
			if (this.equals(that))
			{
				return 0;
			}
			return wpComparator.compare(this, that);
		}

		@Override
		public String toString()
		{
			return String.format("(%s,%s)", word, posType);
		}

		public String toLongString()
		{
			return String.format("KEY WP %s %s", this.getClass().getSimpleName(), this);
		}
	}

	/**
	 * (Word, PosOrType, Pronunciations)
	 */
	class W_P_A extends W_P
	{
		public static <L extends Function<Lex, String>, P extends Function<Lex, Character>> W_P_A of(final Lex lex, final L wordExtractor, final P posTypeExtractor)
		{
			return new W_P_A(wordExtractor.apply(lex), posTypeExtractor.apply(lex), lex.getPronunciations());
		}

		public static W_P_A of_t(final Lex lex)
		{
			return W_P_A.of(lex, Lex::getLemma, Lex::getType);
		}

		public static W_P_A of_p(final Lex lex)
		{
			return W_P_A.of(lex, Lex::getLemma, Lex::getPartOfSpeech);
		}

		public static W_P_A of_lc_t(final Lex lex)
		{
			return W_P_A.of(lex, Lex::getLCLemma, Lex::getType);
		}

		public static W_P_A of_lc_p(final Lex lex)
		{
			return W_P_A.of(lex, Lex::getLCLemma, Lex::getPartOfSpeech);
		}

		public static W_P_A from(final String lemma, final Character type, final Pronunciation... pronunciations)
		{
			return new W_P_A(lemma, type, pronunciations);
		}

		protected final Pronunciation[] pronunciations;

		protected W_P_A(final String word, final Character posType, final Pronunciation[] pronunciations)
		{
			super(word, posType);
			this.pronunciations = pronunciations;
		}

		public Pronunciation[] getPronunciations()
		{
			return pronunciations;
		}

		public Set<Pronunciation> getPronunciationSet()
		{
			return Utils.toSet(pronunciations);
		}

		@Override
		public boolean equals(final Object o)
		{
			if (this == o)
			{
				return true;
			}
			if (o == null || getClass() != o.getClass())
			{
				return false;
			}
			W_P_A that = (W_P_A) o;
			if (!this.word.equals(that.word))
			{
				return false;
			}
			if (this.posType != that.posType)
			{
				return false;
			}
			return Objects.equals(Utils.toSet(this.pronunciations), Utils.toSet(that.pronunciations));
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(word, posType, Arrays.hashCode(pronunciations));
		}

		public static final Comparator<W_P_A> wpaComparator = Comparator.comparing(W_P_A::getWord) //
				.thenComparing(W_P_A::getPosType) //
				.thenComparing(W_P_A::getPronunciations, Comparator.nullsFirst(pronunciationsComparator));

		@Override
		public int compareTo(final W_P that)
		{
			if (this.equals(that))
			{
				return 0;
			}
			return wpaComparator.compare(this, (W_P_A) that);
		}

		@Override
		public String toString()
		{
			return String.format("(%s,%s,%s)", word, posType, Arrays.toString(pronunciations));
		}

		public String toLongString()
		{
			return String.format("KEY WPA %s %s", this.getClass().getSimpleName(), this);
		}
	}

	/**
	 * (Word, PosOrType, Discriminant) - Shallow key
	 */
	class W_P_D extends W_P
	{
		public static <L extends Function<Lex, String>, P extends Function<Lex, Character>> W_P_D of(final Lex lex, final L wordExtractor, final P posTypeExtractor)
		{
			return new W_P_D(wordExtractor.apply(lex), posTypeExtractor.apply(lex), lex.getDiscriminant());
		}

		public static W_P_D of_t(final Lex lex)
		{
			return W_P_D.of(lex, Lex::getLemma, Lex::getType);
		}

		public static W_P_D of_p(final Lex lex)
		{
			return W_P_D.of(lex, Lex::getLemma, Lex::getPartOfSpeech);
		}

		public static W_P_D of_lc_t(final Lex lex)
		{
			return W_P_D.of(lex, Lex::getLCLemma, Lex::getType);
		}

		public static W_P_D of_lc_p(final Lex lex)
		{
			return W_P_D.of(lex, Lex::getLCLemma, Lex::getPartOfSpeech);
		}

		public static W_P_D from(final String lemma, final Character type, final String discriminant)
		{
			return new W_P_D(lemma, type, discriminant);
		}

		protected final String discriminant;

		public W_P_D(final String word, final Character posType, final String discriminant)
		{
			super(word, posType);
			this.discriminant = discriminant;
		}

		public String getDiscriminant()
		{
			return discriminant;
		}

		@Override
		public boolean equals(final Object o)
		{
			if (this == o)
			{
				return true;
			}
			if (o == null || getClass() != o.getClass())
			{
				return false;
			}
			W_P_D that = (W_P_D) o;
			if (!this.word.equals(that.word))
			{
				return false;
			}
			if (this.posType != that.posType)
			{
				return false;
			}
			return Objects.equals(this.discriminant, that.discriminant);
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(word, posType, discriminant);
		}

		public static final Comparator<W_P_D> wpdComparator = Comparator.comparing(W_P_D::getWord) //
				.thenComparing(W_P_D::getPosType) //
				.thenComparing(W_P_D::getDiscriminant, Comparator.nullsFirst(Comparator.naturalOrder()));

		@Override
		public int compareTo(final W_P that)
		{
			if (this.equals(that))
			{
				return 0;
			}
			return wpdComparator.compare(this, (W_P_D) that);
		}

		@Override
		public String toString()
		{
			return String.format("(%s,%s,%s)", word, posType, discriminant);
		}

		public String toLongString()
		{
			return String.format("KEY WPD %s %s", this.getClass().getSimpleName(), this);
		}
	}
}
