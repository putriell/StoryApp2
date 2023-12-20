package com.example.storyapp2.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storyapp2.data.utils.ViewModelFactory
import com.example.storyapp2.databinding.ActivityRegisterBinding
import com.example.storyapp2.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        setupButtonAction()
        setupViewModel()

    }

    private fun setupViewModel() {
        registerViewModel.isLoading.observe(this) {
            isLoading(it)
        }

        registerViewModel.registerResponse.observe(this) {
            if (it.error) {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Register success", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun setupButtonAction() {
        binding.btnRegister.setOnClickListener {

            if (binding.usernameRegister.text.isNullOrEmpty()) {
                isLoading(false)
                binding.inputUsername.error = "username harus diisi"
            } else if (binding.emailRegister.text.isNullOrEmpty()) {
                isLoading(false)
                binding.inputEmail.error = "Email harus diisi"
            } else if (binding.passwordRegister.text.isNullOrEmpty()){
                isLoading(false)
                binding.inputPassword.error = "Password harus diisi"
            } else {
                if (binding.inputUsername.error == null && binding.inputEmail.error == null && binding.inputPassword.error == null){
                    registerViewModel.register(
                        binding.usernameRegister.text.toString(),
                        binding.emailRegister.text.toString(),
                        binding.passwordRegister.text.toString()
                    )
                }
            }
        }
        binding.textviewLogin.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }




    private fun isLoading(b: Boolean) {
        if (b) {
            binding.progressBarRegister.visibility = View.VISIBLE
        } else {
            binding.progressBarRegister.visibility = View.GONE
        }
    }


    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView2, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val desc = ObjectAnimator.ofFloat(binding.textView3, View.ALPHA, 1f).setDuration(500)
        val username = ObjectAnimator.ofFloat(binding.inputUsername, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.inputEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.inputPassword, View.ALPHA, 1f).setDuration(500)
        val button = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val text = ObjectAnimator.ofFloat(binding.linearLayout, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                desc, username, email, password, button, text
            )
            start()
        }

    }
}