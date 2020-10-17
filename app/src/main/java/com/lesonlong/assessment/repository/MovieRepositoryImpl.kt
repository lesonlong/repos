package com.lesonlong.assessment.repository

import android.app.Application
import com.lesonlong.assessment.vo.Movie
import kotlinx.serialization.json.Json

class MovieRepositoryImpl(private val app: Application) : MovieRepository {

    override fun getMovie(): Movie {
        val response =
            app.assets.open("response.json").bufferedReader().use { it.readText() }
        return Json.decodeFromString(Movie.serializer(), response)
    }
}