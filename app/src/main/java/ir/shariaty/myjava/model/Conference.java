package ir.shariaty.myjava.model;

import android.content.Context;
import androidx.core.content.ContextCompat;
import ir.shariaty.myjava.R;

public class Conference {

    public enum Status {
        REGISTERING,
        FULL,
        REGISTERED,
        EXPIRED
    }

    public static String getStatusText(Context context, Status status) {
        switch (status) {
            case REGISTERING: return context.getString(R.string.register_open);
            case FULL:        return context.getString(R.string.register_warning);
            case REGISTERED:  return context.getString(R.string.register_full);
            case EXPIRED:     return context.getString(R.string.register_closed);
            default:          return "";
        }
    }

    public static int getStatusColor(Context context, Status status) {
        switch (status) {
            case REGISTERING: return ContextCompat.getColor(context, R.color.status_registering);
            case FULL:        return ContextCompat.getColor(context, R.color.status_full);
            case REGISTERED:  return ContextCompat.getColor(context, R.color.status_registered);
            case EXPIRED:     return ContextCompat.getColor(context, R.color.status_expired);
            default:          return ContextCompat.getColor(context, R.color.gray);
        }
    }

    private int     id;
    private String  title;
    private String  organizer;
    private String  place;
    private String  date;
    private String  time;
    private Status  status;
    private int     imageResId;
    private String  imageUrl;
    private boolean isFavorite;
    private String  summary;
    private String  address;
    private int     remainingCapacity;
    private String  note;
    private boolean isOnline;
    private String  downloadUrl;

    public Conference() {
        this.remainingCapacity = -1;
    }

    public Conference(String title, String organizer, String place,
                      String date, String time, Status status,
                      int imageResId, String imageUrl, boolean isFavorite,
                      String summary, String address,
                      int remainingCapacity, String note,
                      boolean isOnline, String downloadUrl) {
        this.title             = title;
        this.organizer         = organizer;
        this.place             = place;
        this.date              = date;
        this.time              = time;
        this.status            = status;
        this.imageResId        = imageResId;
        this.imageUrl          = imageUrl;
        this.isFavorite        = isFavorite;
        this.summary           = summary;
        this.address           = address;
        this.remainingCapacity = remainingCapacity;
        this.note              = note;
        this.isOnline          = isOnline;
        this.downloadUrl       = downloadUrl;
    }

    public Conference(String title, String organizer, String place,
                      String date, String time, Status status,
                      int imageResId, boolean isFavorite) {
        this(title, organizer, place, date, time, status,
                imageResId, null, isFavorite,
                null, null, -1, null, false, null);
    }

    public int     getId()                       { return id; }
    public void    setId(int id)                 { this.id = id; }

    public String  getTitle()                    { return title; }
    public void    setTitle(String v)            { title = v; }

    public String  getOrganizer()                { return organizer; }
    public void    setOrganizer(String v)        { organizer = v; }

    public String  getPlace()                    { return place; }
    public void    setPlace(String v)            { place = v; }

    public String  getDate()                     { return date; }
    public void    setDate(String v)             { date = v; }

    public String  getTime()                     { return time; }
    public void    setTime(String v)             { time = v; }

    public Status  getStatus()                   { return status; }
    public void    setStatus(Status v)           { status = v; }

    public int     getImageResId()               { return imageResId; }
    public void    setImageResId(int v)          { imageResId = v; }

    public String  getImageUrl()                 { return imageUrl; }
    public void    setImageUrl(String v)         { imageUrl = v; }

    public boolean isFavorite()                  { return isFavorite; }
    public void    setFavorite(boolean v)        { isFavorite = v; }

    public String  getSummary()                  { return summary; }
    public void    setSummary(String v)          { summary = v; }

    public String  getAddress()                  { return address; }
    public void    setAddress(String v)          { address = v; }

    public int     getRemainingCapacity()        { return remainingCapacity; }
    public void    setRemainingCapacity(int v)   { remainingCapacity = v; }

    public String  getNote()                     { return note; }
    public void    setNote(String v)             { note = v; }

    public boolean isOnline()                    { return isOnline; }
    public void    setOnline(boolean v)          { isOnline = v; }

    public String  getDownloadUrl()              { return downloadUrl; }
    public void    setDownloadUrl(String v)      { downloadUrl = v; }
}
