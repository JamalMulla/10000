package com.jmulla.some10000words;

import androidx.recyclerview.widget.DiffUtil;

import com.jmulla.some10000words.Entities.WordPair;

import java.util.List;

public class SpotDiffCallback extends DiffUtil.Callback {

  private final List<WordPair> old;
  private final List<WordPair> newItems;

  public SpotDiffCallback(List<WordPair> old, List<WordPair> newItems){
    this.old = old;
    this.newItems = newItems;
  }

  @Override
  public int getOldListSize() {
    return old.size();
  }

  @Override
  public int getNewListSize() {
    return newItems.size();
  }

  @Override
  public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
    return old.get(oldItemPosition).getId() == newItems.get(newItemPosition).getId();
  }

  @Override
  public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
    return old.get(oldItemPosition) == newItems.get(newItemPosition);
  }
}
