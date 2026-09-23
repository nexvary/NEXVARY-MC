package com.nexvary.mc;

import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.Color;
import android.net.Uri;
import android.view.*;
import android.widget.*;

public class AboutActivity extends Activity {
 @Override public void onCreate(Bundle b){super.onCreate(b);build();}
 void build(){
  ScrollView s=new ScrollView(this);LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);l.setPadding(28,28,28,28);l.setBackgroundColor(Color.rgb(12,19,25));s.addView(l);
  Button back=new Button(this);back.setText("← "+getString(R.string.back));back.setOnClickListener(v->finish());l.addView(back);
  add(l,"NEXVARY MC",28);add(l,getString(R.string.about_text),16);
  link(l,getString(R.string.website),AppLinks.WEBSITE);link(l,"Facebook",AppLinks.FACEBOOK);link(l,getString(R.string.email),AppLinks.EMAIL);link(l,"YouTube",AppLinks.YOUTUBE);link(l,"X",AppLinks.X);
  setContentView(s);
 }
 void add(LinearLayout l,String x,int z){TextView t=new TextView(this);t.setText(x);t.setTextSize(z);t.setTextColor(Color.WHITE);t.setPadding(10,18,10,18);t.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);l.addView(t);}
 void link(LinearLayout l,String label,String url){Button b=new Button(this);b.setText(label);b.setAllCaps(false);b.setMinHeight(54);b.setOnClickListener(v->{try{startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));}catch(Exception e){Toast.makeText(this,getString(R.string.no_handler),Toast.LENGTH_SHORT).show();}});l.addView(b);}
}
