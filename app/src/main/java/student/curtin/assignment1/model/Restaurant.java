package student.curtin.assignment1.model;

import java.util.*;

public class Restaurant
{
    // Temporary Code While Database Inactive
    //private static List<Restaurant> restaurantList = new LinkedList<Restaurant>();
    private String restName;
    private int restImage;

    // CONSTRUCTOR
    public Restaurant(String restName, int restImage){
        this.restName = restName;
        this.restImage = restImage;
        //restaurantList.add(this);
    }

//    public static List<Restaurant> getRestaurantList()
//    {
//        if(restaurantList.size() == 0) {
//            Restaurant rest11 = new Restaurant("ABC", android.R.color.black);
//            Restaurant rest1 = new Restaurant("KFC", android.R.color.black);
//            Restaurant rest2 = new Restaurant("MCD", android.R.color.black);
//            Restaurant rest3 = new Restaurant("DQ", android.R.color.black);
//            Restaurant rest4 = new Restaurant("CHT", android.R.color.black);
//            Restaurant rest5 = new Restaurant("SUB", android.R.color.black);
//            Restaurant rest6 = new Restaurant("YOT", android.R.color.black);
//            Restaurant rest7 = new Restaurant("MAL", android.R.color.black);
//            Restaurant rest8 = new Restaurant("GIN", android.R.color.black);
//            Restaurant rest9 = new Restaurant("HJ", android.R.color.black);
//            Restaurant rest10 = new Restaurant("ZAM", android.R.color.black);
//            Restaurant rest12 = new Restaurant("DEF", android.R.color.black);
//            Restaurant rest13 = new Restaurant("GHI", android.R.color.black);
//            Restaurant rest14 = new Restaurant("PZH", android.R.color.black);
//            Restaurant rest15 = new Restaurant("DOM", android.R.color.black);
//        }
//
//        return restaurantList;
//    }

    public String getRestName(){
        return restName;
    }
    public int getRestImage(){
        return restImage;
    }

//    public static Restaurant getRestFromString(String restaurantName) {
//
//        Restaurant restaurant = null;
//        for (int i = 0; i < restaurantList.size(); i++)
//        {
//            if ((restaurantList.get(i).restName).equals(restaurantName))
//            {
//                restaurant = restaurantList.get(i);
//            }
//        }
//        return restaurant;
//    }
}
