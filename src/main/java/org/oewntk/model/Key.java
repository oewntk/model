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
	 * Common logic to keys with pronunciations
	 */
	abstract class Base implements Comparable<Base>
	{
		protected final String lemma;
		private final Character typeOrPos;
		protected final Pronunciation[] pronunciations;

		public Base(final String lemma, final Character typeOrPos, final Pronunciation... pronunciations)
		{
			this.lemma = lemma;
			this.typeOrPos = typeOrPos;
			this.pronunciations = pronunciations;
		}

		private Base(final Lex lex)
		{
			this(lex.getLemma(), lex.getType(), lex.getPronunciations());
		}

		public String getLemma()
		{
			return lemma;
		}

		protected Character getTypeOrPos()
		{
			return typeOrPos;
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
			Base that = (Base) o;
			if (!this.lemma.equals(that.lemma))
			{
				return false;
			}
			if (this.typeOrPos != that.typeOrPos)
			{
				return false;
			}
			return Objects.equals(Utils.toSet(this.pronunciations), Utils.toSet(that.pronunciations));
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(lemma, typeOrPos, Arrays.hashCode(pronunciations));
		}

		@Override
		public int compareTo(final Base that)
		{
			if (this.equals(that))
			{
				return 0;
			}
			return Comparator.comparing(Base::getLemma) //
					.thenComparing(Base::getTypeOrPos) //
					.thenComparing(Base::getPronunciations, Comparator.nullsFirst(pronunciationsComparator)) //
					.compare(this, that);
		}

		@Override
		public String toString()
		{
			return String.format("(%s,%s,%s)", lemma, typeOrPos, Arrays.toString(pronunciations));
		}

		public String toLongString()
		{
			return String.format("KEY %s", this);
		}
	}

	/**
	 * Current key
	 */
	class OEWN extends Base implements Mono
	{
		public static OEWN of(final Lex lex)
		{
			return new OEWN(lex);
		}

		public static OEWN from(final String lemma, final Character type, final Pronunciation... pronunciations)
		{
			return new OEWN(lemma, type, pronunciations);
		}

		public OEWN(final String lemma, final Character type, final Pronunciation... pronunciations)
		{
			super(lemma, type, pronunciations);
		}

		private OEWN(final Lex lex)
		{
			this(lex.getLemma(), lex.getType(), lex.getPronunciations());
		}

		public Character getType()
		{
			return getTypeOrPos();
		}

		public String toLongString()
		{
			return String.format("KEY OEWN %s", this);
		}

		@Override
		public Lex apply(final CoreModel model)
		{
			return Finder.getLexHavingPronunciations(Finder.getLexesHavingType(model, lemma, getType()), pronunciations);
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

		public static Shallow from(final String lemma, final Character type, final String discriminant)
		{
			return new Shallow(lemma, type, discriminant);
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
	class Pos extends Base implements Mono
	{
		public static Pos of(final Lex lex)
		{
			return new Pos(lex);
		}

		public static Pos from(final String lemma, final Character pos, final Pronunciation... pronunciations)
		{
			return new Pos(lemma, pos, pronunciations);
		}

		public Pos(final String lemma, final Character pos, final Pronunciation... pronunciations)
		{
			super(lemma, pos, pronunciations);
		}

		private Pos(final Lex lex)
		{
			this(lex.getLemma(), lex.getPartOfSpeech(), lex.getPronunciations());
		}

		public Character getPos()
		{
			return getTypeOrPos();
		}

		public String toLongString()
		{
			return String.format("KEY POS %s", this);
		}

		@Override
		public Lex apply(final CoreModel model)
		{
			return Finder.getLexHavingPronunciations(Finder.getLexesHavingPos(model, lemma, getPos()), pronunciations);
		}
	}

	/**
	 * Part-of-Speech lemma ignore case deep key, returns values
	 */
	class IC extends Base implements Multi
	{
		public static IC of(final Lex lex)
		{
			return new IC(lex);
		}

		public static IC from(final String lemma, final Character type, Pronunciation... pronunciations)
		{
			return new IC(lemma, type, pronunciations);
		}

		public IC(final String lemma, final Character typeOrPos, Pronunciation... pronunciations)
		{
			super(lemma, typeOrPos, pronunciations);
		}

		private IC(final Lex lex)
		{
			this(lex.getLemma(), lex.getType(), lex.getPronunciations());
		}

		public String getLcLemma()
		{
			return lemma.toLowerCase(Locale.ENGLISH);
		}

		public Character getType()
		{
			return getTypeOrPos();
		}

		public Character getPos()
		{
			return getTypeOrPos();
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
			if (!this.getLcLemma().equals(that.getLcLemma()))
			{
				return false;
			}
			if (this.getTypeOrPos() != that.getTypeOrPos())
			{
				return false;
			}
			return Objects.equals(Utils.toSet(this.pronunciations), Utils.toSet(that.pronunciations));
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(getLcLemma(), getTypeOrPos(), Arrays.hashCode(pronunciations));
		}

		public String toLongString()
		{
			return String.format("KEY IGNORE CASE %s", this);
		}

		@Override
		public Lex[] apply(final CoreModel model)
		{
			return Finder.getLexesHavingPronunciations(Finder.getLcLexesHavingTypeOrPos(model, getLcLemma(), getTypeOrPos()), pronunciations);
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
