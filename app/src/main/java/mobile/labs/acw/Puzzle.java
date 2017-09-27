package mobile.labs.acw;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

public class Puzzle  implements Serializable, Parcelable {

    // These are private variables for the class

    int _id;
    String _puzzlename;
    int _rows;
    String _puzzleset;
    String _layout;
    int _active;
    int _attempts;
    int _clicks;
    String _size;


    // Constructor
    //Updater
    public Puzzle(int id, String puzzlename, int Rows, String Puzzleset, String Layout,String Size) {
        this._id = id;
        this._puzzlename = puzzlename;
        this._rows = Rows;
        this._puzzleset = Puzzleset;
        this._layout = Layout;
        this._size = Size;
    }
    //new
    public Puzzle(String puzzlename, int Rows, String Puzzleset, String Layout,int Attempts,int Clicks, String Size) {
        this._puzzlename = puzzlename;
        this._rows = Rows;
        this._puzzleset = Puzzleset;
        this._layout = Layout;
        this._attempts = Attempts;
        this._clicks = Clicks;
        this._size = Size;
    }
    //Get Info
    public Puzzle(int id ,String puzzlename, int Rows, String Puzzleset, String Layout,int Attempts,int Clicks, String Size) {
        this._id = id;
        this._puzzlename = puzzlename;
        this._rows = Rows;
        this._puzzleset = Puzzleset;
        this._layout = Layout;
        this._attempts = Attempts;
        this._clicks = Clicks;
        this._size = Size;
    }
    // Blank Constructor
    public Puzzle() {

    }

    //Getters and setters

    public int GetID()
    {
        return this._id;
    }

    public void SetID(int id)
    {
        this._id = id;
    }

    public String GetPuzzleName()
    {
        return  this._puzzlename;
    }

    public void SetPuzzleName(String puzzlename)
    {
        this._puzzlename = puzzlename;
    }

    public int GetRows()
    {
        return this._rows;
    }

    public void SetRows(int Rows)
    {
        this._rows = Rows;
    }

    public String GetPuzzleSet()
    {
        return this._puzzleset;
    }

    public void SetPuzzleSet(String Puzzleset)
    {
        this._puzzleset = Puzzleset;
    }

    public String GetLayout()
    {
        return this._layout;
    }

    public void SetLayout(String Layout)
    {
        this._layout = Layout;
    }

    public int GetAttempts()
    {
        return this._attempts;
    }

    public void SetAttempts(int Attempts)
    {
        this._attempts = Attempts;
    }

    public int GetClicks()
    {
        return this._clicks;
    }

    public void SetClicks(int Clicks)
    {
        this._clicks = Clicks;
    }

    public String GetSize()
    {
        return this._size;
    }

    public void SetSize(String Size)
    {
        this._size = Size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this._id);
        dest.writeString(this._puzzlename);
        dest.writeInt(this._rows);
        dest.writeString(this._puzzleset);
        dest.writeString(this._layout);
        dest.writeInt(this._active);
        dest.writeInt(this._attempts);
        dest.writeInt(this._clicks);
        dest.writeString(this._size);
    }

    protected Puzzle(Parcel in) {
        this._id = in.readInt();
        this._puzzlename = in.readString();
        this._rows = in.readInt();
        this._puzzleset = in.readString();
        this._layout = in.readString();
        this._active = in.readInt();
        this._attempts = in.readInt();
        this._clicks = in.readInt();
        this._size = in.readString();
    }

    public static final Creator<Puzzle> CREATOR = new Creator<Puzzle>() {
        @Override
        public Puzzle createFromParcel(Parcel source) {
            return new Puzzle(source);
        }

        @Override
        public Puzzle[] newArray(int size) {
            return new Puzzle[size];
        }
    };
}