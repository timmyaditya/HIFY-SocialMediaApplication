package database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

//add database entities
@Database(entities = {Todo.class}, version = 1, exportSchema = false)
public abstract class RoomDb extends RoomDatabase {

    //create dao injecting dao
    //this can be assumed to be dao impl but here we don't override the methods
    //the methods inside todoDao will automatically be overriden by android  and appropriate query will be given as we have mentioned it in the interface
    // this class is just to get the instance of the database , as there might be various databases present so among them we want the specific database
    public abstract TodoDao todoDao();


    //create database instance
    private static RoomDb database;
    //define database name
    private static String DATABASE_NAME = "TodoDatabase";



    public  synchronized static RoomDb getInstance(Context context)
    {
        //check condition
        if (database == null)
        {
            database = Room.databaseBuilder(context.getApplicationContext(),
                RoomDb.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        }
        return database;
    }




    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }

}
