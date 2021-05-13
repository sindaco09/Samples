package com.example.newandroidxcomponentsdemo.ui.main.news

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.newandroidxcomponentsdemo.R
import com.example.newandroidxcomponentsdemo.data.models.news.News
import com.example.newandroidxcomponentsdemo.databinding.FragmentNewsBinding
import com.example.newandroidxcomponentsdemo.ui.base.DataBindingFragment
import com.indaco.myhomeapp.ui.main.news.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment: DataBindingFragment<FragmentNewsBinding>(R.layout.fragment_news) {

    private val viewModel: NewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel

        init()
    }

    private fun init() {
        initUI()

        observeData()

        fetchData()
    }

    private fun fetchData() {
        viewModel.getBreakingNews()
    }

    private fun observeData() {
        viewModel.newsLiveStream.observe(viewLifecycleOwner) {
            if (it.success)
                setNews(it.result!!)
            else
                Toast.makeText(requireContext(), "Error getting news...",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setNews(list: List<News>) {
        with(binding) {
            if (newsRv.adapter == null)
                newsRv.adapter = NewsAdapter(list.toMutableList())
            else {
                (newsRv.adapter as NewsAdapter).updateNews(list)
                newsRv.layoutManager?.scrollToPosition(0)
            }
        }
    }

    fun initUI() {
        setResetButton()
        with(binding) {
            reset.setOnClickListener(setResetButton())
        }
    }

    private fun setResetButton() = View.OnClickListener {
        binding.newsRv.adapter?.let { (it as NewsAdapter).clearNews() }
    }
}