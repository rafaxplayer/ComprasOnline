package rafaxplayer.comprasonline.MODELS;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rafax on 23/06/2016.
 */
public class Place implements Parcelable{
    String id;
    String placeid;
    String name;
    double latittude;
    double longitude;
    String icon;
    String open;
    String photo_reference;
    String[] types;
    String address;

    public Place() {

    }

    protected Place(Parcel in) {
        id = in.readString();
        placeid = in.readString();
        name = in.readString();
        latittude = in.readDouble();
        longitude = in.readDouble();
        icon = in.readString();
        open = in.readString();
        photo_reference = in.readString();
        types=in.createStringArray();
        address = in.readString();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(placeid);
        dest.writeString(name);
        dest.writeDouble(latittude);
        dest.writeDouble(longitude);
        dest.writeString(icon);
        dest.writeString(open);
        dest.writeString(photo_reference);
        dest.writeStringArray(types);
        dest.writeString(address);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    public double getLatittude() {
        return latittude;
    }

    public void setLatittude(double latittude) {
        this.latittude = latittude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPhoto_reference() {
        return photo_reference;
    }

    public void setPhoto_reference(String photo_reference) {
        this.photo_reference = photo_reference;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String vicinity) {
        this.address = vicinity;
    }
}
