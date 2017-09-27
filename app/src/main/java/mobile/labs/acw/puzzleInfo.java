package mobile.labs.acw;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;


public class puzzleInfo extends Fragment implements Serializable{


    // Set the view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the fragment

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_puzzle_info, container, false);


        // Attach string to  puzzle name title
        TextView txtpuzzlenametitle =(TextView)view.findViewById(R.id.puzzlenametitle);
        txtpuzzlenametitle.setText(R.string.PuzzleNumber);

        // Attach string to puzzle size title
        TextView txtPuzzleSize = (TextView)view.findViewById(R.id.puzzlesize);
        txtPuzzleSize.setText(R.string.PuzzleSize);

        // // Attach string to  puzzle set
        TextView txtPuzzleSet = (TextView)view.findViewById(R.id.puzzlelayout);
        txtPuzzleSet.setText(R.string.PuzzleLayout);

        //Wins clicks
        TextView txtwinclick = (TextView)view.findViewById(R.id.puzzlewins);
        txtwinclick.setText(R.string.WinsClicks);


        //Default null values
        TextView txtpuzzlename =(TextView)view.findViewById(R.id.puzzlenamevar);
        txtpuzzlename.setText(" ");
        TextView txtpuzzlesize =(TextView)view.findViewById(R.id.puzzlesizevar);
        txtpuzzlesize.setText(" ");
        TextView txtpictureset =(TextView)view.findViewById(R.id.puzzlepicturesetvar);
        txtpictureset.setText(" ");
        TextView txtwintext =(TextView)view.findViewById(R.id.puzzlewinvar);
        txtwintext.setText(" ");



        // return the view back to the activity
        return view;


    }

}

