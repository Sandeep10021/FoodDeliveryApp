package com.csre.hungerrr;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.andremion.counterfab.CounterFab;
import com.csre.hungerrr.Common.Common;
import com.csre.hungerrr.Database.Database;
import com.csre.hungerrr.Interface.ItemClickListener;
import com.csre.hungerrr.Model.Category;
import com.csre.hungerrr.ViewHolders.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

import static com.csre.hungerrr.Common.Common.CLIENT;
import static com.csre.hungerrr.Common.Common.USER_NAME;
import static com.csre.hungerrr.Common.Common.USER_PASSWORD;
import static com.csre.hungerrr.Common.Common.USER_PHONE;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference category;
    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;
    TextView textViewName;
    RecyclerView recyclerView_menu;
    private boolean isSinglePressed;
    SwipeRefreshLayout swipeRefreshLayout;
    private CounterFab fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        Paper.init(this);

        //Set firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        category = firebaseDatabase.getReference("Category");

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
        fab.setCount(new Database(this).getOrderCount());

        swipeRefreshLayout = findViewById(R.id.swipeHome);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set name on navigation_view header
        View headerView = navigationView.getHeaderView(0);
        textViewName = headerView.findViewById(R.id.textviewName);
        textViewName.setText(Common.currentUser.getName());

        //Load Menu
        recyclerView_menu = findViewById(R.id.recycler_menu);
//        recyclerView_menu.setHasFixedSize(true);
//        recyclerView_menu.setLayoutManager(new LinearLayoutManager(this, VERTICAL, false));
        recyclerView_menu.setLayoutManager(new GridLayoutManager(this, 2));
        loadMenu();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.stopListening();
                loadMenu();
                adapter.startListening();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.setCount(new Database(this).getOrderCount());

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    //Helper Method
    private void loadMenu()
    {
        ProgressBar progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(category, Category.class).build();
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
                return new MenuViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull final Category model)
            {
                TextView textViewMenuName = holder.itemView.findViewById(R.id.menu_name);
                ImageView imageViewMenuImage = holder.itemView.findViewById(R.id.menu_image);

                textViewMenuName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(imageViewMenuImage);
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        //We need to send CategoryId to the next sub-activity
                        Intent intentFood = new Intent(HomeActivity.this, FoodActivity.class);
                        intentFood.putExtra("categoryId", adapter.getRef(position).getKey());
                        startActivity(intentFood);
                    }
                });
            }
        };

        recyclerView_menu.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        if(isSinglePressed)
        {
            super.onBackPressed();
        }
        else
        {
            isSinglePressed = true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isSinglePressed = false;
                }
            },2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_restaurant) {
            // Handle the camera action
        } else if (id == R.id.nav_cart) {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_orders) {
            Intent intent = new Intent(HomeActivity.this, OrderStatusActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_sign_out) {
            //clearing remember me data in PaperDb
            Paper.book(CLIENT).delete(USER_PHONE);
            Paper.book(CLIENT).delete(USER_PASSWORD);
            Paper.book(CLIENT).delete(USER_NAME);

            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
