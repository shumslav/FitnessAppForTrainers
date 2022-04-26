import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.fitnessapp.models.CalendarDay
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun makeToast(context: Context, text:String){
    Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
}

fun getDates(fromDate:String,toDate:String):MutableList<CalendarDay>{
    val dates = mutableListOf<CalendarDay>()
    val df1 = SimpleDateFormat("dd.MM.yyyy", Locale.US)

    var date1:Date?
    var date2:Date?

    try {
        date1 = df1.parse(fromDate)
        date2 = df1.parse(toDate)
        val cal1 = Calendar.getInstance()
        cal1.time = date1!!


        val cal2 = Calendar.getInstance()
        cal2.time = date2!!

        while(!cal1.after(cal2))
        {
            val time = cal1.time
            val weekday = time.toString().split(" ")[0]
            val day = time.toString().split(" ")[2]
            val month = time.toString().split(" ")[1]
            val year = time.toString().split(" ").last()
            dates.add(CalendarDay(day,weekday,month, year))
            cal1.add(Calendar.DATE, 1)
        }
    } catch (e: ParseException) {
        Log.i("ERROR", "ERROR")
        e.printStackTrace();
    }
    Log.i("Count days", "${dates.size}")
    return dates
}