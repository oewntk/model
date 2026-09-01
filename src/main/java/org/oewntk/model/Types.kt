/*
 * Copyright (c) 2024-2024. Bernard Bou.
 */

package org.oewntk.model

import java.io.Serializable
import java.util.Comparator
import java.util.Locale
import java.util.Objects

typealias Lemma = LemmaImpl
typealias Key2 = Key2Impl
typealias Discriminant = DiscriminantImpl
typealias LexId = LexIdImpl
typealias SenseKey = SenseKeyImpl
typealias SynsetId = SynsetIdImpl

typealias Relation = RelationImpl

typealias Example = ExampleImpl
typealias PronunciationValue = PronunciationValueImpl

typealias PronunciationVariety = String
typealias Domain = String
typealias AdjPosition = String
typealias Morph = String
typealias VerbFrameId = String
typealias VerbTemplateId = Int

typealias HyperMap = Map<Lemma, Map<Key2, Collection<Lex>>>
typealias HyperMap1 = Map<Lemma, Map<Key2, Lex>>

typealias Category = CategoryImpl
typealias SynsetType = SynsetTypeImpl
typealias PartOfSpeech = PartOfSpeechImpl
