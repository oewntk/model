/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.util.function.Function;

public interface KeyF<R> extends Function<CoreModel, R>
{
	interface MonoValued extends KeyF<Lex>
	{
	}

	interface MultiValued extends KeyF<Lex[]>
	{
	}

	class F_W_P<L extends Function<Lex, String>, P extends Function<Lex, Character>> extends Key.W_P
	{
		final L wordExtractor;

		final P posTypeExtractor;

		private F_W_P(final String word, final char posType, final L wordExtractor, final P posTypeExtractor)
		{
			super(word, posType);
			this.wordExtractor = wordExtractor;
			this.posTypeExtractor = posTypeExtractor;
		}

		public static <L extends Function<Lex, String>, P extends Function<Lex, Character>> F_W_P<L, P> of(final Lex lex, final L wordExtractor, final P posTypeExtractor)
		{
			return new F_W_P<>(wordExtractor.apply(lex), posTypeExtractor.apply(lex), wordExtractor, posTypeExtractor);
		}

		public static class Mono<L extends Function<Lex, String>, P extends Function<Lex, Character>> extends F_W_P<L, P> implements MonoValued
		{
			public static <L extends Function<Lex, String>, P extends Function<Lex, Character>> Mono<L, P> of(final L wordExtractor, final P posTypeExtractor, final Lex lex)
			{
				return new Mono<>(wordExtractor.apply(lex), posTypeExtractor.apply(lex), wordExtractor, posTypeExtractor);
			}

			public static <L extends Function<Lex, String>, P extends Function<Lex, Character>> Mono<L, P> from(final L wordExtractor, final P posTypeExtractor, final String word, final char posType)
			{
				return new Mono<>(word, posType, wordExtractor, posTypeExtractor);
			}

			private Mono(final String word, final char posType, final L wordExtractor, final P posTypeExtractor)
			{
				super(word, posType, wordExtractor, posTypeExtractor);
			}

			@Override
			public Lex apply(final CoreModel model)
			{
				return Finder.getLexesHaving(model, word, posType, wordExtractor, posTypeExtractor).findFirst().orElseThrow(IllegalArgumentException::new);
			}
		}

		public static class Multi<L extends Function<Lex, String>, P extends Function<Lex, Character>> extends F_W_P<L, P> implements MultiValued
		{
			public static <L extends Function<Lex, String>, P extends Function<Lex, Character>> Multi<L, P> of(final L wordExtractor, final P posTypeExtractor, final Lex lex)
			{
				return new Multi<>(wordExtractor.apply(lex), posTypeExtractor.apply(lex), wordExtractor, posTypeExtractor);
			}

			public static <L extends Function<Lex, String>, P extends Function<Lex, Character>> Multi<L, P> from(final L wordExtractor, final P posTypeExtractor, final String word, final char posType)
			{
				return new Multi<>(word, posType, wordExtractor, posTypeExtractor);
			}

			private Multi(final String word, final char posType, final L wordExtractor, final P posTypeExtractor)
			{
				super(word, posType, wordExtractor, posTypeExtractor);
			}

			@Override
			public Lex[] apply(final CoreModel model)
			{
				return Finder.getLexesHaving(model, word, posType, wordExtractor, posTypeExtractor).toArray(Lex[]::new);
			}
		}
	}

	class F_W_P_A<L extends Function<Lex, String>, P extends Function<Lex, Character>> extends Key.W_P_A
	{
		final L wordExtractor;

		final P posTypeExtractor;

		private F_W_P_A(final String word, final char posType, final Pronunciation[] pronunciations, final L wordExtractor, final P posTypeExtractor)
		{
			super(word, posType, pronunciations);
			this.wordExtractor = wordExtractor;
			this.posTypeExtractor = posTypeExtractor;
		}

		public static <L extends Function<Lex, String>, P extends Function<Lex, Character>> F_W_P_A<L, P> of(final Lex lex, final L wordExtractor, final P posTypeExtractor)
		{
			return new F_W_P_A<>(wordExtractor.apply(lex), posTypeExtractor.apply(lex), lex.getPronunciations(), wordExtractor, posTypeExtractor);
		}

		public static class Mono<L extends Function<Lex, String>, P extends Function<Lex, Character>> extends F_W_P_A<L, P> implements MonoValued
		{
			public static <L extends Function<Lex, String>, P extends Function<Lex, Character>> Mono<L, P> of(final L wordExtractor, final P posTypeExtractor, final Lex lex)
			{
				return new Mono<>(wordExtractor.apply(lex), posTypeExtractor.apply(lex), lex.getPronunciations(), wordExtractor, posTypeExtractor);
			}

