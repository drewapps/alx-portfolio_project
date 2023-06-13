package com.drewapps.ai.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.drewapps.ai.items.PurchaseHistory;

import java.util.List;

@Dao
public abstract class PurchaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<PurchaseHistory> purchaseHistoryList);

    @Query("SELECT * FROM purchase_history ORDER BY transactionId DESC")
    public abstract LiveData<List<PurchaseHistory>> getAllData();

    @Query("DELETE FROM purchase_history")
    public abstract void deleteAll();

}
