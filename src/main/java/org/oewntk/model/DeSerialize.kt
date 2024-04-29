package org.oewntk.model

import java.io.*

object DeSerialize {
	/**
	 * Deserialize core model from file
	 *
	 * @param file file
	 * @return core model
	 * @throws IOException io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	@Throws(IOException::class, ClassNotFoundException::class)
	fun deSerializeCoreModel(file: File): CoreModel {
		FileInputStream(file).use { `is` ->
			return deSerializeCoreModel(`is`)
		}
	}

	/**
	 * Deserialize model from file
	 *
	 * @param file file
	 * @return model
	 * @throws IOException io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	@Throws(IOException::class, ClassNotFoundException::class)
	fun deSerializeModel(file: File): Model {
		FileInputStream(file).use { `is` ->
			return deSerializeModel(`is`)
		}
	}

	/**
	 * Deserialize core model from file
	 *
	 * @param is input stream
	 * @return core model
	 * @throws IOException io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	@Throws(IOException::class, ClassNotFoundException::class)
	fun deSerializeCoreModel(`is`: InputStream): CoreModel {
		return deSerialize(`is`) as CoreModel
	}

	/**
	 * Deserialize model from file
	 *
	 * @param is input stream
	 * @return model
	 * @throws IOException io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	@Throws(IOException::class, ClassNotFoundException::class)
	fun deSerializeModel(`is`: InputStream): Model {
		return deSerialize(`is`) as Model
	}

	/**
	 * Deserialize object
	 *
	 * @param is input stream
	 * @return object
	 * @throws IOException io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	@Throws(IOException::class, ClassNotFoundException::class)
	private fun deSerialize(`is`: InputStream): Any {
		ObjectInputStream(`is`).use { ois ->
			return ois.readObject()
		}
	}
}
