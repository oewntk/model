/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.util.*;
import java.util.function.Function;

public interface Key<R> extends Function<CoreModel, R>
{
	interface Mono extends Key<Lex>
	{
	}

	interface Multi extends Key<Lex[]>
	{
	}

	Comparator<Pronunciation[]> pronunciationsComparator = (pa1, pa2) -> {
		var ps1 = Utils.toSet(pa1);
		var ps2 = Utils.toSet(pa2);
		return ps1.equals(ps2) ? 0 : ps1.toString().compareTo(ps2.toString());
	};

	/**
	 * Current deep key, returns unique value
	 */
	class OEWN implements Mono, Comparable<OEWN>
	{
		public static OEWN of(final Lex lex)
		{
			return new OEWN(lex);
		}

		public static OEWN from(final String lemma, final Character type, final Pronunciation... pronunciations)
		{
			return new OEWN(lemma, type, pronunciations);
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

		public String getLemma()
		{
			return lemma;
		}

		public Character getType()
		{
			return type;
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
			OEWN that = (OEWN) o;
			if (!this.lemma.equals(that.lemma))
			{
				return false;
			}
			if (this.type != that.type)
			{
				return false;
			}
			return Objects.equals(Utils.toSet(this.pronunciations), Utils.toSet(that.pronunciations));
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(lemma, type, Arrays.hashCode(pronunciations));
		}

		@Override
		public int compareTo(final OEWN that)
		{
			if (this.equals(that))
			{
				return 0;
			}
			return Comparator.comparing(OEWN::getLemma) //
					.thenComparing(OEWN::getType) //
					.thenComparing(OEWN::getPronunciations, Comparator.nullsFirst(pronunciationsComparator)) //
					.compare(this, that);
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
		public Lex apply(final CoreModel model)
		{
			return Finder.getLexHavingPronunciations(Finder.getLexesHavingType(model, lemma, type), pronunciations);
		}
	}

	/**
	 * Current shallow key, returns unique value
	 */
	class Shallow implements Mono, Comparable<Shallow>
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

		public String getLemma()
		{
			return lemma;
		}

		public Character getType()
		{
			return type;
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
		public int compareTo(final Shallow that)
		{
			if (this.equals(that))
			{
				return 0;
			}
			return Comparator.comparing(Shallow::getLemma) //
					.thenComparing(Shallow::getType) //
					.thenComparing(Shallow::getDiscriminant, Comparator.nullsFirst(Comparator.naturalOrder())) //
					.compare(this, that);
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
		public Lex apply(final CoreModel model)
		{
			return Finder.getLexHavingDiscriminant(Finder.getLcLexesHavingType(model, lemma, type), discriminant);
		}
	}

	/**
	 * Part-of-Speech (a-s merge) deep key, returns first value
	 */
	class Pos implements Mono, Comparable<Pos>
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

		public String getLemma()
		{
			return lemma;
		}

		public Character getPos()
		{
			return pos;
		}

		public Pronunciation[] getPronunciations()
		{
			return pronunciations;
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
			return Objects.equals(Utils.toSet(this.pronunciations), Utils.toSet(that.pronunciations));
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(lemma, pos, Arrays.hashCode(pronunciations));
		}

		@Override
		public int compareTo(final Pos that)
		{
			if (this.equals(that))
			{
				return 0;
			}
			return Comparator.comparing(Pos::getLemma) //
					.thenComparing(Pos::getPos) //
					.thenComparing(Pos::getPronunciations, Comparator.nullsFirst(pronunciationsComparator)) //
					.compare(this, that);
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
		public Lex apply(final CoreModel model)
		{
			return Finder.getLexHavingPronunciations(Finder.getLexesHavingPos(model, lemma, pos), pronunciations);
		}
	}

	/**
	 * Part-of-Speech lemma ignore case deep key, returns values
	 */
	class IC implements Multi, Comparable<IC>
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

		public String getLemma()
		{
			return lemma;
		}

		public String getLcLemma()
		{
			return lcLemma;
		}

		public Character getType()
		{
			return type;
		}

		public Pronunciation[] getPronunciations()
		{
			return pronunciations;
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
			return Objects.equals(Utils.toSet(this.pronunciations), Utils.toSet(that.pronunciations));
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(lcLemma, type, Arrays.hashCode(pronunciations));
		}

		@Override
		public int compareTo(final IC that)
		{
			if (this.equals(that))
			{
				return 0;
			}
			return Comparator.comparing(IC::getLcLemma) //
					.thenComparing(IC::getType) //
					.thenComparing(IC::getPronunciations, Comparator.nullsFirst(pronunciationsComparator)) //
					.compare(this, that);
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
		public Lex[] apply(final CoreModel model)
		{
			return Finder.getLexesHavingPronunciations(Finder.getLcLexesHavingType(model, lcLemma, type), pronunciations);
		}
	}

	/**
	 * Princeton WordNet key, returns values
	 */
	class PWN implements Multi, Comparable<PWN>
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

		public String getLemma()
		{
			return lemma;
		}

		public Character getPos()
		{
			return pos;
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
		public int compareTo(final PWN that)
		{
			if (this.equals(that))
			{
				return 0;
			}
			return Comparator.comparing(PWN::getLemma) //
					.thenComparing(PWN::getPos) //
					.compare(this, that);
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
		public Lex[] apply(final CoreModel model)
		{
			return Finder.getLexesHavingPos(model, lemma, pos);
		}
	}
}
