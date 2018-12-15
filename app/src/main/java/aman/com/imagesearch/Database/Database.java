package aman.com.imagesearch.Database;


import android.arch.persistence.room.RoomDatabase;

import aman.com.imagesearch.Model.ImageItem;

/*
* setup the database with table name and version =1*/

@android.arch.persistence.room.Database(entities = {ImageItem.class},version = 1)
public abstract  class Database extends RoomDatabase {

    public abstract MyDao myDao();

}
