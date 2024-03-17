package com.clara.timeticket.model

import com.google.gson.annotations.SerializedName

data class DeleteTicketResponse(
    @SerializedName("status") val status: String? = null
)
