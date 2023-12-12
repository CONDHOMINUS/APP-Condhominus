import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.condhominus.R
import com.example.condhominus.ext.visible
import com.example.condhominus.model.AvailableSchedulesResponse
import com.example.condhominus.model.Schedule
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class AvailableSchedulesAdapters(
    var context: Context,
    private var availableSchedules: AvailableSchedulesResponse
) : BaseAdapter() {

    private var onItemClickListener: OnItemClickListener? = null

    override fun getCount(): Int = availableSchedules.agendas.size

    override fun getItem(position: Int): Any = availableSchedules.agendas[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.view_card_item_list, null)

        var dateView: TextView = view.findViewById(R.id.availableDateView)
        var periodMorningView: TextView = view.findViewById(R.id.periodMorningView)
        var periodAfternoonView: TextView = view.findViewById(R.id.periodAfternoonView)

        var items: Schedule = availableSchedules.agendas[position]

        dateView.text = formatForPTBrDate(items.data)

        if (items.periodos.periodoManha != null && items.periodos.periodoManha == 1) {
            periodMorningView.apply {
                visible()
                text = "Manhã"
            }
        }

        if (items.periodos.periodoTarde != null && items.periodos.periodoTarde == 2) {
            periodAfternoonView.apply {
                visible()
                text = "Tarde"
            }
        }

        view.setOnClickListener {
            onItemClickListener?.onItemClick(position, items)
        }

        return view
    }

    private fun formatForPTBrDate(date: String): String {
        var offsetDateTime = OffsetDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"))
        var localDate = offsetDateTime.toLocalDate()
        return localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, schedule: Schedule)
    }
}