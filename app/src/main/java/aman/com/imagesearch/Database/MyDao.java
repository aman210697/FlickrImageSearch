package aman.com.imagesearch.Database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import aman.com.imagesearch.Model.ImageItem;

/*
* to perform operations on the table
* */

@Dao
public interface MyDao {

    //it will insert the data into the table
    @Insert
     void addImageItem(ImageItem imageItem);

    //it will return with items with same searchkey
    @Query("select * from imageitem where searchKey= :query")
     List<ImageItem> getImageItem(String query);

    //delete all the items with same searchkey
    @Query("delete from imageitem where searchKey= :query")
     void deleteImageItem(String query);

}
