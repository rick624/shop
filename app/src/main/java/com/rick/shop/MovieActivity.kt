package com.rick.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rick.shop.databinding.ActivityMovieBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import java.net.URL

class MovieActivity : AppCompatActivity() {
    private val TAG = MovieActivity::class.java.simpleName
    private lateinit var binding: ActivityMovieBinding
//    private lateinit var bindingRow: RowMovieBinding
    var movies: List<Movie2Item>? = null
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.jsonserve.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBinding.inflate(layoutInflater)
//        bindingRow = RowMovieBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_movie)
        setContentView(binding.root)
        movieTask()

    }

    private fun movieTask() = GlobalScope.launch(Dispatchers.Main) {
//        val movie : List<MovieItem>
        withContext(Dispatchers.IO) {
//            val json = URL("https://api.jsonserve.com/DvK1w4").readText()
//            movies = Gson().fromJson<List<Movie2Item>>(json,
//                object : TypeToken<List<Movie2Item>>(){}.type)
//            movie = movies
            val movieService = retrofit.create(MovieService::class.java)
            movies = movieService.listMovies()
                .execute()
                .body()
            movies?.forEach {
                Log.d(TAG, "movies: ${it.Title} ${it.imdbRating}")
            }
        }.run {
            binding.recycler.layoutManager = LinearLayoutManager(this@MovieActivity)
            binding.recycler.setHasFixedSize(true)
            binding.recycler.adapter = MovieAdapter()
        }
    }

    inner class MovieAdapter() : RecyclerView.Adapter<MovieHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_movie, parent, false)
            return MovieHolder(view)
        }

        override fun getItemCount(): Int {
            val size = movies?.size?: 0
            return size
        }

        override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            val movie = movies?.get(position)
            holder.bindMovie(movie!!)
        }

    }

    inner class MovieHolder(view: View) : RecyclerView.ViewHolder(view){
        var titleText: TextView = view.findViewById(R.id.movie_title)
        var imdbText: TextView = view.findViewById(R.id.movie_imdb)
        var directorText: TextView = view.findViewById(R.id.movie_director)
        var posterImage: ImageView = view.findViewById(R.id.movie_poster)
/*
        val titleText: TextView = bindingRow.movieTitle
        val imdbText: TextView = bindingRow.movieImdb
        val directorText: TextView = bindingRow.movieDirector
        經測試無法將json中的文字放入row_movie.xml，網路medium網頁有相關解法
*/
        fun bindMovie(movie: Movie2Item) {
            titleText.text = movie.Title
            imdbText.text = movie.imdbRating.toString()
            directorText.text = movie.Director
            Glide.with(this@MovieActivity)
                .load(movie.Poster)
                .override(300)
                .into(posterImage)
        }
    }
}

class Movie : ArrayList<MovieItem>()

data class MovieItem(
    val Actors: String,
    val Awards: String,
    val ComingSoon: Boolean,
    val Country: String,
    val Director: String,
    val Genre: String,
    val Images: List<String>,
    val Language: String,
    val Metascore: String,
    val Plot: String,
    val Poster: String,
    val Rated: String,
    val Released: String,
    val Response: String,
    val Runtime: String,
    val Title: String,
    val Type: String,
    val Writer: String,
    val Year: String,
    val imdbID: String,
    val imdbRating: String,
    val imdbVotes: String,
    val totalSeasons: String
)

class Movie2 : ArrayList<Movie2Item>()

data class Movie2Item(
    val Actors: String,
    val Awards: String,
    val BoxOffice: String,
    val Country: String,
    val DVD: String,
    val Director: String,
    val Genre: String,
    val Language: String,
    val Metascore: String,
    val Plot: String,
    val Poster: String,
    val Production: String,
    val Rated: String,
    val Ratings: List<Rating>,
    val Released: String,
    val Response: String,
    val Runtime: String,
    val Title: String,
    val Type: String,
    val Website: String,
    val Writer: String,
    val Year: String,
    val imdbID: String,
    val imdbRating: String,
    val imdbVotes: String
)

data class Rating(
    val Source: String,
    val Value: String
)

interface MovieService {
    @GET("DvK1w4")
    fun listMovies(): Call<List<Movie2Item>>
}