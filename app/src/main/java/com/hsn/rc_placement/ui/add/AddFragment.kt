package com.hsn.rc_placement.ui.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import com.hsn.rc_placement.R
import com.hsn.rc_placement.database.AppDatabase
import com.hsn.rc_placement.databinding.FragmentAddBinding
import com.hsn.rc_placement.model.Company
import com.hsn.rc_placement.model.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "AddFragment"

class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding

    // indicates what to add (student or company)
    private var addCompany: Boolean = false
    private var companyId: Long = -1

    private lateinit var appDatabase: AppDatabase
    private val ioScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(R.integer.anim_med).toLong()
        }
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.anim_med).toLong()
        }
        addCompany = AddFragmentArgs.fromBundle(requireArguments()).addCompany
        companyId = AddFragmentArgs.fromBundle(requireArguments()).companyId
        Log.i(TAG, "$companyId $addCompany")
        appDatabase = AppDatabase.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        binding.run {
            // set the layout accordingly to the argument
            if (addCompany) {
                addInputLayout.hint = getString(R.string.company_name)
                (requireActivity() as AppCompatActivity).supportActionBar?.title =
                    getString(R.string.add_comapany)
            } else {
                addInputLayout.hint = getString(R.string.student_name)
                (requireActivity() as AppCompatActivity).supportActionBar?.title =
                    getString(R.string.add_student)
            }
            executePendingBindings()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (addCompany) {
            binding.saveButton.setOnClickListener { saveCompany() }
        } else {
            binding.saveButton.setOnClickListener { saveStudent() }
        }
    }

    private fun saveStudent() {
        val name = binding.addInputEdit.text.toString()

        if (name.isNotBlank()) {
            // save student to the database
            val student = Student(studentName = name, companyId = companyId)
            ioScope.launch {
                appDatabase.studentDb.insert(student)

                Snackbar.make(
                    requireView(),
                    getString(R.string.student_saved),
                    Snackbar.LENGTH_SHORT
                )
                    .setAnimationMode(Snackbar.ANIMATION_MODE_FADE)
                    .show()
            }

            // when saving student also increment number of students hired
            ioScope.launch {
                val company = appDatabase.companyDb.getCompanyById(companyId)
                if (company != null) {
                    company.studentsHired += 1
                    appDatabase.companyDb.update(company)
                }
            }
        }
    }

    private fun saveCompany() {
        val name = binding.addInputEdit.text.toString()
        if (name.isNotBlank()) {
            // save company to the database
            val company = Company(companyName = name)
            ioScope.launch {
                appDatabase.companyDb.insert(company)

                Snackbar.make(
                    requireView(),
                    getString(R.string.company_saved),
                    Snackbar.LENGTH_SHORT
                )
                    .setAnimationMode(Snackbar.ANIMATION_MODE_FADE)
                    .show()
            }
        }
    }

}