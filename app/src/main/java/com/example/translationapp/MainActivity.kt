package com.example.translationapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

class MainActivity : AppCompatActivity() {

    private lateinit var translator: Translator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputEditText = findViewById<EditText>(R.id.englishEditText)
        val translateBtn = findViewById<Button>(R.id.translateButton)
        val outputTextview = findViewById<TextView>(R.id.hindiTextView)

        // Create an English-German translator:
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.HINDI)
            .build()
        translator = Translation.getClient(options)

        var conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                // Model downloaded successfully. Okay to start translating.
                // (Set a flag, unhide the translation UI, etc.)
                translateBtn.setOnClickListener{
                    val textToTranslate = inputEditText.text.toString()
                    translateText(textToTranslate, outputTextview)
                }
            }
            .addOnFailureListener { exception ->
                // Model couldn’t be downloaded or other internal error.
                // ...
                outputTextview.text = "Download Failed"
            }

//        override fun onDestroy() {
//            super.onDestroy()
//            translator.close()
//        }

    }

    private fun translateText(inputText: String, outputTextView: TextView){
        translator.translate(inputText)
            .addOnSuccessListener { translatedText ->
                // Translation successful.
                outputTextView.text = translatedText
            }
            .addOnFailureListener { exception ->
                // Error.
                outputTextView.text = "Translation Failed"
                // ...
            }
    }
}