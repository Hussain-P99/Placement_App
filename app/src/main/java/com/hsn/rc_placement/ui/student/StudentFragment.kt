package com.hsn.rc_placement.ui.student

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import com.hsn.rc_placement.R
import com.hsn.rc_placement.database.AppDatabase
import com.hsn.rc_placement.databinding.FragmentHomeBinding
import com.hsn.rc_placement.ui.adapter.StudentAdapter
import com.hsn.rc_placement.util.themeColor

private const val TAG = "StudentFragment"

class StudentFragment : Fragment() {

    // home fragment reused here to show the list
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: StudentViewModel
    private var companyName: String = ""
    private var companyId: Long = -1

    private val studentAdapter = StudentAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.navHostFragment
            duration = resources.getInteger(R.integer.anim_med).toLong()
            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }

        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.anim_med).toLong()
        }

        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.anim_med).toLong()
        }

        companyName = StudentFragmentArgs.fromBundle(requireArguments()).companyName
        companyId = StudentFragmentArgs.fromBundle(requireArguments()).companyId
        Log.i(TAG, "$companyName $companyId")

        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (requireActivity() as AppCompatActivity).supportActionBar?.title = companyName

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.run {
            emptyListMsg.text = getString(R.string.no_student)
            addButton.text = getString(R.string.add_student)
            executePendingBindings()
        }

        val factory =
            StudentVMFactory(AppDatabase.getInstance(requireContext()).studentDb, companyId)
        viewModel = ViewModelProvider(this, factory).get(StudentViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.setOnClickListener { navigateToAddStudent() }
        setupObservers()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.mainRecycler.adapter = studentAdapter
    }

    private fun setupObservers() {
        viewModel.students.observe(viewLifecycleOwner) {
            // submit the list to the adapter
            studentAdapter.submitList(it)
        }

        // another observer to control ui visibility
        viewModel.students.observe(viewLifecycleOwner) {
            binding.mainRecycler.isVisible = !it.isNullOrEmpty()
            binding.emptyListContainer.isVisible = it.isNullOrEmpty()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_student_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_student -> {
                // navigate to add fragment
                navigateToAddStudent()
                return true
            }
        }

        return false

    }

    private fun navigateToAddStudent() {
        val action = StudentFragmentDirections.actionStudentFragmentToAddFragment(
            addCompany = false,
            companyId = companyId
        )
        findNavController().navigate(action)
    }
}