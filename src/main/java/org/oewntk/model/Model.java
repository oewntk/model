/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Language model
 */
public class Model extends CoreModel
{
	/**
	 * Verb frames
	 */
	public final Collection<VerbFrame> verbFrames;

	/**
	 * Verb templates
	 */
	public final Collection<VerbTemplate> verbTemplates;

	/**
	 * Extra input directory
	 */
	private File source2;

	/**
	 * Constructor
	 *
	 * @param lexes                lexes
	 * @param senses               senses
	 * @param synsets              synsets
	 * @param verbFrames           verb frames
	 * @param verbTemplates        verb templates
	 * @param senseToVerbTemplates sensekey-to-verb template
	 * @param senseToTagCounts     sensekey-to-tagcount
	 */
	public Model( //
			final Collection<Lex> lexes, //
			final Collection<Sense> senses, //
			final Collection<Synset> synsets, //
			final Collection<VerbFrame> verbFrames, //
			final Collection<VerbTemplate> verbTemplates, //
			final Collection<Entry<String, int[]>> senseToVerbTemplates, //
			final Collection<Entry<String, TagCount>> senseToTagCounts)
	{
		super(lexes, senses, synsets);

		this.verbFrames = Collections.unmodifiableCollection(verbFrames);
		this.verbTemplates = Collections.unmodifiableCollection(verbTemplates);

		// set sense's verb templates
		var sensesById = getSensesById();
		for (Entry<String, int[]> entry : senseToVerbTemplates)
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
		for (Entry<String, TagCount> entry : senseToTagCounts)
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
	 * @param coreModel             base model
	 * @param verbFrames            verb frames
	 * @param verbTemplates         verb templates
	 * @param sensesToVerbTemplates collection of entries of type sensekey-to-verb template
	 * @param sensesToTagCounts     collection of entries of type sensekey-to-tagcount
	 */
	public Model(final CoreModel coreModel, final Collection<VerbFrame> verbFrames, final Collection<VerbTemplate> verbTemplates, final Collection<Entry<String, int[]>> sensesToVerbTemplates, final Collection<Entry<String, TagCount>> sensesToTagCounts)
	{
		this(coreModel.lexes, coreModel.senses, coreModel.synsets, verbFrames, verbTemplates, sensesToVerbTemplates, sensesToTagCounts);
	}

	/**
	 * Verb frames mapped by id
	 *
	 * @return verb frames mapped by id
	 */
	public Map<String, VerbFrame> getVerbFramesById()
	{
		return Mapper.map(verbFrames, VerbFrame::getId);
	}

	/**
	 * Verb templates mapped by id
	 *
	 * @return verb templates mapped by id
	 */
	public Map<Integer, VerbTemplate> getVerbTemplatesById()
	{
		return Mapper.map(verbTemplates, VerbTemplate::getId);
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
		return new File[]{source, source2};
	}

	/**
	 * Info about this model
	 *
	 * @return info
	 */
	public String info()
	{
		return super.info() + String.format(", verb frames: %d, verb templates: %s", verbFrames.size(), verbTemplates.size());
	}
}
