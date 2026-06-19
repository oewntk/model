package org.oewntk.model

enum class SerializationMode {
    OEWN,
    DATA,
    MODEL;

    fun serialize(obj: Any, resolver: (SenseKey) -> Sense): Map<String, Any> {
        return when (obj) {
            is Lex -> when (this) {
                OEWN -> obj.toOEWNData(resolver)
                DATA -> obj.toData()
                MODEL -> throw IllegalArgumentException("$obj ${obj::class}")
            }

            is Synset -> when (this) {
                OEWN -> obj.toOEWNData()
                DATA -> obj.toData()
                MODEL -> throw IllegalArgumentException("$obj ${obj::class}")
            }

            is Sense -> when (this) {
                OEWN -> obj.toData()
                DATA -> obj.toData()
                MODEL -> throw IllegalArgumentException("$obj ${obj::class}")
            }

            else -> throw IllegalArgumentException("$obj ${obj::class}")
        }
    }
}
