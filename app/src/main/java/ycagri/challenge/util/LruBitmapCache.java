package ycagri.challenge.util;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Bitmap cache for downloaded photos.
 *
 * @author ycagri
 * @since 10.1.2015.
 */
public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {
    public LruBitmapCache() {
        this(getDefaultLruCacheSize());
    }

    private LruBitmapCache(int maxSize) {
        super(maxSize);
    }

    private static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        return maxMemory / 8;
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    public Bitmap getBitmap(String arg0) {
        return get(arg0);
    }

    @Override
    public void putBitmap(String arg0, Bitmap arg1) {
        put(arg0, arg1);
    }
}

