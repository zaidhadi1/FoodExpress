package student.curtin.assignment1.model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;


public class Order {
    private String email;
    private String restName;
    private int itemCount = 0;
    private double totalCost = 0;
    private String dateTime;
    private LinkedList<Food> foodList;

    public Order(String email, String restName)
    {
        this.email = email;
        this.restName = restName;
        this.foodList = new LinkedList<Food>();
        this.dateTime = "";
    }

    // Can be called by fragments and from DB Handler to create sample records
    public void addToDB(DBHandler dbHandler)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm    dd/MM/yyyy");
        LocalDateTime dateTime = LocalDateTime.now();
        this.dateTime = dtf.format(dateTime);

        dbHandler.addOrderHistory_DB(email, restName, itemCount, totalCost, this.dateTime, foodList);
    }

    public void setFoodList(LinkedList<Food> foodList) {
        this.foodList = foodList;

        for(Food f: this.foodList)
        {
            this.itemCount += f.getQuantity();
            this.totalCost += f.getPrice() * f.getQuantity();
        }
    }

    public void setDateTime(String dateTime) {this.dateTime = dateTime;}

    public String getEmail(){ return this.email;}

    public String getRestName(){ return this.restName;}

    public int getItemCount(){ return this.itemCount;}

    public double getTotalCost(){ return this.totalCost;}

    public String getDateTime() {return this.dateTime;}

    public LinkedList<Food> getFoodList(){ return this.foodList;}
}
