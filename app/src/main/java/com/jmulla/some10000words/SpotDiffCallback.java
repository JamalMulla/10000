package com.jmulla.some10000words;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class SpotDiffCallback extends DiffUtil.Callback {

  private final List<Spot> old;
  private final List<Spot> newItems;

  public SpotDiffCallback(List<Spot> old, List<Spot> newItems){
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
    return old.get(oldItemPosition).getId().equals(newItems.get(newItemPosition).getId());
  }

  @Override
  public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
    return old.get(oldItemPosition).equals(newItems.get(newItemPosition));
  }
}
