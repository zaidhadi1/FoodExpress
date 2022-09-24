package student.curtin.assignment1.model;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.Random;

public class DBHandler extends SQLiteOpenHelper {

    private static DBHandler instance = null;
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "foodExpress.db";

    private DBHandler(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // Singleton Design Pattern, Only one instance of Database at a time
    public static DBHandler getInstance(Context context) {
        if (instance == null) {
            instance = new DBHandler(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + DBSchema.Restaurant.TABLE_NAME + "(" +
                DBSchema.Restaurant.Cols.REST_NAME + " TEXT UNIQUE, " +
                DBSchema.Restaurant.Cols.REST_IMAGE + " TEXT)");

        db.execSQL("CREATE TABLE " + DBSchema.FoodItem.TABLE_NAME + "(" +
                DBSchema.FoodItem.Cols.FOODNAME + " TEXT UNIQUE, " +
                DBSchema.FoodItem.Cols.FOODIMAGE + " TEXT, " +
                DBSchema.FoodItem.Cols.FOODPRICE + " TEXT, " +
                DBSchema.FoodItem.Cols.RESTAURANT + " TEXT)");

        db.execSQL("CREATE TABLE " + DBSchema.OrderHistory.TABLE_NAME + "(" +
                DBSchema.OrderHistory.Cols.EMAIL + " TEXT, " +
                DBSchema.OrderHistory.Cols.RESTAURANT + " TEXT, " +
                DBSchema.OrderHistory.Cols.ITEMCOUNT + " TEXT, " +
                DBSchema.OrderHistory.Cols.TOTALCOST + " TEXT, " +
                DBSchema.OrderHistory.Cols.DATETIME + " TEXT)");

        db.execSQL("CREATE TABLE " + DBSchema.FoodOrder.TABLE_NAME + "(" +
                DBSchema.FoodOrder.Cols.EMAIL + " TEXT, " +
                DBSchema.FoodOrder.Cols.TIME + " TEXT, " +
                DBSchema.FoodOrder.Cols.FOODNAME + " TEXT, " +
                DBSchema.FoodOrder.Cols.FOODIMAGE + " TEXT," +
                DBSchema.FoodOrder.Cols.FOODPRICE + " TEXT, " +
                DBSchema.FoodOrder.Cols.FOODQUANT + " TEXT)");

        updateRestaurants(db);
        updateFoodItemTable(db);
        updateUserTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.Restaurant.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.FoodItem.TABLE_NAME);

        onCreate(db);
    }

    /*** SETTERS ***/

    // Adds Restaurant to Database
    private void addRestaurant_DB(SQLiteDatabase db, String restName, int restImage) {
        ContentValues cv = new ContentValues();
        cv.put(DBSchema.Restaurant.Cols.REST_NAME, restName);
        cv.put(DBSchema.Restaurant.Cols.REST_IMAGE, restImage);
        try {
            db.insertOrThrow(DBSchema.Restaurant.TABLE_NAME, null, cv);
        } catch (SQLException e) { }
    }

    // Adds Food to Database
    private void addFood_DB(SQLiteDatabase db, String foodName, int foodImage, double price, String restName) {
        ContentValues cv = new ContentValues();
        cv.put(DBSchema.FoodItem.Cols.FOODNAME, foodName);
        cv.put(DBSchema.FoodItem.Cols.FOODIMAGE, foodImage);
        cv.put(DBSchema.FoodItem.Cols.FOODPRICE, price);
        cv.put(DBSchema.FoodItem.Cols.RESTAURANT, restName);
        try
        {
            db.insertOrThrow(DBSchema.FoodItem.TABLE_NAME, null, cv);
        } catch (SQLException e){}

    }

    public void addOrderHistory_DB(String email, String restName, int itemCount, double totalCost, String time, LinkedList<Food> foodList)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DBSchema.OrderHistory.Cols.EMAIL, email);
        cv.put(DBSchema.OrderHistory.Cols.RESTAURANT, restName);
        cv.put(DBSchema.OrderHistory.Cols.ITEMCOUNT, itemCount);
        cv.put(DBSchema.OrderHistory.Cols.TOTALCOST, totalCost);
        cv.put(DBSchema.OrderHistory.Cols.DATETIME, time);

        try
        {
            db.insertOrThrow(DBSchema.OrderHistory.TABLE_NAME, null, cv);
        } catch (SQLException e){}

