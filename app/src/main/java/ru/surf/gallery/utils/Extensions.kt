package ru.surf.gallery.utils


fun String.formatPhone(): String {
    var phoneMask = "# # (###) ### ## ##"
    this.toCharArray()
        .forEach {
            phoneMask = phoneMask.replaceFirst('#', it)
        }
    return phoneMask
}

fun String.withQuotation(): String {
    return "\"${this}\""
}
