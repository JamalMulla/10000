package com.jmulla.some10000words;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;

import com.jmulla.some10000words.JSONModels.DEWordPair;
import com.jmulla.some10000words.JSONModels.ESWordPair;
import com.jmulla.some10000words.Utils.AssetUtils;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements CardStackListener {

  private DrawerLayout drawerLayout;
  private CardStackView cardStackView;
  private CardStackLayoutManager manager;
  private CardStackAdapter adapter;
  private List<Spot> deSpots = new ArrayList<>();
  private List<Spot> esSpots = new ArrayList<>();
  private Language currentLang = Language.ES;
  private Stack<Direction> directions = new Stack<>();


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    drawerLayout = findViewById(R.id.drawer_layout);
    cardStackView = findViewById(R.id.card_stack_view);

    setupDESpots();
    setupESSpots();

    manager = new CardStackLayoutManager(this, this);
    adapter = new CardStackAdapter(this.esSpots);

    setupNavigation();
    setupCardStackView();
    setupButton();

  }


  private void setupDESpots() {
    String deWordList = loadJSONAsString("de_wordlist.json");

    if (deWordList == null) {
      return;
    }
    Moshi moshi = new Moshi.Builder().build();
    Type listWords = Types.newParameterizedType(List.class, DEWordPair.class);
    JsonAdapter<List<DEWordPair>> jsonAdapter = moshi.adapter(listWords);
    jsonAdapter = jsonAdapter.lenient();

    List<DEWordPair> wordPairList;
    try {
      wordPairList = jsonAdapter.fromJson(deWordList);
      if (wordPairList == null) {
        Toast.makeText(this, "Couldn't get German words", Toast.LENGTH_SHORT).show();
        return;
      }
      List<Spot> spots = new ArrayList<>();
      for (DEWordPair pair : wordPairList) {
        spots.add(new Spot(pair.getDe(), pair.getEn()));
      }
      this.deSpots = spots;
      shuffle(Language.DE);
    } catch (IOException e) {
      e.printStackTrace();
      Toast.makeText(this, "Couldn't get German words", Toast.LENGTH_SHORT).show();
    }
  }

  private void setupESSpots() {
    String esWordList = loadJSONAsString("es_wordlist.json");

    if (esWordList == null) {
      return;
    }


    Moshi moshi = new Moshi.Builder().build();
    Type listWords = Types.newParameterizedType(List.class, ESWordPair.class);
    JsonAdapter<List<ESWordPair>> jsonAdapter = moshi.adapter(listWords);

    List<ESWordPair> wordPairList;
    try {
      wordPairList = jsonAdapter.fromJson(esWordList);
      if (wordPairList == null) {
        Toast.makeText(this, "Couldn't get Spanish words", Toast.LENGTH_SHORT).show();
        return;
      }
      List<Spot> spots = new ArrayList<>();
      for (ESWordPair pair : wordPairList) {
        String es;
        String en1;
        String en2;
        es = pair.getBase().equals("") ? pair.getOriginal_es() : pair.getBase();
        en1 = pair.getDef1().equals("") ? pair.getOriginal_en() : pair.getDef1();
        en2 = pair.getDef2();
        spots.add(new Spot(es, en1, en2, pair.getPronunciation()));
      }
      this.esSpots = spots;
      shuffle(Language.ES);
    } catch (Exception e) {
      e.printStackTrace();
      Toast.makeText(this, "Couldn't get Spanish words", Toast.LENGTH_SHORT).show();
    }
  }


  private String loadJSONAsString(String filename) {
    String JSONString = AssetUtils.loadJSONFromAsset(this, filename);
    if (JSONString == null) {
      System.out.println("Error in loading word store");
      return null;
    }
    return JSONString;
  }


  private void shuffle(Language language) {
    if (language.equals(Language.DE)) {
      Collections.shuffle(this.deSpots);
    } else if (language.equals(Language.ES)) {
      Collections.shuffle(this.esSpots);
    }
  }


  @Override
  public void onBackPressed() {
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
      drawerLayout.closeDrawers();
    } else {
      super.onBackPressed();
    }

    super.onBackPressed();
  }

  @Override
  public void onCardDragging(Direction direction, float ratio) {
    Log.d("CardStackView", "onCardDragging: d = " + direction.name() + ", r = " + ratio);
  }

  @Override
  public void onCardSwiped(Direction direction) {
    Log.d("CardStackView", "onCardSwiped: p = " + manager.getTopPosition() + ", d = " + direction);
    directions.push(direction);
//    if (manager.getTopPosition() == adapter.getItemCount() - 5){
//      paginate();
//    }
  }

  @Override
  public void onCardRewound() {
    Log.d("CardStackView", "onCardRewound: " + manager.getTopPosition());
  }

  @Override
  public void onCardCanceled() {
    Log.d("CardStackView", "onCardCanceled: " + manager.getTopPosition());
  }

  @Override
  public void onCardAppeared(View view, int position) {
    TextView textView = view.findViewById(R.id.foreign_word);
    Log.d("CardStackView", "onCardAppeared: " + "(" + position + ") " + textView.getText());
  }

  @Override
  public void onCardDisappeared(View view, int position) {
    TextView textView = view.findViewById(R.id.foreign_word);
    Log.d("CardStackView", "onCardDisappeared: " + "(" + position + ") " + textView.getText());
  }

  void setupNavigation() {
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

//    ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,  R.string.open_drawer, R.string.close_drawer);
//
//    actionBarDrawerToggle.syncState();
//    drawerLayout.addDrawerListener(actionBarDrawerToggle);
//
//    NavigationView navigationView = findViewById(R.id.navigation_view);
//
//    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//      @Override
//      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        if (menuItem.getItemId() == R.id.reload){
//          reload();
//        } else if(menuItem.getItemId() == R.id.add_spot_to_first){
//          addFirst(1);
//        } else if(menuItem.getItemId() == R.id.add_spot_to_last){
//          addLast(1);
//        } else if(menuItem.getItemId() == R.id.remove_spot_from_first){
//          removeFirst(1);
//        } else if (menuItem.getItemId() == R.id.remove_spot_from_last){
//          removeLast(1);
//        } else if (menuItem.getItemId() == R.id.swap_first_for_last){
//          swap();
//        } else if (menuItem.getItemId() == R.id.replace_first_spot){
//          replace();
//        }
//        drawerLayout.closeDrawers();
//        return true;
//      }
//    });

  }


  public void setupCardStackView() {
    initialize();
  }

  public void setupButton() {
    View skip = findViewById(R.id.skip_button);
    skip.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
            .setDirection(Direction.Left)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(new AccelerateInterpolator())
            .build();
        manager.setSwipeAnimationSetting(setting);
        cardStackView.swipe();
      }
    });

    View rewind = findViewById(R.id.rewind_button);
    rewind.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (directions.empty()) {
          return;
        }
        RewindAnimationSetting setting = new RewindAnimationSetting.Builder()
            .setDirection(directions.pop())
            .setDuration(Duration.Fast.duration)
            .setInterpolator(new DecelerateInterpolator())
            .build();
        manager.setRewindAnimationSetting(setting);
        cardStackView.rewind();
      }
    });

    View like = findViewById(R.id.like_button);
    like.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
            .setDirection(Direction.Right)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(new AccelerateInterpolator())
            .build();
        manager.setSwipeAnimationSetting(setting);
        cardStackView.swipe();
      }
    });
  }

  void initialize() {
    manager.setStackFrom(StackFrom.Top);
    manager.setVisibleCount(3);
    manager.setTranslationInterval(12.0f);
    manager.setScaleInterval(0.95f);
    manager.setSwipeThreshold(0.4f);
    manager.setMaxDegree(0.0f);
    manager.setDirections(Direction.HORIZONTAL);
    manager.setCanScrollHorizontal(true);
    manager.setCanScrollVertical(false);
    manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
    manager.setOverlayInterpolator(new LinearInterpolator());
    cardStackView.setLayoutManager(manager);
    cardStackView.setAdapter(adapter);
    if (cardStackView.getItemAnimator() instanceof DefaultItemAnimator) {
      ((DefaultItemAnimator) cardStackView.getItemAnimator()).setSupportsChangeAnimations(false);
    }
  }

