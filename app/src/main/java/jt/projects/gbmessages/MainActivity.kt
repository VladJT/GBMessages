package jt.projects.gbmessages

import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import jt.projects.gbmessages.databinding.ActivityMainBinding
import jt.projects.gbmessages.databinding.CustomToastBinding


class MainActivity : AppCompatActivity() {

    var counter = 0

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingToastBinding: CustomToastBinding

    fun <T> setInfo(message: T) {
        binding.textViewInfo.text = message.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initButtonToast()
        initSnackBar()
        initDialog()
    }

    private fun initButtonToast() {
        bindingToastBinding = CustomToastBinding.inflate(layoutInflater)
        bindingToastBinding.customToastText.text = "Hello, custom toast!"
        val t = Toast(this).apply {
            setGravity(Gravity.CENTER_HORIZONTAL, 0, 80);
            setDuration(Toast.LENGTH_SHORT);
            setView(bindingToastBinding.root);
        }

        binding.buttonToast.setOnClickListener {
            t.show()
        }
    }

    private fun initSnackBar() {
        binding.buttonSnackbar.setOnClickListener {
            Snackbar.make(binding.root, "My Snackbar", Snackbar.LENGTH_INDEFINITE)
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                .setAction("Change text") {
                    setInfo(counter++)
                }
                .show()
        }
    }

    private fun initDialog() {
        binding.buttonDialog.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Внимание!")
                .setMessage("Some text...")
                .setPositiveButton("Ok") { dialog, button ->
                    setInfo("Ok pressed")
                }
                .setNeutralButton("NeutralButton") { dialog, _ ->
                    setInfo("NeutralButton pressed")
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    setInfo("Cancel pressed")
                }
                .show()

        }
    }


}