package database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TodoDao {

    //insert query
    @Insert(onConflict = REPLACE)
    void insert(Todo todo);

    //delete query
    @Delete
    void delete(Todo todo);

    //delete all query
    @Delete
    void reset(List<Todo> todoList);

    //Update query
    @Query("UPDATE TodoTable SET text=:sText WHERE id=:sId")
    void update(int sId, String sText);

    //get all data query
    @Query("SELECT * FROM TodoTable")
    List<Todo> getAll();

}
