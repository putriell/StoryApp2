package com.example.storyapp2.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp2.R
import com.example.storyapp2.data.utils.ViewModelFactory
import com.example.storyapp2.databinding.ActivityMainBinding
import com.example.storyapp2.ui.adapter.StoryAdapter
import com.example.storyapp2.ui.addStory.AddStoryActivity
import com.example.storyapp2.ui.welcome.WelcomeActivity
import com.example.storyapp2.ui.Maps.MapsActivity
import com.example.storyapp2.ui.adapter.LoadingStateAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]



        setupViewModel()
        setupAction()
        fetchSession()
        menu()

    }


    private fun fetchSession() {
        mainViewModel.getUser().observe(this) { user ->
            val token = user.token
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
            storyAdapter = StoryAdapter()
            binding.recyclerview.adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter{
                    storyAdapter.retry()
                }
            )
            mainViewModel.listStory(token).observe(this) {
                storyAdapter.submitData(lifecycle, it)
            }
            isLoading(false)
        }
    }


    private fun setupAction() {
        binding.btnAddStory.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupViewModel() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerview.layoutManager = layoutManager
    }

    private fun isLoading(b: Boolean) {
        if (b) {
            binding.progressBarMain.visibility = View.VISIBLE
        } else {
            binding.progressBarMain.visibility = View.GONE
        }
    }

    private fun menu() {
        binding.topAppBar.setOnMenuItemClickListener{ item ->

        when (item.itemId) {
            R.id.action_logout -> {
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.logout_dialog))
                    setMessage(getString(R.string.logout_message))
                    setPositiveButton(getString(R.string.yes)) { _, _ ->
                        mainViewModel.logout()
                    }
                    setNegativeButton(getString(R.string.no)) { _, _ ->

                    }
                    create()
                    show()
                }
                true
            }
            R.id.action_location -> {
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(intent)
                true

            }
            else -> false
        }
        }

    }

}
