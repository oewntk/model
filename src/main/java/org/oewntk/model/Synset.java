/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.Serializable;
import java.util.*;

/**
 * Synset
 */
public class Synset implements Comparable<Synset>, Serializable
{
	/**
	 * Synset id
	 */
	private final String synsetId;

	/**
	 * Synset type ss_type {n, v, a, r, s}
	 */
	private final char type;

	/**
	 * Lemma members
	 */
	private final String[] members;

	/**
	 * Definitions
	 */
	private final String[] definitions;

	/**
	 * Examples
	 */
	private final String[] examples;

	/**
	 * Wiki data
	 */
	private final String wikidata;

	/**
	 * Synset relations
	 */
	private Map<String, Set<String>> relations;

	/**
	 * Source file
	 */
	private final String domain;

	/**
	 * Constructor
	 *
	 * @param synsetId    synset id
	 * @param type        type: {n,v,a,r,s}
	 * @param domain      source file
	 * @param members     synset members
	 * @param definitions definitions
	 * @param examples    examples
	 * @param wikidata    wiki data
	 * @param relations   synset relations
	 */
	public Synset(final String synsetId, final char type, final String domain, final String[] members, final String[] definitions, final String[] examples, final String wikidata, final Map<String, Set<String>> relations)
	{
		this.synsetId = synsetId;
		this.type = type;
		this.domain = domain;
		this.members = members;
		this.definitions = definitions;
		this.examples = examples;
		this.wikidata = wikidata;
		this.relations = relations;
	}

	/**
	 * Get synset id
	 *
	 * @return synset id
	 */
	public String getSynsetId()
	{
		return synsetId;
	}

	/**
	 * Get type
	 *
	 * @return type
	 */
	public char getType()
	{
		return type;
	}

	/**
	 * Get part-of-speech
	 *
	 * @return part-of-speech
	 */
	public char getPartOfSpeech()
	{
		if (type == 's')
		{
			return 'a';
		}
		return type;
	}

	/**
	 * Get member lemmas
	 *
	 * @return member lemmas
	 */
	public String[] getMembers()
	{
		return members;
	}

	/**
	 * Get definitions
	 *
	 * @return definitions
	 */
	public String[] getDefinitions()
	{
		return definitions;
	}

	/**
	 * Get first definition
	 *
	 * @return first definition
	 */
	public String getDefinition()
	{
		if (definitions != null && definitions.length > 0)
		{
			return definitions[0];
		}
		return null;
	}

	/**
	 * Get examples
	 *
	 * @return examples
	 */
	public String[] getExamples()
	{
		return examples;
	}

	/**
	 * Get synset relations
	 *
	 * @return synset relations
	 */
	public Map<String, Set<String>> getRelations()
	{
		return relations;
	}

	/**
	 * Get wiki data
	 *
	 * @return wiki data
	 */
	public String getWikidata()
	{
		return wikidata;
	}

	/**
	 * Get domain
	 *
	 * @return source file
	 */
	public String getDomain()
	{
		return domain;
	}

	/**
	 * Get lex file
	 *
	 * @return lex file
	 */
	public String getLexfile()
	{
		switch (getPartOfSpeech())
		{
			case 'n':
				return "noun." + domain;
			case 'v':
				return "verb." + domain;
			case 'a':
				return "adj." + domain;
			case 'r':
				return "adv." + domain;
		}
		return null;
	}

	// mutation

	/**
	 * Add inverse synset relations of this synset
	 *
	 * @param inverseType    inverse type
	 * @param targetSynsetId target synset id
	 */
	public void addInverseRelation(final String inverseType, final String targetSynsetId)
	{
		if (relations == null)
		{
			relations = new HashMap<>();
		}
		var rels = relations.computeIfAbsent(inverseType, (k) -> new LinkedHashSet<>());
		if (rels.contains(targetSynsetId))
		{
			throw new IllegalArgumentException(String.format("%s duplicate %s to %s", getSynsetId(), inverseType, targetSynsetId));
		}
		rels.add(targetSynsetId);
	}

	// computed

	/**
	 * Find senses of this synset
	 *
	 * @param lexesByLemma lexes
	 * @return senses of this synset
	 */
	public Sense[] findSenses(Map<String, List<Lex>> lexesByLemma)
	{
		String[] members = getMembers();
		Sense[] senses = new Sense[members.length];
		int i = 0;
		for (String member : members)
		{
			for (Lex lex : lexesByLemma.get(member))
			{
				if (lex.getPartOfSpeech() != getPartOfSpeech())
				{
					continue;
				}
				for (Sense sense : lex.getSenses())
				{
					if (sense.getSynsetId().equals(getSynsetId()))
					{
						senses[i] = sense;
						i++;
					}
				}
			}
		}
		assert i == members.length;
		return senses;
	}

	/**
	 * Find sense of lemma in this synset
	 *
	 * @param lemma        lemma
	 * @param lexesByLemma lexes
	 * @return sense of lemma in this synset, null if not found
	 */
	public Sense findSenseOf(String lemma, Map<String, List<Lex>> lexesByLemma)
	{
		var lexes = lexesByLemma.get(lemma);
		assert lexes != null : String.format("%s has no sense", lemma);
		for (Lex lex : lexes)
		{
			if (lex.getPartOfSpeech() != getPartOfSpeech())
			{
				continue;
			}
			for (Sense sense : lex.getSenses())
			{
				if (sense.getSynsetId().equals(getSynsetId()))
				{
					return sense;
				}
			}
		}
		// System.err.printf("lemma %s not found in synset %s%n", lemma, this);
		return null;
	}

	/**
	 * Find index of member is members
	 *
	 * @param lemma member lemma
	 * @return index of member is members, -1 if not found
	 */
	public int findIndexOfMember(final String lemma)
	{
		String[] members = getMembers();
		List<String> memberList = Arrays.asList(members);
		return memberList.indexOf(lemma);
	}

	// stringify

	@Override
	public String toString()
	{
		String membersStr = Formatter.join(getMembers(), ",");
		String relationsStr = Formatter.join(getRelations(), ",");
		return String.format("%s %c {%s} '%s' {%s}", getSynsetId(), getType(), membersStr, getDefinition(), relationsStr);
	}

	// identity

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
		Synset synset = (Synset) o;
		return synsetId.equals(synset.synsetId);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(synsetId);
	}

	// ordering

	@Override
	public int compareTo(final Synset that)
	{
		return getSynsetId().compareTo(that.getSynsetId());
	}
}

