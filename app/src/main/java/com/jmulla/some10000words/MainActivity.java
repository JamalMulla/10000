package com.jmulla.some10000words;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.jmulla.some10000words.Entities.WordPair;
import com.jmulla.some10000words.ViewModels.WordPairViewModel;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements CardStackListener {

  private DrawerLayout drawerLayout;
  private CardStackView cardStackView;
  private CardStackLayoutManager manager;
  private CardStackAdapter adapter;
  private Language currentLang = Language.ES;
  private WordPairViewModel viewModel;
  private Stack<Direction> directions = new Stack<>();


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    final ProgressDialog nDialog;
    nDialog = new ProgressDialog(this);
    nDialog.setMessage("Loading..");
    nDialog.setTitle("Getting Spanish words");
    nDialog.setIndeterminate(false);
    nDialog.setCancelable(true);
    nDialog.show();

    drawerLayout = findViewById(R.id.drawer_layout);
    cardStackView = findViewById(R.id.card_stack_view);


    manager = new CardStackLayoutManager(this, this);
    viewModel = ViewModelProviders.of(this).get(WordPairViewModel.class);
    adapter = new CardStackAdapter(viewModel);

    setupNavigation();
    setupCardStackView();
    setupButtons();


    final long start = System.currentTimeMillis();

    viewModel.getRandomWordPairs(Language.ES, 50).observe(this, new Observer<List<WordPair>>() {
      @Override
      public void onChanged(List<WordPair> wordPairs) {
        if (wordPairs.size() > 0){
          adapter.setSpots(wordPairs);
          nDialog.dismiss();
          long end = System.currentTimeMillis();
          Log.i("MainActivity", "Time taken (ms) = " + (end - start));
        }
      }
    });


    viewModel.getAllStarred().observe(this, new Observer<List<WordPair>>() {
      @Override
      public void onChanged(List<WordPair> wordPairs) {
        for (WordPair wordPair : wordPairs){
          System.out.println(wordPair.getForeignWord() + ": " + wordPair.getDef1());
        }
      }
    });


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
    directions.pop();
  }

  @Override
  public void onCardCanceled() {
    Log.d("CardStackView", "onCardCanceled: " + manager.getTopPosition());
  }

  @Override
  public void onCardAppeared(View view, int position) {
//    TextView textView = view.findViewById(R.id.foreign_word);
//    Log.d("CardStackView", "onCardAppeared: " + "(" + position + ") " + textView.getText());
  }

  @Override
  public void onCardDisappeared(View view, int position) {
//    TextView textView = view.findViewById(R.id.foreign_word);
//    Log.d("CardStackView", "onCardDisappeared: " + "(" + position + ") " + textView.getText());
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

  public void setupButtons() {
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
        if ( directions.empty()) {
          return;
        }

        RewindAnimationSetting setting = new RewindAnimationSetting.Builder()
            .setDirection(directions.peek())
            .setDuration(Duration.Normal.duration)
            .setInterpolator(new DecelerateInterpolator())
            .build();
        manager.setRewindAnimationSetting(setting);
        cardStackView.rewind();
      }
    });

    View shuffle = findViewById(R.id.shuffle_button);
    shuffle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(MainActivity.this, "Getting new deck", Toast.LENGTH_SHORT).show();
        viewModel.swapDeck(Language.ES, 50);
        directions.clear();
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
