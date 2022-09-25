package student.curtin.assignment1.model;

import java.sql.Blob;

public class DBSchema
{
        public static class Restaurant{
            public static final String TABLE_NAME = "restaurant";
            public static class Cols{
                public static final String REST_NAME = "restaurant_name";
                public static final String REST_IMAGE = "restaurant_image";
            }
        }

        public static class FoodItem {
            public static final String TABLE_NAME = "food";
            public static class Cols{
                public static final String FOODNAME = "food_name";
                public static final String FOODIMAGE = "food_image";
                public static final String FOODPRICE = "food_price";
                public static final String RESTAURANT = "rest_name";
            }
        }

        public static class OrderHistory{
            public static final String TABLE_NAME = "orderHistory";
            public static class Cols{
                public static final String EMAIL = "email";
                public static final String RESTAURANT = "restaurant";
                public static final String ITEMCOUNT = "itemCount";
                public static final String TOTALCOST = "cost";
                public static final String DATETIME = "date_time";
            }
        }

        public static class FoodOrder{
            public static final String TABLE_NAME = "foodOrder";
            public static class Cols{
                public static final String EMAIL = "email";
                public static final String TIME = "time";
                public static final String FOODNAME = "food_name";
                public static final String FOODIMAGE = "food_image";
                public static final String FOODPRICE = "food_price";
                public static final String FOODQUANT = "food_quant";
            }
        }

        public static class UserTable {
            public static final String TABLE_NAME = "user";
            public static class Cols {
                public static final String EMAIL = "email";
                public static final String PASSWORD = "password";
            }
        }
}
