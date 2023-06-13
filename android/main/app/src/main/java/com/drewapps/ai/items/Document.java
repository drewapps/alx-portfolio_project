package com.drewapps.ai.items;

import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "document", primaryKeys = "documentId")
public class Document implements Serializable {
    @SerializedName("documentId")
    @Expose
    public Integer documentId;
    @SerializedName("documentName")
    @Expose
    public String documentName;
    @SerializedName("templateName")
    @Expose
    public String templateName;
    @SerializedName("templateImage")
    @Expose
    public String templateImage;
    @SerializedName("lastChangeDate")
    @Expose
    public String lastChangeDate;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("content_word")
    @Expose
    public Integer contentWord;
    @SerializedName("content_character")
    @Expose
    public Integer contentCharacter;

    public Document(Integer documentId, String documentName, String templateName, String templateImage, String lastChangeDate, String content, Integer contentWord, Integer contentCharacter) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.templateName = templateName;
        this.templateImage = templateImage;
        this.lastChangeDate = lastChangeDate;
        this.content = content;
        this.contentWord = contentWord;
        this.contentCharacter = contentCharacter;
    }
}
