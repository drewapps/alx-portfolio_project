package com.drewapps.ai.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.drewapps.ai.items.AllTemplateData;
import com.drewapps.ai.items.Template;

import java.util.List;

@Dao
public abstract class TemplateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAllTemplate(AllTemplateData allTemplateData);

    @Query("SELECT * FROM all_template")
    public abstract LiveData<AllTemplateData> getAllTemplate();

    @Query("DELETE FROM all_template")
    public abstract void deleteAllTemplate();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertTemplate(List<Template> template);

    @Query("SELECT * FROM template WHERE category = :category")
    public abstract List<Template> getTemplateByCategory(String category);

    @Query("DELETE FROM template WHERE category = :category")
    public abstract void deleteTemplateByCategory(String category);

    @Query("UPDATE template SET isFavorite = :isFavorite WHERE templateId =:templateId")
    public abstract void favorite(Integer templateId, boolean isFavorite);

    @Query("SELECT * FROM template WHERE templateName LIKE :query OR category LIKE :query OR templateDescription LIKE :query")
    public abstract LiveData<List<Template>> getSearchTemplate(String query);

}
