/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.model

import org.junit.Assert
import org.oewntk.model.KeyF.MonoValued
import org.oewntk.model.KeyF.MultiValued
import java.io.PrintStream
import java.util.function.Consumer
import java.util.function.Function
import java.util.stream.Collectors

object LibTestModelDuplicates {

	private fun testDuplicatesForKeyMono(model: CoreModel, key: Function<Lex?, MonoValued>?, ps: PrintStream) {
		val dups = model.lexes //
			.stream() // stream of lexes
			.map(key) // stream of values
			.collect(Collectors.groupingBy(Function.identity(), Collectors.counting())) // map(key, count)
			.entries.stream() // stream of pairs(key,count)
			.filter { m: Map.Entry<MonoValued, Long> -> m.value > 1 } // if map value > 1, duplicate element
			.sorted(Comparator.comparing { e: Map.Entry<MonoValued, Long> -> e.key.toString() }) //
			.collect(
				Collectors.toCollection { LinkedHashSet() }
			)
		ps.println(dups.size)
		dups.forEach(Consumer { x: Map.Entry<MonoValued, Long>? -> ps.println(x) })
		Assert.assertEquals(0, dups.size.toLong())
	}

	private fun testDuplicatesForKeyMulti(model: CoreModel, key: Function<Lex?, MultiValued>?, ps: PrintStream) {
		val dups = model.lexes //
			.stream() // stream of lexes
			.map(key) // stream of keys
			.collect(Collectors.groupingBy(Function.identity(), Collectors.counting())) // map(key, count));
			.entries.stream() // stream of map entries
			.filter { m: Map.Entry<MultiValued, Long> -> m.value > 1 } // if map value > 1, duplicate element
			.sorted(Comparator.comparing { e: Map.Entry<MultiValued, Long> -> e.key.toString() }) //
			.collect(Collectors.toCollection { LinkedHashSet() })
		ps.println(dups.size)
		dups.forEach(Consumer { x: Map.Entry<MultiValued, Long>? -> ps.println(x) })
	}

	@JvmStatic
	fun testDuplicatesForKeyOEWN(model: CoreModel, ps: PrintStream) {
		testDuplicatesForKeyMono(model, { lex: Lex? -> KeyF.F_W_P_A.Mono.of(Lex::lemma, Lex::type, lex!!) }, ps)
	}

	@JvmStatic
	fun testDuplicatesForKeyPos(model: CoreModel, ps: PrintStream) {
		testDuplicatesForKeyMono(
			model,
			{ lex: Lex? -> KeyF.F_W_P_A.Mono.of(Lex::lemma, Lex::partOfSpeech, lex!!) },
			ps
		)
	}

	@JvmStatic
	fun testDuplicatesForKeyIC(model: CoreModel, ps: PrintStream) {
		testDuplicatesForKeyMulti(
			model,
			{ lex: Lex? -> KeyF.F_W_P_A.Multi.of(Lex::lCLemma, Lex::type, lex!!) },
			ps
		)
	}

	@JvmStatic
	fun testDuplicatesForKeyPWN(model: CoreModel, ps: PrintStream) {
		testDuplicatesForKeyMulti(
			model,
			{ lex: Lex? -> KeyF.F_W_P.Multi.of(Lex::lCLemma, Lex::type, lex!!) },
			ps
		)
	}
}
