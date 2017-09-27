package mobile.labs.acw;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;


public class scorescreen extends Fragment implements Serializable{
    // TODO: Rename parameter arguments, choose names that match

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View ScoreView = inflater.inflate(R.layout.fragment_scorescreen, container, false);

        final TextView currentScore = (TextView)ScoreView.findViewById(R.id.currentclickcounter);
        final TextView historyScore = (TextView)ScoreView.findViewById(R.id.historyclickcounter);

        return ScoreView;
    }


}
