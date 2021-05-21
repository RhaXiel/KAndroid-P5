package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.Repository
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.dtos.asDomainModel
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class VoterInfoViewModel(val election: Election, application: Application) : AndroidViewModel(application) {

    var client: CivicsApiService = CivicsApi.retrofitService

    private val database = ElectionDatabase.getInstance(application)
    private val appRepository: Repository = Repository(database)

    private val _selectedElection = MutableLiveData<Election>()

    val selectedElection: LiveData<Election>
        get() = _selectedElection

    private val voterInfoForSelectedElection = MutableLiveData<VoterInfoResponse>()

    private val _followButtonText = MutableLiveData<String>()
    val followButtonText: LiveData<String> = _followButtonText

    private suspend fun refreshVoterInfo(electionId: Int, address: String) {

        withContext(Dispatchers.IO) {

            try {
                val networkVoterInfoResponse = client.getVoterInfo(electionId, address)
                val voterInfoDomainModel = networkVoterInfoResponse.asDomainModel()
                voterInfoForSelectedElection.postValue(voterInfoDomainModel)
            } catch (e: Exception) {
                Log.d("ExceptionInRefreshVoterInfo", e.toString())
            }

        }

    }

    val votingLocation: LiveData<String> = Transformations.map(voterInfoForSelectedElection) {
        it?.state?.first()?.electionAdministrationBody?.votingLocationFinderUrl
                ?: ""
    }
    val ballotInfoUrl: LiveData<String> = Transformations.map(voterInfoForSelectedElection) {
        it?.state?.first()?.electionAdministrationBody?.ballotInfoUrl
                ?: ""
    }

    val mailingAddress: LiveData<String> = Transformations.map(voterInfoForSelectedElection) {
        it?.state?.first()?.electionAdministrationBody?.correspondenceAddress?.toFormattedString()
                ?: "No address provided"
    }

    private suspend fun updateFollowButtonText() = withContext(Dispatchers.IO) {
        val text = if (appRepository.isElectionFollowed(election.id)) {
            "Unfollow Election"
        } else {
            "Follow Election"
        }

        _followButtonText.postValue(text)
    }

    fun followOrUnfollow() = viewModelScope.launch(Dispatchers.IO) {
        if (appRepository.isElectionFollowed(election.id)) {
            appRepository.unfollowElection(election.id)
            updateFollowButtonText()
        } else {
            appRepository.followElection(election.id)
            updateFollowButtonText()
        }

    }

    init {
        _selectedElection.value = election
        viewModelScope.launch {
            updateFollowButtonText()
            selectedElection.value?.let {
                refreshVoterInfo(it.id, it.division.country + "/" + it.division.state)
            }

        }

    }

}