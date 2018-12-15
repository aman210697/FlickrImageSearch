package aman.com.imagesearch.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import aman.com.imagesearch.Model.ImageItem;
import aman.com.imagesearch.R;
import aman.com.imagesearch.widget.SquareImageView;


/*
Created by Aman on 2/8/18
*/
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    final static private String TAG="RecyclerViewAdapter";

    //calling activity context
    private Context context;

    //list of images
    private ArrayList<ImageItem> mList;

    private OnImageClickListener listener;


    //initialise components
    public RecyclerViewAdapter(Context context, ArrayList<ImageItem> mList, OnImageClickListener listener) {
        this.context = context;
        this.mList = mList;
        this.listener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //set the layout for recycler view

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent , false);

        return new ViewHolder(v);
    }


    /*
    * I have used picasso to load the images
    * It will create image cache to load images if the internet is available
    * It looks for the cache first
    * if cache is available for the url it loads from the cache else it fetch from the internet
    **/

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder,  int position) {

        final int pos=position;
        Log.d(TAG,"in onBindIVewHolder");
        //perform action on imageview

        final ImageItem item=mList.get(position);

        //loading the image in imageview

       final SquareImageView imageView=holder.imageView;
        Picasso.with(context)
                .load(item.getUrl())
                .placeholder(R.drawable.ic_gallery_icon)
                .into(imageView);



        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onImageClick(imageView,pos);
            }
        });


    }

    @Override
    public int getItemCount() {
        //total no. of items
        return mList.size();
    }


    public interface  OnImageClickListener{

      void onImageClick(SquareImageView view,int position);

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public SquareImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);

            imageView=(SquareImageView) itemView.findViewById(R.id.imageView);

        }
    }


}
