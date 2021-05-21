package com.example.android.politicalpreparedness.network.dtos

import com.example.android.politicalpreparedness.network.models.State

data class StateDTO (
        val name: String,
        val electionAdministrationBody: AdministrationBodyDTO
)

fun StateDTO.asDomainModelForNetworkState(): State {
    return State(
            name = this.name,
            electionAdministrationBody = this.electionAdministrationBody.asDomainModel()
    )

}