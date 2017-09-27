package mobile.labs.acw;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class GameObject implements Serializable {
    int _id;
    int _index;
    String _picture;
    String _pictureSet;
    boolean _turned;
    boolean _completed;



    public GameObject(int id, int index, String picture, String pictureSet,boolean turned, boolean completed)
    {
        this._id = id;
        this._index = index;
        this._picture = picture;
        this._pictureSet = pictureSet;
        this._turned = turned;
        this._completed = completed;
    }

    public GameObject()
    {

    }



    public int GetId()
    {
        return this._id;
    }
    public void SetId(int id)
    {
        this._id = id;
    }
    public int GetIndex()
    {
        return this._index;
    }
    public void SetIndex(int index)
    {
        this._index = index;
    }
    public String GetPicture()
    {
        return this._picture;
    }
    public void SetPicture(String picture)
    {
        this._picture = picture;
    }
    public void SetPictureSet(String PictureSet)
    {
        this._pictureSet = PictureSet;
    }
    public String GetPictureSet()
    {
        return this._pictureSet;
    }
    public boolean GetTurned()
    {
        return this._turned;
    }
    public void SetTurned(boolean turned)
    {
        this._turned = turned;
    }
    public boolean GetCompleted()
    {
        return this._completed;
    }
    public void SetCompleted(boolean completed)
    {
        this._completed = completed;
    }


}