//  void paginate(){
//    List<Spot> old = adapter.getSpots();
//    List<Spot> newItems = new ArrayList<>();
//    newItems.addAll(old);
//    newItems.addAll(createSpots());
//    SpotDiffCallback callback = new SpotDiffCallback(old, newItems);
//    DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
//    adapter.setSpots(newItems);
//    result.dispatchUpdatesTo(adapter);
//  }

  void reload() {
//    List<Spot> old = adapter.getSpots();
//    List<Spot> newItems = createSpots();
//    SpotDiffCallback callback = new SpotDiffCallback(old, newItems);
//    DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

    shuffle(currentLang);
    adapter.notifyDataSetChanged();
    //result.dispatchUpdatesTo(adapter);
  }
//
//  void addFirst(int size){
//    List<Spot> old = adapter.getSpots();
//    List<Spot> newItems = new ArrayList<>(old);
//    for (int i = 0; i < size; i++){
//      newItems.add(manager.getTopPosition(), createSpot());
//    }
//    SpotDiffCallback callback = new SpotDiffCallback(old, newItems);
//    DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
//    adapter.setSpots(newItems);
//    result.dispatchUpdatesTo(adapter);
//  }
//
//  void addLast(int size){
//    List<Spot> old = adapter.getSpots();
//    List<Spot> newItems = new ArrayList<>(old);
//    for (int i = 0; i < size; i++){
//      newItems.add(createSpot());
//    }
//    SpotDiffCallback callback = new SpotDiffCallback(old, newItems);
//    DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
//    adapter.setSpots(newItems);
//    result.dispatchUpdatesTo(adapter);
//  }
//
//
//  void removeFirst(int size){
//    if (adapter.getSpots().isEmpty()){
//      return;
//    }
//
//    List<Spot> old = adapter.getSpots();
//    List<Spot> newItems = new ArrayList<>(old);
//    for (int i = 0; i < size; i++){
//      newItems.remove(manager.getTopPosition());
//    }
//    SpotDiffCallback callback = new SpotDiffCallback(old, newItems);
//    DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
//    adapter.setSpots(newItems);
//    result.dispatchUpdatesTo(adapter);
//  }

