

package com.ajitmaurya.basicsetup.update;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;

import com.ajitmaurya.basicsetup.utility.BasicFunctions;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.ThinDownloadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class HttpClient {


    /**
     * Information from the server about new ads app
     */

    public static class AsyncStringRequest {

        private Context context;
        private String url;
        private HttpCallback<UpdateInfo> listener;
        private OkHttpClient client;
        private Response response;
        private Request request;

        public AsyncStringRequest(Context context, String url, HttpCallback<UpdateInfo> listener) {
            this.context = context;
            this.url = url;
            this.listener = listener;
            this.client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS).build();

        }

        public void execute() {

            if (listener == null || context == null || client == null) {
                return;
            } else if (LibraryUtils.isNetworkAvailable(context)) {
                if (url == null || url.length() == 0) {
                    throw new RuntimeException("Argument Url cannot be null or empty");
                }
            } else {
                listener.onFailure(UpdateErrors.NETWORK_NOT_AVAILABLE);
                return;
            }


            request = new Request.Builder()
                    .url(this.url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    BasicFunctions.log("###", "onResponse: " + e.getLocalizedMessage());
                    listener.onFailure(UpdateErrors.ERROR_CHECKING_UPDATES);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {

                        if (response != null) {
                            try {

                                String jsonData = response.body().string();
                                BasicFunctions.log("###", "onResponse: " + jsonData);
                                UpdateInfo updateModel = JSONParser.parse(new JSONObject(
                                        jsonData)
                                );

                              /*  JSONObject data = new JSONObject(response.body().toString());

                                UpdateInfo updateModel = new UpdateInfo(data.getString("version"),
                                        data.getString("url"),
                                        data.getString("notes"));*/

                                if (updateModel != null) {
                                    listener.onSuccess(updateModel);
                                }

                            } catch (IOException | JSONException e) {
                                BasicFunctions.log("###", "onResponse: " + e.getLocalizedMessage());

                                listener.onFailure(UpdateErrors.ERROR_CHECKING_UPDATES);
                            }
                        } else {
                            listener.onFailure(UpdateErrors.FILE_JSON_NO_DATA);
                        }

                    } else {

                        if (response.code() == 404) {
                            listener.onFailure(UpdateErrors.JSON_FILE_IS_MISSING);
                        }

                    }
                }
            });

        }

    }

    /**
     * Download the updates from the server
     */

    public static class AsyncDownloadRequest {

        private Activity context;
        private String url;

        private CharSequence message;
        private String downloadFileName;
        private HttpCallback<File> listener;
        private DownloadRequest downloadRequest;

        public AsyncDownloadRequest(final Activity context, String url, CharSequence message,
                                    String downloadFileName, HttpCallback<File> listener) {
            this.context = context;
            this.url = url;
            this.message = message;
            this.downloadFileName = downloadFileName;
            this.listener = listener;
        }

        public void execute() {
            if (listener == null || context == null) {
                return;
            } else if (LibraryUtils.isNetworkAvailable(context)) {
                if (url == null || url.length() == 0) {
                    throw new RuntimeException("Argument Url cannot be null or empty");
                }
            } else {
                listener.onFailure(UpdateErrors.NETWORK_NOT_AVAILABLE);
                return;
            }

            StrictMode.VmPolicy.Builder builder2 = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder2.build());

            Uri downloadUri = Uri.parse(this.url);

            final File SDCardRoot = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/Download");

            if (SDCardRoot.exists() == false) {
                SDCardRoot.mkdirs();
            }

            File file = new File(SDCardRoot, downloadFileName);

            Uri destinationUri = Uri.parse(file.getPath());

            ThinDownloadManager downloadManager = new ThinDownloadManager(5);

            downloadRequest = new DownloadRequest(downloadUri)
                    .setDestinationURI(destinationUri);

            downloadManager.add(downloadRequest);

            downloadRequest.setStatusListener(new DownloadStatusListenerV1() {
                @Override
                public void onDownloadComplete(DownloadRequest downloadRequest) {
                    listener.onSuccess(new File(SDCardRoot, downloadFileName));
                }

                @Override
                public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode,
                                             String errorMessage) {
                    listener.onFailure(UpdateErrors.ERROR_DOWNLOADING_UPDATES);
                }

                @Override
                public void onProgress(DownloadRequest downloadRequest, long totalBytes,
                                       long downloadedBytes, int progress) {
                    listener.onProgress(downloadRequest, totalBytes, downloadedBytes, progress);
                }
            });

        }

        public DownloadRequest get() {
            return downloadRequest;
        }

    }

}
