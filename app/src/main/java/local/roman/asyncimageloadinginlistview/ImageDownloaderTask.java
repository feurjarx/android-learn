package local.roman.asyncimageloadinginlistview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap>
{
    private final WeakReference<ImageView> imageViewReference;

    private boolean isActiveNetwork;

    private Context mainActivityContext;

    private AsyncImagesLoadingListAdapter adapter;

    private int cacheKey;

    public ImageDownloaderTask(AsyncImagesLoadingListAdapter adapter, ImageView imageView, boolean isActiveNetwork, Context mainActivityContext, int cacheKey) {
        imageViewReference = new WeakReference<ImageView>(imageView);
        this.isActiveNetwork = isActiveNetwork;
        this.mainActivityContext = mainActivityContext;
        this.cacheKey = cacheKey;

        this.adapter = adapter;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        if (this.isActiveNetwork) {

            return downloadImgByUrlInBitmap(params[0]);

        } else {

            return loadImgFromFileInBitmap(params[1]);
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        if (isCancelled()) {
            bitmap = null;
        }

        ImageView imageView = imageViewReference.get();

        if (imageView != null) {

            if (bitmap == null) {

                Drawable placeholder = ContextCompat.getDrawable(this.mainActivityContext, R.drawable.vk);
                imageView.setImageDrawable(placeholder);

            } else {

//                imageView.setImageBitmap(bitmap);
                this.adapter.notifyDataSetChanged();
                ((MainActivity)this.mainActivityContext).cache.put(cacheKey, bitmap);
            }
        }
    }

    private Bitmap downloadImgByUrlInBitmap(String imgUrl) {

        Bitmap result = null;

        HttpURLConnection urlConnection = null;
        try {

            URL uri = new URL(imgUrl);
            urlConnection = (HttpURLConnection) uri.openConnection();

            int statusCode = urlConnection.getResponseCode();
            if (statusCode ==  HttpURLConnection.HTTP_OK) {

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    result = BitmapFactory.decodeStream(inputStream);
                }
            }

        } catch (Exception e) {

            String log = e.getMessage();

        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return result;
    }

    private Bitmap loadImgFromFileInBitmap(String filename) {
        Bitmap result = null;

        int id = mainActivityContext.getResources().getIdentifier(filename, "drawable", mainActivityContext.getPackageName());
        try {
            Drawable drawable = mainActivityContext.getResources().getDrawable(id);

            if (drawable != null) {
                result = ((BitmapDrawable)drawable).getBitmap();
            }
        } catch (Exception e) {
            String log = e.getMessage();
        }


        return result;
    }

}