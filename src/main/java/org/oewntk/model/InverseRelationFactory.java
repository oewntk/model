/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generator of inverse synset relations
 */
public class InverseRelationFactory
{
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

	private InverseRelationFactory()
	{
	}

	/**
	 * Generate inverse synset relations
	 *
	 * @param synsetsById synsets mapped by id
	 */
	public static void make(Map<String, Synset> synsetsById)
	{
		for (Map.Entry<String, Synset> entry : synsetsById.entrySet())
		{
			String sourceSynsetId = entry.getKey();
			Synset sourceSynset = entry.getValue();
			Map<String, List<String>> relations = sourceSynset.getRelations();
			if (relations != null && relations.size() > 0)
			{
				for (String type : INVERSE_SYNSET_RELATIONS_KEYS)
				{
					List<String> targetSynsetIds = relations.get(type);
					if (targetSynsetIds != null && targetSynsetIds.size() > 0)
					{
						String inverseType = INVERSE_SYNSET_RELATIONS.get(type);
						for (String targetSynsetId : targetSynsetIds)
						{
							Synset targetSynset = synsetsById.get(targetSynsetId);
							assert targetSynset != null;
							targetSynset.addInverseRelation(inverseType, sourceSynsetId);
						}
					}
				}
			}
		}
	}
}
