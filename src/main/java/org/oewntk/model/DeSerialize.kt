/*
 * Copyright (c) 2024-2024. Bernard Bou.
 */

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
        FileInputStream(file).use {
            return deSerializeCoreModel(it)
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
        FileInputStream(file).use {
            return deSerializeModel(it)
        }
    }

    /**
     * Deserialize core model from file
     *
     * @param inputStream input stream
     * @return core model
     * @throws IOException io exception
     * @throws ClassNotFoundException class not found exception
     */
    @Throws(IOException::class, ClassNotFoundException::class)
    fun deSerializeCoreModel(inputStream: InputStream): CoreModel {
        return deSerialize(inputStream) as CoreModel
    }

    /**
     * Deserialize model from file
     *
     * @param inputStream input stream
     * @return model
     * @throws IOException io exception
     * @throws ClassNotFoundException class not found exception
     */
    @Throws(IOException::class, ClassNotFoundException::class)
    fun deSerializeModel(inputStream: InputStream): Model {
        return deSerialize(inputStream) as Model
    }

    /**
     * Deserialize object
     *
     * @param inputStream input stream
     * @return object
     * @throws IOException io exception
     * @throws ClassNotFoundException class not found exception
     */
    @Throws(IOException::class, ClassNotFoundException::class)
    private fun deSerialize(inputStream: InputStream): Any {
        ObjectInputStream(inputStream).use {
            return it.readObject()
        }
    }
}
