package rafaxplayer.comprasonline.MODELS;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rafax on 26/06/2016.
 */
public class PLaceDetails implements Parcelable {
    public String name;
    public String icon;
    public String placeid;
    public String formated_address;
    public String phone_number;
    public String international_phone;
    public String url;
    public double rating;
    public double latitude;
    public double longitude;
    public String photo;
    public String website;
    public String googlesite;
    public String[] weekday_text;

    public PLaceDetails(){

    }
    protected PLaceDetails(Parcel in) {
        name = in.readString();
        icon = in.readString();
        placeid = in.readString();
        formated_address = in.readString();
        phone_number = in.readString();
        international_phone = in.readString();
        photo=in.readString();
        url = in.readString();
        rating = in.readDouble();
        latitude = in.readDouble();
        longitude = in.readDouble();
        website = in.readString();
        weekday_text=in.createStringArray();
        googlesite=in.readString();
    }

    public static final Creator<PLaceDetails> CREATOR = new Creator<PLaceDetails>() {
        @Override
        public PLaceDetails createFromParcel(Parcel in) {
            return new PLaceDetails(in);
        }

        @Override
        public PLaceDetails[] newArray(int size) {
            return new PLaceDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(icon);
        parcel.writeString(placeid);
        parcel.writeString(formated_address);
        parcel.writeString(phone_number);
        parcel.writeString(international_phone);
        parcel.writeString(url);
        parcel.writeDouble(rating);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(website);
        parcel.writeStringArray(weekday_text);
        parcel.writeString(googlesite);
        parcel.writeString(photo);
    }
}
