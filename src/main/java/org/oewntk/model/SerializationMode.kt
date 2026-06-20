package org.oewntk.model

enum class SerializationMode {
    OEWN,
    DATA,
    MODEL;

    fun serialize(obj: Any, resolver: (SenseKey) -> Sense, leaveRedundantRelation: Boolean = false): Map<String, Any> {
        return when (obj) {
            is Lex -> when (this) {
                OEWN -> obj.toOEWNData(resolver, leaveRedundantRelation = leaveRedundantRelation)
                DATA -> obj.toData()
                MODEL -> throw IllegalArgumentException("$obj ${obj::class}")
            }

            is Synset -> when (this) {
                OEWN -> obj.toOEWNData(leaveRedundantRelation = leaveRedundantRelation)
                DATA -> obj.toData()
                MODEL -> throw IllegalArgumentException("$obj ${obj::class}")
            }

            is Sense -> when (this) {
                OEWN -> obj.toOEWNData(leaveRedundantRelation = leaveRedundantRelation)
                DATA -> obj.toData()
                MODEL -> throw IllegalArgumentException("$obj ${obj::class}")
            }

            else -> throw IllegalArgumentException("${obj::class} $obj")
        }
    }
}
