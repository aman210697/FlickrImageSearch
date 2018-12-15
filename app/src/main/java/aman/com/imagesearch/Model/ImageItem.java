package aman.com.imagesearch.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by Aman on 2/8/18.
 */

/*
* Using Room to make table with same column name as variables
* */

@Entity(tableName = "imageitem")
public class ImageItem implements Serializable {

    @PrimaryKey(autoGenerate = true)    //primary key of the table
    @NonNull
    private int imageId;
    private String id;
    private String secret;
    private String server;
    private String farm;
    private int pageNo;
    private String searchKey;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getFarm() {
        return farm;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public ImageItem(int ImageId, String secret, String server, String farm, int pageNo, String searchKey) {


        this.imageId = ImageId;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
        this.pageNo=pageNo;
        this.searchKey=searchKey;


    }

    public  ImageItem(){

    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        Log.d("ImageItem", "-------------getUrl-------: http://farm" + farm + ".static.flickr.com/" + server + "/" + id + "_" + secret + ".jpg" );

        return "http://farm" + farm + ".static.flickr.com/" + server + "/" + id + "_" + secret + ".jpg" ;
    }

}
