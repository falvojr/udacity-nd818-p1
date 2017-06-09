package com.falvojr.nd818.p1.infra;

import com.falvojr.nd818.p1.model.Config;
import com.falvojr.nd818.p1.model.Movie;
import com.falvojr.nd818.p1.model.MovieList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

/**
 * Service generator using Retrofit.
 * <p>
 * Created by falvojr on 6/4/17.
 *
 * @see <a href="https://goo.gl/Zff6lL">Creating a Sustainable Android Client</a>
 */
public class TMDbService {

    /**
     * TMDb Retrofit interface.
     * <p>
     * Created by falvojr on 6/4/17.
     */
    public interface Api {

        String BASE_URL = "https://api.themoviedb.org/3/";

        @GET("configuration")
        Observable<Config> getConfig(@Query("api_key") String apiKey);

        @GET("movie/popular")
        Observable<MovieList> getPopularMovies(@Query("api_key") String apiKey);

        @GET("movie/top_rated")
        Observable<MovieList> getTopRatedMovies(@Query("api_key") String apiKey);

        @GET("movie/{id}")
        Observable<Movie> getMovie(@Query("api_key") String apiKey, @Path("id") int movieId);
    }

    private Api mApi;

    /**
     * Get the TMDb {@link Api}.
     *
     * @return {@link Api} (concrete implementation).
     */
    public Api getApi() {
        if (mApi == null) {
            final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(BODY));
            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            mApi = new Retrofit.Builder()
                    .baseUrl(Api.BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(Api.class);
        }
        return mApi;
    }

    /**
     * Private constructor for Singleton pattern (Bill Pugh's).
     *
     * @see <a href="https://goo.gl/2RfcWb">Singleton Design Pattern Best Practices</a>
     */
    private TMDbService() {
        super();
    }

    private static class TMDbServiceHolder {
        static final TMDbService INSTANCE = new TMDbService();
    }

    public static TMDbService getInstance() {
        return TMDbServiceHolder.INSTANCE;
    }
}