//  void removeLast(int size){
//    if (adapter.getSpots().isEmpty()){
//      return;
//    }
//
//    List<Spot> old = adapter.getSpots();
//    List<Spot> newItems = new ArrayList<>(old);
//    for (int i = 0; i < size; i++){
//      newItems.remove(newItems.size() - 1);
//    }
//    SpotDiffCallback callback = new SpotDiffCallback(old, newItems);
//    DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
//    adapter.setSpots(newItems);
//    result.dispatchUpdatesTo(adapter);
//  }

//  void replace(){
//    List<Spot> old = adapter.getSpots();
//    List<Spot> newItems = new ArrayList<>(old);
//    newItems.remove(manager.getTopPosition());
//    newItems.add(manager.getTopPosition(), createSpot());
//    adapter.setSpots(newItems);
//    adapter.notifyItemChanged(manager.getTopPosition());
//  }

//  void swap(){
//    List<Spot> old = adapter.getSpots();
//    List<Spot> newItems = new ArrayList<>(old);
//    Spot first = newItems.remove(manager.getTopPosition());
//    Spot last = newItems.remove(newItems.size() - 1);
//    newItems.add(manager.getTopPosition(), last);
//    newItems.add(first);
//    SpotDiffCallback callback = new SpotDiffCallback(old, newItems);
//    DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
//    adapter.setSpots(newItems);
//    result.dispatchUpdatesTo(adapter);
//  }


}
