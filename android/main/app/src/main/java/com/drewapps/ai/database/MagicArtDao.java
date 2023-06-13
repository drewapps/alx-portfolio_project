package com.drewapps.ai.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.drewapps.ai.items.ItemGenMagicArt;
import com.drewapps.ai.items.ItemMagicArt;

import java.util.List;

@Dao
public abstract class MagicArtDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<ItemMagicArt> magicArtList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(ItemMagicArt magicArtList);

    @Query("SELECT * FROM magic_art ORDER BY id DESC")
    public abstract LiveData<List<ItemMagicArt>> getAllMagicArt();

    @Query("SELECT * FROM magic_art WHERE querys = :query")
    public abstract LiveData<List<ItemMagicArt>> getByQuery(String query);

    @Query("DELETE FROM magic_art WHERE id =:id")
    public abstract void deleteById(Integer id);

    @Query("DELETE FROM magic_art")
    public abstract void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertGenAll(List<ItemGenMagicArt> magicArtList);

    @Query("SELECT * FROM gen_magic_art")
    public abstract LiveData<List<ItemGenMagicArt>> getAllGenMagicArt();

    @Query("DELETE FROM gen_magic_art")
    public abstract void deleteGenAll();
}

