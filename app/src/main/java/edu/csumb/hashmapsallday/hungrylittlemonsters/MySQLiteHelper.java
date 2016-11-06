package edu.csumb.hashmapsallday.hungrylittlemonsters;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by karentafolla on 11/5/16.
 */


public class MySQLiteHelper extends SQLiteOpenHelper{

    private static MySQLiteHelper sInstance;
    private SQLiteDatabase database;
    private final Context context;
    private String DATABASE_PATH;

//    public void openDataBase() throws SQLException {
//
//        //Open the database
//        String myPath = DATABASE_PATH + DATABASE_NAME;
//        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
//
//    }

    // Database Name - Starving Students
    private static final String DATABASE_NAME = "StarvingStudents";

    // Table Name - Locations
    private static final String TABLE_LOCATIONS = "locations";

    // Table Name - Monster Information
    private static final String TABLE_MONSTER = "monster";

    // Columns Names of locations Table
    private static final String KEY_NAME = "name";
    private static final String KEY_PLACE = "place";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";

    // Columns Names of user Table
    private static final String KEY_MONSTER_NAME = "monster_name";
    private static final String KEY_BUDGET = "budget";
    private static final String KEY_ICOOK = "icook";
    private static final String KEY_TRANSPORTATION = "transportation";
    private static final String KEY_FIRST_CHOICE = "first_choice";
    private static final String KEY_SECOND_CHOICE = "second_choice";
    private static final String KEY_THIRD_CHOICE = "third_choice";
    private static final String KEY_BIRTHDAY = "birthday";
    private static final String KEY_EYEBROWS = "eyebrows";
    private static final String KEY_EYE = "eye";
    private static final String KEY_MOUTH = "mouth";
    private static final String KEY_ACCESSORY = "accessory";
    private static final String KEY_COLOR = "color";

    // Database Version
    private static final int DATABASE_VERSION = 9;

    // Log TAG for debugging purpose
    private static final String TAG = "StarvingStudents";

