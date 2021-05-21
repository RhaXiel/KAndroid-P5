package com.example.android.politicalpreparedness.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "followed_elections")
data class DatabaseFollowedElection constructor(
        @PrimaryKey
        val id: Int
)