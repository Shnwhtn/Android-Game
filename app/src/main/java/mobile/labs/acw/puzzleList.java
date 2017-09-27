package mobile.labs.acw;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class puzzleList extends Fragment implements Serializable {


    // Variables
    Database database;
    public static ArrayList<String>puzzleListDisplay = new ArrayList<>();
    public Boolean moved = false;


    public puzzleList() {
        // Required empty public constructor
    }


    /*

    This function refreshes the listing in the puzzle list

     */
    public ArrayList<String> refreshListing(String var)
    {
        Log.d("trigger",var);
        String all = "All";
        // Define Database
        Database database = new Database(getActivity());
        // Create a temp array list to return data
        ArrayList<String> puzzleListDisplayTemp = new ArrayList<>();
        // If we are asking for all the records aka all sizes
        // Java equivalent to if statement aka lame


        switch (var) {
            case "Available":
                try {
                    JSONArray dataFile = openFile("/data/mobile.labs.acw/PuzzleIndex", "/index.json", "PuzzleIndex");

                    if (dataFile != null) {
                        for (int i = 0; i < dataFile.length(); i++) {
                            String puzzleTitle = dataFile.getString(i);
                           String puzzleTitleTemp = puzzleTitle.replace(".json", "");
                            puzzleListDisplayTemp.add(puzzleTitleTemp);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
                case "On Device":
                    List<Puzzle> puzzleListall = database.getAllThePuzzles();
                    for (Puzzle pn : puzzleListall) {
                        String puzzlename = pn.GetPuzzleName();
                        puzzleListDisplayTemp.add(puzzlename);
                    }
                    break;
                case "Select Filter..":
                    puzzleListDisplayTemp.clear();
                    break;
                default:
                    List<Puzzle> puzzleListvar = database.getAllThePuzzlesOfSize(var);
                    for (Puzzle pn : puzzleListvar) {
                        String puzzlename = pn.GetPuzzleName();
                        puzzleListDisplayTemp.add(puzzlename);
                    }
                    break;

            }

        //return the array list
        return puzzleListDisplayTemp;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_puzzle_list, container, false);
        // Set List Variables
        List<String> list = new ArrayList<String>();
        final ArrayAdapter listViewAdapter;

        TextView txtdirections =(TextView)rootView.findViewById(R.id.Direct);
        txtdirections.setText(R.string.directions);
        // Set a list view to what list it needs to be use
        final ListView listView = (ListView) rootView.findViewById(R.id.PuzzleLister);
        listViewAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, puzzleListDisplay);

        listView.setAdapter((ListAdapter) listViewAdapter);

        // This is the click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /* Create a puzzle object based on what the item has been clicked, create a class in order to
                access the text within the puzzle info fragment.
                 */

                String puzzlestorageLocation = Environment.getDataDirectory().toString() + "/data/mobile.labs.acw/Puzzles/";

                File puzzledirectory = new File(puzzlestorageLocation);

                // If directory doesnt exist create it
                if (!puzzledirectory.isDirectory()) {
                    puzzledirectory.mkdirs();
                }

                // Get String from list view
                String puzzleString = listView.getItemAtPosition(position).toString();
                MainActivity.puzzleID = puzzleString;
                Log.d("puzzlestring",puzzleString);
                File file = new File(puzzlestorageLocation,"puzzle"+puzzleString+".json");
                Log.d("file",puzzlestorageLocation +" " +puzzleString);
                UpdateInfoTextClass infotext = new UpdateInfoTextClass(getContext());
                Database db = new Database(getActivity());
                if(file.exists()) {

                    String puzzleStringInfo = puzzleString.replace(".json","");
                    Log.d("filecheck", puzzleStringInfo);
                    Puzzle puzzle2 = db.getPuzzle(puzzleStringInfo);
                   infotext.Update(puzzle2);
                    infotext.buttonActions("play");
                    Log.d("ready","gobabygo");
                }
                else
                {
                    JSONArray dataFile = openFile("/data/mobile.labs.acw/PuzzleIndex","/index.json","PuzzleIndex");
                    infotext.buttonActions("download");
                }

            }
        });




        /*
        Puzzle selection spinner which liss puzzles by size
         */

        final Spinner filterSpinner = (Spinner)rootView.findViewById(R.id.PuzzleFilterSpinner);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {


                ArrayList<String> puzzleListDisplayq ;
                if(moved = true) {
                    Log.d("trigger","list clear call");
                    // if the spinner is moved then clear the list
                    puzzleListDisplay.clear();

                }
                //refill the list

                puzzleListDisplayq = refreshListing(filterSpinner.getSelectedItem().toString());

                puzzleListDisplay.clear();
                puzzleListDisplay.addAll(puzzleListDisplayq);
                listViewAdapter.notifyDataSetChanged();
                moved = true;

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        setRetainInstance(true);


        return rootView;

    }

    public JSONArray openFile(String location, String file , String getString) {
        JSONArray data = null;
        try {
            String storageLocation = Environment.getDataDirectory().toString() + location;
            //Json Index Location and a new file pointer
            String JsonIndexLocation = storageLocation + file;
            File indexFileCheck = new File(JsonIndexLocation);
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
            data = puzzleIndexJson.getJSONArray(getString);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }



    public class UpdateInfoTextClass {
        Context context;

        public UpdateInfoTextClass(Context context) {
            this.context = context;
        }


        public void Update(Puzzle pn) {
            TextView txtpuzzlenamevar = (TextView) ((Activity) context).findViewById(R.id.puzzlenamevar);
            txtpuzzlenamevar.setText(pn.GetPuzzleName());


            TextView txtpuzzlesize = (TextView) ((Activity) context).findViewById(R.id.puzzlesizevar);
            txtpuzzlesize.setText(pn.GetSize());


            TextView txtPuzzleWins = (TextView) ((Activity) context).findViewById(R.id.puzzlewinvar);
            String winAttempts = String.valueOf(pn.GetAttempts()) + " " + String.valueOf(pn.GetClicks());
            Log.d("win",winAttempts);
            txtPuzzleWins.setText(winAttempts);

            TextView txtPuzzleSet = (TextView) ((Activity) context).findViewById(R.id.puzzlepicturesetvar);
            String puzzleSetPictures = null;

             /*
             picture set values

             */
            switch (pn.GetPuzzleSet()) {
                case "androidFlavours.json":
                    txtPuzzleSet.setText(R.string.androidflavors);
                    puzzleSetPictures = "androidFlavours";
                    break;
                case "beans.json":
                    txtPuzzleSet.setText(R.string.beans);
                    puzzleSetPictures = "beans";
                    break;
                case "fruitAndVeg.json":
                    txtPuzzleSet.setText(R.string.fruitandveg);
                    puzzleSetPictures = "fruitAndVeg";
                    break;
                case "fruit.json":
                    txtPuzzleSet.setText(R.string.fruit);
                    puzzleSetPictures = "fruit";
                    break;
                case "greenThings.json":
                    txtPuzzleSet.setText(R.string.greenthings);
                    puzzleSetPictures = "greenThings";
                    break;
                case "memes.json":
                    txtPuzzleSet.setText(R.string.memes);
                    puzzleSetPictures = "memes";
                    break;
                case "redThings.json":
                    txtPuzzleSet.setText(R.string.redthings);
                    puzzleSetPictures = "redThings";
                    break;
                case "veg.json":
                    txtPuzzleSet.setText(R.string.veg);
                    puzzleSetPictures = "veg";
                    break;
                default:
                    txtPuzzleSet.setText("-");
                    break;
            }
        }
        public void buttonActions(String downloadorplay) {

            Button downloadPlay = (Button) ((Activity) context).findViewById(R.id.downloadplay);
            downloadPlay.setVisibility(View.VISIBLE);


            switch(downloadorplay) {
                case "download":
                    downloadPlay.setText(R.string.needpuzzles);
                    break;
                case "play":
                    downloadPlay.setText(R.string.needplay);
                break;
            }

           // String pictureSetLocation = Environment.getDataDirectory().toString() + "/data/mobile.labs.acw/PictureSet/" + puzzleSetPictures;
            //Log.d("derpaderp", pictureSetLocation);
            //File puzzleSetDirectory = new File(pictureSetLocation);

            //Button downloadPlay = (Button) ((Activity) context).findViewById(R.id.downloadplay);
           // downloadPlay.setVisibility(View.VISIBLE);
           // MainActivity.pictureSet = puzzleSetPictures;

            // Is the files there or not, if the directory is empty set the download string, else its play string
            // Downloading will be done via the button click
           // if (!puzzleSetDirectory.exists()) {
          //      downloadPlay.setText(R.string.needpuzzles);
         //       Log.d("derpaderp", "doesnt exist");
         //   } else {
         //       String[] filelist = puzzleSetDirectory.list();
         //       if (filelist.length == 0) {
         //           // If dir is empty show download the pictures on the button
        //            downloadPlay.setText(R.string.needpuzzles);

          //      } else {
                    //show the play puzzle text

         //       }
          //  }
       // }


        }


    }
}