        addFoodOrder_DB(db, email, time, foodList);
    }

    private void addFoodOrder_DB(SQLiteDatabase db, String email, String time, LinkedList<Food> foodList)
    {
        ContentValues cv;
        for(Food f: foodList)
        {
            cv = new ContentValues();
            cv.put(DBSchema.FoodOrder.Cols.EMAIL, email);
            cv.put(DBSchema.FoodOrder.Cols.TIME, time);
            cv.put(DBSchema.FoodOrder.Cols.FOODNAME, f.getFoodName());
            cv.put(DBSchema.FoodOrder.Cols.FOODIMAGE, f.getImage());
            cv.put(DBSchema.FoodOrder.Cols.FOODPRICE, f.getPrice());
            cv.put(DBSchema.FoodOrder.Cols.FOODQUANT, f.getQuantity());

            try
            {
                db.insertOrThrow(DBSchema.FoodOrder.TABLE_NAME, null, cv);
            } catch (SQLException e){}
        }
    }

    public void addUser_DB(String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DBSchema.UserTable.Cols.EMAIL, email);
        cv.put(DBSchema.UserTable.Cols.PASSWORD, password);

        try
        {
            db.insertOrThrow(DBSchema.UserTable.TABLE_NAME, null, cv);
        } catch (SQLException e){}
    }


    /*** GETTERS ***/

    // Check if user already exists
    public boolean checkUserExists(String email)
    {
        LinkedList<User> userList = getUserList();

        for(User u: userList)
        {
            if(u.getEmail().equals(email)) {return true;};
        }

        return false;
    }

    // Check if user details match an entry (for log in)
    public boolean validateUser(User user)
    {
        LinkedList<User> userList = getUserList();

        for(User u: userList)
        {
            if(u.getEmail().equals(user.getEmail()) &&
                    u.getPassword().equals(user.getPassword())) {return true;};
        }

        return false;
    }

    // Returns Restaurant Object using restaurant name
    public Restaurant getRestaurantByName(String restName)
    {
        Restaurant restaurant = null;
        String[] where = {restName};

        // Gains access to Database and Initialises Cursor (now with conditions)
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.Restaurant.TABLE_NAME,
                null,
                DBSchema.Restaurant.Cols.REST_NAME + " = ?",
                where,
                null, null, null);

        // Move through with cursor and add row to foodList as food Type (that meets condition)
        try
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast() && restaurant == null)
            {
                restaurant = (new Restaurant(cursor.getString(0), cursor.getInt(1)));
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }

        return restaurant;
    }

    // Returns all Restaurant entries as list
    public LinkedList<Restaurant> getRestaurantList()
    {
        LinkedList<Restaurant> restaurantList = new LinkedList<Restaurant>();

        // Gains access to Database and Initialises Cursor
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.Restaurant.TABLE_NAME, null, null, null, null, null, null);

        // Move through with cursor and add row to restaurantList as Restaurant Type
        try
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                restaurantList.add(new Restaurant(cursor.getString(0), cursor.getInt(1)));
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }

        return restaurantList;
    }

    // Returns all Food entries as list
    public LinkedList<Food> getFoodList(){
        LinkedList<Food> foodList = new LinkedList<Food>();

        // Gains access to Database and Initialises Cursor
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.FoodItem.TABLE_NAME, null, null, null, null, null, null);

        // Move through with cursor and add row to foodList as food Type
        try
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                foodList.add(new Food(cursor.getString(0), cursor.getInt(1), cursor.getDouble(2), cursor.getString(3)));
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }

        return foodList;
    }

    //Returns all Food entries that belong to a specific restaurant as list
    public LinkedList<Food> getRestFoodList(String restName){
        LinkedList<Food> foodList = new LinkedList<Food>();
        String[] where = {restName};

        // Gains access to Database and Initialises Cursor (now with conditions)
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.FoodItem.TABLE_NAME,
                null,
                DBSchema.FoodItem.Cols.RESTAURANT + " = ?",
                where,
                null, null, null);

        // Move through with cursor and add row to foodList as food Type (that meets condition)
        try
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                foodList.add(new Food(cursor.getString(0), cursor.getInt(1), cursor.getDouble(2), cursor.getString(3)));
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }

        return foodList;
    }


    public LinkedList<Order> getOrderHistoryList()
    {
        LinkedList<Order> orderList = new LinkedList<Order>();

        // Gains access to Database and Initialises Cursor
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.OrderHistory.TABLE_NAME, null, null, null, null, null, null);

        try
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                Order order = new Order(cursor.getString(0),cursor.getString(1));
                LinkedList<Food> foodList = (getFoodOrderList(order.getEmail(), cursor.getString(4), order.getRestName()));
                order.setFoodList(foodList);
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }

        return orderList;
    }

    private LinkedList<Food> getFoodOrderList(String email, String time, String restName)
    {
        LinkedList<Food> foodList = new LinkedList<Food>();
        String[] where = {email,time};

        // Gains access to Database and Initialises Cursor
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.FoodOrder.TABLE_NAME,
                null,
                DBSchema.FoodOrder.Cols.EMAIL + " = ? AND " + DBSchema.FoodOrder.Cols.TIME + " = ?", where, null, null, null);

        try
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                Food food = new Food(cursor.getString(0),cursor.getInt(1), cursor.getDouble(2), restName);
                food.setQuantity(cursor.getInt(3));
                foodList.add(food);
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }

        return foodList;
    }

    private LinkedList<User> getUserList()
    {
        LinkedList<User> userList = new LinkedList<User>();

        // Gains access to Database and Initialises Cursor
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.UserTable.TABLE_NAME, null, null, null, null, null, null);

        try
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                userList.add(new User(cursor.getString(0), cursor.getString(1)));
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }

        return userList;
    }

    /*** UPDATER METHODS ***/
    // Adding entries prior to use of Database
    private void updateRestaurants(SQLiteDatabase db)
    {
        /* Format: db, Restaurant Name, Restaurant Image */

        addRestaurant_DB(db,"McDonalds", android.R.color.black);
        addRestaurant_DB(db,"Terrace Cafe", android.R.color.black);
        addRestaurant_DB(db,"KFC", android.R.color.black);
        addRestaurant_DB(db,"MCD", android.R.color.black);
        addRestaurant_DB(db,"DQ", android.R.color.black);
        addRestaurant_DB(db,"CHT", android.R.color.black);
        addRestaurant_DB(db,"SUB", android.R.color.black);
        addRestaurant_DB(db,"YOT", android.R.color.black);
        addRestaurant_DB(db,"MAL", android.R.color.black);
        addRestaurant_DB(db,"GIN", android.R.color.black);
        addRestaurant_DB(db,"HJ", android.R.color.black);
        addRestaurant_DB(db,"ZAM", android.R.color.black);
        addRestaurant_DB(db,"DEF", android.R.color.black);
        addRestaurant_DB(db,"GHI", android.R.color.black);
        addRestaurant_DB(db,"PZH", android.R.color.black);
        addRestaurant_DB(db,"DOM", android.R.color.black);
    }

    private void updateFoodItemTable(SQLiteDatabase db)
    {
        /* Format: db, Food Name, Food Image, Food Price, Restaurant Name) */

        addFood_DB(db,"Quarter-Pounder", android.R.color.black, 8.50 ,"McDonalds");
        addFood_DB(db,"Fillet-o-fish", android.R.color.black, 4.50 ,"McDonalds");
        addFood_DB(db,"Big Mac", android.R.color.black, 9.50 ,"McDonalds");
        addFood_DB(db,"Mc Chicken", android.R.color.black, 7.50 ,"McDonalds");
        addFood_DB(db,"McMuffin", android.R.color.black, 5.00 ,"McDonalds");
        addFood_DB(db,"Hashbrown", android.R.color.black, 2.00 ,"McDonalds");
        addFood_DB(db,"Nuggets 4pc", android.R.color.black, 3.50 ,"McDonalds");
        addFood_DB(db,"Nuggets 12pc", android.R.color.black, 10.00 ,"McDonalds");
        addFood_DB(db,"Nuggets 24pc", android.R.color.black, 18.75 ,"McDonalds");
        addFood_DB(db,"Large Fries", android.R.color.black, 5.50 ,"McDonalds");
        addFood_DB(db,"Large Coke", android.R.color.black, 3.50 ,"McDonalds");

        addFood_DB(db,"Water", android.R.color.black, 2.50 ,"Terrace Cafe");
        addFood_DB(db,"Sparkling Water", android.R.color.black, 25.00 ,"Terrace Cafe");
        addFood_DB(db,"Tea", android.R.color.black, 3.00 ,"Terrace Cafe");
        addFood_DB(db,"Espresso", android.R.color.black, 3.20 ,"Terrace Cafe");
        addFood_DB(db,"Latte", android.R.color.black, 4.50 ,"Terrace Cafe");
        addFood_DB(db,"Ice Latte", android.R.color.black, 7.50 ,"Terrace Cafe");
        addFood_DB(db,"Brownie", android.R.color.black, 4.00 ,"Terrace Cafe");
        addFood_DB(db,"Banana Bread", android.R.color.black, 4.50 ,"Terrace Cafe");
        addFood_DB(db,"Carrot Cake", android.R.color.black, 6.00 ,"Terrace Cafe");
        addFood_DB(db,"Chocolate Cake", android.R.color.black, 9.50 ,"Terrace Cafe");
        addFood_DB(db,"Double Choc Muffin", android.R.color.black, 6.50 ,"Terrace Cafe");
        addFood_DB(db,"Salmon Bagel", android.R.color.black, 16.50 ,"Terrace Cafe");
        addFood_DB(db,"Cheese Toastie", android.R.color.black, 12.50 ,"Terrace Cafe");
        addFood_DB(db,"Steak Sandwich", android.R.color.black, 22.50 ,"Terrace Cafe");
        addFood_DB(db,"Bacon-Egg Wrap", android.R.color.black, 20.00 ,"Terrace Cafe");

        addFood_DB(db,"4-Piece Chicken", android.R.color.black, 5.50 ,"KFC");
        addFood_DB(db,"8-Piece Chicken", android.R.color.black, 12.50 ,"KFC");
        addFood_DB(db,"4-Piece Spicy Chicken", android.R.color.black, 7.50 ,"KFC");
        addFood_DB(db,"8-Piece Spicy Chicken", android.R.color.black, 14.50 ,"KFC");
        addFood_DB(db,"Mash Potatoes", android.R.color.black, 5.00 ,"KFC");
    }

    private void updateUserTable(SQLiteDatabase db)
    {
        addUser_DB("User1@gmail.com","User1");
        addUser_DB("User2@gmail.com","User2");
    }

    private void updateOrderHistoryTable(SQLiteDatabase db)
    {
        Order order;

        /*** FORMAT:
         * Order order = new Order(*PUT EMAIL HERE*, *PUT RESTAURANT NAME HERE*);
         * order.setFoodList(getRandFood(order.getRestName(), *PUT TOTAL QUANTITY HERE*));
         * DO NOT MAKE CHANGES TO THE LAST LINE
         */

        order = new Order("User1@gmail.com","ABC");
        order.setFoodList(getRandFood(order.getRestName(), 12));
        addOrderHistory_DB(order.getEmail(), order.getRestName(), order.getItemCount(),
                order.getTotalCost(), order.getDateTime(), order.getFoodList());

        order = new Order("User1@gmail.com","KFC");
        order.setFoodList(getRandFood(order.getRestName(), 8));
        addOrderHistory_DB(order.getEmail(), order.getRestName(), order.getItemCount(),
                order.getTotalCost(), order.getDateTime(), order.getFoodList());

        order = new Order("User2@gmail.com","ABC");
        order.setFoodList(getRandFood(order.getRestName(), 10));
        addOrderHistory_DB(order.getEmail(), order.getRestName(), order.getItemCount(),
                order.getTotalCost(), order.getDateTime(), order.getFoodList());

        order = new Order("User2@gmail.com","KFC");
        order.setFoodList(getRandFood(order.getRestName(), 6));
        addOrderHistory_DB(order.getEmail(), order.getRestName(), order.getItemCount(),
                order.getTotalCost(), order.getDateTime(), order.getFoodList());

        order = new Order("User2@gmail.com","McDonalds");
        order.setFoodList(getRandFood(order.getRestName(), 1));
        addOrderHistory_DB(order.getEmail(), order.getRestName(), order.getItemCount(),
                order.getTotalCost(), order.getDateTime(), order.getFoodList());

    }


    // Helper for generating random order's for updateOrderHistoryTable()
    private LinkedList<Food> getRandFood(String restName, int itemCount)
    {
        LinkedList<Food> foodList = new LinkedList<>();
        LinkedList<Food> randFoodList = new LinkedList<>();
        Random rng = new Random();

        while(itemCount > 0 || randFoodList.size() == foodList.size()) {
            Food f = foodList.get(rng.nextInt(foodList.size()));

            if (!randFoodList.contains(f)) {
                while (f.getQuantity() <= 0) {
                    f.setQuantity(rng.nextInt(itemCount));
                }

                itemCount -= f.getQuantity();
                randFoodList.add(f);

            }
        }

        return randFoodList;
    }
}

