package com.hsn.rc_placement.ui.home

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialElevationScale
import com.hsn.rc_placement.R
import com.hsn.rc_placement.database.AppDatabase
import com.hsn.rc_placement.database.CompanyDao
import com.hsn.rc_placement.databinding.FragmentHomeBinding
import com.hsn.rc_placement.model.Company
import com.hsn.rc_placement.ui.adapter.CompanyAdapter
import com.hsn.rc_placement.ui.adapter.CompanyAdapterInterface


class HomeFragment : Fragment(), CompanyAdapterInterface {


    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var companyDb: CompanyDao
    private val companyAdapter = CompanyAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        companyDb = AppDatabase.getInstance(requireContext()).companyDb
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.run {
            binding.emptyListMsg.text = getString(R.string.no_company)
            binding.addButton.text = getString(R.string.add_comapany)
            executePendingBindings()
        }

        val factory = HomeVMFactory(companyDb)
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.setOnClickListener { navigateToAddCompany() }
        setupObservers()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.mainRecycler.adapter = companyAdapter
    }

    private fun setupObservers() {
        viewModel.companyList.observe(viewLifecycleOwner) {
            // submit the list to the recycler view
            companyAdapter.submitList(it)
        }

        // another observer to control ui visibility
        viewModel.companyList.observe(viewLifecycleOwner) {
            binding.mainRecycler.isVisible = !it.isNullOrEmpty()
            binding.emptyListContainer.isVisible = it.isNullOrEmpty()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_company_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.add_company -> {
                // navigate to add fragment
                navigateToAddCompany()
            }
        }
        return false
    }

    // navigate to Add Fragment
    private fun navigateToAddCompany() {
        val action = HomeFragmentDirections.actionHomeFragmentToAddFragment(addCompany = true)
        findNavController().navigate(action)
    }

    // navigate to company detail screen
    override fun onClickCompany(view: View, item: Company) {

        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.anim_med).toLong()
        }

        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.anim_med).toLong()
        }

        val transitionDestName = getString(R.string.home_transition_name)
        val extras = FragmentNavigatorExtras(view to transitionDestName)
        val action = HomeFragmentDirections.actionHomeFragmentToStudentFragment(
            companyName = item.companyName,
            companyId = item.companyId
        )
        findNavController().navigate(action, extras)
    }

}