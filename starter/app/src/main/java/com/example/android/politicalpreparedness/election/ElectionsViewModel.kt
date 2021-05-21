package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.Repository
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance

class ElectionsViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getInstance(application)

    private val appRepository: Repository = Repository(database)

    val allElections = appRepository.allElections

    val followedElections = appRepository.followedElections

    init {

        viewModelScope.launch {
            appRepository.refreshElectionData()
        }

    }

    private val _navigateToSelectedElection = MutableLiveData<Election>()

    val navigateToSelectedElection: LiveData<Election>
        get() = _navigateToSelectedElection

    fun displayElectionDetails(election: Election) {
        _navigateToSelectedElection.value = election
    }

    fun displayElectionDetailsComplete() {
        _navigateToSelectedElection.value = null
    }

}