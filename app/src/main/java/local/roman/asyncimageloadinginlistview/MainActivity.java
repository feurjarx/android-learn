package local.roman.asyncimageloadinginlistview;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public Map<Integer, Bitmap> cache = new HashMap<Integer, Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView imagesListView = (ListView) findViewById(R.id.listView);

        imagesListView.setAdapter(new AsyncImagesLoadingListAdapter(this/*, listData*/));
    }
}
