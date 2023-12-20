package com.example.storyapp2.ui.login


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storyapp2.data.utils.ViewModelFactory
import com.example.storyapp2.ui.main.MainActivity
import com.example.storyapp2.databinding.ActivityLoginBinding
import com.example.storyapp2.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupViewModel()
        setupButtonAction()
        playAnimation()
    }

    private fun setupButtonAction() {

        binding.textviewRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        binding.btnLogin.setOnClickListener {

            val email = binding.emailLogin.text.toString()
            val password = binding.passwordLogin.text.toString()

            if ( email.isNullOrEmpty()) {
                showLoading(false)
                binding.inputEmail.error = "Email harus diisi"
            } else if ( password.isNullOrEmpty()) {
                showLoading(false)
                binding.inputPassword.error = "password harus diisi"
            } else {
                if (binding.inputEmail.error == null && binding.inputPassword.error == null) {
                    loginViewModel.login(
                        binding.emailLogin.text.toString(),
                        binding.passwordLogin.text.toString()
                    )
                }
            }

        }
    }

    private fun setupViewModel() {
        loginViewModel.isLoading.observe(this){
            showLoading(it)
        }
        loginViewModel.loginResponse.observe(this){
            if (it.error){
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            } else{
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


    private fun showLoading(b: Boolean){
        if (b) {
            binding.progressBarLogin.visibility = View.VISIBLE
        } else {
            binding.progressBarLogin.visibility = View.GONE
        }}

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView2, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val desc = ObjectAnimator.ofFloat(binding.textView3, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.inputEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.inputPassword, View.ALPHA, 1f).setDuration(500)
        val button = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val text = ObjectAnimator.ofFloat(binding.linearLayoutLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                desc, email, password, button, text
            )
            start()
        }

    }
}