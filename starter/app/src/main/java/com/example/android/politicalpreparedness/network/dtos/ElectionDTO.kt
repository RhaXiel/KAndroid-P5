package com.example.android.politicalpreparedness.network.dtos

import com.example.android.politicalpreparedness.database.entities.DatabaseElection
import com.example.android.politicalpreparedness.network.models.Election
import com.squareup.moshi.Json
import java.util.*

data class ElectionDTO(
        val id: Int,
        val name: String,
        val electionDay: Date,
        @Json(name="ocdDivisionId") val division: DivisionDTO
)

fun List<ElectionDTO>.asDomainModel(): List<DatabaseElection>{
    return this.map {
        DatabaseElection(
                id = it.id,
                name = it.name,
                electionDay = it.electionDay,
                division = it.division.asDomainModel()
        )
    }
}

fun List<ElectionDTO>.asDatabaseModel(): Array<DatabaseElection>{
    return this.map {
        DatabaseElection(
                id = it.id,
                name = it.name,
                electionDay = it.electionDay,
                division = it.division.asDatabaseModel()
        )
    }.toTypedArray()
}

fun ElectionDTO.asDomainModel(): Election{
    return Election(
                id = this.id,
                name = this.name,
                electionDay = this.electionDay,
                division = this.division.asDomainModel()
        )
}