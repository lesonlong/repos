package com.lesonlong.assessment.repository

import android.app.Application
import android.content.res.AssetManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import java.io.BufferedReader
import java.io.InputStream

@RunWith(JUnit4::class)
class MovieRepositoryTest {
    private lateinit var repository: MovieRepository

    private val application = mock(Application::class.java)
    private val assetManager = mock(AssetManager::class.java)
    private val inputStream = mock(InputStream::class.java)
    private val bufferedReader = mock(BufferedReader::class.java)


    @Before
    fun setUp() {
        repository = MovieRepositoryImpl(application)
    }

    @Test
    fun `should have movie when get from repository`() {
        /*Mockito.`when`(application.assets).thenReturn(assetManager)
        Mockito.`when`(assetManager.open(anyString())).thenReturn(inputStream)
        Mockito.`when`(inputStream.bufferedReader(Charsets.UTF_8)).thenReturn(bufferedReader)
        Mockito.`when`(bufferedReader.readText())
            .thenReturn(response)
        runBlocking {
            val movie = repository.getMovie()
            assertNotNull(movie)
        }*/
    }

    private val response = "{\n" +
            "  \"title\": \"Civil War\",\n" +
            "  \"image\": [\n" +
            "    \"http://movie.phinf.naver.net/20151127_272/1448585271749MCMVs_JPEG/movie_image.jpg\",\n" +
            "    \"http://movie.phinf.naver.net/20151127_84/1448585272016tiBsF_JPEG/movie_image.jpg\",\n" +
            "    \"http://movie.phinf.naver.net/20151125_36/1448434523214fPmj0_JPEG/movie_image.jpg\"\n" +
            "  ]\n" +
            "}"
}
