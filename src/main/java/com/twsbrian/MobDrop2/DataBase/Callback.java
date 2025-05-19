package com.twsbrian.MobDrop2.DataBase;

public interface Callback<T> {
  void onSuccess(T result);
  void onFailure(Exception e);
}
