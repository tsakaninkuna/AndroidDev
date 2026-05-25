package com.example.tipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tipapp.ui.theme.TipAppTheme
import android.widget.TextView
import android.widget.EditText
import android.widget.SeekBar
import android.text.Editable
import android.text.TextWatcher

//declare your values here for logging and initial tip percent
private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
class MainActivity : ComponentActivity() {
    //declare your variables here
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTippercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView

    private lateinit var tvTipDescription: TextView

    //when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //initialize your variables here
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTippercentLabel = findViewById(R.id.tvTippercentlabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)

//set the initial tip percent
        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTippercentLabel.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)

//add a listener to the seekbar
        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i("TAG", "onProgressChanged $progress")
                tvTippercentLabel.text = "$progress%"
                //compute the tip and total as it will change based the scrolling of the seekbar/percent
                computeTipAndTotal()
                //update the tip description
                updateTipDescription(progress)
            }

            private fun updateTipDescription(tipPercent: Int) {
                val tipDescription = when (tipPercent) {
                    in 0..9 -> "Poor"
                    in 10..14 -> "Acceptable"
                    in 15..19 -> "Good"
                    in 20..24 -> "Great"
                    else -> "Amazing"
                }
                tvTipDescription.text = tipDescription
            }

            //implement the other methods
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        //listen for text changes in the EditText
        etBaseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
            }
        }

        )
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"

        }
        tvTipDescription.text = tipDescription

    }

    //increases the tip and the total
    //based on the tip percent and the base amount20
    private fun computeTipAndTotal() {
        //check if the base amount is empty and decimals
        if (etBaseAmount.text.isEmpty()) {
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            return
        }
        //1. get the value of the base and tip percent
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress

        //2. compute the tip and total
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount
        //3. update the UI
    //update the UI with 2 decimal places
        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)
    }
}