package mobile.labs.acw;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameActivity extends AppCompatActivity  {


    // card image https://commons.wikimedia.org/wiki/Playing_card#/media/File:Carte_Napoletane_retro.jpg


    private static int NUM_ROWS ;

    private static int NUM_COLS ;
    private static boolean instance;
    private static int CURRENTCLICK = 0;
    private static String HISTORYCLICK ;
    private static ArrayList<Button> buttonsCreated = new ArrayList<Button>();
    private static List<String> layoutList = new ArrayList<String>();
    private static List<String> pictureLists = new ArrayList<String>();
    private static List<GameObject> objectList = new ArrayList<GameObject>();
    public static String puzzlePicturePart = null;
    private int clickCount;
    public static int card1;
    public static int card2;
    public static TableLayout gameTable;
    private static TableRow gameTableRow;
    private static int newWidth;
    private static int newHeight;
    private static boolean active = true;
    private static String puzzleno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        // Create a database connection

        Database database = new Database(this);


            //import the puzzle
            Puzzle puzzle = new Puzzle();
            puzzleno = getIntent().getStringExtra("puzzle");
            puzzle = database.getPuzzle(puzzleno);
            Log.d("pizzle",puzzleno);

            // Get puzzle layout
            String str = puzzle.GetLayout();
            //Replace [ and ] with null , then split in a list and remove commas
            str = str.replace("[", "");
            str = str.replace("]", "");
            layoutList = Arrays.asList(str.split(","));


            NUM_ROWS = puzzle.GetRows();
            NUM_COLS = layoutList.size() / NUM_ROWS;
            //Set record click
            UpdateScores updateScoreText = new UpdateScores(this);
            updateScoreText.Update(9,"9");


            //Get Picture Sets
            String puzzlePicture = puzzle.GetPuzzleSet();

            puzzlePicturePart = puzzlePicture.replace(".json", "");
            try {
                String storageLocation = Environment.getDataDirectory().toString() + "/data/mobile.labs.acw/PictureSet/" + puzzlePicturePart + "/";

                //Json Index Location and a new file pointer
                String JsonIndexLocation = storageLocation + puzzlePicture;
                Log.d("pic", JsonIndexLocation);
                File indexFileCheck = new File(JsonIndexLocation);


                /*This is the json interpretor action*/

                // Open a filestream and read to a buffer called jsonbuffer
                FileInputStream jsonIndexFile = new FileInputStream(JsonIndexLocation);
                int size = jsonIndexFile.available();
                byte[] jsonbuffer = new byte[size];
                jsonIndexFile.read(jsonbuffer);
                // Close the file stream
                jsonIndexFile.close();
                //Parse the buffer to a json string
                String parsedJsonIndexFile = new String(jsonbuffer, "UTF-8");
                //Create an object
                JSONObject puzzleIndexJson = new JSONObject(parsedJsonIndexFile);
                // Create an Array from the object
                JSONArray data = puzzleIndexJson.getJSONArray("PictureFiles");

                if(savedInstanceState == null) {
                    // Convert to a list
                    for (int i = 0; i < data.length(); i++) {
                        String temp = data.getString(i);
                        pictureLists.add(temp);
                    }

                    for (int i = 0; i < layoutList.size(); i++) {
                        int layoutListIndex = Integer.parseInt(layoutList.get(i));
                        GameObject object = new GameObject(i, layoutListIndex, pictureLists.get(layoutListIndex - 1), puzzlePicturePart, false, false);
                        objectList.add(object);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(savedInstanceState != null){
                pictureLists = (List<String>) savedInstanceState.getSerializable("PICTURELISTS");
                objectList = (List<GameObject>) savedInstanceState.getSerializable("OBJECTLIST");
                newWidth = savedInstanceState.getInt("NEWWIDTH");
                newHeight = savedInstanceState.getInt("NEWHEIGHT");
                CURRENTCLICK = savedInstanceState.getInt("CURRENTCLICK");
                // If user is on one click or two for pairing
                clickCount = savedInstanceState.getInt("CLICKCOUNT");
                updateScoreText.Update(CURRENTCLICK);
                //HISTORYCLICK = savedInstanceState.getInt("HISTORYCLICK");
                //bb = (Button)findViewById();


            }
            makeGameTable();
        updateImages();
        }

    private void updateImages()
    {

        for(GameObject o:objectList)
        {
            if(o.GetCompleted())
            {
                changePicture(o.GetId(),o.GetPicture());

            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        Log.d("derp","instancesave");

        outState.putInt("NUM_ROWS",NUM_ROWS);
        outState.putInt("NUM_COLS",NUM_COLS);
        outState.putInt("CURRENTCLICK",CURRENTCLICK);
        outState.putSerializable("LAYOUTLIST", (Serializable) layoutList);
        outState.putSerializable("PICTURELIST", (Serializable) pictureLists);
        outState.putSerializable("OBJECTLIST", (Serializable) objectList);
        outState.putInt("CLICKCOUNT",clickCount);
        outState.putInt("CARD1",card1);
        outState.putInt("CARD2",card2);
        outState.putInt("NEWWIDTH",newWidth);
        outState.putInt("NEWHEIGHT",newHeight);
        outState.putBoolean("ACTIVE",active);
        //outState.putInt("HISTORYCLICK",HISTORYCLICK);

        super.onSaveInstanceState(outState);

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
            NUM_ROWS = savedInstanceState.getInt("NUM_ROWS");
            NUM_COLS = savedInstanceState.getInt("NUM_COLS");
            CURRENTCLICK = savedInstanceState.getInt("CURRENTCLICK");
           //buttonsCreated = (ArrayList<Button>) savedInstanceState.getSerializable("BUTTONCREATED");
            //HISTORYCLICK = savedInstanceState.getInt("HISTORYCLICK");
        layoutList = (List<String>) savedInstanceState.getSerializable("LAYOUTLIST");
            pictureLists = (List<String>) savedInstanceState.getSerializable("PICTURELIST");
            objectList = (List<GameObject>) savedInstanceState.getSerializable("OBJECTLIST");
            clickCount = savedInstanceState.getInt("CLICKCOUNT");
            card1 = savedInstanceState.getInt("CARD1");
            card2 = savedInstanceState.getInt("CARD2");
           newHeight = savedInstanceState.getInt("NEWHEIGHT");
            newWidth = savedInstanceState.getInt("NEWWIDTH");
            active = savedInstanceState.getBoolean("ACTIVE");
            //gameTable = (TableLayout) savedInstanceState.getSerializable("GAMETABLE");


    }



    /*
    This method creates the table at run time
     */
    private void makeGameTable() {
        //Inital counter
        int buttonCount = 0;
        // Find table in fragmnet
        gameTable = (TableLayout) findViewById(R.id.gameTableLayout);
        for (int row = 0; row < NUM_ROWS; row++) {

            gameTableRow = new TableRow(this);
            //Match Parent

            gameTableRow.setLayoutParams(new TableLayout.LayoutParams(
                    //Constructor
                    //Width
                    TableLayout.LayoutParams.MATCH_PARENT,
                    //Height
                    TableLayout.LayoutParams.MATCH_PARENT,
                    //Degree to scale
                    1.0f
            ));
            gameTable.addView(gameTableRow);

            for (int col = 0; col < NUM_COLS; col++) {
                final Button bb = new Button(this);
                bb.setId(buttonCount);


                bb.setLayoutParams(new TableRow.LayoutParams(
                        //Constructor
                        //Width
                        TableRow.LayoutParams.MATCH_PARENT,
                        //Height
                        TableRow.LayoutParams.MATCH_PARENT,
                        //Degree to scale
                        1.0f));




                // for each button give a card from the element list
                bb.setId(buttonCount);
                bb.setTag(buttonCount);


                bb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("click", String.valueOf(bb.getTag()));
                        Log.d("click", "click");
                        GameObject obj = objectList.get((Integer) bb.getTag());
                        int tag = (int) bb.getId();
                        if(!obj.GetCompleted())
                        {
                            click(tag);
                        }
                    }
                });



                // Add button to the view
                gameTableRow.addView(bb);
                // Add button to the array
                buttonsCreated.add(bb);
                buttonCount++;

            }


        }
    }

    public void click(int tag) {


        Button bb = (Button)findViewById(tag);

        Log.d("buttonlog", String.valueOf(tag));
        GameObject object = objectList.get(tag);

        if (!object.GetTurned() && !object.GetCompleted()) {
            switch (clickCount) {
                case 0:
                    objectList.get(tag).SetTurned(true);
                    bb.findViewById(tag).setClickable(false);
                    changePicture(tag, object.GetPicture());
                    card1 = tag;
                    clickCount++;
                    break;
                case 1:
                    changePicture(tag, object.GetPicture());
                    objectList.get(tag).SetTurned(true);
                    bb.findViewById(tag).setClickable(false);
                    clickCount = 0;
                    CURRENTCLICK++;
                    UpdateScores updateScoreText = new UpdateScores(this);
                    updateScoreText.Update(CURRENTCLICK);
                    card2 = tag;
                    check();
                    gameStatusCheck();
                    break;

            }
        }
    }


    public void changePicture(int tag, String picture) {
        Log.d("shit",String.valueOf(tag)+" "+picture);

        Button bb = (Button) findViewById(tag);

        GameObject object = objectList.get(tag);

            Log.d("tagnpic", tag + picture);


            if (!picture.equals("showtop")) {
                // Change back to standard picture
              String storageLocation = Environment.getDataDirectory().toString() + "/data/mobile.labs.acw/PictureSet/" + puzzlePicturePart + "/";
                File f = new File(storageLocation, object.GetPicture());
                //Bitmap originalBitmap = null;
                try {
                    Bitmap originalBitmap = BitmapFactory.decodeStream(new FileInputStream(f));
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
                    //Resources resource = getResources();
                    Drawable d = new BitmapDrawable(getResources(), scaledBitmap);
                    bb.setBackground(d);

                Log.d("sizing", String.valueOf(newWidth)+" "+String.valueOf(newHeight));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("Change", "change");
                // Show the picture card
               Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.top);
               Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
                Resources resource = getResources();
                bb.setBackground(new BitmapDrawable(resource, scaledBitmap));

            }


        }


    /*
    Set margins solution from
    http://stackoverflow.com/questions/12728255/in-android-how-do-i-set-margins-in-dp-programmatically
     */
    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d("focus","focuschange");

        if(buttonsCreated.isEmpty() || buttonsCreated.equals(null))
        {
            Log.d("make","make");
            makeGameTable();
        }
        Button b = (Button)findViewById(0);
        for (Button bb : buttonsCreated) {
            GameObject object = objectList.get((Integer) bb.getTag());
            Log.d("button ", String.valueOf(bb.getTag()));

            setMargins(bb, 10, 10, 10, 10);

            Log.d("focus sinzg",String.valueOf(bb.getWidth())+" "+String.valueOf(bb.getHeight()));

            newWidth = b.getWidth();
            newHeight = b.getHeight();

            bb.setMinWidth(newWidth);
            bb.setMinHeight(newHeight);

            bb.setMinHeight(newHeight);
            bb.setMaxWidth(newWidth);
            if(!object.GetTurned()&!object.GetCompleted()) {
                Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.top);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
                Resources resource = getResources();
                bb.setBackground(new BitmapDrawable(resource, scaledBitmap));
            }
            if (object.GetCompleted()||object.GetTurned()) {
                // Change back to standard picture
                Log.d("tagnpic","gay");
                String storageLocation = Environment.getDataDirectory().toString() + "/data/mobile.labs.acw/PictureSet/" + puzzlePicturePart + "/";
                File f = new File(storageLocation, object.GetPicture());
                //Bitmap originalBitmap = null;
                try {
                    Bitmap originalBitmap = BitmapFactory.decodeStream(new FileInputStream(f));
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
                    Drawable d = new BitmapDrawable(getResources(), scaledBitmap);
                    bb.setBackground(d);
                    // Resources resource = getResources();
                    // bb.setBackground(new BitmapDrawable(resource, scaledBitmap));


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                UpdateScores updateScoreText = new UpdateScores(this);
                updateScoreText.Update(CURRENTCLICK);
                //updateScoreText.Update(HISTORYCLICK,"derp");

            }}}


    public void check() {


        Log.d("match", "match start");
        //List<GameObject> CheckList = new ArrayList<>(0);


        //LinkedList<Integer> objectListIndex = new LinkedList<>();
        List<Integer> idList = null;

        GameObject object1 = objectList.get(card1);
        GameObject object2 = objectList.get(card2);
        Log.d("topstart1", object1.GetId() + " " + object2.GetId());
        int object1index = object1.GetIndex();
        int object2index = object2.GetIndex();
        Log.d("equals", "equals" + object2index + object1index);
        if (object1index == object2index) {
            Log.d("equals", "matchingpair" + object2index + object1index);

            //This is  a match
            Log.d("test", String.valueOf(objectList.get(object1.GetId()).GetCompleted()));
            objectList.get(object1.GetId()).SetCompleted(true);
            Log.d("test", String.valueOf(objectList.get(object1.GetId()).GetCompleted()));
            objectList.get(object2.GetId()).SetCompleted(true);
            objectList.get(object1.GetId()).SetTurned(false);
            objectList.get(object2.GetId()).SetTurned(false);

            //object1.SetTurned(false);
            // object2.SetTurned(false);
        } else {
            Log.d("notequals", "notequals");
            //No match

            objectList.get(object1.GetId()).SetTurned(false);
            objectList.get(object2.GetId()).SetTurned(false);
            Button bb = (Button) findViewById(object1.GetId());
            bb.setClickable(true);

            Button bb2 = (Button) findViewById(object2.GetId());
            bb2.setClickable(true);
            //changePicture(object1.GetId(),"showtop");
            //changePicture(object2.GetId(),"showtop");
            Worker work = new Worker();
            work.execute();

            Log.d("topstart2", object1.GetId() + " " + object2.GetId());

        }

    }


    public void gameStatusCheck()
    {
        int qcompletecount = 0;
        int elemetsize =0;
        for(GameObject o:objectList){
            elemetsize++;
            if(o.GetCompleted())
            {
                qcompletecount++;
            }
        }
        Log.d("complete",String.valueOf(qcompletecount)+" "+elemetsize);
        if(qcompletecount== elemetsize)
            {
                active = true;
                Intent popupIntent = new Intent(this,pop.class);
                //popupIntent.getIntExtra("currentscore",CURRENTCLICK);
                startActivity(new Intent(this,pop.class));
                Database db = new Database(this);
                Puzzle puzzle = new Puzzle();
                puzzle = db.getPuzzle(puzzleno);
                int puzzleAttempts = puzzle.GetAttempts()+1;
                puzzle.SetAttempts(puzzleAttempts);
                puzzle.SetClicks(CURRENTCLICK);
                puzzle.SetPuzzleName(puzzleno);
                db.updateAPuzzle(puzzle);
            }
        }


    private class Worker extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                setText(card1, "showtop");
                setText(card2, "showtop");
                return null;
            }


        private void setText(final int id, final String picture) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("top",String.valueOf(id)+picture);
                    changePicture(id, picture);
                }
            });
        }


        }

    public class UpdateScores {
        Context context;

        public UpdateScores(Context context) {
            this.context = context;
        }

        public void Update(int score)
        {
            TextView CurrentScore = (TextView) ((Activity) context).findViewById(R.id.currentclickcounter);
            CurrentScore.setText(String.valueOf(CURRENTCLICK));
        }

        public void Update(int score,String intd)
        {
            TextView CurrentScore = (TextView) ((Activity) context).findViewById(R.id.historyclickcounter);
            CurrentScore.setText(intd);
        }
    }
}


