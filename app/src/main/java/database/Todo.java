package database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//define table name
@Entity(tableName = "todoTable")
public class Todo {

    //create id column
    @PrimaryKey(autoGenerate = true)
    private int id;

    //create text column
    @ColumnInfo(name = "text")
    private String text;

    //generate getter and setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
