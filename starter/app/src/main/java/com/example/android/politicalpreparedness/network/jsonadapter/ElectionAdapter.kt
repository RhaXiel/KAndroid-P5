package com.example.android.politicalpreparedness.network.jsonadapter

import com.example.android.politicalpreparedness.network.dtos.DivisionDTO
import com.example.android.politicalpreparedness.network.models.Division
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class ElectionAdapter {
    @FromJson
    fun divisionFromJson(ocdDivisionId: String): DivisionDTO {
        val countryDelimiter = "country:"
        val stateDelimiter = "state:"
        val country = ocdDivisionId.substringAfter(countryDelimiter, "")
                .substringBefore("/")
        val state = ocdDivisionId.substringAfter(stateDelimiter, "")
                .substringBefore("/")
        return DivisionDTO(ocdDivisionId, country, state)
    }

    @ToJson
    fun divisionToJson(division: Division): String {
        return division.id
    }
}