    // Constructor
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        //this.DATABASE_PATH = context.getApplicationInfo().dataDir;
    }

    public MySQLiteHelper(Context context, String db_name, String something, Integer version){
        super(context, db_name, null, version);
        this.context = context;
        this.DATABASE_PATH = context.getApplicationInfo().dataDir;
    }

    public static synchronized MySQLiteHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MySQLiteHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "INSIDE SQLite");

        // SQL statement to create a table called "locations"
        String CREATE_LOCATION_TABLE = "CREATE TABLE locations ( " +
                "name TEXT, " +
                "place TEXT, " +
                "latitude TEXT, " +
                "longitude TEXT, " +
                "address TEXT)";

        // SQL statement to create a table called "monster"
        String CREATE_MONSTER_TABLE = "CREATE TABLE monster ( " +
                "monster_name TEXT, " +
                "budget TEXT, " +
                "icook TEXT, " +
                "transportation TEXT, " +
                "first_choice TEXT, " +
                "second_choice TEXT, " +
                "third_choice TEXT, " +
                "birthday TEXT, " +
                "eyebrows TEXT, " +
                "eye TEXT, " +
                "mouth TEXT, " +
                "accessory TEXT, " +
                "color TEXT)";

        // execute an SQL statement to create the table
        db.execSQL(CREATE_LOCATION_TABLE);
        db.execSQL(CREATE_MONSTER_TABLE);

        //INSERT PRESET VALUES
        BufferedReader reader;
        try{
            InputStream is = context.getResources().getAssets().open("hackathonAddresses.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;

            int counter = 0;

            String name = "";
            Location locations = new Location();
            ContentValues values = new ContentValues();

            while ((line = br.readLine()) != null) {
                //play with each line here
                counter++;
                Log.d(TAG, line);
                String place = "";
                String address = "";
                String latitude = "";
                String longitude = "";
                if(counter == 1 || counter == 9 || counter == 18 || counter ==  25){
                    name = line;
                }
                else if(line.equals("-----------------------") || line.equals("\n")) {
                    //skip
                }
                else{
                    //1. Using StringTokenizer constructor
                    StringTokenizer st1 = new StringTokenizer(line, "|");


                    //iterate through tokens
                    while(st1.hasMoreTokens()) {
                        place = st1.nextToken();
                        address = st1.nextToken();
                        latitude = st1.nextToken();
                        longitude = st1.nextToken();
                    }

                    values.put(KEY_NAME, name); // get name
                    values.put(KEY_PLACE, place); // get place
                    values.put(KEY_LATITUDE, latitude); // get latitude
                    values.put(KEY_LONGITUDE, longitude); //get account longitude
                    values.put(KEY_ADDRESS, address); // get address
                    db.insert(TABLE_LOCATIONS, // table
                            null, //nullColumnHack
                            values); // key/value -> keys = column names/ values = column values
                }

            }
        } catch(IOException ioe){
            Log.d(TAG, "ERRORS!!!");
            ioe.printStackTrace();
        }

        Log.d(TAG, "Locations() Added");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older locations if table exists
        db.execSQL("DROP TABLE IF EXISTS locations");
        // Drop older monster if table exists
        db.execSQL("DROP TABLE IF EXISTS monster");
        this.onCreate(db);
    }

    public void setMonsterName(Monster monster){
        Log.d(TAG, "addMonster() - " + monster.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_MONSTER_NAME, monster.getName()); // get name

        // 3. insert
        db.insert(TABLE_MONSTER, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close - release the reference of writable DB
        db.close();
    }

    public Monster getMonster(String name){
        String query = "SELECT * FROM " + TABLE_MONSTER + " WHERE " + KEY_MONSTER_NAME + "=?" + name;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build book and add it to list
        Monster monster = null;

        if (cursor.moveToFirst()) {
            do {
                monster = new Monster();
                monster.setName(cursor.getString(0));
                monster.setCooking(cursor.getString(1));
                monster.setTransportation(cursor.getString(2));
                name = monster.getName();
            } while (cursor.moveToNext());
        }

        // return name
        return monster;
    }

    public String getMonsterName(){
        String query = "SELECT monster_name FROM " + TABLE_MONSTER;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build book and add it to list
        Monster monster = null;
        String name = "";

        if (cursor.moveToFirst()) {
            do {
                monster = new Monster();
                monster.setName(cursor.getString(0));
                name = monster.getName();
            } while (cursor.moveToNext());
        }

        // return name
        return name;
    }

    public boolean checkMonsterName(String name) {

        // 1. build the query
        String query = "SELECT monster_name FROM " + TABLE_MONSTER;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        //Monster monster = null;
        String temp_name;
        Boolean isTrue = false;
        Monster monster;

        if (cursor.moveToFirst()) {
            do {
                monster = new Monster();
                temp_name = cursor.getString(0);
                if(temp_name.equals(name)){
                   // monster = cursor.getString(0);
                    isTrue = true;

                }
            } while (cursor.moveToNext());
        }

        return isTrue;
    }

    public Location randomLatLon(){
        // 1. build the query
        String query = "SELECT * FROM " + TABLE_LOCATIONS + " ORDER BY RANDOM() " + "LIMIT 1";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        //Monster monster = null;
        String temp_name;
        Boolean isTrue = false;
        Location location = null;

        if (cursor.moveToFirst()) {
            {
                location = new Location();
                location.setPlace(cursor.getString(1));
                location.setLatitude(cursor.getString(2));
                location.setLongitude(cursor.getString(3));

            } while (cursor.moveToNext());
        }

        return location;
    }

    public int setCreateAccount(String name, Monster monster){
        Log.d(TAG, "Entered Set Create Account");
        //String birthday, String} eyebrows, String eye, String mouth, String accessory){

        Boolean nameExist = checkMonsterName(name);

        if(nameExist == true) {

            SQLiteDatabase db = this.getWritableDatabase();

            Log.d(TAG, "Before Values");

            ContentValues values = new ContentValues();
            values.put(KEY_MONSTER_NAME, monster.getName());
            //values.put(KEY_BIRTHDAY, monster.getBirthday());
            values.put(KEY_COLOR, monster.getColor());

            Log.d(TAG, "update");

            // updating row
            return db.update(TABLE_MONSTER, values, KEY_MONSTER_NAME + "=?",
                    new String[]{name});
        }
        Log.d(TAG, "SETCREATEERROR");

        return 0;

    }

    public int setCustomizeProfile(String name, Monster monster){
        //String cook, String } budget, String transporation){

        boolean isAvailable = checkMonsterName(name);
        if(isAvailable == true) {

            SQLiteDatabase db = this.getWritableDatabase();

            // 2. create ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put(KEY_BUDGET, monster.getBudget()); // get budget
            values.put(KEY_ICOOK, monster.getCook()); // get cook
            values.put(KEY_TRANSPORTATION, monster.getTransportation()); // get transportation

            // updating row
            return db.update(TABLE_MONSTER, values, KEY_NAME + "=?",
                    new String[]{name});

        }

        return 0;
    }

    public int setFeedMonster(String name, Monster monster){
        boolean isAvailable = checkMonsterName(name);
        if(isAvailable == true) {
            // 1. get reference to writable DB
            SQLiteDatabase db = this.getWritableDatabase();

            // 2. create ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            //values.put(KEY_MONSTER_NAME, monster.getName()); // get name
            values.put(KEY_FIRST_CHOICE, monster.getFirstChoice()); // get name
            values.put(KEY_SECOND_CHOICE, monster.getSecondChoice()); // get name
            values.put(KEY_THIRD_CHOICE, monster.getThirdChoice()); // get name

            // updating row
            return db.update(TABLE_MONSTER, values, KEY_NAME + " = ?",
                    new String[]{name});


        }

        return 0;
    }

    // Get all Choices from the database
    public ArrayList<Monster> getChoices() {
        ArrayList<Monster> monsters = new ArrayList<Monster>();

        // 1. build the query
        String query = "SELECT * FROM " + TABLE_MONSTER;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Monster monster = null;
        if (cursor.moveToFirst()) {
            do {
                monster = new Monster();
                monster.setChoices(cursor.getString(4), cursor.getString(5), cursor.getString(6));

                // Add book to books
                monsters.add(monster);
            } while (cursor.moveToNext());
        }

        Log.d(TAG, "getChoices() - " + monsters.toString());

        // return books
        return monsters;
    }

    // Based on Choices get Locations
    // Get all locations from the database
    public ArrayList<Location> getAllLocations() {
        ArrayList<Location> locations = new ArrayList<Location>();

        // 1. build the query
        String query = "SELECT * FROM " + TABLE_LOCATIONS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String name = "";

        // 3. go over each row, build book and add it to list
        Location location = null;
        if (cursor.moveToFirst()) {
            do {
                //name =0, place =1, latitude =2, long=3, address =4
                location = new Location();
                location.setName(cursor.getString(0));
                location.setPlace(cursor.getString(1));
                location.setLatitude(cursor.getString(2));
                location.setLongitude(cursor.getString(3));
                location.setAddress(cursor.getString(4));

                locations.add(location);
            } while (cursor.moveToNext());
        }

        db.close();

        // return locations
        return locations;
    }


    public void createMonster(Monster monster) {
        Log.d(TAG, "CreateMonster() - " + monster.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_MONSTER_NAME, monster.getName()); // get name
        values.put(KEY_BUDGET, monster.getBudget());
        values.put(KEY_FIRST_CHOICE, monster.getFirstChoice());
        values.put(KEY_SECOND_CHOICE, monster.getSecondChoice());
        values.put(KEY_THIRD_CHOICE, monster.getThirdChoice());
        values.put(KEY_TRANSPORTATION, monster.getTransportation());
        values.put(KEY_ICOOK, monster.getCook());

        // 3. insert
        db.insert(TABLE_MONSTER, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close - release the reference of writable DB
        db.close();
    }
}
