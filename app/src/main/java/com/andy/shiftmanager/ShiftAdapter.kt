package com.andy.shiftmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ShiftAdapter(private var shifts: List<Shift>, private val onItemClick: (Shift) -> Unit) : RecyclerView.Adapter<ShiftAdapter.ShiftViewHolder>() {

    class ShiftViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDataOra: TextView = view.findViewById(R.id.tv_DataOra)
        val tvOreLavorate: TextView = view.findViewById(R.id.tv_OreLavorate)
        val tvPaga: TextView = view.findViewById(R.id.tv_Paga)
        val tvNote: TextView = view.findViewById(R.id.tv_Note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShiftViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shift, parent, false)
        return ShiftViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShiftViewHolder, position: Int) {
        val shift = shifts[position]
        holder.tvDataOra.text = "Data: ${shift.dataOra}"
        holder.tvOreLavorate.text = "Ore: ${shift.oreLavorate}"
        holder.tvPaga.text = "Paga: ${shift.pagaOraria} €"
        holder.tvNote.text = "Note: ${shift.note}"

        // Click per modificare il turno
        holder.itemView.setOnClickListener {
            onItemClick(shift)
        }
    }

    fun updateShifts(newList: List<Shift>) {
        shifts = newList
        notifyDataSetChanged()
    }

    override fun getItemCount() = shifts.size
}