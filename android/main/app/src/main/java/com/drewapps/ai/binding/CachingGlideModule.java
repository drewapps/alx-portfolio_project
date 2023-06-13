package com.drewapps.ai.binding;

import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.drewapps.ai.Config;

@GlideModule
public class CachingGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        int memoryCacheSizeBytes = 1024 * 1024 * Config.IMAGE_CACHE_LIMIT; // 250mb
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, memoryCacheSizeBytes));
        builder.setDefaultRequestOptions(requestOptions(context));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

    }

    private static RequestOptions requestOptions(Context context) {
        return new RequestOptions()
//                .placeholder(R.drawable.placeholder_image)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .format(PREFER_ARGB_8888)
                .dontTransform();
    }
}
