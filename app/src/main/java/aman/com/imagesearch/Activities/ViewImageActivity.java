package aman.com.imagesearch.Activities;

import android.arch.persistence.room.Room;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import aman.com.imagesearch.Adapter.SwipableImageViewAdapter;
import aman.com.imagesearch.Database.Database;
import aman.com.imagesearch.Model.ImageItem;
import aman.com.imagesearch.R;

public class ViewImageActivity extends AppCompatActivity {


    SwipableImageViewAdapter adapter;
    List<ImageItem> mList;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        // mList = (ArrayList<ImageItem>) getIntent().getSerializableExtra(MainActivity.IMAGEITEMLIST);

        String searchQuery =getIntent().getStringExtra(MainActivity.SEARCHQUERY);
        Database mDatabase= Room.databaseBuilder(this, Database.class, "database")
                .allowMainThreadQueries()
                .build();

        mList= mDatabase.myDao().getImageItem(searchQuery);

        int position=getIntent().getIntExtra(MainActivity.POSTION,0);

       // init viewpager and adapter
        adapter=new SwipableImageViewAdapter(this,mList);
        viewPager=(ViewPager)findViewById(R.id.imageViewPager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);

    }


}
