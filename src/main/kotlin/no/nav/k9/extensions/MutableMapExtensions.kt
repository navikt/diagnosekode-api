package no.nav.k9.extensions

fun <T>Map<String, T>.getMatchingEntries(query: String): Map<String, T> {
    return this.filterKeys { it.contains(query.lowercase()) }
}
