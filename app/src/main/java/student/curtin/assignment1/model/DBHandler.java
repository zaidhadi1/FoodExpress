package student.curtin.assignment1.model;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;

import student.curtin.assignment1.R;

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

        db.execSQL("CREATE TABLE " + DBSchema.UserTable.TABLE_NAME + "(" +
                DBSchema.UserTable.Cols.EMAIL + " TEXT, " +
                DBSchema.UserTable.Cols.PASSWORD + " TEXT)");

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

        db.insert(DBSchema.Restaurant.TABLE_NAME, null, cv);
    }

    // Adds Food to Database
    private void addFood_DB(SQLiteDatabase db, String foodName, int foodImage, double price, String restName) {
        ContentValues cv = new ContentValues();
        cv.put(DBSchema.FoodItem.Cols.FOODNAME, foodName);
        cv.put(DBSchema.FoodItem.Cols.FOODIMAGE, foodImage);
        cv.put(DBSchema.FoodItem.Cols.FOODPRICE, price);
        cv.put(DBSchema.FoodItem.Cols.RESTAURANT, restName);

        db.insert(DBSchema.FoodItem.TABLE_NAME, null, cv);

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

        db.insert(DBSchema.OrderHistory.TABLE_NAME, null, cv);
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

            db.insert(DBSchema.FoodOrder.TABLE_NAME, null, cv);
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

    private void addUser_DB(SQLiteDatabase db, String email, String password)
    {
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


    public LinkedList<Order> getOrderHistoryList(String email)
    {
        String[] where = {email};
        LinkedList<Order> orderList = new LinkedList<Order>();

        // Gains access to Database and Initialises Cursor
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.OrderHistory.TABLE_NAME, null,
                DBSchema.OrderHistory.Cols.EMAIL + " = ?", where, null, null, null);

        try
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                Order order = new Order(cursor.getString(0),cursor.getString(1));
                LinkedList<Food> foodList = (getFoodOrderList(order.getEmail(), cursor.getString(4), order.getRestName()));
                order.setFoodList(foodList);
                order.setDateTime(cursor.getString(4));
                orderList.add(order);
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }

        return orderList;
    }

    public LinkedList<Food> getFoodOrderList(String email, String time, String restName)
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
                Food food = new Food(cursor.getString(2),cursor.getInt(3), cursor.getDouble(4), restName);
                food.setQuantity(cursor.getInt(5));
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

        addRestaurant_DB(db,"McDonalds", R.mipmap.mcdonalds);
        addRestaurant_DB(db,"Terrace Cafe", R.mipmap.terracecafe);
        addRestaurant_DB(db,"Zambrero", R.mipmap.zambreros);
        addRestaurant_DB(db,"Dominos", R.mipmap.dominos);
        addRestaurant_DB(db,"Subway", R.mipmap.subway);
        addRestaurant_DB(db,"Nandos", R.mipmap.nandos);
        addRestaurant_DB(db,"Gelare", R.mipmap.gelare);
        addRestaurant_DB(db,"Grilld", R.mipmap.grilld); /**TODO**/
        addRestaurant_DB(db,"Kebabalicous", R.mipmap.kebabalicous); /**TODO*/
        addRestaurant_DB(db,"Sushi Sushi", R.mipmap.sushisushi);
        addRestaurant_DB(db,"Hungry Jacks", R.mipmap.hungryjacks);
        addRestaurant_DB(db,"Tony's Italian", R.mipmap.tonysitalian);
        addRestaurant_DB(db,"KFC", R.mipmap.kfc);
        addRestaurant_DB(db,"Chicken Treat", R.mipmap.chickentreat);
        addRestaurant_DB(db,"Tao Cafe", R.mipmap.taocafe);
        addRestaurant_DB(db,"Spice Market", R.mipmap.spicemarket);
    }

    private void updateFoodItemTable(SQLiteDatabase db)
    {
        /* Format: db, Food Name, Food Image, Food Price, Restaurant Name) */

        addFood_DB(db,"Quarter-Pounder", R.mipmap.quarterpounder, 8.50 ,"McDonalds");
        addFood_DB(db,"Cheeseburger", R.mipmap.cheeseburger, 8.50 ,"McDonalds");
        addFood_DB(db,"Classic Angus", R.mipmap.classicangus, 8.50 ,"McDonalds");
        addFood_DB(db,"Fillet-o-fish", R.mipmap.filletofish, 4.50 ,"McDonalds");
        addFood_DB(db,"Big Mac", R.mipmap.bigmac, 9.50 ,"McDonalds");
        addFood_DB(db,"Mc Chicken", R.mipmap.mcchicken, 7.50 ,"McDonalds");
        addFood_DB(db,"Hashbrown", R.mipmap.hashbrown, 2.00 ,"McDonalds");
        addFood_DB(db,"Water", R.mipmap.water, 2.50 ,"McDonalds");
        addFood_DB(db,"Sparkling Water", R.mipmap.sparklingwater, 25.00 ,"McDonalds");
        addFood_DB(db,"Coke", R.mipmap.coke, 2.50 ,"McDonalds");
        addFood_DB(db,"Coke No Sugar", R.mipmap.cokenosugar, 25.00 ,"McDonalds");
        addFood_DB(db,"Sprite", R.mipmap.sprite, 2.50 ,"McDonalds");
        addFood_DB(db,"Fanta", R.mipmap.fanta, 25.00 ,"McDonalds");



        addFood_DB(db,"Brownie", R.mipmap.brownie, 4.00 ,"Terrace Cafe");
        addFood_DB(db,"Banana Bread", R.mipmap.bananabread, 4.50 ,"Terrace Cafe");
        addFood_DB(db,"Carrot Cake", R.mipmap.carrotcake, 6.00 ,"Terrace Cafe");
        addFood_DB(db,"Chocolate Cake", R.mipmap.chocolatecake, 9.50 ,"Terrace Cafe");
        addFood_DB(db,"Water ", R.mipmap.water, 2.50 ,"Terrace Cafe");
        addFood_DB(db,"Sparkling Water ", R.mipmap.sparklingwater, 25.00 ,"Terrace Cafe");
        addFood_DB(db,"Coke", R.mipmap.coke, 2.50 ,"Terrace Cafe");
        addFood_DB(db,"Coke No Sugar ", R.mipmap.cokenosugar, 25.00 ,"Terrace Cafe");
        addFood_DB(db,"Sprite ", R.mipmap.sprite, 2.50 ,"Terrace Cafe");
        addFood_DB(db,"Fanta ", R.mipmap.fanta, 25.00 ,"Terrace Cafe");

        addFood_DB(db,"Chicken Burrito", R.mipmap.burrito, 2.50 ,"Zambrero");
        addFood_DB(db,"Chicken Nachos", R.mipmap.nachos, 2.50 ,"Zambrero");
        addFood_DB(db,"Soft Taco", R.mipmap.softtaco, 2.50 ,"Zambrero");
        addFood_DB(db,"Hard Taco", R.mipmap.hardtaco, 2.50 ,"Zambrero");
        addFood_DB(db,"Quesadilla", R.mipmap.quesadillas, 2.50 ,"Zambrero");
        addFood_DB(db,"Water  ", R.mipmap.water, 2.50 ,"Zambrero");
        addFood_DB(db,"Sparkling Water"  , R.mipmap.sparklingwater, 25.00 ,"Zambrero");
        addFood_DB(db,"Coke  ", R.mipmap.coke, 2.50 ,"Zambrero");
        addFood_DB(db,"Coke No Sugar  ", R.mipmap.cokenosugar, 25.00 ,"Zambrero");
        addFood_DB(db,"Sprite  ", R.mipmap.sprite, 2.50 ,"Zambrero");
        addFood_DB(db,"Fanta  ", R.mipmap.fanta, 25.00 ,"Zambrero");

        addFood_DB(db,"Vegetarian", R.mipmap.vegetarian, 2.50 ,"Dominos");
        addFood_DB(db,"Pepperoni", R.mipmap.pepperoni, 2.50 ,"Dominos");
        addFood_DB(db,"Meat Lovers", R.mipmap.meatlovers, 2.50 ,"Dominos");
        addFood_DB(db,"Hawaiian", R.mipmap.hawaiianc, 2.50 ,"Dominos");
        addFood_DB(db,"Margarita", R.mipmap.margarita, 2.50 ,"Dominos");
        addFood_DB(db,"Water    ", R.mipmap.water, 2.50 ,"Dominos");
        addFood_DB(db,"Sparkling Water    ", R.mipmap.sparklingwater, 25.00 ,"Dominos");
        addFood_DB(db,"Coke    ", R.mipmap.coke, 2.50 ,"Dominos");
        addFood_DB(db,"Coke No Sugar    ", R.mipmap.cokenosugar, 25.00 ,"Dominos");
        addFood_DB(db,"Sprite    ", R.mipmap.sprite, 2.50 ,"Dominos");
        addFood_DB(db,"Fanta    ", R.mipmap.fanta, 25.00 ,"Dominos");

        addFood_DB(db,"BLT", R.mipmap.blt, 2.50 ,"Subway");
        addFood_DB(db,"Buffalo Chicken", R.mipmap.buffalochicken, 2.50 ,"Subway");
        addFood_DB(db,"Chicken Teriyaki", R.mipmap.chickenteriyaki, 2.50 ,"Subway");
        addFood_DB(db,"Turkey", R.mipmap.turkey, 2.50 ,"Subway");
        addFood_DB(db,"Water     ", R.mipmap.water, 2.50 ,"Subway");
        addFood_DB(db,"Sparkling Water     ", R.mipmap.sparklingwater, 25.00 ,"Subway");
        addFood_DB(db,"Coke     ", R.mipmap.coke, 2.50 ,"Subway");
        addFood_DB(db,"Coke No Sugar     ", R.mipmap.cokenosugar, 25.00 ,"Subway");
        addFood_DB(db,"Sprite     ", R.mipmap.sprite, 2.50 ,"Subway");
        addFood_DB(db,"Fanta     ", R.mipmap.fanta, 25.00 ,"Subway");

        addFood_DB(db,"Classic Pita", R.mipmap.classicpita, 2.50 ,"Nandos");
        addFood_DB(db,"Classic Wrap", R.mipmap.classicwrap, 2.50 ,"Nandos");
        addFood_DB(db,"Grilled tenders", R.mipmap.tenders, 2.50 ,"Nandos");
        addFood_DB(db,"Whole Chicken", R.mipmap.wholechicken, 2.50 ,"Nandos");
        addFood_DB(db,"Water      ", R.mipmap.water, 2.50 ,"Nandos");
        addFood_DB(db,"Sparkling Water      ", R.mipmap.sparklingwater, 25.00 ,"Nandos");
        addFood_DB(db,"Coke      ", R.mipmap.coke, 2.50 ,"Nandos");
        addFood_DB(db,"Coke No Sugar      ", R.mipmap.cokenosugar, 25.00 ,"Nandos");
        addFood_DB(db,"Sprite      ", R.mipmap.sprite, 2.50 ,"Nandos");
        addFood_DB(db,"Fanta      ", R.mipmap.fanta, 25.00 ,"Nandos");

        addFood_DB(db,"Chocolate", R.mipmap.chocchipicecream, 2.50 ,"Gelare");
        addFood_DB(db,"Vanilla", R.mipmap.vanillaicecream, 2.50 ,"Gelare");
        addFood_DB(db,"Strawberry", R.mipmap.strawberryicecream, 2.50 ,"Gelare");
        addFood_DB(db,"Choc Chip", R.mipmap.chocchipicecream, 2.50 ,"Gelare");
        addFood_DB(db,"Water       ", R.mipmap.water, 2.50 ,"Gelare");
        addFood_DB(db,"Sparkling Water       ", R.mipmap.sparklingwater, 25.00 ,"Gelare");
        addFood_DB(db,"Coke       ", R.mipmap.coke, 2.50 ,"Gelare");
        addFood_DB(db,"Coke No Sugar       ", R.mipmap.cokenosugar, 25.00 ,"Gelare");
        addFood_DB(db,"Sprite       ", R.mipmap.sprite, 2.50 ,"Gelare");
        addFood_DB(db,"Fanta       ", R.mipmap.fanta, 25.00 ,"Gelare");

        addFood_DB(db,"Simply Grilld", R.mipmap.simplygrilld, 2.50 ,"Grilld");
        addFood_DB(db,"Simon Says", R.mipmap.simonsays, 2.50 ,"Grilld");
        addFood_DB(db,"Summer Sunset", R.mipmap.summersunset, 2.50 ,"Grilld");
        addFood_DB(db,"Crispy Bacon & Cheese", R.mipmap.crispybacon, 2.50 ,"Grilld");
        addFood_DB(db,"Water        ", R.mipmap.water, 2.50 ,"Grilld");
        addFood_DB(db,"Sparkling Water        ", R.mipmap.sparklingwater, 25.00 ,"Grilld");
        addFood_DB(db,"Coke        ", R.mipmap.coke, 2.50 ,"Grilld");
        addFood_DB(db,"Coke No Sugar        ", R.mipmap.cokenosugar, 25.00 ,"Grilld");
        addFood_DB(db,"Sprite        ", R.mipmap.sprite, 2.50 ,"Grilld");
        addFood_DB(db,"Fanta        ", R.mipmap.fanta, 25.00 ,"Grilld");

        addFood_DB(db,"Chicken Kebab", R.mipmap.chickenkebab, 2.50 ,"Kebabalicous");
        addFood_DB(db,"Lamb Kebab", R.mipmap.lambkebab, 2.50 ,"Kebabalicous");
        addFood_DB(db,"Beef Kebab", R.mipmap.beefkebab, 2.50 ,"Kebabalicous");
        addFood_DB(db,"Falafel Kebab", R.mipmap.falafelkebab, 2.50 ,"Kebabalicous");
        addFood_DB(db,"Chips", R.mipmap.chips, 2.50 ,"Kebabalicous");
        addFood_DB(db,"Water         ", R.mipmap.water, 2.50 ,"Kebabalicous");
        addFood_DB(db,"Sparkling Water         ", R.mipmap.sparklingwater, 25.00 ,"Kebabalicous");
        addFood_DB(db,"Coke         ", R.mipmap.coke, 2.50 ,"Kebabalicous");
        addFood_DB(db,"Coke No Sugar         ", R.mipmap.cokenosugar, 25.00 ,"Kebabalicous");
        addFood_DB(db,"Sprite         ", R.mipmap.sprite, 2.50 ,"Kebabalicous");
        addFood_DB(db,"Fanta         ", R.mipmap.fanta, 25.00 ,"Kebabalicous");

        addFood_DB(db,"Chicken Teriyaki Sushi", R.mipmap.chickensushi, 2.50 ,"Sushi Sushi");
        addFood_DB(db,"Salmon Sushi", R.mipmap.salmonsushi, 2.50 ,"Sushi Sushi");
        addFood_DB(db,"Avocado Rolls", R.mipmap.avorolls, 2.50 ,"Sushi Sushi");
        addFood_DB(db,"California Rolls", R.mipmap.californiarolls, 2.50 ,"Sushi Sushi");
        addFood_DB(db,"Water          ", R.mipmap.water, 2.50 ,"Sushi Sushi");
        addFood_DB(db,"Sparkling Water          ", R.mipmap.sparklingwater, 25.00 ,"Sushi Sushi");
        addFood_DB(db,"Coke          ", R.mipmap.coke, 2.50 ,"Sushi Sushi");
        addFood_DB(db,"Coke No Sugar          ", R.mipmap.cokenosugar, 25.00 ,"Sushi Sushi");
        addFood_DB(db,"Sprite          ", R.mipmap.sprite, 2.50 ,"Sushi Sushi");
        addFood_DB(db,"Fanta          ", R.mipmap.fanta, 25.00 ,"Sushi Sushi");

        addFood_DB(db,"Whopper", R.mipmap.whopper, 2.50 ,"Hungry Jacks");
        addFood_DB(db,"Memphis Grilled Chicken", R.mipmap.grilledchicken, 2.50 ,"Hungry Jacks");
        addFood_DB(db,"Pop'n Chicken", R.mipmap.popnchicken, 2.50 ,"Hungry Jacks");
        addFood_DB(db,"Double Whopper", R.mipmap.doublewhopper, 2.50 ,"Hungry Jacks");
        addFood_DB(db,"Water           ", R.mipmap.water, 2.50 ,"Hungry Jacks");
        addFood_DB(db,"Sparkling Water           ", R.mipmap.sparklingwater, 25.00 ,"Hungry Jacks");
        addFood_DB(db,"Coke           ", R.mipmap.coke, 2.50 ,"Hungry Jacks");
        addFood_DB(db,"Coke No Sugar           ", R.mipmap.cokenosugar, 25.00 ,"Hungry Jacks");
        addFood_DB(db,"Sprite           ", R.mipmap.sprite, 2.50 ,"Hungry Jacks");
        addFood_DB(db,"Fanta           ",R.mipmap.fanta, 25.00 ,"Hungry Jacks");

        addFood_DB(db,"Spaghetti Bolognese", R.mipmap.spagbol, 2.50 ,"Tony's Italian");
        addFood_DB(db,"Lasagne", R.mipmap.lasagne, 2.50 ,"Tony's Italian");
        addFood_DB(db,"Gnocci", R.mipmap.gnocchi, 2.50 ,"Tony's Italian");
        addFood_DB(db,"Spaghetti Carbonara", R.mipmap.spagcarb, 2.50 ,"Tony's Italian");
        addFood_DB(db,"Garlic Bread", R.mipmap.garlicbread, 2.50 ,"Tony's Italian");
        addFood_DB(db,"Water            ", R.mipmap.water, 2.50 ,"Tony's Italian");
        addFood_DB(db,"Sparkling Water            ", R.mipmap.sparklingwater, 25.00 ,"Tony's Italian");
        addFood_DB(db,"Coke            ", R.mipmap.coke, 2.50 ,"Tony's Italian");
        addFood_DB(db,"Coke No Sugar            ", R.mipmap.cokenosugar, 25.00 ,"Tony's Italian");
        addFood_DB(db,"Sprite            ",R.mipmap.sprite, 2.50 ,"Tony's Italian");
        addFood_DB(db,"Fanta            ", R.mipmap.fanta, 25.00 ,"Tony's Italian");

        addFood_DB(db,"Zinger Wrap", R.mipmap.zingerwrap, 2.50 ,"KFC");
        addFood_DB(db,"Grilled Chicken ", R.mipmap.grilledchicken, 2.50 ,"KFC");
        addFood_DB(db,"Zinger Burger", R.mipmap.zingerburger, 2.50 ,"KFC");
        addFood_DB(db,"Double Tender Burger", R.mipmap.doubletender, 2.50 ,"KFC");
        addFood_DB(db,"Water             ", R.mipmap.water, 2.50 ,"KFC");
        addFood_DB(db,"Sparkling Water             ", R.mipmap.sparklingwater, 25.00 ,"KFC");
        addFood_DB(db,"Coke             ", R.mipmap.coke, 2.50 ,"KFC");
        addFood_DB(db,"Coke No Sugar             ", R.mipmap.cokenosugar, 25.00 ,"KFC");
        addFood_DB(db,"Sprite             ", R.mipmap.sprite, 2.50 ,"KFC");
        addFood_DB(db,"Fanta             ", R.mipmap.fanta, 25.00 ,"KFC");

        addFood_DB(db,"Quarter Chicken", R.mipmap.quarterchick, 2.50 ,"Chicken Treat");
        addFood_DB(db,"Half Chicken", R.mipmap.halfchick, 2.50 ,"Chicken Treat");
        addFood_DB(db,"Half Hawaiian", R.mipmap.hawaiianc, 2.50 ,"Chicken Treat");
        addFood_DB(db,"Family Classic", R.mipmap.familyclassic, 2.50 ,"Chicken Treat");
        addFood_DB(db,"Chips ", R.mipmap.chips, 2.50 ,"Chicken Treat");
        addFood_DB(db,"Water              ", R.mipmap.water, 2.50 ,"Chicken Treat");
        addFood_DB(db,"Sparkling Water              ", R.mipmap.sparklingwater, 25.00 ,"Chicken Treat");
        addFood_DB(db,"Coke              ", R.mipmap.coke, 2.50 ,"Chicken Treat");
        addFood_DB(db,"Coke No Sugar              ", R.mipmap.cokenosugar, 25.00 ,"Chicken Treat");
        addFood_DB(db,"Sprite              ", R.mipmap.sprite, 2.50 ,"Chicken Treat");
        addFood_DB(db,"Fanta              ", R.mipmap.fanta, 25.00 ,"Chicken Treat");

        addFood_DB(db,"Laksa Soup", R.mipmap.laksa, 2.50 ,"Tao Cafe");
        addFood_DB(db,"Tom Yum Soup", R.mipmap.tomyum, 2.50 ,"Tao Cafe");
        addFood_DB(db,"Pad Thai Noodles", R.mipmap.padthai, 2.50 ,"Tao Cafe");
        addFood_DB(db,"Thai Red Curry", R.mipmap.thaired, 2.50 ,"Tao Cafe");
        addFood_DB(db,"Water               ", R.mipmap.water, 2.50 ,"Tao Cafe");
        addFood_DB(db,"Sparkling Water               ", R.mipmap.sparklingwater, 25.00 ,"Tao Cafe");
        addFood_DB(db,"Coke               ", R.mipmap.coke, 2.50 ,"Tao Cafe");
        addFood_DB(db,"Coke No Sugar               ", R.mipmap.cokenosugar, 25.00 ,"Tao Cafe");
        addFood_DB(db,"Sprite               ", R.mipmap.sprite, 2.50 ,"Tao Cafe");
        addFood_DB(db,"Fanta               ", R.mipmap.fanta, 25.00 ,"Tao Cafe");

        addFood_DB(db,"Chicken Basil Noodle", R.mipmap.chillibasil, 2.50 ,"Spice Market");
        addFood_DB(db,"Satay Noodle", R.mipmap.sataynoodle, 2.50 ,"Spice Market");
        addFood_DB(db,"Chicken Fried Rice", R.mipmap.chickenfriedrice, 2.50 ,"Spice Market");
        addFood_DB(db,"Sweet and Sour Pork", R.mipmap.sweetsourpork, 2.50 ,"Spice Market");
        addFood_DB(db,"Water                ", R.mipmap.water, 2.50 ,"Spice Market");
        addFood_DB(db,"Sparkling Water                ", R.mipmap.sparklingwater, 25.00 ,"Spice Market");
        addFood_DB(db,"Coke                ", R.mipmap.coke, 2.50 ,"Spice Market");
        addFood_DB(db,"Coke No Sugar                ", R.mipmap.cokenosugar, 25.00 ,"Spice Market");
        addFood_DB(db,"Sprite                ", R.mipmap.sprite, 2.50 ,"Spice Market");
        addFood_DB(db,"Fanta                ", R.mipmap.fanta, 25.00 ,"Spice Market");

    }

    private void updateUserTable(SQLiteDatabase db)
    {
        addUser_DB(db,"User1@gmail.com","User1");
        addUser_DB(db,"User2@gmail.com","User2");
    }

