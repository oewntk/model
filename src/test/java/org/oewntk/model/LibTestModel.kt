package org.oewntk.model

import org.oewntk.model.Finder.getLexes
import org.oewntk.model.Finder.getLexesHavingPos
import org.oewntk.model.Key.W_P.Companion.of_lc_p
import org.oewntk.model.Key.W_P_A.Companion.of_p
import java.io.PrintStream
import java.util.*
import java.util.AbstractMap.SimpleEntry
import java.util.function.Function
import java.util.stream.Collectors
import java.util.stream.Stream

object LibTestModel {
	@JvmStatic
	fun makeIndexMap(stream: Stream<Key?>): Map<Key?, Int> {
		val i = intArrayOf(0)
		val map = stream //
			.sequential() //
			.peek { _: Key? -> i[0]++ } //
			.map { item: Key? -> SimpleEntry(item, i[0]) } //
			.collect(
				Collectors.toMap(
					{ obj: SimpleEntry<Key?, Int> -> obj.key },
					{ obj: SimpleEntry<Key?, Int> -> obj.value })
			)
		return map
	}

	@JvmStatic
	fun makeSortedIndexMap(stream: Stream<Key?>): Map<Key, Int> {
		val i = intArrayOf(0)
		val map: Map<Key, Int> = stream //
			.sequential() //
			.peek { _: Key? -> i[0]++ } //
			.map { item: Key? -> SimpleEntry(item, i[0]) } //
			.collect(
				Collectors.toMap( //
					{ obj: SimpleEntry<Key?, Int> -> obj.key },  //
					{ obj: SimpleEntry<Key?, Int> -> obj.value },  //
					{ existing: Int, replacement: Int ->
						require(existing != replacement) { "$existing,$replacement" }
						existing
					},  //
					{ TreeMap() })
			)
		// map.forEach((k, v) -> ps.printf("%s %s%n", k, v));
		return map
	}

	@JvmStatic
	fun testScanLexesForTestWords(
		model: CoreModel,
		keyGetter: Function<Lex?, Key>,
		indexerByKey: Function<Stream<Key>?, Map<Key?, Int?>>,
		testWords: Set<String?>,
		peekTestWords: Boolean,
		ps: PrintStream
	) {
		// stream of lexes
		val lexKeyStream = model.lexes.stream().peek { lex: Lex ->
			if (testWords.contains(lex.lemma)) {
				if (peekTestWords) {
					ps.println("@$lex")
				}
			}
		}.map(keyGetter)

		// make lex-to-index map
		val lexKeyToIndex = indexerByKey.apply(lexKeyStream)

		// test map
		ps.printf("%-12s %s%n", "index", "lex")
		for (word in testWords) {
			val lexes = model.lexesByLemma!![word!!]!!
			for (lex in lexes) {
				ps.printf("%-12d %s%n", lexKeyToIndex[keyGetter.apply(lex)], lex)
			}
		}
	}

	@JvmStatic
	fun testWords(model: CoreModel, ps: PrintStream, vararg words: String?) {
		for (word in words) {
			val lexes = model.lexesByLemma!![word!!]!!
			for (lex in lexes) {
				ps.println(lex)
				dumpKeys(lex, ps)
			}
		}
	}

	@JvmStatic
	fun testWord(lemma: String?, model: CoreModel?, ps: PrintStream) {
		val lexes = getLexes(model!!, lemma)
		for (lex in lexes) {
			ps.println(lex)
			dumpKeys(lex, ps)
		}
	}

	@JvmStatic
	fun testWord(lemma: String?, posFilter: Char, model: CoreModel?, ps: PrintStream) {
		val lexes = getLexesHavingPos(model!!, lemma!!, posFilter)!!.toArray<Lex> { arrayOf() }
		for ((i, lex) in lexes.withIndex()) {
			ps.printf("[%d] %s%n", i + 1, lex)
			dumpKeys(lex, ps)
		}

		if (lexes.size > 1) {
			ps.println()
			ps.printf("comparing keys equals() for [%d] and [%d]%n", 1, 2)

			ps.println(lexes[0])
			ps.println(lexes[1])
			dumpKeyEquals(lexes[0], lexes[1], ps)
		}
	}

	private fun dumpKeyEquals(lex1: Lex, lex2: Lex, ps: PrintStream) {
		ps.printf("\t--- key = %s%n", Key.W_P_A.of_t(lex1) == Key.W_P_A.of_t(lex2))
		ps.printf("\tsha key = %s%n", Key.W_P_D.of_t(lex1) == Key.W_P_D.of_t(lex2))
		ps.printf("\tic  key = %s%n", Key.W_P_D.of_lc_t(lex1) == Key.W_P_D.of_lc_t(lex2))
		ps.printf("\tpos key = %s%n", of_p(lex1) == of_p(lex2))
		ps.printf("\tpwn key = %s%n", of_lc_p(lex1) == of_lc_p(lex2))
	}

	private fun dumpKeys(lex: Lex, ps: PrintStream) {
		ps.printf("\t--- key = %s%n", Key.W_P_A.of_t(lex))
		ps.printf("\tsha key = %s%n", Key.W_P_D.of_t(lex))
		ps.printf("\tigc key = %s%n", Key.W_P_A.of_lc_t(lex))
		ps.printf("\tpos key = %s%n", of_p(lex))
		ps.printf("\tpwn key = %s%n", of_lc_p(lex))
	}
}
