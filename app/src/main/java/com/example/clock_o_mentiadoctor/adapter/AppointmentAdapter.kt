package com.example.clock_o_mentiadoctor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clock_o_mentiadoctor.databinding.AppointmentListBinding
import com.example.clock_o_mentiadoctor.models.Appointment
import javax.inject.Inject

class AppointmentAdapter @Inject constructor(): RecyclerView.Adapter<AppointmentAdapter.MyViewHolder>() {

    private var listOfAppointment = ArrayList<Appointment>()
    private var onItemClick: OnItemClick? = null

    fun setListOfAppointments(listOfAppointment: ArrayList<Appointment>,onItemClick: OnItemClick) {
        this.listOfAppointment.clear()
        this.listOfAppointment.addAll(listOfAppointment)
        this.onItemClick = onItemClick
        notifyDataSetChanged()
    }

    inner class MyViewHolder(var binding: AppointmentListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = AppointmentListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.appointmentData = listOfAppointment[position]
        holder.itemView.setOnClickListener {
            onItemClick?.itemClick(listOfAppointment[position])
        }
    }

    override fun getItemCount(): Int {
        return listOfAppointment.size
    }

    interface OnItemClick {
        fun itemClick(appointmentModel: Appointment?)
    }
}