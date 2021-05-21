package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter

class ElectionsFragment : Fragment() {

    private lateinit var binding: FragmentElectionBinding

    private val viewModel: ElectionsViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, ElectionsViewModelFactory(activity.application)).get(
                ElectionsViewModel::class.java
        )
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_election, container, false)
        binding.lifecycleOwner = this
        binding.electionListViewModel = viewModel

        binding.upcomingElectionRecycler.adapter = ElectionListAdapter(ElectionListAdapter.OnClickListener {
            viewModel.displayElectionDetails(it)
        })

        viewModel.navigateToSelectedElection.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController()
                        .navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(it))
                viewModel.displayElectionDetailsComplete()
            }
        })

        binding.savedElectionRecycler.adapter = ElectionListAdapter(ElectionListAdapter.OnClickListener {
            viewModel.displayElectionDetails(it)
        })

        return binding.root
    }
}