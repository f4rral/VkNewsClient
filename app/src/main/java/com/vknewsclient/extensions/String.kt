package com.vknewsclient.extensions

import android.net.Uri

fun String.encode(): String {
    return Uri.encode(this)
}