package com.example.android.politicalpreparedness.network.dtos

import com.example.android.politicalpreparedness.network.models.Division

data class DivisionDTO(
        val id: String,
        val country: String,
        val state: String
)

fun DivisionDTO.asDomainModel(): Division {
    return Division(
            id = this.id,
            country = this.country,
            state = this.state

    )

}

fun DivisionDTO.asDatabaseModel(): Division {
    return Division(
            id = this.id,
            country = this.country,
            state = this.state

    )

}
