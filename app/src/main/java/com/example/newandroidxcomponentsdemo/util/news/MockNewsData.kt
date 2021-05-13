package com.indaco.myhomeapp.util.mock.news

import com.example.newandroidxcomponentsdemo.data.models.news.News

object MockNewsData {

    private var breakingNewsReelItemIndex = 0
    private val breakingNewsReel = listOf(
        "Bears attack have organized attacks across the Western Coast and are moving in!",
        "Turns out peanut allergies are early signs of spontaneous combustion!",
        "Leaving your electronics plugged in when you're on vacation doesn't do anything!",
        "Horoscopes challenged by Horrorscopes in epic showdown of the ages!",
        "Subway sandwiches are actually eaten by people... in subways!",
        "Turns out she DIDN'T sell seashells by the seashore. Breaking news on this shortly",
        "Bermuda Triangle officially announces it is rising to the 3rd dimension as a pyramid!",
        "Polar Icecaps not related to Polar Opposites. Aliens are scrambling for final attack!",
        "Staring into a solar eclipse will turn  you to stone, new study suggests!",
        "Chicken seen at edge of road, postulating the reason to cross and it's own existence",
        "Hungry Hungry Hippos sues over misrepresentation of Hippos appetites",
        "Cut Costs Culminating into the Breaking Forest Words"
    )

    fun getBreakingnewsReelItem(): News {
        val news = News(breakingNewsReel[breakingNewsReelItemIndex], News.Type.BREAKING)
        incrementIndex()
        return news
    }

    fun getBreakingNewsReelList(size: Int = 2): List<News> {
       return List(size) { getBreakingnewsReelItem() }
    }

    private fun incrementIndex() {
        breakingNewsReelItemIndex++
        if (breakingNewsReelItemIndex == breakingNewsReel.lastIndex)
            breakingNewsReelItemIndex = 0
    }
}