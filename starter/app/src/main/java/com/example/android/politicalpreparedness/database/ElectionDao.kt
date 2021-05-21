package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.database.entities.DatabaseElection
import com.example.android.politicalpreparedness.database.entities.DatabaseFollowedElection

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllElections(vararg elections: DatabaseElection)

    @Query("SELECT * FROM election_table")
    fun getAllElections(): LiveData<List<DatabaseElection>>  // No need for a suspend function since LiveData is already asynchronous.

    @Query("SELECT * FROM election_table INNER JOIN followed_elections ON election_table.id = followed_elections.id ")
    fun getFollowedElections(): LiveData<List<DatabaseElection>>

    @Query("SELECT election_table.id FROM election_table WHERE id =:id AND  id IN followed_elections ")
    suspend fun getFollowedElection(id: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun followElection(id: DatabaseFollowedElection)

    @Query("DELETE FROM followed_elections WHERE id = :id")
    suspend fun unfollowElection(id: Int)

    @Query("DELETE FROM followed_elections")
    suspend fun unfollowAllElections()

}