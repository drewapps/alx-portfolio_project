package com.drewapps.ai.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.drewapps.ai.items.AdapTemplateData;
import com.drewapps.ai.items.AllTemplateData;
import com.drewapps.ai.items.AppInfo;
import com.drewapps.ai.items.AppVersion;
import com.drewapps.ai.items.ChatItem;
import com.drewapps.ai.items.Document;
import com.drewapps.ai.items.ItemChatHistory;
import com.drewapps.ai.items.ItemGenMagicArt;
import com.drewapps.ai.items.ItemMagicArt;
import com.drewapps.ai.items.ItemSubsPlan;
import com.drewapps.ai.items.ItemSupportReq;
import com.drewapps.ai.items.LoginUser;
import com.drewapps.ai.items.Message;
import com.drewapps.ai.items.PurchaseHistory;
import com.drewapps.ai.items.SupportDetails;
import com.drewapps.ai.items.Template;
import com.drewapps.ai.items.UserData;
import com.drewapps.ai.items.UserMonthlyUsageImage;
import com.drewapps.ai.items.UserMonthlyUsageWord;

@Database(entities = {UserData.class, UserMonthlyUsageWord.class, UserMonthlyUsageImage.class, Template.class,
        AllTemplateData.class, Document.class, AdapTemplateData.class, ItemMagicArt.class, ItemGenMagicArt.class,
        PurchaseHistory.class, ItemSupportReq.class, SupportDetails.class, Message.class, ItemSubsPlan.class,
        LoginUser.class, AppInfo.class, AppVersion.class, ChatItem.class, ItemChatHistory.class},
        version = 7, exportSchema = false)
@TypeConverters({DataConverters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "magic_database";

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                INSTANCE = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return INSTANCE;
    }

    abstract public UserDataDao getUserDataDao();

    abstract public TemplateDao getTemplateDao();

    abstract public DocumentDao getDocumentDao();

    abstract public MagicArtDao getMagicArtDao();

    abstract public PurchaseDao getPurchaseDao();

    abstract public SupportDao getSupportDao();

    abstract public SubscriptionDao getSubscriptionDao();
}


