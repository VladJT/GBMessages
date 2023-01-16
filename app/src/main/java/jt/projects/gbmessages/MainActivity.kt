package jt.projects.gbmessages

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnPreDrawListener
import android.view.animation.AnticipateInterpolator
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.snackbar.Snackbar
import jt.projects.gbmessages.databinding.ActivityMainBinding
import jt.projects.gbmessages.databinding.CustomToastBinding


class MainActivity : AppCompatActivity(), IInformative {

    private var counter = 0

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingToastBinding: CustomToastBinding

    private fun <T> setInfo(message: T) {
        binding.textViewInfo.text = message.toString()
    }

    override fun showInfoText(message: String) {
        this.setInfo(message)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun setBlurEffect(isBlur: Boolean) {
        val blurEffect = RenderEffect.createBlurEffect(15f, 0f, Shader.TileMode.MIRROR)
        if(isBlur) {
            binding.root.setRenderEffect(blurEffect)
        }else{
            binding.root.setRenderEffect(null)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        showSplashScreen()

        initButtonToast()
        initSnackBar()
        initDialog()
        initFragmentDialog()
        initBottomSheetDialog()
        initNotification()
        initBanner()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun showSplashScreen() {
        var isHideSplashScreen = false
        object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d("@@@", "onTick: $millisUntilFinished")
            }

            override fun onFinish() {
                isHideSplashScreen = true
            }
        }.start()

        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val slideLeft = ObjectAnimator.ofFloat(
                splashScreenView,
                View.TRANSLATION_X,
                0f,
                -splashScreenView.height.toFloat()
            )
            slideLeft.duration = 500L
            slideLeft.doOnEnd { splashScreenView.remove() }
            slideLeft.start()
        }

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (isHideSplashScreen) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            })
    }

    /**
     * 1. Toast
     */
    private fun initButtonToast() {
        bindingToastBinding = CustomToastBinding.inflate(layoutInflater)
        bindingToastBinding.customToastText.text = "Hello, custom toast!"
        val t = Toast(this).apply {
            setGravity(Gravity.BOTTOM, 0, 80)
            duration = Toast.LENGTH_SHORT
            view = bindingToastBinding.root
        }

        binding.buttonToast.setOnClickListener {
            t.show()
        }
    }

    /**
     * 2. SnackBar
     */
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

    /**
     * 3. AlertDialog! ПРОБЛЕМА : исчезает при повороте экрана
     */
    private fun initDialog() {
        binding.buttonDialog.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Внимание!")
                .setMessage("Some text...")
                .setPositiveButton("Ok") { dialog, button ->
                    setInfo("Ok pressed")
                }
                .setNeutralButton("Neutral") { dialog, _ ->
                    setInfo("NeutralButton pressed")
                }
                .setNegativeButton("Negative") { dialog, _ ->
                    setInfo("Negative pressed")
                }
                .setCancelable(false)
                .show()

        }
    }


    /**
     * 4. FragmentDialog НЕ исчезает при повороте экрана!
     */
    @RequiresApi(Build.VERSION_CODES.S)
    private fun initFragmentDialog() {
        binding.buttonDialogFragment.setOnClickListener {
            setBlurEffect(true)
            MyFragmentDialog()
                .show(supportFragmentManager, MyFragmentDialog.TAG)
        }
    }

    /**
     * 4. BottomSheetDialog НЕ исчезает при повороте экрана!
     */
    private fun initBottomSheetDialog() {
        binding.buttonDialogBottomSheet.setOnClickListener {
            MyBottomSheetFragment()
                .show(supportFragmentManager, MyBottomSheetFragment.TAG)
        }
    }

    /**
     * 5. NOTIFICATIONS
     */
    // NOTIFICATIONS
    private val channelGroupId = "group_1"
    private val channelGroupName = "Каналы для GBMessages"
    private val channelId = "channel_1"
    private val channelName = "channel_high"
    private val messagePriority = NotificationCompat.PRIORITY_DEFAULT

    private val messageId = 444

    @SuppressLint("ResourceAsColor")
    private fun initNotification() {
        initNotificationChannels()

        binding.buttonNotification.setOnClickListener {
            val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle("Нотификация")
                .setContentText("Some text...")
                .setLights(R.color.purple_200, 3000, 1000)
                .setPriority(messagePriority)

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                NotificationManagerCompat.from(this).notify(messageId, builder.build())
            }
        }
    }

    // инициализация каналов нотификаций
    private fun initNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.run {
                createNotificationChannelGroup(
                    NotificationChannelGroup(
                        channelGroupId,
                        channelGroupName
                    )
                )

                createNotificationChannel(
                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                        .apply {
                            description = "Канал c IMPORTANCE_HIGH"
                            group = channelGroupId
                        })
            }
        }
    }


    /**
     * BANNER
     * работает только с !!!! LinearLayout
     */
    private fun initBanner() {
        binding.banner.apply {
            setLeftButtonListener { this.dismiss() }
            setRightButtonListener {
                Toast.makeText(this@MainActivity, "Wifi", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonBanner.setOnClickListener { binding.banner.show() }

    }


}