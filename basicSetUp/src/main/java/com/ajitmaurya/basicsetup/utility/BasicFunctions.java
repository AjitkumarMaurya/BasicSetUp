package com.ajitmaurya.basicsetup.utility;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.widget.NestedScrollView;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ajitmaurya.basicsetup.BuildConfig;
import com.ajitmaurya.basicsetup.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okio.ByteString;


public class BasicFunctions {

    public final static Pattern INDIAN_MOBILE_NUMBER = Pattern.compile("^[6-9]\\d{9}$");
    private static final String IMAGEPATHTOSAVE = "/download";
    private static final float maxHeight = 1280.0f;
    private static final float maxWidth = 1280.0f;

    public static boolean checkMobileNumber(String value) {
        return value != null && value.length() > 7 && value.length() < 16;
    }

    public static void setSystemBarColor(Activity act, @ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.clearFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
            }
            window.setStatusBarColor(ContextCompat.getColor(act, color));
        }
    }


    public static int dpToPx(Context c, int dp) {
        if (c != null) {
            Resources r = c.getResources();
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
        } else {
            return 0;
        }
    }


    public static void displayImageRound(final Context ctx, final ImageView img, @DrawableRes int drawable) {
        try {
            Glide.with(ctx).asBitmap().load(drawable).into(new BitmapImageViewTarget(img) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(ctx.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    img.setImageDrawable(circularBitmapDrawable);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayImageRoundUrl(final Context ctx, final ImageView img, String urlimg, int placeholder, int error) {
        try {
            Glide.with(ctx).asBitmap().load(urlimg).apply(new RequestOptions().placeholder(placeholder).error(error))
                    .into(new BitmapImageViewTarget(img) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(ctx.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            img.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void displayImageBG(Context ctx, ImageView img, String drawable) {
        try {
            Glide.with(ctx).load(drawable).into(img);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    public static void displayImageOriginal(Context ctx, ImageView img, @DrawableRes int drawable, int placeholder, int error) {
        try {
            Glide.with(ctx).load(drawable).apply(new RequestOptions().placeholder(placeholder).error(error))
                    .into(img);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static void viewImageFromLatLong(Context ctx, ImageView img, String latitudelongitude, String keyGM) {
        String mapUrl = "https://maps.googleapis.com/maps/api/staticmap?zoom=15&size=600x300&maptype=roadmap&markers=color:green%7Clabel:G%7C"
                + latitudelongitude + "&key=" + keyGM;

        try {
            Glide.with(ctx).load(mapUrl)
                    .into(img);
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public static void displayImage(Context ctx, ImageView img, String url, int placeholder, int error) {
        try {
            Glide.with(ctx).load(url).apply(new RequestOptions().placeholder(placeholder).error(error))
                    .into(img);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    public void displayImageDrawable(Context ctx, ImageView img, Drawable url) {
        try {
            Glide.with(ctx).load(url).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true))
                    .into(img);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    public static void displayImageProfileCir(Context ctx, CircularImageView img, String url, int placeholder, int error) {
        try {
            Glide.with(ctx).load(url).apply(new RequestOptions().placeholder(placeholder).error(error))
                    .into(img);
        } catch (Exception e) {
            e.printStackTrace();


        }
    }


    public static void displayImageProfileCir(Context ctx, CircleImageView img, String url, int placeholder, int error) {
        try {
            Glide.with(ctx).load(url).apply(new RequestOptions().placeholder(placeholder).error(error))
                    .into(img);
        } catch (Exception e) {
            e.printStackTrace();


        }
    }

    public static void displayImageURL(Context ctx, CircularImageView img, String url, int placeholder, int error) {
        try {
            Glide.with(ctx).load(url).apply(new RequestOptions().placeholder(placeholder).error(error))
                    .into(img);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static void displayImageURL(Context ctx, CircleImageView img, String url, int placeholder, int error) {
        try {
            Glide.with(ctx).load(url).apply(new RequestOptions().placeholder(placeholder).error(error))
                    .into(img);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static void nestedScrollTo(final NestedScrollView nested, final View targetView) {
        nested.post(new Runnable() {
            @Override
            public void run() {
                nested.scrollTo(500, targetView.getBottom());
            }
        });
    }


    public static void displayImage(Context ctx, CircularImageView img, int url) {
        try {
            Glide.with(ctx).load(url)
                    .into(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayImage(Context ctx, CircleImageView img, int url) {
        try {
            Glide.with(ctx).load(url)
                    .into(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void displayImageViewURI(Context ctx, ImageView img, Uri url) {
        try {
            Glide.with(ctx)
                    .load(new File(Objects.requireNonNull(url.getPath()))) // Uri of the picture
                    .into(img);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static void toast(Context ctx, String msg, int type) {
        Toast.makeText(ctx, "" + msg, Toast.LENGTH_LONG).show();
    }

    public static void log(String tag, String msg) {
        if (BuildConfig.DEBUG && msg != null) {
            Log.e(tag, msg);
        }
    }

    public static void logNetwork(String tag, String msg) {
        if (BuildConfig.DEBUG && msg != null) {
            Log.e(tag, msg);
        }
    }

    public static String capFirstLetter(String str) {
        String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
        return cap;
    }

    public static String toCamelCase(String str) {

        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {

                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;

                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }

    public static String capitalize(String capString) {

        if (capString == null || capString.length() < 1) {
            return "";
        }

        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }
        return capMatcher.appendTail(capBuffer).toString();
    }

    public static String getFileNameFromURL(String url) {
        if (url == null) {
            return "";
        }
        try {
            URL resource = new URL(url);
            String host = resource.getHost();
            if (host.length() > 0 && url.endsWith(host)) {
                // handle ...example.com
                return "";
            }
        } catch (MalformedURLException e) {
            return "";
        }

        int startIndex = url.lastIndexOf('/') + 1;
        int length = url.length();

        // find end index for ?
        int lastQMPos = url.lastIndexOf('?');
        if (lastQMPos == -1) {
            lastQMPos = length;
        }

        // find end index for #
        int lastHashPos = url.lastIndexOf('#');
        if (lastHashPos == -1) {
            lastHashPos = length;
        }

        // calculate the end index
        int endIndex = Math.min(lastQMPos, lastHashPos);
        return url.substring(startIndex, endIndex);
    }

    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        assert imageStream != null;
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(context, img, selectedImage);
        return img;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = Math.min(heightRatio, widthRatio);

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    public static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23) {
            assert input != null;
            ei = new ExifInterface(input);
        } else
            ei = new ExifInterface(Objects.requireNonNull(selectedImage.getPath()));

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    public static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public static String getIds(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();
        Set<String> keys = hashMap.keySet();
        for (String key : keys) {
            sb.append(key).append(",");
        }

        return removeLastCharacter(sb.toString());

    }

    public static String getIdsInteger(HashMap<Integer, String> hashMap) {
        StringBuilder sb = new StringBuilder();
        Set<Integer> keys = hashMap.keySet();
        for (Integer key : keys) {
            sb.append(key).append(",");
        }

        return removeLastCharacter(sb.toString());
    }

    public static String removeLastCharacter(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    @SuppressLint("HardwareIds")
    private static String getDeviceID(@NonNull Context context) {
        return Settings.Secure.getString(context.getContentResolver(), "android_id");
    }

    @NonNull
    private static String getPrecision(double d) {
        try {
            return String.format(Locale.ENGLISH, "%.2f", d);
        } catch (Exception unused) {
            return "0.00";
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String addDayInCurrentDate(String dateNew, int days) {
        SimpleDateFormat sdf = null;
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(dateNew)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, days);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        return sdf1.format(c.getTime());
    }

    public static String getReverseDate(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        String str2 = "-";
        String[] split = str.split(str2);
        if (split.length <= 2) {
            return str;
        }
        String sb = split[2] +
                str2 +
                split[1] +
                str2 +
                split[0];
        return sb;
    }

    public static Date convertStringDateToDate(String str) {
        DateFormat format = null;
        Date date = null;

        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static long getRoundingOffDownValue(double d, long j) {
        double d2 = (double) j;
        Double.isNaN(d2);
        double d3 = d % d2;
        return d3 > 0.0d ? Math.round(d - d3) : Math.round(d);
    }

    public static float getRoundingOffValue(double d, float f) {
        Double.isNaN((double) f);
        double d3 = d % (double) f;
        if (d3 > 0.0d) {
            Double.isNaN((double) f);
            d += (double) f - d3;
        }
        return (float) Math.round(d);
    }

    public static float getRoundingOffValue(float f, float f2) {
        double d = (double) (f % f2);
        if (d <= 0.0d) {
            return (float) Math.round(f);
        }
        Double.isNaN((double) f2);
        Double.isNaN(d);
        double d4 = (double) f2 - d;
        Double.isNaN((double) f);
        return (float) Math.round((double) f + d4);
    }

    public static long getRoundingOffValue(double d, int i) {
        Double.isNaN((double) i);
        double d3 = d % (double) i;
        if (d3 <= 0.0d) {
            return Math.round(d);
        }
        Double.isNaN((double) i);
        return Math.round(d + ((double) i - d3));
    }

    public static long getRoundingOffValue(double d, long j) {
        double d2 = (double) j;
        Double.isNaN(d2);
        double d3 = d % d2;
        if (d3 <= 0.0d) {
            return Math.round(d);
        }
        Double.isNaN(d2);
        return Math.round(d + (d2 - d3));
    }

    public static String getSimplifiedUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String replace = str.replace("\\", "/");
        String str2 = " ";
        return replace.contains(str2) ? replace.replace(str2, "%20") : replace;
    }

    public static void hideSoftKeyboard(@NonNull Activity activity) {
        @SuppressLint("WrongConstant")
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService("input_method");
        if (activity.getCurrentFocus() != null && inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void showSoftKeyboard(@NonNull Activity activity, View view) {
        @SuppressLint("WrongConstant")
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService("input_method");
        if (activity.getCurrentFocus() != null && inputMethodManager != null) {
            inputMethodManager.showSoftInput(view, 0);
        }
    }

    public static void hideSoftKeyboard(@NonNull Activity activity, View view) {
        if (view != null) {
            @SuppressLint("WrongConstant") InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService("input_method");
            if (activity.getCurrentFocus() != null && inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static boolean isValidGSTNumber(@NonNull String str) {
        if (str.length() != 15) {
            return false;
        }
        return Pattern.compile("[0-9]{2}[A-Za-z]{5}[0-9]{4}[A-Za-z]{1}[A-Za-z1-9]{1}Z{1}[0-9A-Za-z]{1}").matcher(str).matches();
    }

    public static boolean isValidLatLng(double d, double d2) {
        return d >= -90.0d && d <= 90.0d && d2 >= -180.0d && d2 <= 180.0d;
    }

    public static boolean isValidPanNumber(@NonNull String str) {
        return Pattern.compile("[A-Z,a-z]{5}[0-9]{4}[A-Z,a-z]").matcher(str).matches();
    }

    public static boolean isValidUrl(String str) {
        if (!TextUtils.isEmpty(str)) {
            return Patterns.WEB_URL.matcher(str.toLowerCase()).matches();
        }
        return false;
    }

    public static boolean isValidEmail(String str) {
        if (!TextUtils.isEmpty(str)) {
            return Patterns.EMAIL_ADDRESS.matcher(str.toLowerCase()).matches();
        }
        return false;
    }

    private static String getLastCharacterFromString(String str, int i) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (str.length() == i) {
            return str;
        }
        if (str.length() > i) {
            str = str.substring(str.length() - i);
        }
        return str;
    }

    public static String mobileNoValidation(@NonNull String str) {
        String str2 = "";
        return !TextUtils.isEmpty(str) ? getLastCharacterFromString(str.replaceAll("\\s+", str2).replace("-", str2).replace("(", str2).replace(")", str2).replace("+", str2).replaceAll("[^\\d.]", str2).replaceFirst("^0*", str2), 10) : str2;
    }

    public static Fragment getVisibleFragment(FragmentManager fm) {
        List<Fragment> fragments = fm.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    public static String basic(String username, String password, Charset charset) {
        String usernameAndPassword = username + ":" + password;
        String encoded = ByteString.encodeString(usernameAndPassword, charset).base64();
        return "Basic " + encoded;
    }

    public static boolean vpn(Context context) {
        String iface = "";
        boolean Value = false;
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp())
                    iface = networkInterface.getName();
                Log.d("DEBUG", "IFACE NAME: " + iface);
                if (iface.contains("tun") || iface.contains("ppp") || iface.contains("pptp")) {
                    Value = true;
                }
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
            Log.e("##", "VPN error" + e1.getLocalizedMessage());
        }

        return Value;
    }

    public static boolean hostAvailable(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 2000);
            return true;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
    }

    public static boolean isPackageInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return false;
        }
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static boolean isCameraPermissionGranted(Context ctx) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ctx.checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("##", "Permission is granted1");
                return true;
            } else {

                Log.v("##", "Permission is revoked1");
                ActivityCompat.requestPermissions((Activity) ctx, new String[]{Manifest.permission.CAMERA}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("##", "Permission is granted1");
            return true;
        }
    }

    public static boolean isReadStoragePermissionGranted(Context ctx) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ctx.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("##", "Permission is granted1");
                return true;
            } else {

                Log.v("##", "Permission is revoked1");
                ActivityCompat.requestPermissions((Activity) ctx, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("##", "Permission is granted1");
            return true;
        }
    }

    public static boolean isWriteStoragePermissionGranted(Context ctx) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ctx.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("##", "Permission is granted2");
                return true;
            } else {

                Log.v("##", "Permission is revoked2");
                ActivityCompat.requestPermissions((Activity) ctx, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("##", "Permission is granted2");
            return true;
        }
    }


    public static int getAlphabetImage(Context context, String alphabet) {
        String[] alphabetArray = context.getResources().getStringArray(R.array.alphabet_array);

        @SuppressLint("Recycle") final TypedArray alphabetIcon = context.getResources().obtainTypedArray(R.array.alphabet_icon);

        for (int i = 0; i < alphabetArray.length; i++) {
            if (alphabet.equalsIgnoreCase(alphabetArray[i])) {
                return alphabetIcon.getResourceId(i, -1);
            }
        }
        return -1;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getFormattedDate(String strDate) {

        try {
            SimpleDateFormat fmtOut = null;
            SimpleDateFormat fmt = null;
            Date date;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                fmt = new SimpleDateFormat("yyyy-MM-dd");
                date = fmt.parse(strDate);

                fmtOut = new SimpleDateFormat("dd-MM-yyyy");
                if (date != null) {
                    return fmtOut.format(date);
                }else {
                    return strDate;
                }
            } else {
                return strDate;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getFormattedDateYMD(String strDate) {

        if (strDate != null) {
            try {
                SimpleDateFormat fmtOut = null;
                SimpleDateFormat fmt = null;
                Date date;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    fmt = new SimpleDateFormat("dd-MM-yyyy");
                    date = fmt.parse(strDate);

                    fmtOut = new SimpleDateFormat("yyyy-MM-dd");
                    if (date != null) {
                        return fmtOut.format(date);
                    }
                } else {
                    return strDate;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";

    }

    @SuppressLint("SimpleDateFormat")
    public static String getFormattedDateYMD(String strDate, String inputPattern, String outputPattern) {

        if (strDate != null) {
            try {
                SimpleDateFormat fmtOut = null;
                SimpleDateFormat fmt = null;
                Date date;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    fmt = new SimpleDateFormat(inputPattern);
                    date = fmt.parse(strDate);

                    fmtOut = new SimpleDateFormat(outputPattern);
                    if (date != null) {
                        return fmtOut.format(date);
                    }
                } else {
                    return strDate;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";

    }

    public static String getOnlyStrings(String s) {
        Pattern pattern = Pattern.compile("[^a-z A-Z]");
        Matcher matcher = pattern.matcher(s);
        return matcher.replaceAll("");
    }

    public static String getOnlyDigits(String s) {
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(s);
        return matcher.replaceAll("");
    }

    public static boolean appInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception e) {
            log("@@",e.getLocalizedMessage());
        }

        return false;
    }

    public static void openWhatsAppConversationUsingUri(Context context, String numberWithCountryCode, String message) {

        Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + numberWithCountryCode + "&text=" + message);

        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);

        context.startActivity(sendIntent);
    }

    public static String getGSonData(Object obj) {
        return new Gson().toJson(obj);
    }

    public static String getFormattedAmount(String amount) {
        double d = Double.parseDouble(amount);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);
        return formatter.format(d);
    }

    public static String getDefaultPath(Context ctx) {
        StrictMode.VmPolicy.Builder builder2 = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder2.build());

        File mediaStorageDir = new File(ctx.getExternalFilesDir(null)
                + "/Android/data/"
                + ctx.getApplicationContext().getPackageName()
                + "/Files/PDFFiles/");

        File file = new File(ctx.getExternalFilesDir(null) + "/Fincasys/PDFFiles/");
        if (!file.exists()) {
            file.mkdirs();
        }

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdir();
        }

        return file.getAbsolutePath() + "/";
    }

    public static String getDefaultPathLanguage(Context ctx) {
        StrictMode.VmPolicy.Builder builder2 = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder2.build());

        File mediaStorageDir = new File(ctx.getExternalFilesDir(null)
                + "/Android/data/"
                + ctx.getApplicationContext().getPackageName()
                + "/Files/language/");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdir();
        }

        return mediaStorageDir.getAbsolutePath();
    }

    public static File getOutputMediaFile(Context ctx) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        StrictMode.VmPolicy.Builder builder2 = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder2.build());

        File file = new File(ctx.getExternalFilesDir(null) + "/Fincasys/PDFFiles/");
        if (!file.exists()) {
            file.mkdirs();
        }

        File mediaStorageDir = new File(ctx.getExternalFilesDir(null)
                + "/Android/data/"
                + ctx.getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        // Create a media file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new java.text.SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        File mediaFile;
        String mImageName = "IMAGE_" + timeStamp + ".jpg";
        mediaFile = new File(file.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public static File getOutputMediaVideoFile(Context ctx) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        StrictMode.VmPolicy.Builder builder2 = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder2.build());

        File file = new File(ctx.getExternalFilesDir(null) + "/Fincasys/PDFFiles/");
        if (!file.exists()) {
            file.mkdirs();
        }

        File mediaStorageDir = new File(ctx.getExternalFilesDir(null)
                + "/Android/data/"
                + ctx.getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        // Create a media file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new java.text.SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        File mediaFile;
        String mImageName = "VIDEO_" + timeStamp + ".mp4";
        mediaFile = new File(file.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public static File getOutputMediaFileFromCacheDir(Context ctx) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        StrictMode.VmPolicy.Builder builder2 = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder2.build());

        File file = getCacheDir(ctx);

        // Create a media file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new java.text.SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        File mediaFile;
        String mImageName = "IMAGE_" + timeStamp + ".jpg";
        mediaFile = new File(file.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public static File getCacheDir(Context ctx) {
        String state = null;
        try {
            state = Environment.getExternalStorageState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (state == null || state.startsWith(Environment.MEDIA_MOUNTED)) {
            try {
                File file = ctx.getExternalCacheDir();
                if (file != null) {
                    return file;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            File file = ctx.getCacheDir();
            if (file != null) {
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new File("");
    }


    public static String compressImage(Context ctx, String imagePath) {
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        try {
            options.inSampleSize = calculateInSampleSizeNew(options, actualWidth, actualHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        bmp.recycle();

        ExifInterface exif;
        try {
            exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            if (scaledBitmap != null) {
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filepath = getFilename(ctx);
        try {
            out = new FileOutputStream(filepath);

            //write the compressed bitmap at the destination specified by filename.
            if (scaledBitmap != null) {
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filepath;
    }

    public static int calculateInSampleSizeNew(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = Math.min(heightRatio, widthRatio);
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static String getFilename(Context ctx) {
        File mediaStorageDir = new File(ctx.getExternalFilesDir(null)
                + "/Android/data/"
                + ctx.getApplicationContext().getPackageName()
                + "/Files/Compressed");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }

        String mImageName = "IMG_" + System.currentTimeMillis() + ".jpg";
        return (mediaStorageDir.getAbsolutePath() + "/" + mImageName);
    }

    @SuppressLint("SimpleDateFormat")
    public static String timeConversion12to24(String twelveHoursTime) {

        try {
            DateFormat df = null;
            DateFormat outputformat = null;
            Date date = null;
            String output = twelveHoursTime;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                df = new SimpleDateFormat("hh:mm aa");
                outputformat = new SimpleDateFormat("HH:mm:ss");

                //Returns Date object
                date = df.parse(twelveHoursTime);

                //old date format to new date format
                if (date != null) {
                    output = outputformat.format(date);
                }
            }
            return output;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }

    public static int RoundFloatValue(float f) {
        int c = (int) ((f) + 0.5f);
        float n = f + 0.5f;
        return (n - c) % 2 == 0 ? (int) f : c;
    }

    public static long getTimeDiff(String time1, String time2) {
        long difference = 0;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("h:mm a");
                Date date1 = null, date2 = null;
                date1 = format.parse(time1);
                date2 = format.parse(time2);
                if (date2 != null) {
                    if (date1 != null) {
                        difference = date2.getTime() - date1.getTime();
                    }
                }
                difference = difference / 1000;

            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return difference;
    }

    public static String getUrlExtenssion(String serverPath) {
        if (!serverPath.contains(".")) {
            return null;
        }
        return serverPath.substring(serverPath.lastIndexOf("."));
    }

    public static String getMimeTypeExtenssion(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static String getDuration(File file) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(file.getAbsolutePath());
        String durationStr = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return formateMilliSeccond(Long.parseLong(durationStr));
    }

    public static String formateMilliSeccond(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        //      return  String.format("%02d Min, %02d Sec",
        //                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
        //                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
        //                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));

        // return timer string
        return finalTimerString;
    }

    public static boolean validateMicAvailability(Context context) {
        boolean available = true;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return false;
        }
        AudioRecord recorder =
                new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_DEFAULT, 44100);
        try {
            if (recorder.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED) {
                available = false;

            }

            recorder.startRecording();
            if (recorder.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
                recorder.stop();
                available = false;

            }
            recorder.stop();
        } finally {
            recorder.release();
            recorder = null;
        }

        return available;
    }

    public static boolean getMicrophoneAvailable(Context context) {
        MediaRecorder recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setOutputFile(new File(context.getCacheDir(), "MediaUtil#micAvailTestFile").getAbsolutePath());
        boolean available = true;
        try {
            recorder.prepare();
            recorder.start();

        } catch (Exception exception) {
            available = false;
        }
        recorder.release();
        return available;
    }

    @SuppressLint("SimpleDateFormat")
    public static boolean CompareTime(String one, String two) {

        boolean answer = false;
        String a = timeConversion12to24(one);
        String b = timeConversion12to24(two);

        SimpleDateFormat formatter5 = null;
        formatter5 = new SimpleDateFormat("HH:mm");
        try {
            Date date1 = null;
            if (a != null) {
                date1 = formatter5.parse(a);
            }
            Date date2 = null;
            if (b != null) {
                date2 = formatter5.parse(b);
            }

            if (date1 != null) {
                if (date1.after(date2)) {
                    if (date2 != null) {
                        log("##", date1.toString() + "before =>true" + date2.toString());
                    }
                    answer = true;
                } else {
                    log("##", "before => false");
                }
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return answer;
    }

    @SuppressLint("QueryPermissionsNeeded")
    public static void callDialer(Context context, String number) {

        if (number != null && number.length() > 2) {
            final Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
            if (dialIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(dialIntent);
            } else {
                toast(context, "Calling not supported.", 0);
            }
        }
    }

    public static String getCurrentDateTime() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String getCurrentDateTimeLong() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String getCurrentDateTime(String strFormet) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(strFormet, Locale.getDefault());
        return sdf.format(new Date());
    }

    public static boolean isNumeric(String str) {
        DecimalFormatSymbols currentLocaleSymbols = DecimalFormatSymbols.getInstance();
        char localeMinusSign = currentLocaleSymbols.getMinusSign();

        if (!Character.isDigit(str.charAt(0)) && str.charAt(0) != localeMinusSign) return false;

        boolean isDecimalSeparatorFound = false;
        char localeDecimalSeparator = currentLocaleSymbols.getDecimalSeparator();

        for (char c : str.substring(1).toCharArray()) {
            if (!Character.isDigit(c)) {
                if (c == localeDecimalSeparator && !isDecimalSeparatorFound) {
                    isDecimalSeparatorFound = true;
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    public static String[] getStringArray(JSONArray jsonArray) {
        String[] stringArray = null;
        if (jsonArray != null) {
            int length = jsonArray.length();
            stringArray = new String[length];
            for (int i = 0; i < length; i++) {
                stringArray[i] = jsonArray.optString(i);
            }
        }
        return stringArray;
    }

    public static void setSpinnerValue(Context context, Spinner spinner, String[] spinnerArray) {
        ArrayAdapter<String> aa = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinnerArray);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
    }

    public static void setSpinnerValue(Context context, AppCompatSpinner spinner, String[] spinnerArray) {
        ArrayAdapter<String> aa = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinnerArray);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
    }

    public static String getCurrentPassword(String societyId, String userId, String userMobile) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String date = sdf.format(new Date());
        String subMobile = getLastThreeChatFromString(userMobile);
        return userId + "@" + subMobile + "@" + societyId;
    }

    public static String getLastThreeChatFromString(String word) {
        if (word != null && word.length() == 3) {
            return word;
        } else if (word != null && word.length() > 3) {
            return word.substring(word.length() - 3);
        } else {
            // whatever is appropriate in this case
            return "";
        }
    }

    public static String generateRandomHexToken(int byteLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[byteLength];
        secureRandom.nextBytes(token);
        return new BigInteger(1, token).toString(16); //hex encoding
    }

    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public void activity_anim(Activity context) {
        context.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }
}
