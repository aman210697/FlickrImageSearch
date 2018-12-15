package aman.com.imagesearch.Utils;

import android.net.Uri;

/**
 * Created by Aman on 2/8/18.
 */
public class UrlManager {



    public static final String API_KEY = "1f3f455a0314fe7a493084a1ce22aab3" ;
    // must public
    public static final String PREF_SEARCH_QUERY = "searchQuery";

    /*----------- private -------------------------*/


    private static final String ENDPOINT = "https://api.flickr.com/services/rest/" ;
    private static final String METHOD_GETRECENT = "flickr.photos.getRecent" ;
    private static final String METHOD_SEARCH = "flickr.photos.search" ;
    private static final String FORMAT= "json" ;
    private static final String NOJSONCALLBACK = "1" ;



    private static volatile UrlManager instance = null;
    private UrlManager() {

    }

    public static UrlManager getInstance() {
        if (instance == null) {
            synchronized (UrlManager.class) {
                if (instance == null) {
                    instance = new UrlManager();
                }
            }
        }
        return instance;
    }


   /*
    *get the url of the api
    *if the query is null it will load the random images
    */


    public static String getItemUrl(String query, int page) {
        String url;
        if (query != null) {
            url = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter("method", METHOD_SEARCH)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", FORMAT)
                    .appendQueryParameter("nojsoncallback", NOJSONCALLBACK)
                    .appendQueryParameter("text", query)
                    .appendQueryParameter("page", String.valueOf(page))
                    .build().toString();
        } else {
            url = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter("method", METHOD_GETRECENT)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("page", String.valueOf(page))
                    .build().toString();
        }
        return url;
    }


}
