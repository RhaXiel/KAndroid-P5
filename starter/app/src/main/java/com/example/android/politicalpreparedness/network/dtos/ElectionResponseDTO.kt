package com.example.android.politicalpreparedness.network.dtos

data class ElectionResponseDTO(
        val kind: String,
        val elections: List<ElectionDTO>
)