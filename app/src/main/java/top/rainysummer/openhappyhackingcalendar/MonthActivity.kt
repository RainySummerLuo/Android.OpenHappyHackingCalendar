package top.rainysummer.openhappyhackingcalendar

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.xhinliang.lunarcalendar.LunarCalendar
import java.util.*


class MonthActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_month)

        val calendar: Calendar = Calendar.getInstance()

        val yearNum = calendar.get(Calendar.YEAR)
        val monthNum = calendar.get(Calendar.MONTH)

        val monthName = arrayOf(
            "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November",
            "December"
        )
        val month = monthName[monthNum]
        val textView = findViewById<TextView>(R.id.textMonth)
        textView.text = month

        val startOfMonth = Calendar.getInstance()
        startOfMonth.set(yearNum, monthNum, 1)
        var weekIndex: Int = startOfMonth.get(Calendar.DAY_OF_WEEK) - 2
        if (weekIndex == -1) {
            weekIndex = 6
        }

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 0 until daysInMonth) {
            val lunarCalender = LunarCalendar.obtainCalendar(yearNum, monthNum + 1, i + 1)

            val index = weekIndex + i
            val wk = index % 7
            val it = index / 7

            val weekid: Int =
                resources.getIdentifier("week$it", "id", packageName)
            val weekrow = findViewById<TableRow>(weekid)
            val lpweek: LinearLayout.LayoutParams = weekrow.layoutParams as TableLayout.LayoutParams
            lpweek.topMargin = 50
            weekrow.layoutParams = lpweek
            weekrow.visibility = View.VISIBLE

            val chnweekid: Int =
                resources.getIdentifier("chnweek$it", "id", packageName)
            val chnweekrow = findViewById<TableRow>(chnweekid)
            val lpchnweek: LinearLayout.LayoutParams = chnweekrow.layoutParams as TableLayout.LayoutParams
            lpchnweek.topMargin = 10
            chnweekrow.layoutParams = lpchnweek
            chnweekrow.visibility = View.VISIBLE;

            val dayid: Int =
                resources.getIdentifier("day$it$wk", "id", packageName)
            val daycell = findViewById<TextView>(dayid)
            daycell.text = (i + 1).toString()
            daycell.width = 120
            daycell.setPadding(0, 0, 0, 0)

            val chnid: Int =
                resources.getIdentifier("chn$it$wk", "id", packageName)
            val chndaycell = findViewById<TextView>(chnid)
            val festivals = lunarCalender.festivals
            var festivalStr = ""
            for (festival in festivals.set) {
                festivalStr += festival
            }
            if (festivalStr != "") {
                chndaycell.text = festivalStr
                chndaycell.setTextColor(resources.getColor(android.R.color.holo_red_dark, theme))
            } else {
                chndaycell.text = lunarCalender.lunarDay
            }
            chndaycell.width = 120
            chndaycell.includeFontPadding = false
            chndaycell.setLines(3)
            chndaycell.setPadding(0, 0, 0, 0)
        }

        val detector = GestureDetector(this, object : SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                val intent = Intent()
                intent.setClass(this@MonthActivity, DayActivity::class.java)
                startActivity(intent)
                this@MonthActivity.finish()
                return true
            }

            override fun onDoubleTapEvent(e: MotionEvent): Boolean {
                return true
            }

            override fun onDown(e: MotionEvent): Boolean {
                return true
            }
        })

        val layout = findViewById<LinearLayout>(R.id.layout1)
        layout.setOnTouchListener(OnTouchListener { _, event -> detector.onTouchEvent(event) })
    }
}