package com.example.cataaloguemovie.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;

import com.example.cataaloguemovie.R;
import com.example.cataaloguemovie.adapter.RecyclerMoviesAdapter;
import com.example.cataaloguemovie.model.Response;
import com.example.cataaloguemovie.model.Result;
import com.example.cataaloguemovie.rest.ApiClient;
import com.example.cataaloguemovie.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    private RecyclerMoviesAdapter adapter;
    private SearchView searchView;
    String API_KEY = "2b156f09b9b28e5d3a4fe9a9ba65bf4b";
    String LANGUAGE = "en-US";
    String CATEGORY = "popular";
    int PAGE = 1;
    RecyclerView recyclerView;
    private SearchView SearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rvMovie);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        panggilRetrofit();
    }

    private void panggilRetrofit() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Response> call = apiInterface.getMovie(CATEGORY,API_KEY,LANGUAGE,PAGE);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                List<Result> mList = response.body().getResults();
                adapter = new RecyclerMoviesAdapter(MainActivity.this,mList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                t.fillInStackTrace();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()>1){
                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<Response> call = apiInterface.getQuery(API_KEY,LANGUAGE,newText,PAGE);
                    call.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            List<Result> mList = response.body().getResults();
                            adapter = new RecyclerMoviesAdapter(MainActivity.this,mList);
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {
                            t.fillInStackTrace();
                        }
                    });
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
