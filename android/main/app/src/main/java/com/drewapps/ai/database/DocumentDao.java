package com.drewapps.ai.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.drewapps.ai.items.Document;

import java.util.List;

@Dao
public abstract class DocumentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Document document){
        updateAvailableWord(document.contentWord);
    }

    @Query("UPDATE user_data SET availableWord = (availableWord - :value)")
    public abstract void updateAvailableWord(Integer value);

    @Query("SELECT * FROM document ORDER BY documentId DESC")
    public abstract LiveData<List<Document>> getAllDocuments();

    @Query("SELECT * FROM document WHERE documentId = :documentId")
    public abstract Document getDocumentById(Integer documentId);

    @Query("DELETE FROM document WHERE documentId = :documentId")
    public abstract void deleteById(Integer documentId);

    @Query("DELETE FROM document ")
    public abstract void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<Document> documents);

    @Query("UPDATE document SET documentName = :documentTitle, contentWord =:documentContent WHERE documentId =:documentId")
    public abstract void updateDocument(Integer documentId, String documentTitle, String documentContent);
}