			public static <L extends Function<Lex, String>, P extends Function<Lex, Character>> Mono<L, P> from(final L wordExtractor, final P posTypeExtractor, final String word, final char posType, final Pronunciation... pronunciations)
			{
				return new Mono<>(word, posType, pronunciations, wordExtractor, posTypeExtractor);
			}

			private Mono(final String word, final char posType, final Pronunciation[] pronunciations, final L wordExtractor, final P posTypeExtractor)
			{
				super(word, posType, pronunciations, wordExtractor, posTypeExtractor);
			}

			@Override
			public Lex apply(final CoreModel model)
			{
				return Finder.getLexesHavingPronunciations(Finder.getLexesHaving(model, word, posType, wordExtractor, posTypeExtractor), pronunciations).findFirst().orElseThrow(IllegalArgumentException::new);
			}
		}

		public static class Multi<L extends Function<Lex, String>, P extends Function<Lex, Character>> extends F_W_P_A<L, P> implements MultiValued
		{
			public static <L extends Function<Lex, String>, P extends Function<Lex, Character>> Multi<L, P> of(final L wordExtractor, final P posTypeExtractor, final Lex lex)
			{
				return new Multi<>(wordExtractor.apply(lex), posTypeExtractor.apply(lex), lex.getPronunciations(), wordExtractor, posTypeExtractor);
			}

			public static <L extends Function<Lex, String>, P extends Function<Lex, Character>> Multi<L, P> from(final L wordExtractor, final P posTypeExtractor, final String word, final char posType, final Pronunciation... pronunciations)
			{
				return new Multi<>(word, posType, pronunciations, wordExtractor, posTypeExtractor);
			}

			private Multi(final String word, final char posType, final Pronunciation[] pronunciations, final L wordExtractor, final P posTypeExtractor)
			{
				super(word, posType, pronunciations, wordExtractor, posTypeExtractor);
			}

			@Override
			public Lex[] apply(final CoreModel model)
			{
				return Finder.getLexesHavingPronunciations(Finder.getLexesHaving(model, word, posType, wordExtractor, posTypeExtractor), pronunciations).toArray(Lex[]::new);
			}
		}
	}

	class F_W_P_D<L extends Function<Lex, String>, P extends Function<Lex, Character>> extends Key.W_P_D
	{
		final L wordExtractor;

		final P posTypeExtractor;

		private F_W_P_D(final String word, final char posType, final String discriminant, final L wordExtractor, final P posTypeExtractor)
		{
			super(word, posType, discriminant);
			this.wordExtractor = wordExtractor;
			this.posTypeExtractor = posTypeExtractor;
		}

		public static <L extends Function<Lex, String>, P extends Function<Lex, Character>> F_W_P_D<L, P> of(final Lex lex, final L wordExtractor, final P posTypeExtractor)
		{
			return new F_W_P_D<>(wordExtractor.apply(lex), posTypeExtractor.apply(lex), lex.getDiscriminant(), wordExtractor, posTypeExtractor);
		}

		public static class Mono<L extends Function<Lex, String>, P extends Function<Lex, Character>> extends F_W_P_D<L, P> implements MonoValued
		{
			public static <L extends Function<Lex, String>, P extends Function<Lex, Character>> Mono<L, P> from(final L wordExtractor, final P posTypeExtractor, final String word, final char posType, final String discriminant)
			{
				return new Mono<>(word, posType, discriminant, wordExtractor, posTypeExtractor);
			}

			private Mono(final String word, final char posType, final String discriminant, final L wordExtractor, final P posTypeExtractor)
			{
				super(word, posType, discriminant, wordExtractor, posTypeExtractor);
			}

			@Override
			public Lex apply(final CoreModel model)
			{
				return Finder.getLexesHavingDiscriminant(Finder.getLexesHaving(model, word, posType, wordExtractor, posTypeExtractor), discriminant).findFirst().orElseThrow(IllegalArgumentException::new);
			}
		}

		public static class Multi<L extends Function<Lex, String>, P extends Function<Lex, Character>> extends F_W_P_D<L, P> implements MultiValued
		{
			public static <L extends Function<Lex, String>, P extends Function<Lex, Character>> Multi<L, P> from(final L wordExtractor, final P posTypeExtractor, final String word, final char posType, final String discriminant)
			{
				return new Multi<>(word, posType, discriminant, wordExtractor, posTypeExtractor);
			}

			private Multi(final String word, final char posType, final String discriminant, final L wordExtractor, final P posTypeExtractor)
			{
				super(word, posType, discriminant, wordExtractor, posTypeExtractor);
			}


			@Override
			public Lex[] apply(final CoreModel model)
			{
				return Finder.getLexesHavingDiscriminant(Finder.getLexesHaving(model, word, posType, wordExtractor, posTypeExtractor), discriminant).toArray(Lex[]::new);
			}
		}
	}
}
