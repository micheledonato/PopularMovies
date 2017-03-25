package com.devmicheledonato.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Michele on 25/03/2017.
 */

public class Video implements Parcelable {

    private String id;
    private String iso6391;
    private String iso31661;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public final static Parcelable.Creator<Video> CREATOR = new Creator<Video>() {

        @SuppressWarnings({
                "unchecked"
        })
        public Video createFromParcel(Parcel in) {
            Video instance = new Video();
            instance.id = ((String) in.readValue((String.class.getClassLoader())));
            instance.iso6391 = ((String) in.readValue((String.class.getClassLoader())));
            instance.iso31661 = ((String) in.readValue((String.class.getClassLoader())));
            instance.key = ((String) in.readValue((String.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.site = ((String) in.readValue((String.class.getClassLoader())));
            instance.size = ((int) in.readValue((int.class.getClassLoader())));
            instance.type = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Video[] newArray(int size) {
            return (new Video[size]);
        }
    };

    /**
     * No args constructor for use in serialization
     */
    public Video() {
    }

    /**
     * @param id
     * @param iso6391
     * @param iso31661
     * @param key
     * @param name
     * @param site
     * @param size
     * @param type
     */
    public Video(String id, String iso6391, String iso31661, String key, String name, String site, int size, String type) {
        this.id = id;
        this.iso6391 = iso6391;
        this.iso31661 = iso31661;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public Video(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getString("id");
        this.iso6391 = jsonObject.getString("iso_639_1");
        this.iso31661 = jsonObject.getString("iso_3166_1");
        this.key = jsonObject.getString("key");
        this.name = jsonObject.getString("name");
        this.site = jsonObject.getString("site");
        this.size = jsonObject.getInt("size");
        this.type = jsonObject.getString("type");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public String getIso31661() {
        return iso31661;
    }

    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id='" + id + '\'' +
                ", iso6391='" + iso6391 + '\'' +
                ", iso31661='" + iso31661 + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", site='" + site + '\'' +
                ", size=" + size +
                ", type='" + type + '\'' +
                '}';
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(iso6391);
        dest.writeValue(iso31661);
        dest.writeValue(key);
        dest.writeValue(name);
        dest.writeValue(site);
        dest.writeValue(size);
        dest.writeValue(type);
    }

    public int describeContents() {
        return 0;
    }
}
