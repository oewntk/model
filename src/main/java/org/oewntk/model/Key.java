/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.util.*;
import java.util.function.Function;

public class Key
{
	/**
	 * Current deep key, returns unique value
	 */
	public static class OEWN implements Function<Map<String, List<Lex>>, Lex>
	{
		public static OEWN of(final Lex lex)
		{
			return new OEWN(lex);
		}

		private final String lemma;
		private final Character type;
		private final Pronunciation[] pronunciations;

		public OEWN(final String lemma, final Character type, final Pronunciation... pronunciations)
		{
			this.lemma = lemma;
			this.type = type;
			this.pronunciations = pronunciations;
		}

		private OEWN(final Lex lex)
		{
			this(lex.getLemma(), lex.getType(), lex.getPronunciations());
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
			OEWN that = (OEWN) o;
			if (!this.lemma.equals(that.lemma))
			{
				return false;
			}
			if (this.type != that.type)
			{
				return false;
			}
			return Objects.equals(Finder.toSet(this.pronunciations), Finder.toSet(that.pronunciations));
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(lemma, type, Arrays.hashCode(pronunciations));
		}

		@Override
		public String toString()
		{
			return String.format("(%s,%s,%s)", lemma, type, Arrays.toString(pronunciations));
		}

		public String toLongString()
		{
			return String.format("KEY %s", this);
		}

		@Override
		public Lex apply(final Map<String, List<Lex>> lexesByLemma)
		{
			return Finder.getLexHavingPronunciations(Finder.getLexesHavingType(lexesByLemma, lemma, type), pronunciations);
		}
	}

	/**
	 * Current shallow key, returns unique value
	 */
	public static class Shallow implements Function<Map<String, List<Lex>>, Lex>
	{
		public static Shallow of(final Lex lex)
		{
			return new Shallow(lex);
		}

		private final String lemma;
		private final Character type;
		private final String discriminant;

		public Shallow(final String lemma, final Character type, final String discriminant)
		{
			this.lemma = lemma;
			this.type = type;
			this.discriminant = discriminant;
		}

		private Shallow(final Lex lex)
		{
			this(lex.getLemma(), lex.getType(), lex.getDiscriminant());
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
			Shallow that = (Shallow) o;
			if (!this.lemma.equals(that.lemma))
			{
				return false;
			}
			if (this.type != that.type)
			{
				return false;
			}
			return Objects.equals(this.discriminant, that.discriminant);
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(lemma, type, discriminant);
		}

		@Override
		public String toString()
		{
			return String.format("(%s,%s,%s)", lemma, type, discriminant);
		}

		public String toLongString()
		{
			return String.format("KEY SHALLOW %s", this);
		}

		@Override
		public Lex apply(final Map<String, List<Lex>> lexesByLemma)
		{
			return Finder.getLexHavingDiscriminant(Finder.getLcLexesHavingType(lexesByLemma, lemma, type), discriminant);
		}
	}

	/**
	 * Part-of-Speech (a-s merge) deep key, returns first value
	 */
	public static class Pos implements Function<Map<String, List<Lex>>, Lex>
	{
		public static Pos of(final Lex lex)
		{
			return new Pos(lex);
		}

		private final String lemma;
		private final Character pos;
		private final Pronunciation[] pronunciations;

		public Pos(final String lemma, final Character pos, final Pronunciation... pronunciations)
		{
			this.lemma = lemma;
			this.pos = pos;
			this.pronunciations = pronunciations;
		}

		private Pos(final Lex lex)
		{
			this(lex.getLemma(), lex.getPartOfSpeech(), lex.getPronunciations());
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
			Pos that = (Pos) o;
			if (!this.lemma.equals(that.lemma))
			{
				return false;
			}
			if (this.pos != that.pos)
			{
				return false;
			}
			return Objects.equals(Finder.toSet(this.pronunciations), Finder.toSet(that.pronunciations));
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(lemma, pos, Arrays.hashCode(pronunciations));
		}

		@Override
		public String toString()
		{
			return String.format("(%s,%s,%s)", lemma, pos, Arrays.toString(pronunciations));
		}

		public String toLongString()
		{
			return String.format("KEY POS %s", this);
		}

		@Override
		public Lex apply(final Map<String, List<Lex>> lexesByLemma)
		{
			return Finder.getLexHavingPronunciations(Finder.getLexesHavingPos(lexesByLemma, lemma, pos), pronunciations);
		}
	}

	/**
	 * Part-of-Speech lemma ignore case deep key, returns values
	 */
	public static class IC implements Function<Map<String, List<Lex>>, Lex[]>
	{
		public static IC of(final Lex lex)
		{
			return new IC(lex);
		}

		private final String lemma;
		private final String lcLemma;
		private final Character type;
		private final Pronunciation[] pronunciations;

		public IC(final String lemma, final Character type, Pronunciation... pronunciations)
		{
			this.lemma = lemma;
			this.lcLemma = lemma.toLowerCase(Locale.ENGLISH);
			this.type = type;
			this.pronunciations = pronunciations;
		}

		private IC(final Lex lex)
		{
			this(lex.getLemma(), lex.getType(), lex.getPronunciations());
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
			IC that = (IC) o;
			if (!this.lcLemma.equals(that.lcLemma))
			{
				return false;
			}
			if (this.type != that.type)
			{
				return false;
			}
			return Objects.equals(Finder.toSet(this.pronunciations), Finder.toSet(that.pronunciations));
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(lcLemma, type, Arrays.hashCode(pronunciations));
		}

		@Override
		public String toString()
		{
			return String.format("(%s,%s,%s)", lemma, type, Arrays.toString(pronunciations));
		}

		public String toLongString()
		{
			return String.format("KEY IGNORE CASE %s", this);
		}

		@Override
		public Lex[] apply(final Map<String, List<Lex>> lexesByLemma)
		{
			return Finder.getLexesHavingPronunciations(Finder.getLcLexesHavingType(lexesByLemma, lcLemma, type), pronunciations);
		}
	}

	/**
	 * Princeton WordNet key, returns values
	 */
	public static class PWN implements Function<Map<String, List<Lex>>, Lex[]>
	{
		public static PWN of(final Lex lex)
		{
			return new PWN(lex);
		}

		private final String lemma;
		private final Character pos;

		public PWN(final String lemma, final Character pos)
		{
			this.lemma = lemma;
			this.pos = pos;
		}

		private PWN(final Lex lex)
		{
			this(lex.getLemma(), lex.getPartOfSpeech());
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
			PWN that = (PWN) o;
			if (!this.lemma.equals(that.lemma))
			{
				return false;
			}
			return this.pos == that.pos;
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(lemma, pos);
		}

		@Override
		public String toString()
		{
			return String.format("(%s,%s)", lemma, pos);
		}

		public String toLongString()
		{
			return String.format("KEY PWN %s", this);
		}

		@Override
		public Lex[] apply(final Map<String, List<Lex>> lexesByLemma)
		{
			return Finder.getLexesHavingPos(lexesByLemma, lemma, pos);
		}
	}
}
