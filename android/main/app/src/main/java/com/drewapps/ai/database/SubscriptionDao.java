package com.drewapps.ai.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.drewapps.ai.items.ItemSubsPlan;

import java.util.List;

@Dao
public abstract class SubscriptionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<ItemSubsPlan> list);

    @Query("SELECT * FROM subscriptions ORDER BY mostPopular DESC")
    public abstract LiveData<List<ItemSubsPlan>> getAll();

    @Query("DELETE FROM subscriptions")
    public abstract void deleteAll();

    @Query("UPDATE subscriptions SET gPrice = :price WHERE googleProductId = :id")
    public abstract void updateData(String price, String id);
}
