package student.curtin.assignment1.model;

import android.database.sqlite.SQLiteDatabase;

public class User {
    private String email;
    private String password;


    public User()
    {
        this.email = "";
        this.password = "";
    }

    public User(String username, String password)
    {
        this.email = username;
        this.password = password;
    }

    public void addToDB(DBHandler dbHandler)
    {
        dbHandler.addUser_DB(email, password);
    }

    public String getEmail(){ return email;}
    public String getPassword(){ return password;}

}
