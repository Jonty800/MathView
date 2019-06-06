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

import com.x5.template.Chunk;
import com.x5.template.Theme;
import com.x5.template.providers.AndroidTemplates;

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

    boolean loadAsync = true;



    LoadingTask loadingTask;
    @SuppressLint("StaticFieldLeak")
    public void setText(String text) {
        mText = text;

        if(loadAsync) {
            if (loadingTask != null)
                loadingTask.cancel(true);
            loadingTask = new LoadingTask() {
                @Override
                protected void onPostExecute(Chunk chunk) {
                    super.onPostExecute(chunk);
                    loadDataWithBaseURL(null, chunk.toString(), "text/html", "utf-8", null);
                }
            };
            loadingTask.execute();
        }else{
            Chunk chunk = getChunk();

            String TAG_HEAD = "head";
            String TAG_FORMULA = "formula";
            chunk.set(TAG_HEAD, mHead);
            chunk.set(TAG_FORMULA, mText);
            loadDataWithBaseURL(null, chunk.toString(), "text/html", "utf-8", null);
        }
    }

    public void loadPlaceHolderText(String text) {
        loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
    }

    public String getText() {
        return mText;
    }

    private Chunk getChunk() {
        String template = "mathjax";
        AndroidTemplates loader = new AndroidTemplates(getContext());

        return new Theme(loader).makeChunk(template);
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

    private class LoadingTask extends AsyncTask<Void, Integer, Chunk> {
        @Override
        protected Chunk doInBackground(Void... params) {
            Chunk chunk = getChunk();

            String TAG_HEAD = "head";
            String TAG_FORMULA = "formula";
            chunk.set(TAG_HEAD, mHead);
            chunk.set(TAG_FORMULA, mText);

            return chunk;
        }

        private Chunk getChunk() {
            String template = "mathjax";
            AndroidTemplates loader = new AndroidTemplates(getContext());

            return new Theme(loader).makeChunk(template);
        }
    }
}