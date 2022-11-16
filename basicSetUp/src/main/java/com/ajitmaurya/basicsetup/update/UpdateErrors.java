

package com.ajitmaurya.basicsetup.update;

public enum UpdateErrors {

    /**
     * No internet connection.
     */

    NETWORK_NOT_AVAILABLE,


    /**
     * An error occurred when downloading updates.
     */

    ERROR_DOWNLOADING_UPDATES,


    /**
     * An error occurred while checking for updates.
     */

    ERROR_CHECKING_UPDATES,


    /**
     * Json file is missing.
     */

    JSON_FILE_IS_MISSING,


    /**
     * "The file containing information about the updates are empty.
     */

    FILE_JSON_NO_DATA

}
