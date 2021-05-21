package com.example.android.politicalpreparedness.network.dtos

import com.example.android.politicalpreparedness.network.models.Office
import com.example.android.politicalpreparedness.network.models.Official

data class RepresentativeResponseDTO(
        val offices: List<Office>,
        val officials: List<Official>
)
val RepresentativeResponseDTO.representatives
    get() = offices.flatMap { it.getRepresentatives(officials) }


