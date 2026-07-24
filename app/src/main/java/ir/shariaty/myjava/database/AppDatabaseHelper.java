package ir.shariaty.myjava.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ir.shariaty.myjava.model.Conference;
import ir.shariaty.myjava.model.Conference.Status;

public class AppDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME    = "myjava.db";
    private static final int    DATABASE_VERSION = 3;

    private static final String TABLE_USERS            = "users";
    private static final String COL_USER_NATIONAL_CODE = "national_code";
    private static final String COL_USER_PASSWORD      = "password";

    private static final String TABLE_CONF              = "conferences";
    private static final String COL_ID                  = "id";
    private static final String COL_TITLE               = "title";
    private static final String COL_ORGANIZER           = "organizer";
    private static final String COL_PLACE               = "place";
    private static final String COL_DATE                = "date";
    private static final String COL_TIME                = "time";
    private static final String COL_STATUS              = "status";
    private static final String COL_IMAGE_RES_ID        = "image_res_id";
    private static final String COL_IS_FAVORITE         = "is_favorite";
    private static final String COL_SUMMARY             = "summary";
    private static final String COL_ADDRESS             = "address";
    private static final String COL_REMAINING_CAPACITY  = "remaining_capacity";
    private static final String COL_NOTE                = "note";
    private static final String COL_IS_ONLINE           = "is_online";
    private static final String COL_DOWNLOAD_URL        = "download_url";

    public AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_NATIONAL_CODE + " TEXT PRIMARY KEY, " +
                COL_USER_PASSWORD      + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_CONF + " (" +
                COL_ID                 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE              + " TEXT NOT NULL, " +
                COL_ORGANIZER          + " TEXT NOT NULL, " +
                COL_PLACE              + " TEXT NOT NULL, " +
                COL_DATE               + " TEXT NOT NULL, " +
                COL_TIME               + " TEXT NOT NULL, " +
                COL_STATUS             + " TEXT NOT NULL, " +
                COL_IMAGE_RES_ID       + " INTEGER NOT NULL, " +
                COL_IS_FAVORITE        + " INTEGER DEFAULT 0, " +
                COL_SUMMARY            + " TEXT, " +
                COL_ADDRESS            + " TEXT, " +
                COL_REMAINING_CAPACITY + " INTEGER DEFAULT -1, " +
                COL_NOTE               + " TEXT, " +
                COL_IS_ONLINE          + " INTEGER DEFAULT 0, " +
                COL_DOWNLOAD_URL       + " TEXT)");

        ContentValues user = new ContentValues();
        user.put(COL_USER_NATIONAL_CODE, "1234567890");
        user.put(COL_USER_PASSWORD,      "1234");
        db.insert(TABLE_USERS, null, user);

        insertTestConferences(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONF);
        onCreate(db);
    }

    private void insertTestConferences(SQLiteDatabase db) {
        insertConference(db,
                "همایش توسعه نرم‌افزار", "استاد احمدی", "امفی تئاتر فاز ۲",
                "۲۳ بهمن ۱۴۰۴", "۱۵:۳۰", Status.REGISTERING,
                android.R.drawable.ic_menu_gallery, false,
                "لورم ایپسوم متن ساختگی با تولید سادگی نامفهوم از صنعت چاپ، و با استفاده از طراحان گرافیک است.",
                "لورم ایپسوم متن ساختگی با تولید سادگی نامفهوم از صنعت چاپ.",
                3, "توجه : سه روز قبل از برگزاری همایش مهلت ثبت نام به پایان میرسد",
                false, null);

        insertConference(db,
                "همایش هوش مصنوعی", "استاد رضایی", "سالن آمفی تئاتر",
                "۲۴ بهمن ۱۴۰۴", "۱۰:۰۰", Status.FULL,
                android.R.drawable.ic_menu_gallery, false,
                "خلاصه مطالب همایش هوش مصنوعی.",
                "دانشکده فنی، ساختمان اصلی، سالن آمفی تئاتر",
                0, "ظرفیت این همایش تکمیل شده است.",
                false, null);

        insertConference(db,
                "همایش امنیت اطلاعات", "استاد محمدی", "ساختمان شماره ۳",
                "۲۵ بهمن ۱۴۰۴", "۱۴:۰۰", Status.REGISTERED,
                android.R.drawable.ic_menu_gallery, true,
                "خلاصه مطالب همایش امنیت اطلاعات و شبکه.",
                "ساختمان شماره ۳، طبقه دوم، اتاق ۲۰۱",
                10, "لطفاً کارت دانشجویی همراه داشته باشید.",
                false, null);

        insertConference(db,
                "همایش اینترنت اشیا (آنلاین)", "استاد کریمی", "آنلاین — لینک در سیستم",
                "۲۰ بهمن ۱۴۰۴", "۰۹:۰۰", Status.EXPIRED,
                android.R.drawable.ic_menu_gallery, false,
                "خلاصه مطالب همایش اینترنت اشیا.",
                "برگزاری آنلاین — لینک ورود در سیستم دانشگاه",
                0, "فیلم ضبط شده در دسترس است.",
                true, "https://example.com/download/iot-conference.mp4");
    }

    private void insertConference(SQLiteDatabase db,
                                   String title, String organizer, String place,
                                   String date, String time, Status status,
                                   int imageResId, boolean isFavorite,
                                   String summary, String address,
                                   int remainingCapacity, String note,
                                   boolean isOnline, String downloadUrl) {
        ContentValues v = new ContentValues();
        v.put(COL_TITLE,              title);
        v.put(COL_ORGANIZER,          organizer);
        v.put(COL_PLACE,              place);
        v.put(COL_DATE,               date);
        v.put(COL_TIME,               time);
        v.put(COL_STATUS,             status.name());
        v.put(COL_IMAGE_RES_ID,       imageResId);
        v.put(COL_IS_FAVORITE,        isFavorite ? 1 : 0);
        v.put(COL_SUMMARY,            summary);
        v.put(COL_ADDRESS,            address);
        v.put(COL_REMAINING_CAPACITY, remainingCapacity);
        v.put(COL_NOTE,               note);
        v.put(COL_IS_ONLINE,          isOnline ? 1 : 0);
        v.put(COL_DOWNLOAD_URL,       downloadUrl);
        db.insert(TABLE_CONF, null, v);
    }

    public boolean checkUser(String nationalCode, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COL_USER_NATIONAL_CODE},
                COL_USER_NATIONAL_CODE + " = ? AND " + COL_USER_PASSWORD + " = ?",
                new String[]{nationalCode, password},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public List<Conference> getAllConferences() {
        List<Conference> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_CONF, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                Conference conf = new Conference();
                conf.setId(c.getInt(c.getColumnIndexOrThrow(COL_ID)));
                conf.setTitle(c.getString(c.getColumnIndexOrThrow(COL_TITLE)));
                conf.setOrganizer(c.getString(c.getColumnIndexOrThrow(COL_ORGANIZER)));
                conf.setPlace(c.getString(c.getColumnIndexOrThrow(COL_PLACE)));
                conf.setDate(c.getString(c.getColumnIndexOrThrow(COL_DATE)));
                conf.setTime(c.getString(c.getColumnIndexOrThrow(COL_TIME)));
                conf.setStatus(Status.valueOf(c.getString(c.getColumnIndexOrThrow(COL_STATUS))));
                conf.setImageResId(c.getInt(c.getColumnIndexOrThrow(COL_IMAGE_RES_ID)));
                conf.setFavorite(c.getInt(c.getColumnIndexOrThrow(COL_IS_FAVORITE)) == 1);
                conf.setSummary(c.getString(c.getColumnIndexOrThrow(COL_SUMMARY)));
                conf.setAddress(c.getString(c.getColumnIndexOrThrow(COL_ADDRESS)));
                conf.setRemainingCapacity(c.getInt(c.getColumnIndexOrThrow(COL_REMAINING_CAPACITY)));
                conf.setNote(c.getString(c.getColumnIndexOrThrow(COL_NOTE)));
                conf.setOnline(c.getInt(c.getColumnIndexOrThrow(COL_IS_ONLINE)) == 1);
                conf.setDownloadUrl(c.getString(c.getColumnIndexOrThrow(COL_DOWNLOAD_URL)));
                list.add(conf);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }

    public void updateFavoriteStatus(Conference conference) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_IS_FAVORITE, conference.isFavorite() ? 1 : 0);
        db.update(TABLE_CONF, v, COL_ID + " = ?",
                new String[]{String.valueOf(conference.getId())});
        db.close();
    }

    public void updateConferenceStatus(int conferenceId, Status newStatus) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_STATUS, newStatus.name());
        db.update(TABLE_CONF, v, COL_ID + " = ?",
                new String[]{String.valueOf(conferenceId)});
        db.close();
    }
}
