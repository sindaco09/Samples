package com.indaco.news.ui.screens

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.HorizontalScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.indaco.news.R
import com.indaco.news.core.Injector
import com.indaco.news.data.models.News
import com.indaco.news.databinding.FragmentNewsBinding
import com.indaco.samples.core.hilt.viewmodel.ViewModelFactory
import com.indaco.samples.util.common.Ext.toPx
import com.indaco.samples.util.viewBinding
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class NewsFragment: Fragment(R.layout.fragment_news) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: NewsViewModel
    private val binding by viewBinding(FragmentNewsBinding::bind)

    @Volatile var breakingNews: Queue<News.BreakingNews> = LinkedList()
    private var breakingNewsAnimator: ObjectAnimator? = null
    private var marqueeRunning = AtomicBoolean(false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Injector.from(requireContext()).inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory)[NewsViewModel::class.java]

        binding.constraintRoot.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        init()
    }

    private fun init() {
        initUI()

        observeData()

        fetchData()
    }

    private fun fetchData() {
        viewModel.getLocalNews()

        viewModel.getBreakingNews()
    }

    private fun observeData() {
        viewModel.localNewsLiveStream.observe(viewLifecycleOwner) {
            if (it.isSuccess)
                setNews(it.result!!)
            else
                Toast.makeText(requireContext(), "Error getting news...",Toast.LENGTH_SHORT).show()
        }

        viewModel.breakingNewsLiveStream.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                result.result?.forEach(breakingNews::add)
                if (marqueeRunning.compareAndSet(false, true))
                    continueBreakingNews(breakingNews.poll())
            }
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

    private fun initUI() {
        setResetButton()
        with(binding) {
            reset.setOnClickListener(setResetButton())

            stop.setOnClickListener { viewModel.stopBreakingNews() }

            resume.setOnClickListener { viewModel.getBreakingNews() }
        }
    }

    private fun setResetButton() = View.OnClickListener {
        binding.newsRv.adapter?.let { (it as NewsAdapter).clearNews() }
    }

    private fun continueBreakingNews(news: News.BreakingNews?) {
        lifecycleScope.launchWhenStarted {
            if (news != null) {
                binding.breakingBody.text = news.title
                binding.guideTop.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    startScrollAnimation(binding.breakingBody, binding.breakingSv)
                    guidePercent = OPEN
                }
            } else {
                marqueeRunning.set(false)
                hideBreakingNews()
            }
        }
    }

    //TODO: binding can be null/leaking if fragment finishes before
    private fun hideBreakingNews() {
        binding.breakingBody.text = ""
        binding.guideTop.updateLayoutParams<ConstraintLayout.LayoutParams> { guidePercent = CLOSED }
    }

    private fun startScrollAnimation(scrollTv: TextView, hsv: HorizontalScrollView) {
        hsv.scrollTo(0, 0)
        val distance = getStringWidth(scrollTv, hsv)
        val duration = getDuration(distance) + 2000

        breakingNewsAnimator = ObjectAnimator.ofInt(hsv, "scrollX", distance)

        with(breakingNewsAnimator!!) {
            this.duration = duration
            interpolator = LinearInterpolator()
            doOnEnd { finishAnimationTasks() }
            startDelay = 200
            start()
        }
    }

    override fun onPause() {
        super.onPause()
        breakingNewsAnimator?.cancel()
    }

    private fun finishAnimationTasks() {
        continueBreakingNews(breakingNews.poll())
    }

    private fun getDuration(stringWidth: Int): Long {
        return (stringWidth / PX_S_CONSTANT * 1000).toLong()
    }

    private fun getStringWidth(tv: TextView, hsv: HorizontalScrollView): Int {
        val bounds = Rect()
        tv.paint.getTextBounds(tv.text.toString(), 0, tv.length(), bounds)

        val width: Int = bounds.width()
        val svWidth = hsv.width

        val adjustedScreenWidth = svWidth - (tv.paddingEnd.toPx() + tv.paddingStart.toPx() + hsv.paddingStart.toPx() + hsv.paddingEnd.toPx())

        // need to account for padding of string
        val difference = width - (adjustedScreenWidth)
        return if (difference >= 0) difference else 0
    }

    companion object {
        private const val PX_S_CONSTANT = 240 // 120px/s is good
        private const val CLOSED = 0f
        private const val OPEN = 0.075f
    }
}