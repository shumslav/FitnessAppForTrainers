import android.content.Context
import android.widget.Toast
import java.util.*

fun makeToast(context: Context, text:String){
    Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
}