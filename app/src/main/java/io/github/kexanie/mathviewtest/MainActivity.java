package io.github.kexanie.mathviewtest;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import io.github.jonty800.library.MathView;

public class MainActivity extends AppCompatActivity {
    MathView formula_two;
    String mathml =
            "$x = 2$" +
            "<br><br> $$ x = 13 $$"
            + "<br><br>  $H(2; 1; \\Phi(\\tau))^{(1)}$" +
            "<br><br>  \\(O(h^{2}+\\tau ^{2})\\)" +
                    "" +
                    "" +
                    "" +
                    "<br><br>" +
                    "<br><br>" +
                    "<div><h2>Abstract</h2><p>This paper deals with the parabolic-parabolic chemotaxis system<span><span><math>{ut=∇⋅(D(u)∇u)−∇⋅(S(u)∇φ(v))+f(u),x∈Ω,t&gt;0vt=△v−v+u,x∈Ω,t&gt;0</math></span></span> in a bounded domain <span><math>Ω⊂Rn(n≥1)</math></span> with smooth boundary conditions, <span><math>D,S∈C2([0,+∞))</math></span> nonnegative, with <span><math>D(u)=a0(u+1)−α</math></span> for <span><math>a0&gt;0</math></span> and <span><math>α&lt;0</math></span>, <span><math>0≤S(u)≤b0(u+1)β</math></span> for <span><math>b0&gt;0,β∈R</math></span> and the singular sensitivity satisfies <span><math>0&lt;φ′(v)≤χvk</math></span> for <span><math>χ&gt;0,k≥1</math></span>. In addition, <span><math>f:R→R</math></span> is a smooth function satisfying <span><math>f(s)≡0</math></span> or generalizing the logistic source <span><math>f(s)=rs−μsm</math></span> for all <span><math>s≥0</math></span> with <span><math>r∈R,μ&gt;0</math></span> and <span><math>m&gt;1</math></span>. It is showed that, for the case without growth source, if <span><math>2β−α&lt;2</math></span>, the corresponding system possesses a globally bounded classical solution. While for the case with logistic source, if <span><math>2β+α&lt;2</math></span> and <span><math>n=1</math></span> or <span><math>n≥2</math></span> with <span><math>m&gt;2β+1</math></span>, the corresponding system has a globally classical solution.</p></div>" +
                    "<br><br>" +
                    "<br><br>" +
                    "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        formula_two = (MathView) findViewById(R.id.formula_two);
        formula_two.setConfig("MathJax.Hub.Config({\n" +
                "    extensions: [\"tex2jax.js\", \"mml2jax.js\"],\n" +
                "    jax: [\"input/TeX\", \"output/SVG\"],\n" +
                "    tex2jax: {\n" +
                "      inlineMath: [ ['$','$'], [\"\\\\(\",\"\\\\)\"] ],\n" +
                "      displayMath: [ ['$$','$$'], [\"\\\\[\",\"\\\\]\"] ],\n" +
                "      processEscapes: true\n" +
                "    },\n" +
                //"    \"HTML-CSS\": { fonts: [\"TeX\"] }\n" +
                "  });");
        formula_two.setText(mathml);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
