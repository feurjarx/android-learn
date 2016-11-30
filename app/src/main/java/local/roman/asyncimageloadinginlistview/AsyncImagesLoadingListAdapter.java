package local.roman.asyncimageloadinginlistview;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class AsyncImagesLoadingListAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private boolean isActiveNetwork;
    private Context mainActivityContext;

    private int maxSize = 9999;

    public AsyncImagesLoadingListAdapter(Context context/*, ArrayList<ListItem> listData*/) {
        this.layoutInflater = LayoutInflater.from(context);
        this.mainActivityContext = context;

        // is net connected ?
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        this.isActiveNetwork = activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public int getCount() {
        return this.maxSize;
    }

    @Override
    public Integer getItem(int pos) {
        return pos;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    ArrayList<Integer> handledPositions = new ArrayList<Integer>();

    public View getView(int pos, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.images_list_row_layout, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.titleForThumbImage);
            holder.imageView = (ImageView) convertView.findViewById(R.id.thumbImage);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();

        holder.title.setText(Integer.toString(pos + 1));

        Bitmap bitmap = ((MainActivity)this.mainActivityContext).cache.get(pos);
        if (handledPositions.indexOf(pos) == -1 || bitmap == null) {

            AsyncTask<String, Void, Bitmap> a = new ImageDownloaderTask(this, holder.imageView, this.isActiveNetwork, this.mainActivityContext, pos)
                    .execute(
                            "https://vk.com/images/stickers/" + Integer.toString(pos + 1) + "/64.png",
                            "_" + Integer.toString(pos % 20)
                    );

            handledPositions.add(pos);

            holder.imageView.setImageDrawable(this.mainActivityContext.getResources().getDrawable(R.drawable.spinner));

        } else {

            holder.imageView.setImageBitmap(bitmap);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView title;
        ImageView imageView;
    }
}
