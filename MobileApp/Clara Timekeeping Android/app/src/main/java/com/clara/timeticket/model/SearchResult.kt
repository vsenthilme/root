package com.clara.timeticket.model

import java.io.Serializable

data class SearchResult(
    var id: String? = null,
    var name: String? = null,
    var isChecked: Boolean = false
) : Serializable

