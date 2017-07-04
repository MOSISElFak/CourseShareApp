package com.example.stefanzivic.courseshare;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stefanzivic.courseshare.activities.CreateLectureActivity;
import com.example.stefanzivic.courseshare.activities.EditProfileActivity;
import com.example.stefanzivic.courseshare.activities.LectureDetailsActivity;
import com.example.stefanzivic.courseshare.activities.UserDetailsActivity;
import com.example.stefanzivic.courseshare.adapters.FirebaseLectureAdapter;
import com.example.stefanzivic.courseshare.adapters.FirebaseUserAdapter;
import com.example.stefanzivic.courseshare.adapters.OnLectureClickListener;
import com.example.stefanzivic.courseshare.adapters.OnUserClickListener;
import com.example.stefanzivic.courseshare.adapters.RecyclerLectureAdapter;
import com.example.stefanzivic.courseshare.adapters.RecyclerUserAdapter;
import com.example.stefanzivic.courseshare.model.Lecture;
import com.example.stefanzivic.courseshare.model.User;
import com.example.stefanzivic.courseshare.services.LocationService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnLectureClickListener, OnUserClickListener {

    public static final String TYPE_EXTRA = "type";
    public static final String USER_ID_EXTRA = "userId";

    TextView textViewEmail;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  textViewEmail = (TextView)findViewById(R.id.textViewEmail);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //textViewEmail.setText(currentUser.getEmail().toString());

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //handle intents

        int type = getIntent().getIntExtra(TYPE_EXTRA, -1);
        String userId = getIntent().getStringExtra(USER_ID_EXTRA);
        if (type != -1) {
            decideWhatToShow(type, userId);
        }
        else {
            recyclerView.setAdapter(new FirebaseLectureAdapter(FirebaseDatabase.getInstance().getReference("lectures"), MainActivity.this));
        }

//        Intent locationServiceIntent = new Intent(MainActivity.this, LocationService.class);
//        startService(locationServiceIntent);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_new_lecture) {
           Intent intent = new Intent(MainActivity.this, CreateLectureActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_view_profile) {
            Intent profile = new Intent(MainActivity.this,UserDetailsActivity.class);
            profile.putExtra(UserDetailsActivity.USER_ID_EXTRA,FirebaseAuth.getInstance().getCurrentUser().getUid());
            startActivity(profile);
        }
        if (id == R.id.action_edit_profile) {
            Intent edit = new Intent(MainActivity.this, EditProfileActivity.class);
            startActivity(edit);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id == R.id.all_lectures_item) {
            //recyclerView.setLayoutManager(new LinearLayoutManager(this));
            //recyclerView.setAdapter(new FirebaseLectureAdapter(FirebaseDatabase.getInstance().getReference("lectures"), MainActivity.this));
            FirebaseDatabase.getInstance().getReference("lectures").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Lecture> lectures = new ArrayList<Lecture>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Lecture lecture = snapshot.getValue(Lecture.class);
                        lectures.add(lecture);
                    }
                    recyclerView.setAdapter(new RecyclerLectureAdapter(lectures, MainActivity.this));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if (id==R.id.all_teachers_item) {
            //recyclerView.setLayoutManager(new LinearLayoutManager(this));
            FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<User> users = new ArrayList<User>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        users.add(user);
                    }
                    recyclerView.setAdapter(new RecyclerUserAdapter(users, MainActivity.this));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            //recyclerView.setAdapter(new FirebaseUserAdapter(FirebaseDatabase.getInstance().getReference("users"), MainActivity.this));
        }
        else if(id == R.id.favourite_teachers_item) {
            FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<User> users = new ArrayList<User>();
                    User currentUser = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(User.class);
                    if (currentUser != null && currentUser.get_favouriteTeachers() != null) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            if (user != null) {
                                if (currentUser.get_favouriteTeachers().containsKey(user.getId())) {
                                    users.add(user);
                                }
                            }
                        }
                        recyclerView.setAdapter(new RecyclerUserAdapter(users, MainActivity.this));
                    }
                    else {
                        recyclerView.setAdapter(new FirebaseUserAdapter(FirebaseDatabase.getInstance().getReference("users"), MainActivity.this));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(id == R.id.pending_lectures_item) {
            FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final User currentUser = dataSnapshot.getValue(User.class);
                    if (currentUser != null) {
                        if (currentUser.get_interestedLectures() != null) {
                            FirebaseDatabase.getInstance().getReference("lectures").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new );
                                    List<Lecture> lectures = new ArrayList<Lecture>();
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Lecture lecture = snapshot.getValue(Lecture.class);
                                        if (lecture != null) {
                                            if (currentUser.get_interestedLectures().containsKey(lecture.getId())) {
                                                lectures.add(lecture);
                                            }
                                        }
                                    }
                                    recyclerView.setAdapter(new RecyclerLectureAdapter(lectures, MainActivity.this));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            recyclerView.setAdapter(new FirebaseLectureAdapter(FirebaseDatabase.getInstance().getReference("lectures"), MainActivity.this));
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onUserClick(String userId) {
        Intent intent = new Intent(this, UserDetailsActivity.class);
        intent.putExtra(UserDetailsActivity.USER_ID_EXTRA, userId);
        startActivity(intent);
    }

    @Override
    public void onLectureClick(String lectureId) {
        Intent intent = new Intent(this, LectureDetailsActivity.class);
        intent.putExtra(LectureDetailsActivity.LECTURE_ID_EXTRA, lectureId);
        startActivity(intent);
    }

    public void decideWhatToShow(final int type, String userId) {

        if (type == -1) {
            recyclerView.setAdapter(new FirebaseLectureAdapter(FirebaseDatabase.getInstance().getReference("lectures"), MainActivity.this));
        }
        else {
            if (userId != null) {
                FirebaseDatabase.getInstance().getReference("users").child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            final Map<String, Object> userLectures;
                            if (type == 1) { //future lectures
                                userLectures = user.get_myLectures();
                            }
                            else if (type == 2) {
                                userLectures = user.get_myPastLectures();
                            }
                            else {
                                userLectures = null;
                            }
                            if (userLectures != null) {
                                FirebaseDatabase.getInstance().getReference("lectures").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        List<Lecture> lectureList  = new ArrayList<Lecture>();
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            if (userLectures.containsKey(snapshot.getKey())) {
                                                Lecture lecture = snapshot.getValue(Lecture.class);
                                                if (lecture != null) {
                                                    lectureList.add(lecture);
                                                }
                                            }
                                        }
                                        recyclerView.setAdapter(new RecyclerLectureAdapter(lectureList, MainActivity.this));
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                recyclerView.setAdapter(new FirebaseLectureAdapter(FirebaseDatabase.getInstance().getReference("lectures"), MainActivity.this));
                            }

                        } else {
                            recyclerView.setAdapter(new FirebaseLectureAdapter(FirebaseDatabase.getInstance().getReference("lectures"), MainActivity.this));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }
}
