package com.example.android.politicalpreparedness.network.models

data class RepresentativeResponse(
        val offices: List<Office>,
        val officials: List<Official>
)

val RepresentativeResponse.representatives
    get() = offices.flatMap { it.getRepresentatives(officials) }