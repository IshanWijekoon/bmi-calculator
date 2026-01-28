package com.example.bmi_calculator

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val weightText = findViewById<EditText>(R.id.etWeight)
        val heightText = findViewById<EditText>(R.id.etHeight)
        val calcButton = findViewById<Button>(R.id.btnCalculate)

        calcButton.setOnClickListener {
            val weightInput = weightText.text.toString().trim()
            val heightInput = heightText.text.toString().trim()
            val weight = weightInput.toFloatOrNull()
            val heightCm = heightInput.toFloatOrNull()

            if (!validateInput(weight, heightCm)) return@setOnClickListener

            val weightValue = weight!!
            val heightCmValue = heightCm!!
            val heightMeters = heightCmValue / 100f
            val bmi = weightValue / heightMeters.pow(2)
            val bmiRounded = String.format(Locale.getDefault(), "%.1f", bmi).toFloat()
            displayResult(bmiRounded, weightValue, heightCmValue)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun validateInput(weight: Float?, heightCm: Float?): Boolean {
        return when {
            weight == null || weight <= 0f -> {
                Toast.makeText(this, "Please enter a valid weight", Toast.LENGTH_LONG).show()
                false
            }
            heightCm == null || heightCm <= 0f -> {
                Toast.makeText(this, "Please enter a valid height", Toast.LENGTH_LONG).show()
                false
            }
            else -> true
        }
    }

    private fun displayResult(bmi: Float, weight: Float, heightCm: Float) {
        val resultIndex = findViewById<TextView>(R.id.tvIndex)
        val resultDescription = findViewById<TextView>(R.id.tvResult)
        val info = findViewById<TextView>(R.id.tvInfo)
        val formula = findViewById<TextView>(R.id.tvFormula)

        resultIndex.visibility = View.VISIBLE
        resultDescription.visibility = View.VISIBLE
        info.visibility = View.VISIBLE
        formula.visibility = View.VISIBLE
        resultIndex.text = String.format(Locale.getDefault(), "%.1f", bmi)

        val (resultText, colorRes) = when {
            bmi < 18.50f -> "Underweight" to R.color.under_weight
            bmi < 25.00f -> "Healthy" to R.color.normal
            bmi < 30.00f -> "Overweight" to R.color.over_weight
            else -> "Obese" to R.color.obese
        }

        val categoryColor = ContextCompat.getColor(this, colorRes)
        resultDescription.text = resultText
        resultDescription.background = ContextCompat.getDrawable(this, R.drawable.bg_result_chip)
        resultDescription.setTextColor(ContextCompat.getColor(this, R.color.colorOnPrimary))
        ViewCompat.setBackgroundTintList(resultDescription, ColorStateList.valueOf(categoryColor))

        info.text = getString(R.string.bmi_info_template, bmi, resultText)
        val heightMeters = heightCm / 100f
        formula.text = getString(R.string.bmi_formula_template, weight, heightMeters, bmi)
    }
}