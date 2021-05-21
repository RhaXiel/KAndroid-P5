package com.example.android.politicalpreparedness.network.dtos

import com.example.android.politicalpreparedness.network.models.Address

data class AddressDTO(
        val line1: String,
        val line2: String? = null,
        val city: String,
        val state: String,
        val zip: String
) {
    fun toFormattedString(): String {
        var output = line1.plus("\n")
        if (!line2.isNullOrEmpty()) output = output.plus(line2).plus("\n")
        output = output.plus("$city, $state $zip")
        return output
    }
}

fun AddressDTO.asDomainModel(): Address {
    return Address(
            line1 = this.line1,
            line2 = this.line2,
            city = this.city,
            state = this.state,
            zip = this.zip

    )
}