/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.Serializable;

/**
 * Verb frame
 */
public class VerbFrame implements Serializable
{
	// name, frame, frameid
	public static final Object[][] VALUES = new Object[][]{ //
			{"vii", "Something ----s", 1}, //
			{"via", "Somebody ----s", 2}, //
			{"nonreferential", "It is ----ing", 3}, //
			{"vii-pp", "Something is ----ing PP", 4}, //
			{"vtii-adj", "Something ----s something Adjective/Noun", 5}, //
			{"vii-adj", "Something ----s Adjective/Noun", 6}, //
			{"via-adj", "Somebody ----s Adjective", 7}, //
			{"vtai", "Somebody ----s something", 8}, //
			{"vtaa", "Somebody ----s somebody", 9}, //
			{"vtia", "Something ----s somebody", 10}, //
			{"vtii", "Something ----s something", 11}, //
			{"vii-to", "Something ----s to somebody", 12}, //
			{"via-on-inanim", "Somebody ----s on something", 13}, //
			{"ditransitive", "Somebody ----s somebody something", 14}, //
			{"vtai-to", "Somebody ----s something to somebody", 15}, //
			{"vtai-from", "Somebody ----s something from somebody", 16}, //
			{"vtaa-with", "Somebody ----s somebody with something", 17}, //
			{"vtaa-of", "Somebody ----s somebody of something", 18}, //
			{"vtai-on", "Somebody ----s something on somebody", 19}, //
			{"vtaa-pp", "Somebody ----s somebody PP", 20}, //
			{"vtai-pp", "Somebody ----s something PP", 21}, //
			{"via-pp", "Somebody ----s PP", 22}, //
			{"vibody", "Somebody's (body part) ----s", 23}, //
			{"vtaa-to-inf", "Somebody ----s somebody to INFINITIVE", 24}, //
			{"vtaa-inf", "Somebody ----s somebody INFINITIVE", 25}, //
			{"via-that", "Somebody ----s that CLAUSE", 26}, //
			{"via-to", "Somebody ----s to somebody", 27}, //
			{"via-to-inf", "Somebody ----s to INFINITIVE", 28}, //
			{"via-whether-inf", "Somebody ----s whether INFINITIVE", 29}, //
			{"vtaa-into-ger", "Somebody ----s somebody into V-ing something", 30}, //
			{"vtai-with", "Somebody ----s something with something", 31}, //
			{"via-inf", "Somebody ----s INFINITIVE", 32}, //
			{"via-ger", "Somebody ----s VERB-ing", 33}, //
			{"nonreferential-sent", "It ----s that CLAUSE", 34}, //
			{"vii-inf", "Something ----s INFINITIVE", 35}, //
			{"via-at", "Somebody ----s at something", 36}, //
			{"via-for", "Somebody ----s for something", 37}, //
			{"via-on-anim", "Somebody ----s on somebody", 38}, //
			{"via-out-of", "Somebody ----s out of somebody", 39}, //
	};

	/**
	 * Verb frame id
	 */
	private final String id;

	/**
	 * Verb frame
	 */
	private final String frame;

	/**
	 * Constructor
	 *
	 * @param id    verb frame id
	 * @param frame verb frame
	 */
	public VerbFrame(final String id, final String frame)
	{
		this.id = id;
		this.frame = frame;
	}

	/**
	 * Get verb frame id
	 *
	 * @return verb frame id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * Get verb frame
	 *
	 * @return verb frame
	 */
	public String getFrame()
	{
		return frame;
	}

	// stringify

	@Override
	public String toString()
	{
		return String.format("%s '%s'", getId(), getFrame());
	}
}