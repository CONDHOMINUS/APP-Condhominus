package com.example.condhominus.utils

class TextUtils {
    companion object {
        @JvmStatic
        fun removeMask(s: String): String {
            return s.replace("[.]".toRegex(), "").replace("[-]".toRegex(), "")
                .replace("[/]".toRegex(), "").replace("[(]".toRegex(), "")
                .replace("[)]".toRegex(), "").replace(" ".toRegex(), "")
                .replace(",".toRegex(), "").replace(" ".toRegex(), "")
        }
    }
}