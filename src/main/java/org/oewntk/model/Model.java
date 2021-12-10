/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Language model
 */
public class Model extends CoreModel
{
	/**
	 * Verb frames mapped by id (tag)
	 */
	public final Map<String, VerbFrame> verbFramesById;

	/**
	 * Verb templates mapped by id (num)
	 */
	public final Map<Integer, VerbTemplate> verbTemplatesById;

	/**
	 * Extra input directory
	 */
	private File source2;

	/**
	 * Constructor
	 *
	 * @param lexesByLemma         lexes a multimap
	 * @param sensesById           senses
	 * @param synsetsById          synsets
	 * @param verbFramesById       verb frames
	 * @param verbTemplatesById    verb templates
	 * @param senseToVerbTemplates sensekey-to-verb template
	 * @param senseToTagCounts     sensekey-to-tagcount
	 */
	public Model( //
			final Map<String, List<Lex>> lexesByLemma, //
			final Map<String, Sense> sensesById, //
			final Map<String, Synset> synsetsById, //
			final Map<String, VerbFrame> verbFramesById, //
			final Map<Integer, VerbTemplate> verbTemplatesById, //
			final Map<String, int[]> senseToVerbTemplates, //
			final Map<String, TagCount> senseToTagCounts)
	{
		super(lexesByLemma, sensesById, synsetsById);

		this.verbFramesById = verbFramesById;
		this.verbTemplatesById = verbTemplatesById;

		// set sense's verb templates
		for (Map.Entry<String, int[]> entry : senseToVerbTemplates.entrySet())
		{
			String sensekey = entry.getKey();
			int[] templatesIds = entry.getValue();
			Sense sense = sensesById.get(sensekey);
			if (sense != null)
			{
				sense.setVerbTemplates(templatesIds);
			}
		}
		// set sense's tag counts
		for (Map.Entry<String, TagCount> entry : senseToTagCounts.entrySet())
		{
			String sensekey = entry.getKey();
			TagCount tagCount = entry.getValue();
			Sense sense = sensesById.get(sensekey);
			if (sense != null)
			{
				sense.setTagCount(tagCount);
			}
		}
	}

	/**
	 * Constructor from base model
	 *
	 * @param coreModel            base model
	 * @param verbFramesById       verb frames
	 * @param verbTemplatesById    verb templates
	 * @param senseToVerbTemplates sensekey-to-verb template
	 * @param senseToTagCounts     sensekey-to-tagcount
	 */
	public Model(final CoreModel coreModel, final Map<String, VerbFrame> verbFramesById, final Map<Integer, VerbTemplate> verbTemplatesById, final Map<String, int[]> senseToVerbTemplates, final Map<String, TagCount> senseToTagCounts)
	{
		this(coreModel.lexesByLemma, coreModel.sensesById, coreModel.synsetsById, verbFramesById, verbTemplatesById, senseToVerbTemplates, senseToTagCounts);
	}

	/**
	 * Record sources
	 *
	 * @param sources sources
	 * @return this
	 */
	public Model setSources(final File... sources)
	{
		if (sources.length > 0)
		{
			this.setSource(sources[0]);
		}
		if (sources.length > 1)
		{
			this.source2 = sources[1];
		}
		return this;
	}

	/**
	 * Get input sources
	 *
	 * @return input sources
	 */
	public File[] getSources()
	{
		return new File[]{ source, source2 };
	}

	/**
	 * Info about this model
	 *
	 * @return info
	 */
	public String info()
	{
		return super.info() + String.format(", verb frames: %d, verb templates: %s", verbFramesById.size(), verbTemplatesById.size());
	}
}
