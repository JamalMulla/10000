package com.jmulla.some10000words;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.jmulla.some10000words.Entities.Pronunciation;
import com.jmulla.some10000words.Entities.WordPair;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.util.ArrayList;
import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

  private List<WordPair> spots;

  private AnimatorSet mSetRightOut;
  private AnimatorSet mSetLeftIn;

  private List<Integer> colors;


  public CardStackAdapter(List<WordPair> spots) {
    this.spots = spots;
  }

  public CardStackAdapter(){}

  private void setupColors() {
    colors = new ArrayList<>();
    colors.add(ContextCompat.getColor(context, R.color.deep_aqua));
    colors.add(ContextCompat.getColor(context, R.color.ocean));
    colors.add(ContextCompat.getColor(context, R.color.reflection));
    colors.add(ContextCompat.getColor(context, R.color.rain));
    colors.add(ContextCompat.getColor(context, R.color.wave));
    colors.add(ContextCompat.getColor(context, R.color.greenery));
    colors.add(ContextCompat.getColor(context, R.color.new_grass));


  }

  private int getColor(int adapterPosition) {
    if (adapterPosition < 0) {
      adapterPosition = 0;
    }
    int col = adapterPosition % colors.size();
    return colors.get(col);
  }


  private Context context;

  @NonNull
  @Override
  public CardStackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    this.context = parent.getContext();
    loadAnimations(context);
    setupColors();
    return new ViewHolder(inflater.inflate(R.layout.item_spot, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull CardStackAdapter.ViewHolder holder, int position) {
    final WordPair spot = this.spots.get(position);
    holder.foreign_word.setText(spot.getForeignWord());
    holder.en_word1.setText(spot.getDef1());
    if (spot.getDef2().equals("")) {
      holder.divider_back.setVisibility(View.GONE);
    } else {
      holder.en_word2.setText(spot.getDef2());
    }

    if (spot.getPronunciation().isEmpty()) {
      holder.divider_front.setVisibility(View.GONE);
    } else {
      SpannableStringBuilder pronunciationString = new SpannableStringBuilder();
      //int whiteish = ContextCompat.getColor(context, R.color.almost_white);
      for (Pronunciation p : spot.getPronunciation()) {
        int start = pronunciationString.length();
        pronunciationString.append(p.getSyllable());
        if (p.hasEmphasis()) {
//        pronunciationString.setSpan(new ForegroundColorSpan(whiteish), start, pronunciationString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
          pronunciationString.setSpan(new StyleSpan(Typeface.BOLD), start, pronunciationString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
      }

      holder.pronunciation.setText(pronunciationString);
    }


    int color = getColor(holder.getAdapterPosition());
    ImageViewCompat.setImageTintList(holder.frontBg, ColorStateList.valueOf(color));
    ImageViewCompat.setImageTintList(holder.backBg, ColorStateList.valueOf(color));
  }

  @Override
  public int getItemCount() {
    if (spots == null){
      return 0;
    }
    return spots.size();
  }

  void setSpots(List<WordPair> spots) {
    this.spots = spots;
    notifyDataSetChanged();
  }

  List<WordPair> getSpots() {
    return spots;
  }

  private void flipCard(RelativeLayout mCardFrontLayout, RelativeLayout mCardBackLayout, boolean isBackVisible) {
    if (!isBackVisible) {
      mSetRightOut.setTarget(mCardFrontLayout);
      mSetLeftIn.setTarget(mCardBackLayout);
      mSetRightOut.start();
      mSetLeftIn.start();
    } else {
      mSetRightOut.setTarget(mCardBackLayout);
      mSetLeftIn.setTarget(mCardFrontLayout);
      mSetRightOut.start();
      mSetLeftIn.start();
    }
  }

  private void changeCameraDistance(Context context, RelativeLayout mCardFrontLayout, RelativeLayout mCardBackLayout) {
    int distance = 128000;
    float scale = context.getResources().getDisplayMetrics().density * distance;
    mCardFrontLayout.setCameraDistance(scale);
    mCardBackLayout.setCameraDistance(scale);
  }

  @Override
  public long getItemId(int position) {
    return spots.get(position).getId();
  }

  @Override
  public int getItemViewType(int position) {
    return spots.get(position).getId();
  }

  private void loadAnimations(Context context) {
    mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.out_animation);
    mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.in_animation);
  }


  class ViewHolder extends RecyclerView.ViewHolder {
    private TextView foreign_word;
    private TextView en_word1;
    private TextView en_word2;
    private TextView pronunciation;
    private RelativeLayout card_front;
    private RelativeLayout card_back;
    private ImageView frontBg;
    private ImageView backBg;
    private View divider_back;
    private View divider_front;
    private boolean isBackVisible = false;
    private SparkButton star;


    ViewHolder(final View view) {
      super(view);
      this.foreign_word = view.findViewById(R.id.foreign_word);
      this.en_word1 = view.findViewById(R.id.en_word1);
      this.en_word2 = view.findViewById(R.id.en_word2);
      this.pronunciation = view.findViewById(R.id.pronunciation);
      this.card_front = view.findViewById(R.id.card_front);
      this.card_back = view.findViewById(R.id.card_back);
      this.frontBg = view.findViewById(R.id.card_front_bg);
      this.backBg = view.findViewById(R.id.card_back_bg);
      this.divider_back = view.findViewById(R.id.divider_back);
      this.divider_front = view.findViewById(R.id.divider_front);
      this.star = view.findViewById(R.id.star);


      changeCameraDistance(context, card_front, card_back);
      this.card_front.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (mSetLeftIn.isRunning() || mSetRightOut.isRunning()) {
            return;
          }
          flipCard(card_front, card_back, isBackVisible);
          isBackVisible = !isBackVisible;
        }
      });

//      this.card_back.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//          flipCard(card_front, card_back, isBackVisible);
//        }
//      });


      this.star.setEventListener(new SparkEventListener() {
        @Override
        public void onEvent(ImageView button, boolean buttonState) {
          star.playAnimation();
        }

        @Override
        public void onEventAnimationEnd(ImageView button, boolean buttonState) {

        }

        @Override
        public void onEventAnimationStart(ImageView button, boolean buttonState) {

        }
      });
    }
  }


}
