package com.aepl.atcu.util;

public class Constants {

	// Private constructor to prevent instantiation
    private Constants() {
        throw new UnsupportedOperationException("Utility class - cannot be instantiated");
    }

    // Base URL
    public static final String BASE_URL = "http://aepl-tcu4g-qa.accoladeelectronics.com:6102";

    // General URLs
    public static final String LOGIN_URL = BASE_URL + "/login";
    public static final String EXP_FRGT_PWD_URL = BASE_URL + "/login";
    public static final String DASH_URL = BASE_URL + "/device-dashboard";
    public static final String GOV_LINK = BASE_URL + "/govt-servers";
    public static final String DEVICE_LINK = BASE_URL + "/model";
    public static final String ADD_MODEL_LINK = BASE_URL + "/model-firmware";
    public static final String USR_MAN = BASE_URL + "/user-tab";
    public static final String USR_PROFILE = BASE_URL + "/profile";
    public static final String ROLE_MANAGEMENT = BASE_URL + "/user-role";
    public static final String PROD_DEVICE_LINK = BASE_URL + "/production-device-page";
    public static final String DISP_DEVICE_LINK = BASE_URL + "/dispatch-device-page";
    public static final String CREATE_DIS_DEVICE_LINK = BASE_URL + "/dispatch-device-add-page";
    public static final String ROLE_GROUP = BASE_URL + "/role-group";
    public static final String OTA_LINK = BASE_URL + "/ota-batch-page";
    public static final String SIM_MANUAL_UPLOAD = BASE_URL + "/sensorise-sim-manual-upload";
    public static final String DEVICE_URL = "http://aepl-tcu4g-qa.accoladeelectronics.com:6102/device-batch";
    public static final String CHANGE_MOBILE_VIEW = BASE_URL + "/change-mobile";

    // Footer
    public static final String EXP_VERSION_TEXT = "Version: 4.2.3";
    public static final String EXP_COPYRIGHT_TEXT = "Accolade Electronics Pvt. Ltd.";

    // Validation Error Messages
    public static final String blank_input_box_error = "This field is required and can't be only spaces.";
    public static final String email_error_msg_02 = "Please enter a valid Email ID.";
    public static final String password_error_msg_01 = "Please Enter Password";
    public static final String password_error_msg_02 = "Minimum 8 characters required.";

    // Toast messages
    public static final String toast_error_msg = "Invalid credentials!!";
    public static final String toast_error_msg_01 = "User Not Found";
    public static final String toast_error_msg_02 = "login Failed due to Incorrect email or password";   //login Failed due to Incorrect email or password
    public static final String toast_error_msg_03 = "Validation Error";
}
