package ru.toliak.chatpostprocessor.config

fun generateRandomString(length: Int): String {
    // https://stackoverflow.com/a/57077555/14142236

    // Descriptive alphabet using three CharRange objects, concatenated
    val alphabet: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    // Build list from 20 random samples from the alphabet,
    // and convert it to a string using "" as element separator
    return List(length) { alphabet.random() }.joinToString("")
}

data class ConfigMainItem(
        val pattern: String,
        val replaceBy: String,
        private val _tempString: String?,
        private val _ignoreCase: Boolean?,
        val key: String
) {
    val tempString: String = _tempString ?: "__TMP_${generateRandomString(16)}"
    val ignoreCase: Boolean = _ignoreCase ?: false
}

class ConfigMain(cfg: List<Map<*, *>>) {
    val values: List<ConfigMainItem>

    init {
        values = ArrayList()
        for (v in cfg) {
            values += ConfigMainItem(
                    pattern = v["pattern"].toString(),
                    replaceBy = v["replace-by"].toString(),
                    _tempString = v["temp-string"] as String?,
                    _ignoreCase = v["ignore-case"] as Boolean?,
                    key = v["key"].toString(),
            )
        }
    }
}