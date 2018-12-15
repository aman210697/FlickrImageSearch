package aman.com.imagesearch.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import aman.com.imagesearch.Model.ImageItem;
import aman.com.imagesearch.R;

public class SwipableImageViewAdapter extends PagerAdapter {

    List<ImageItem> mList;
    Context context;


    public SwipableImageViewAdapter(Context context,List<ImageItem> mList) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
             return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);

       // imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setTransitionName(context.getString(R.string.transition_name));

        Picasso.with(context)
                .load(mList.get(position).getUrl())
                .placeholder(R.drawable.ic_gallery_icon)
                .into(imageView);
        container.addView(imageView, 0);

        return imageView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}
