package app;

/**
 * Created by Judy T Raj on 04-Mar-17.
 */

public class Config {
    public static final String REGISTER_URL = "http://smartlbus.esy.es/register.php";
    public static final String DATA_URL = "http://www.smartlbus.esy.es/getLoc.php";
    public static final String TYPE_URL = "http://www.smartlbus.esy.es/getUserType.php?userid=";
    public static final String STUDENT_URL = "http://www.smartlbus.esy.es/getAllStudents.php?busno=";
    public static final String ADD_CHILD_URL = "http://www.smartlbus.esy.es/getChild.php?id=";
    public static final String BUS_URL = "http://www.smartlbus.esy.es/getRoute.php?busno=";
    public static final String ADDSTUDENT_URL = "http://www.smartlbus.esy.es/addStudent.php";
    public static final String SETPARENT_URL = "http://www.smartlbus.esy.es/addChild.php";
    public static final String ADDSTOP_URL = "http://www.smartlbus.esy.es/addStop.php";
    public static final String UPDSTUDENT_URL = "http://www.smartlbus.esy.es/updStudent.php";
    public static final String UPDBUS_URL = "http://www.smartlbus.esy.es/updBus.php";
    public static final String LOGIN_URL = "http://smartlbus.esy.es/login.php";
    public static final String UPDID_URL = "http://www.smartlbus.esy.es/updEmail.php";
    public static final String UPDPASS_URL = "http://smartlbus.esy.es/updPassword.php";
    public static final String DELETE_URL = "http://smartlbus.esy.es/delUser.php";
    public static final String DELETE_STOP_URL = "http://smartlbus.esy.es/delStop.php";
    public static final String DELETE_STUDENT_URL = "http://smartlbus.esy.es/delStudent.php";
    public static final String DRIVER_URL = "http://smartlbus.esy.es/getDriver.php?busno=";
    public static final String KEY_USERNAME="username";
    public static final String KEY_PASSWORD="password";
    public static final String KEY_NEW_USERNAME="newid";
    public static final String KEY_NEW_PASSWORD="newpass";
    public static final String DEST_LOCATION="9.673745,76.826303";
    public static final String STUDENT_ID = "id";
    public static final String DRIVER_NAME = "name";
    public static final String DRIVER_CON = "contact";
    public static final String STUDENT_NAME = "name";
    public static final String STUDENT_PARENT = "parent";
    public static final String STUDENT_BUS = "busno";
    public static final String BUS_ID = "serialno";
    public static final String BUS_NAME = "stopid";
    public static final String BUS_AM = "amtime";
    public static final String BUS_PM = "pmtime";
    public static final String STUDENT_CLASS = "class";
    public static final String STUDENT_STATUS = "status";
    public static final String STUDENT_STOP = "stopid";
    public static final String KEY_LAT = "latitude";
    public static final String KEY_TYPE = "usertype";
    public static final String KEY_LNG = "longitude";
    public static final String KEY_SPEED = "spd";
    public static final String JSON_ARRAY = "result";


    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";
}
