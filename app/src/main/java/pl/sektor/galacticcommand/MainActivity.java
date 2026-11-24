package pl.sektor.galacticcommand;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.view.View;
import android.view.WindowManager;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends Activity {
    private static final int PICK_JSON = 501;
    private static final int SAVE_JSON = 502;
    private WebView web;
    private ValueCallback<Uri[]> fileCallback;
    private String pendingJson;

    private void immersive() {
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        immersive();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        web = new WebView(this);
        web.setBackgroundColor(0xff06121a);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setAllowFileAccess(true);
        web.getSettings().setAllowContentAccess(true);
        web.getSettings().setDefaultTextEncodingName("UTF-8");
        web.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        web.setWebViewClient(new WebViewClient());
        web.setWebChromeClient(new WebChromeClient() {
            @Override public boolean onShowFileChooser(WebView view,
                    ValueCallback<Uri[]> callback, FileChooserParams params) {
                if (fileCallback != null) fileCallback.onReceiveValue(null);
                fileCallback = callback;
                try {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("application/json");
                    intent.putExtra(Intent.EXTRA_MIME_TYPES,
                        new String[]{"application/json","text/plain","application/octet-stream"});
                    startActivityForResult(intent, PICK_JSON);
                } catch (Exception ex) {
                    fileCallback = null;
                    callback.onReceiveValue(null);
                }
                return true;
            }
        });
        web.addJavascriptInterface(new Object() {
            @JavascriptInterface public void exportJson(String json, String filename) {
                runOnUiThread(() -> {
                    pendingJson = json;
                    Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("application/json");
                    intent.putExtra(Intent.EXTRA_TITLE,
                        filename != null && filename.endsWith(".json") ? filename : "sektor-b-backup.json");
                    try { startActivityForResult(intent, SAVE_JSON); }
                    catch (Exception e) { pendingJson = null; }
                });
            }
        }, "SektorAndroid");
        setContentView(web);
        web.loadUrl("file:///android_asset/www/index.html");
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_JSON) {
            if (fileCallback != null) {
                Uri[] selected = resultCode == RESULT_OK && data != null && data.getData() != null
                    ? new Uri[]{data.getData()} : null;
                fileCallback.onReceiveValue(selected);
                fileCallback = null;
            }
        } else if (requestCode == SAVE_JSON) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null && pendingJson != null) {
                try (OutputStream out = getContentResolver().openOutputStream(data.getData())) {
                    if (out != null) {
                        out.write(pendingJson.getBytes(StandardCharsets.UTF_8));
                        out.flush();
                    }
                } catch (Exception e) {
                    android.widget.Toast.makeText(this, "Błąd zapisu JSON", android.widget.Toast.LENGTH_LONG).show();
                }
            }
            pendingJson = null;
        }
        immersive();
    }

    @Override public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) immersive();
    }

    @Override public void onBackPressed() {
        if (web != null) {
            web.evaluateJavascript(
                "(function(){var o=document.getElementById('overlay');" +
                "if(o&&o.classList.contains('show')){document.getElementById('closeBtn').click();return true;}return false;})()",
                result -> { if (!"true".equals(result)) super.onBackPressed(); }
            );
        } else super.onBackPressed();
    }
}
