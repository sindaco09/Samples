package com.example.newandroidxcomponentsdemo.util.news

import com.example.newandroidxcomponentsdemo.App
import com.example.newandroidxcomponentsdemo.R
import com.example.newandroidxcomponentsdemo.data.models.news.News

object MockNewsData {

    object BreakingNews {
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

        fun getBreakingNewsReelItem(): News.BreakingNews {
            val news = News.BreakingNews(breakingNewsReel[breakingNewsReelItemIndex])
            incrementIndex()
            return news
        }

        fun getBreakingNewsReelList(size: Int = 1): List<News.BreakingNews> {
            return List(size) { getBreakingNewsReelItem() }
        }

        private fun incrementIndex() {
            breakingNewsReelItemIndex++
            if (breakingNewsReelItemIndex == breakingNewsReel.lastIndex)
                breakingNewsReelItemIndex = 0
        }
    }

    object AlertNews {
        private var alertNewsReelItemIndex = 0
        private val alertNewsReel = listOf(
            "Pigeons have been amassing in Brooklyn for an inevitable attack",
            "Airplane toilet paper providers sued over useless toilet paper",
            "Marooned sea men are landing and starting society on Garbage Island",
            "Talking Parrots apparently lying about owners whereabouts",
            "Athiest community beaten by Southern Baptists with bibles",
            "Sharks evolve to point of growing mini-legs to go on land",
            "2nd grader devastated upon learning cursive isn't used anymore",
            "Children realize lifeguard is all talk, adult swim is ignored",
            "Man dies of radiation poisoning after exposure to radiation for super powers",
            "The priest, a cop, and a lawyer meet at a bar for a drink"
        )

        fun getAlertNewsReelItem(): News.AlertNews {
            val news = News.AlertNews(alertNewsReel[alertNewsReelItemIndex])
            incrementIndex()
            return news
        }

        fun getAlertNewsReelList(size: Int = 2): List<News.AlertNews> {
            return List(size) { getAlertNewsReelItem() }
        }

        private fun incrementIndex() {
            alertNewsReelItemIndex++
            if (alertNewsReelItemIndex == alertNewsReel.lastIndex)
                alertNewsReelItemIndex = 0
        }
    }

    object LocalNews {
        private var localNewsReelItemIndex = 0
        private val localNewsReel = listOf(
            News.LocalNews(
                App.Instance.getString(R.string.news_lasertag_title),
                App.Instance.getString(R.string.news_lasertag_body)
                ),
            News.LocalNews(
                App.Instance.getString(R.string.news_disney_fandom_title),
                App.Instance.getString(R.string.news_disney_fandom_body)
            ),
            News.LocalNews(
                App.Instance.getString(R.string.news_parents_disbelief_title),
                App.Instance.getString(R.string.news_parents_disbelief_body)
            ),
            News.LocalNews(
                App.Instance.getString(R.string.news_biden_childcare_title),
                App.Instance.getString(R.string.news_biden_childcare_body)
            ),
            News.LocalNews(
                App.Instance.getString(R.string.news_woman_disbelief_title),
                App.Instance.getString(R.string.news_woman_disbelief_body)
            ),
            News.LocalNews(
                App.Instance.getString(R.string.news_missing_tiger_title),
                App.Instance.getString(R.string.news_missing_tiger_body)
            )
        )

        fun getLocalNewsReelItem(): News.LocalNews {
            val news = localNewsReel[localNewsReelItemIndex]
            incrementIndex()
            return news
        }

        fun getLocalNewsReelList(size: Int = 2): List<News.LocalNews> {
            return List(size) { getLocalNewsReelItem() }
        }

        private fun incrementIndex() {
            localNewsReelItemIndex++
            if (localNewsReelItemIndex == localNewsReel.lastIndex)
                localNewsReelItemIndex = 0
        }
    }
}