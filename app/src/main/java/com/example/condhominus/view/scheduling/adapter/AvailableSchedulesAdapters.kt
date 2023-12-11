package com.example.condhominus.view.scheduling.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.condhominus.R
import com.example.condhominus.model.AvailableSchedulesResponse
import com.example.condhominus.model.Schedule
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class AvailableSchedulesAdapters(var context: Context, private var availableSchedules: AvailableSchedulesResponse) : BaseAdapter() {

    override fun getCount(): Int {
        return availableSchedules.agendas.size
    }

    override fun getItem(position: Int): Any {
        return availableSchedules.agendas[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        val view:View = View.inflate(context, R.layout.view_card_item_list, null)

        var dateView: TextView = view.findViewById(R.id.availableDateView)
        var periodMorningView: TextView = view.findViewById(R.id.periodMorningView)
        var periodAfternoonView: TextView = view.findViewById(R.id.periodAfternoonView)
        var slashPeriodView: TextView = view.findViewById(R.id.slashMorningView)

        var items: Schedule = availableSchedules.agendas[position]

        dateView.text = formatForPTBrDate(items.data)

        if(items.periodos.periodoManha != null && items.periodos.periodoManha == 1){
            periodMorningView.text = "Manhã"
        }

        if(items.periodos.periodoTarde != null && items.periodos.periodoTarde == 2){
            periodAfternoonView.text = "Tarde"
        }

        return  view!!
    }

    private fun formatForPTBrDate(date: String): String {
        var offsetDateTime = OffsetDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"))
        var localDate = offsetDateTime.toLocalDate()
        return localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }
}