package student.curtin.assignment1.model;

public class Restaurant
{
    private final String restName;
    private final int restImage;

    // CONSTRUCTOR
    public Restaurant(String restName, int restImage){
        this.restName = restName;
        this.restImage = restImage;
        //restaurantList.add(this);
    }

    public String getRestName(){
        return restName;
    }
    public int getRestImage(){
        return restImage;
    }
}
