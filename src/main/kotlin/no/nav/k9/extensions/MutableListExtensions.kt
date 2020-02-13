package no.nav.k9.extensions

fun <T>List<T>.safeSubList(min: Int = 0, max: Int?): List<T> {
    if (max == null || 0 > max) {
        return this
    }

    if (max >= this.size) {
        return this.subList(min, this.size)
    }

    return this.subList(min, max)
}
