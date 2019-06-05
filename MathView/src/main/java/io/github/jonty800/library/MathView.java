package io.github.jonty800.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.x5.template.Chunk;
import com.x5.template.Theme;
import com.x5.template.providers.AndroidTemplates;


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

    private Chunk getChunk() {
        String template = "mathjax";
        AndroidTemplates loader = new AndroidTemplates(getContext());

        return new Theme(loader).makeChunk(template);
    }

    public void setText(String text) {
        mText = text;
        Chunk chunk = getChunk();

        String TAG_HEAD = "head";
        String TAG_FORMULA = "formula";
        chunk.set(TAG_HEAD, mHead);
        chunk.set(TAG_FORMULA, mText);
        this.loadDataWithBaseURL(null, chunk.toString(), "text/html", "utf-8", "about:blank");
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
}