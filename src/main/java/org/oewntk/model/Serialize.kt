package org.oewntk.model

import java.io.*

/**
 * Serialize models
 */
object Serialize {
	/**
	 * Serialize model to file
	 *
	 * @param model model
	 * @param file  file
	 * @throws IOException io exception
	 */
	@Throws(IOException::class)
	fun serializeModel(model: Model, file: File) {
		FileOutputStream(file).use { os ->
			serializeModel(os, model)
		}
	}

	/**
	 * Serialize model to output stream
	 *
	 * @param model model
	 * @param os    output stream
	 * @throws IOException io exception
	 */
	@Throws(IOException::class)
	fun serializeModel(os: OutputStream, model: Model) {
		serialize(os, model)
	}

	/**
	 * Serialize core model to file
	 *
	 * @param model core model
	 * @param file  file
	 * @throws IOException io exception
	 */
	@JvmStatic
	@Throws(IOException::class)
	fun serializeCoreModel(model: CoreModel, file: File) {
		FileOutputStream(file).use { os ->
			serializeCoreModel(os, model)
		}
	}

	/**
	 * Serialize core model to output stream
	 *
	 * @param model core model
	 * @param os    output stream
	 * @throws IOException io exception
	 */
	@Throws(IOException::class)
	fun serializeCoreModel(os: OutputStream, model: CoreModel?) {
		serialize(os, model)
	}

	/**
	 * Serialize object to output stream
	 *
	 * @param os     output stream
	 * @param object object
	 * @throws IOException io exception
	 */
	@Throws(IOException::class)
	private fun serialize(os: OutputStream, `object`: Any?) {
		ObjectOutputStream(os).use { oos ->
			oos.writeObject(`object`)
		}
	}
}
