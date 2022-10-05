package com.example.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pixabayapps.R
import com.example.pixabayapps.databinding.ActivityMainBinding
import retrofit2.http.Query

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel : MainViewModel by viewModels()
    private val adapter: PostAdapter by lazy {
        PostAdapter {

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeData()
    }

    private fun observeData() {
        viewModel.loadingState.observe(this){ isLoading ->
            binding.pbPost.isVisible = isLoading
            binding.rvPost.isVisible = !isLoading
        }
        viewModel.errorstate.observe(this) { errorData ->
            binding.tvError.isVisible = errorData.first
            errorData.second?.message?.let {
                binding.tvError.text = it
            }
        }
        viewModel.searhResult.observe(this) {
            if (it.posts.isNullOrEmpty()) {
                adapter.clearItems()
                binding.tvError.isVisible = true
                binding.tvError.text = "Data Pencarian Tidak Ditemukan"
            } else {
                adapter.setItems(it.posts)
            }
        }
    }

    private fun initList() {
        binding.rvPost.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search,menu)
        val search =menu.findItem(R.id.menu_search_bar)
        val searchView = search.actionView as SearchView
        searchView.queryHint = getString(R.string.title_search)
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchPost(it)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}