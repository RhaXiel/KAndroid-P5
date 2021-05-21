package com.example.android.politicalpreparedness.network.dtos

import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

class VoterInfoResponseDTO (
        val election: ElectionDTO,
        //val pollingLocations: String? = null, //TODO: Future Use
        // val contests: String? = null, //TODO: Future Use
        val state: List<StateDTO>
        //val electionElectionOfficials: List<NetworkElectionOfficial>? = null
)

fun VoterInfoResponseDTO.asDomainModel(): VoterInfoResponse{
    return VoterInfoResponse(
            election = this.election.asDomainModel(),
            state = this.state.map { it.asDomainModelForNetworkState() }
    )
}