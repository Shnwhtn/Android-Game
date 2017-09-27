package mobile.labs.acw;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Serializable{

    public static String pictureSet = null;
    public static String puzzleID = null;

    //Database class handler
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Create a new database handler
        Database database = new Database(this);
        List<Puzzle> puzzles = database.getAllThePuzzles();


        for (Puzzle pn : puzzles) {
            String log = "ID " + pn.GetID() + pn.GetPuzzleName() + " " + pn.GetRows() + " " + pn.GetPuzzleSet() + " " + pn.GetLayout() + " " + " " + pn.GetAttempts() + " " +
                    pn.GetClicks() + " " + pn.GetSize();
            Log.d("name: ", log);
        }
    }


    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }
    /*
    using code from http://stackoverflow.com/questions/5620772/get-text-from-pressed-button
     */
    public void downloadorplay(View view) {
        Button downloadplay = (Button) view;
        String buttonText = downloadplay.getText().toString();
        Log.d("derpaderpt", buttonText);
        if (buttonText.equals("Download Puzzle")) {
            Log.d("derpaderp", "needpuzzles");
            DownloadPuzzleSet work = new DownloadPuzzleSet(puzzleID);
            work.execute();
            Spinner spinner = (Spinner)findViewById(R.id.PuzzleFilterSpinner);
            spinner.setSelection(0);
        } else {
            Intent intent = new Intent(this, GameActivity.class);
            getIntent().removeExtra("puzzle");
            intent.putExtra("puzzle",puzzleID);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            Log.d("derpaderp", "needplay");
        }

    }

    public void refreshButton() {
        Button downloadplay = (Button) findViewById(R.id.downloadplay);
        downloadplay.setText(R.string.needplay);
    }


    public void refreshPuzzles(View view) {
        DownloadTheFiles work = new DownloadTheFiles();
        work.execute();
        Spinner spinner = (Spinner)findViewById(R.id.PuzzleFilterSpinner);
        spinner.setSelection(0);


    }


    private class DownloadPuzzleSet extends AsyncTask<String, String, Boolean>  {
        private String resp;
        private String data;

        public DownloadPuzzleSet(String puzzleID) {
            this.data = puzzleID;
            Log.d("json puzzleid",puzzleID);
        }


        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Show Process dialog
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Importing Puzzle Set",
                    "Working");
        }

        // Set default URL Connection
        URLConnection urlConnection = null;


        protected void onPostExecute(Boolean jobDone) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();

            // If all done correctly
            if (jobDone) {

            } else {
                //Wipe Directory
                String storageLocation = Environment.getDataDirectory().toString() + "/data/mobile.labs.acw/PictureSet/" + pictureSet;
                File file = new File(storageLocation);
                if (file != null && file.isDirectory()) {
                    File[] files = file.listFiles();
                    if (files != null) {
                        for (File f : files) {
                            f.delete();
                        }
                    }
                }
            }

        }

        @Override
        protected Boolean doInBackground(String... puzzleID) {

            // Set url input and start a new connection
            try {
                URL URLinput = new URL("http://www.hull.ac.uk/php/349628/08027/acw/puzzles/"+MainActivity.puzzleID+".json");
                urlConnection = URLinput.openConnection();

                urlConnection.connect();


                // Set File Download Location and write file to it
                String storageLocation = Environment.getDataDirectory().toString() + "/data/mobile.labs.acw/Puzzles/";
                Log.d("url", String.valueOf(URLinput));
                Log.d("storage",storageLocation);
                File directory = new File(storageLocation);
                directory.mkdirs();
                //File storage = Environment.getDataDirectory();
                File file = new File(storageLocation, MainActivity.puzzleID+".json");
                Log.d("wafdsfa", String.valueOf(file));
                // Create the input to the output
                FileOutputStream fileOutput = new FileOutputStream(file);
                InputStream inputStream = urlConnection.getInputStream();

                // Download until the buffer length is zero
                byte[] buffer = new byte[1024];
                int bufferLength = 0;

                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                }
                //Close the output and flush
                fileOutput.close();


//Json Index Location and a new file pointer
                String JsonIndexLocation = storageLocation + MainActivity.puzzleID+".json";
                Log.d("jsonpuzzleid",JsonIndexLocation);
                File indexFileCheck = new File(JsonIndexLocation);

                // Open a filestream and read to a buffer called jsonbuffer
                FileInputStream jsonIndexFile = new FileInputStream(JsonIndexLocation);
                int Puzzlesize = jsonIndexFile.available();
                byte[] jsonbuffer = new byte[Puzzlesize];
                jsonIndexFile.read(jsonbuffer);
                // Close the file stream
                jsonIndexFile.close();
                //Parse the buffer to a json string
                String parsedJsonIndexFile = new String(jsonbuffer, "UTF-8");
                //Create an object
                JSONObject puzzleObj = new JSONObject(parsedJsonIndexFile);
                String puzzleIdString = puzzleObj.getJSONObject("Puzzle").getString("Id");
                String puzzlePictureSetString = puzzleObj.getJSONObject("Puzzle").getString("PictureSet");
                int puzzleRowsString = puzzleObj.getJSONObject("Puzzle").getInt("Rows");
                String puzzleLayoutString = puzzleObj.getJSONObject("Puzzle").getString("Layout");


                        /*
                        Get how many cards there are in the puzzle from layout
                         */

                JSONObject getArr = puzzleObj.getJSONObject("Puzzle");
                JSONArray jsonArray = getArr.getJSONArray("Layout");
                int elementCount = 0;

                for (int qi = 0; qi < jsonArray.length(); qi++) {
                    elementCount++;
                    Log.d("json", jsonArray.getString(qi));
                }

                String puzzleSizeStringRows = "x" + String.valueOf(puzzleRowsString);
                switch (elementCount / puzzleRowsString) {
                    case 3:
                        puzzleSizeStringRows = "4" + puzzleSizeStringRows;
                        break;
                    case 4:
                        puzzleSizeStringRows = "4" + puzzleSizeStringRows;
                        break;
                    case 5:
                        puzzleSizeStringRows = "5" + puzzleSizeStringRows;
                        break;
                    case 6:
                        puzzleSizeStringRows = "6" + puzzleSizeStringRows;
                        break;
                    default:
                        break;
                }



                Puzzle puzzle = new Puzzle();
                // Database Connection
                Database database = new Database(getBaseContext());



                if(database.getExistingRecord(puzzleIdString) == true)
                {
                            /*
                            If the puzzle already exists in the database then all we do is update the
                            puzzle details instead of a full insertion keeping the players scores.
                             */

                    puzzle.SetPuzzleName(puzzleIdString);
                    puzzle.SetRows(puzzleRowsString);
                    puzzle.SetPuzzleSet(puzzlePictureSetString);
                    puzzle.SetSize(puzzleSizeStringRows);
                    puzzle.SetLayout(puzzleLayoutString);

                    database.updateAPuzzle(puzzle);
                }
                else
                {
                    puzzle.SetPuzzleName(puzzleIdString);
                    puzzle.SetRows(puzzleRowsString);
                    puzzle.SetPuzzleSet(puzzlePictureSetString);
                    puzzle.SetSize(puzzleSizeStringRows);
                    puzzle.SetLayout(puzzleLayoutString);
                    puzzle.SetClicks(0);
                    puzzle.SetAttempts(0);

                    database.addPuzzle(puzzle);
                }


                String puzzlepicture = puzzlePictureSetString.replace(".json","");
                // Set url input and start a new connection
                URL pictureDownload = new URL("http://www.hull.ac.uk/php/349628/08027/acw/picturesets/"+puzzlePictureSetString);
                Log.d("url input",URLinput.toString());
                urlConnection = pictureDownload.openConnection();
                urlConnection.connect();

                // Make Directory
                String pictureStorageLocation = Environment.getDataDirectory().toString() + "/data/mobile.labs.acw/PictureSet/"+puzzlepicture;
                Log.d("url input",pictureStorageLocation);
                File pictureDirectory = new File(pictureStorageLocation);
                if(!pictureDirectory.exists()) {
                    pictureDirectory.mkdirs();


                    File pictureJsonFile = new File(pictureStorageLocation, puzzlepicture+".json");
                    Log.d("jsonpicturejsonfile", String.valueOf(pictureJsonFile));
                    // Create the input to the output
                    FileOutputStream picturefileOutput = new FileOutputStream(pictureJsonFile);
                    InputStream pictureinputStream = urlConnection.getInputStream();

                    // Download until the buffer length is zero
                    byte[] picturebuffer = new byte[1024];
                    int picturebufferLength = 0;

                    while ((picturebufferLength = pictureinputStream.read(picturebuffer)) > 0) {
                        picturefileOutput.write(picturebuffer, 0, picturebufferLength);
                    }
                    //Close the output and flush
                    picturefileOutput.close();


                    String JsonFileLocation = Environment.getDataDirectory().toString() + "/data/mobile.labs.acw/PictureSet/" + puzzlepicture + "/" + puzzlepicture + ".json";
                    // String JsonIndexLocation = storageLocation + "/"+pictureSet+".json";
                    Log.d("jsonfile", JsonFileLocation);
                    // Open a filestream and read to a buffer called jsonbuffer



                    File pictureIndexFileJSON = new File(JsonFileLocation);
                    FileInputStream pictureIndexFileStream = new FileInputStream(pictureIndexFileJSON);
                    int pictureIndexFileSize = pictureIndexFileStream.available();
                    byte[] pictureIndexFileBuffer = new byte[pictureIndexFileSize];
                    pictureIndexFileStream.read(pictureIndexFileBuffer);
                    String parsedPictureIndexFile = new String(pictureIndexFileBuffer,"UTF-8");

                    JSONObject pictureIndexJson = new JSONObject(parsedPictureIndexFile);
                    // Create an Array from the object
                    JSONArray data = pictureIndexJson.getJSONArray("PictureFiles");


                    //For each instance in the array ( List Of Puzzles) Download each puzzle to the the data directory puzzles
                    for (int i = 0; i < data.length(); i++) {
                        Log.d("jsondata", data.getString(i));
                        // The Url
                        String pictureUrl = "http://www.hull.ac.uk/php/349628/08027/acw/images/" + data.getString(i);
                        // Construct Puzzle URL
                        URL pictureDownloadURL = new URL(pictureUrl);
                        //Yet another filestream
                        urlConnection = pictureDownloadURL.openConnection();
                        urlConnection.connect();


                        File picturejsonfile = new File(pictureStorageLocation, data.getString(i));
                        FileOutputStream filepictureOutput = new FileOutputStream(picturejsonfile);

                        InputStream inputpuzzleStream = urlConnection.getInputStream();
                        byte[] puzzlebuffer = new byte[1024];
                        int puzzlebufferLength = 0;

                        while ((puzzlebufferLength = inputpuzzleStream.read(puzzlebuffer)) > 0) {
                            filepictureOutput.write(puzzlebuffer, 0, puzzlebufferLength);
                        }
                        //Close the output and flush
                        filepictureOutput.close();

                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return true;

        }

    }

    private class DownloadTheFiles extends AsyncTask<Void, Void, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Show Process dialog
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Importing New Puzzles",
                    "Working");
        }

        // Set default URL Connection
        URLConnection urlConnection = null;


        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(Void... Params) {
            publishProgress();
            try {

                // Set url input and start a new connection
                URL URLinput = new URL("http://www.hull.ac.uk/php/349628/08027/acw/index.json");
                urlConnection = URLinput.openConnection();
                urlConnection.connect();

                // Set File Download Location and write file to it
                String storageLocation = Environment.getDataDirectory().toString() + "/data/mobile.labs.acw/PuzzleIndex";
                File directory = new File(storageLocation);
                directory.mkdirs();
                //File storage = Environment.getDataDirectory();
                File file = new File(storageLocation, "index.json");

                // Create the input to the output
                FileOutputStream fileOutput = new FileOutputStream(file);
                InputStream inputStream = urlConnection.getInputStream();

                // Download until the buffer length is zero
                byte[] buffer = new byte[1024];
                int bufferLength = 0;

                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                }
                //Close the output and flush
                fileOutput.close();

                //Json Index Location and a new file pointer
                String JsonIndexLocation = storageLocation + "/index.json";
                File indexFileCheck = new File(JsonIndexLocation);

                // If the index.file exists then parse the json
                if (indexFileCheck.exists()) {
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
                    JSONArray data = puzzleIndexJson.getJSONArray("PuzzleIndex");

                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

    }


    }

