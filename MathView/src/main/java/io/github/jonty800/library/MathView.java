package io.github.jonty800.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.lang.ref.WeakReference;


public class MathView extends WebView {
    private String mText;
    private String mConfig;
    private String mHead;

    public MathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getSettings().setJavaScriptEnabled(true);
        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        setBackgroundColor(Color.TRANSPARENT);

        TypedArray mTypeArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MathView,
                0, 0
        );

        try { // the order of execution of setEngine() and setText() matters
            setText(mTypeArray.getString(R.styleable.MathView_text));
        } finally {
            mTypeArray.recycle();
        }
    }

    private String basicHead = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "    <head>\n" +
            "        {$head}\n" +
            "    </head>\n" +
            "    <body>\n" +
            "        {$formula}\n" +
            "    </body>\n" +
            "</html>";

    public void setNoCache(){
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    public void setHead(String head){
        /*
        <link rel="stylesheet" type="text/css" href="file:///android_asset/themes/style.css">
        <script type="text/x-mathjax-config">
            MathJax.Hub.Config({
                messageStyle: 'none',
                tex2jax: {preview: 'none'}
            });
        </script>
        <script type="text/x-mathjax-config">
            {$config}
        </script>
        <script type="text/javascript"
            src="file:///android_asset/MathJax/MathJax.js">
        </script>
         */
        mHead = "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/themes/style.css\">\n" +
                "        <script type=\"text/x-mathjax-config\">\n" +
                            mConfig +
                "        </script>\n" +
                "        <script type=\"text/javascript\"\n" +
                "            src=\"file:///android_asset/MathJax/MathJax.js\">\n" +
                "        </script>" + head;

    }

//    // disable touch event on MathView
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return false;
//    }

    public void setLoadAsync(boolean loadAsync) {
        this.loadAsync = loadAsync;
    }

    boolean loadAsync = false;

    LoadingTask loadingTask;
    @SuppressLint("StaticFieldLeak")
    public void setText(String text) {
        mText = text;

        if(loadAsync) {
            if (loadingTask != null)
                loadingTask.cancel(true);
            loadingTask = new LoadingTask() {
                @Override
                protected void onPostExecute(String html) {
                    super.onPostExecute(html);
                    loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
                }
            };
            loadingTask.execute();
        }else {
            String head = basicHead;
            if (mHead == null)
                head = head.replace("{$head}", "");
            else
                head = head.replace("{$head}", mHead);
            if (mText == null)
                head = head.replace("{$formula}", "");
            else
                head = head.replace("{$formula}", mText);
            loadDataWithBaseURL(null, head, "text/html", "utf-8", null);
        }
    }

    public void loadPlaceHolderText(String text) {
        loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
    }

    public String getText() {
        return mText;
    }

    /**
     * Tweak the configuration of MathJax.
     * The `config` string is a call statement for MathJax.Hub.Config().
     * For example, to enable auto line breaking, you can call:
     * setConfig.("MathJax.Hub.Config({
     *      CommonHTML: { linebreaks: { automatic: true } },
     *      "HTML-CSS": { linebreaks: { automatic: true } },
     *      SVG: { linebreaks: { automatic: true } }
     *  });");
     *
     * This method should be call BEFORE setText()
     * @param config
     */
    public void setConfig(String config) {
        this.mConfig = config;
    }

    private class LoadingTask extends AsyncTask<Void, Integer, String> {
        @Override
        protected String doInBackground(Void... params) {
            String head = basicHead;
            if(mHead==null)
                head = head.replace("{$head}", "");
            else
                head = head.replace("{$head}", mHead);
            if(mText==null)
                head = head.replace("{$formula}", "");
            else
                head = head.replace("{$formula}", mText);
            return head;

        }
    }
}