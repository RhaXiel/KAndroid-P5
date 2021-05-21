package com.example.android.politicalpreparedness.network.dtos

import com.example.android.politicalpreparedness.network.models.AdministrationBody

data class AdministrationBodyDTO(
        val name: String? = null,
        val electionInfoUrl: String? = null,
        val votingLocationFinderUrl: String? = null,
        val ballotInfoUrl: String? = null,
        val correspondenceAddress: AddressDTO? = null
)

fun AdministrationBodyDTO.asDomainModel(): AdministrationBody {
    return AdministrationBody(
            name = this.name,
            electionInfoUrl = this.electionInfoUrl,
            votingLocationFinderUrl = this.votingLocationFinderUrl,
            ballotInfoUrl = this.ballotInfoUrl,
            correspondenceAddress = this.correspondenceAddress?.asDomainModel()

    )
}