//    private void updateOrderHistoryTable(SQLiteDatabase db)
//    {
//        Order order;
//
//        /*** FORMAT:
//         * Order order = new Order(*PUT EMAIL HERE*, *PUT RESTAURANT NAME HERE*);
//         * order.setFoodList(getRandFood(order.getRestName(), *PUT TOTAL QUANTITY HERE*));
//         * DO NOT MAKE CHANGES TO THE LAST LINE
//         */
//
//        order = new Order("User1@gmail.com","ABC");
//        order.setFoodList(getRandFood(order.getRestName(), 12));
//        addOrderHistory_DB(order.getEmail(), order.getRestName(), order.getItemCount(),
//                order.getTotalCost(), order.getDateTime(), order.getFoodList());
//
//        order = new Order("User1@gmail.com","KFC");
//        order.setFoodList(getRandFood(order.getRestName(), 8));
//        addOrderHistory_DB(order.getEmail(), order.getRestName(), order.getItemCount(),
//                order.getTotalCost(), order.getDateTime(), order.getFoodList());
//
//        order = new Order("User2@gmail.com","ABC");
//        order.setFoodList(getRandFood(order.getRestName(), 10));
//        addOrderHistory_DB(order.getEmail(), order.getRestName(), order.getItemCount(),
//                order.getTotalCost(), order.getDateTime(), order.getFoodList());
//
//        order = new Order("User2@gmail.com","KFC");
//        order.setFoodList(getRandFood(order.getRestName(), 6));
//        addOrderHistory_DB(order.getEmail(), order.getRestName(), order.getItemCount(),
//                order.getTotalCost(), order.getDateTime(), order.getFoodList());
//
//        order = new Order("User2@gmail.com","McDonalds");
//        order.setFoodList(getRandFood(order.getRestName(), 1));
//        addOrderHistory_DB(order.getEmail(), order.getRestName(), order.getItemCount(),
//                order.getTotalCost(), order.getDateTime(), order.getFoodList());
//
//    }
//
//
//    // Helper for generating random order's for updateOrderHistoryTable()
//    private LinkedList<Food> getRandFood(String restName, int itemCount)
//    {
//        LinkedList<Food> foodList = new LinkedList<>();
//        LinkedList<Food> randFoodList = new LinkedList<>();
//        Random rng = new Random();
//
//        while(itemCount > 0 || randFoodList.size() == foodList.size()) {
//            Food f = foodList.get(rng.nextInt(foodList.size()));
//
//            if (!randFoodList.contains(f)) {
//                while (f.getQuantity() <= 0) {
//                    f.setQuantity(rng.nextInt(itemCount));
//                }
//
//                itemCount -= f.getQuantity();
//                randFoodList.add(f);
//
//            }
//        }
//
//        return randFoodList;
//    }
}
