/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Generator of inverse synset relations
 */
public class InverseRelationFactory
{
	private static final boolean LOG_ALREADY_PRESENT = false;

	private static final Map<String, String> INVERSE_SYNSET_RELATIONS = new HashMap<>();

	static
	{
		INVERSE_SYNSET_RELATIONS.put("hypernym", "hyponym");
		INVERSE_SYNSET_RELATIONS.put("instance_hypernym", "instance_hyponym");
		INVERSE_SYNSET_RELATIONS.put("mero_part", "holo_part");
		INVERSE_SYNSET_RELATIONS.put("mero_member", "holo_member");
		INVERSE_SYNSET_RELATIONS.put("mero_substance", "holo_substance");
		INVERSE_SYNSET_RELATIONS.put("causes", "is_caused_by");
		INVERSE_SYNSET_RELATIONS.put("entails", "is_entailed_by");
		INVERSE_SYNSET_RELATIONS.put("exemplifies", "is_exemplified_by");
		INVERSE_SYNSET_RELATIONS.put("domain_topic", "has_domain_topic");
		INVERSE_SYNSET_RELATIONS.put("domain_region", "has_domain_region");
	}

	private static final Set<String> INVERSE_SYNSET_RELATIONS_KEYS = INVERSE_SYNSET_RELATIONS.keySet();

	private static final Map<String, String> INVERSE_SENSE_RELATIONS = new HashMap<>();

	static
	{
		INVERSE_SENSE_RELATIONS.put("exemplifies", "is_exemplified_by");
		INVERSE_SENSE_RELATIONS.put("domain_topic", "has_domain_topic");
		INVERSE_SENSE_RELATIONS.put("domain_region", "has_domain_region");
	}

	private static final Set<String> INVERSE_SENSE_RELATIONS_KEYS = INVERSE_SENSE_RELATIONS.keySet();

	private InverseRelationFactory()
	{
	}

	/**
	 * Generate inverse synset relations
	 *
	 * @param synsetsById synsets mapped by id
	 */
	public static int makeSynsetRelations(Map<String, Synset> synsetsById)
	{
		int count = 0;
		for (Map.Entry<String, Synset> entry : synsetsById.entrySet())
		{
			String sourceSynsetId = entry.getKey();
			Synset sourceSynset = entry.getValue();
			Map<String, Set<String>> relations = sourceSynset.getRelations();
			if (relations != null && relations.size() > 0)
			{
				for (String type : INVERSE_SYNSET_RELATIONS_KEYS)
				{
					Collection<String> targetSynsetIds = relations.get(type);
					if (targetSynsetIds != null && targetSynsetIds.size() > 0)
					{
						String inverseType = INVERSE_SYNSET_RELATIONS.get(type);
						for (String targetSynsetId : targetSynsetIds)
						{
							Synset targetSynset = synsetsById.get(targetSynsetId);
							assert targetSynset != null;
							try
							{
								targetSynset.addInverseRelation(inverseType, sourceSynsetId);
								count++;
							}
							catch (IllegalArgumentException e)
							{
								if (LOG_ALREADY_PRESENT)
								{
									Tracing.psErr.printf("[W] %s%n", e.getMessage());
								}
							}
						}
					}
				}
			}
		}
		return count;
	}

	/**
	 * Generate inverse sense relations
	 *
	 * @param sensesById senses mapped by id
	 */
	public static int makeSenseRelations(Map<String, Sense> sensesById)
	{
		int count = 0;
		for (Map.Entry<String, Sense> entry : sensesById.entrySet())
		{
			String sourceSenseId = entry.getKey();
			Sense sourceSense = entry.getValue();
			Map<String, Set<String>> relations = sourceSense.getRelations();
			if (relations != null && relations.size() > 0)
			{
				for (String type : INVERSE_SENSE_RELATIONS_KEYS)
				{
					Collection<String> targetSenseIds = relations.get(type);
					if (targetSenseIds != null && targetSenseIds.size() > 0)
					{
						String inverseType = INVERSE_SENSE_RELATIONS.get(type);
						for (String targetSenseId : targetSenseIds)
						{
							Sense targetSense = sensesById.get(targetSenseId);
							assert targetSense != null;
							try
							{
								targetSense.addInverseRelation(inverseType, sourceSenseId);
								count++;
							}
							catch (IllegalArgumentException e)
							{
								if (LOG_ALREADY_PRESENT)
								{
									Tracing.psErr.printf("[W] %s%n", e.getMessage());
								}
							}
						}
					}
				}
			}
		}
		return count;
	}
}
