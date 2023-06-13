package com.drewapps.ai.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.drewapps.ai.items.ItemSupportReq;
import com.drewapps.ai.items.Message;
import com.drewapps.ai.items.SupportDetails;

import java.util.List;

@Dao
public abstract class SupportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<ItemSupportReq> supportReqList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(ItemSupportReq itemSupportReq);

    @Query("SELECT*FROM support_request")
    public abstract LiveData<List<ItemSupportReq>> getAllSupportReq();

    @Query("SELECT * FROM support_request WHERE ticketId = :ticketId")
    public abstract LiveData<ItemSupportReq> getById(String ticketId);

    @Query("DELETE FROM support_request")
    public abstract void deleteAll();

    @Query("DELETE FROM support_request WHERE ticketId = :ticketId")
    public abstract void deleteById(String ticketId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertDetail(SupportDetails supportDetails);

    @Query("SELECT * FROM support_detail WHERE ticketId = :ticketId")
    public abstract LiveData<SupportDetails> getDetail(String ticketId);

    @Query("DELETE FROM support_detail WHERE ticketId = :ticketId")
    public abstract void deleteDetail(String ticketId);

    @Query("UPDATE support_detail SET message = :item AND status = :status WHERE ticketId = :ticketId")
    public abstract void update(List<Message> item, String status, String ticketId);
}
