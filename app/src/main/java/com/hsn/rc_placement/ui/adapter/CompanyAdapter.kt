package com.hsn.rc_placement.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hsn.rc_placement.R
import com.hsn.rc_placement.model.Company

class CompanyAdapter(val clickInterface: CompanyAdapterInterface) :
    ListAdapter<Company, CompanyAdapter.CompanyViewHolder>(CompanyDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_data_item, parent, false)
        return CompanyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CompanyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nameText = view.findViewById<TextView>(R.id.name)
        val numberText = view.findViewById<TextView>(R.id.number)

        fun bind(item: Company) {
            nameText.text = item.companyName
            numberText.text = item.studentsHired.toString()
            view.setOnClickListener { clickInterface.onClickCompany(view, item) }
            view.transitionName = "Company_${item.companyId}"
        }

    }

}

class CompanyDiffUtil : DiffUtil.ItemCallback<Company>() {
    override fun areItemsTheSame(oldItem: Company, newItem: Company): Boolean {
        return oldItem.companyId == newItem.companyId
    }

    override fun areContentsTheSame(oldItem: Company, newItem: Company): Boolean {
        return oldItem == newItem
    }

}

interface CompanyAdapterInterface {
    fun onClickCompany(view: View, item: Company)
}