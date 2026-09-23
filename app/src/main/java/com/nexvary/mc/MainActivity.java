package com.nexvary.mc;

import android.app.*;
import android.os.*;
import android.content.*;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {
    LinearLayout root, content;
    TextView title, subtitle;
    Button selectPhotos, about, language;
    final String[] langs={"English","العربية","Türkçe","Español","Deutsch","Italiano","Français","اردو","فارسی","Русский"};
    final String[] tags={"en","ar","tr","es","de","it","fr","ur","fa","ru"};

    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        buildHome();
        handleIncoming(getIntent());
    }
    void buildHome(){
        root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(28,28,28,28); root.setBackgroundColor(Color.rgb(12,19,25));
        ScrollView scroll=new ScrollView(this); content=new LinearLayout(this); content.setOrientation(LinearLayout.VERTICAL); scroll.addView(content);
        title=text(getString(R.string.app_name),28); content.addView(title);
        subtitle=text(getString(R.string.tagline),17); content.addView(subtitle);
        selectPhotos=button(getString(R.string.select_photos)); selectPhotos.setOnClickListener(v->pickImages()); content.addView(selectPhotos);
        about=button(getString(R.string.about)); about.setOnClickListener(v->startActivity(new Intent(this,AboutActivity.class))); content.addView(about);
        language=button(getString(R.string.language)); language.setOnClickListener(v->showLanguage()); content.addView(language);
        TextView privacy=text(getString(R.string.offline_note),14); content.addView(privacy);
        root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1)); setContentView(root);
    }
    TextView text(String s,int sp){TextView t=new TextView(this);t.setText(s);t.setTextSize(sp);t.setTextColor(Color.WHITE);t.setPadding(12,18,12,18);return t;}
    Button button(String s){Button b=new Button(this);b.setText(s);b.setAllCaps(false);b.setMinHeight(56);return b;}
    void pickImages(){Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT);i.setType("image/*");i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);i.addCategory(Intent.CATEGORY_OPENABLE);startActivityForResult(i,100);}
    @Override protected void onActivityResult(int r,int c,Intent d){super.onActivityResult(r,c,d);if(r==100&&c==RESULT_OK&&d!=null){ArrayList<Uri> u=new ArrayList<>();if(d.getClipData()!=null)for(int i=0;i<d.getClipData().getItemCount();i++)u.add(d.getClipData().getItemAt(i).getUri());else if(d.getData()!=null)u.add(d.getData());showReview(u);}}
    void showReview(ArrayList<Uri> uris){content.removeAllViews();Button back=button("← "+getString(R.string.back));back.setOnClickListener(v->buildHome());content.addView(back);content.addView(text(getString(R.string.review_metadata),24));content.addView(text(getString(R.string.photos_selected)+": "+uris.size(),16));Button clean=button(getString(R.string.remove_metadata));clean.setOnClickListener(v->clean(uris));content.addView(clean);}
    void clean(ArrayList<Uri> uris){content.removeAllViews();content.addView(text(getString(R.string.cleaning),24));int ok=0;for(Uri u:uris){try{MetadataCleaner.clean(this,u);ok++;}catch(Exception ignored){}}content.removeAllViews();Button back=button("← "+getString(R.string.back));back.setOnClickListener(v->buildHome());content.addView(back);content.addView(text(getString(R.string.completed),24));content.addView(text(ok+" / "+uris.size()+" "+getString(R.string.cleaned_successfully),17));Button more=button(getString(R.string.clean_more));more.setOnClickListener(v->pickImages());content.addView(more);}
    void showLanguage(){new AlertDialog.Builder(this).setTitle(getString(R.string.language)).setItems(langs,(d,w)->setLanguage(tags[w])).show();}
    void setLanguage(String tag){Locale l=Locale.forLanguageTag(tag);Locale.setDefault(l);Configuration c=new Configuration(getResources().getConfiguration());c.setLocale(l);c.setLayoutDirection(l);getResources().updateConfiguration(c,getResources().getDisplayMetrics());recreate();}
    void handleIncoming(Intent i){if(i==null)return;String a=i.getAction();if(Intent.ACTION_SEND.equals(a)&&i.getParcelableExtra(Intent.EXTRA_STREAM)!=null){ArrayList<Uri> u=new ArrayList<>();u.add(i.getParcelableExtra(Intent.EXTRA_STREAM));showReview(u);}else if(Intent.ACTION_SEND_MULTIPLE.equals(a)){ArrayList<Uri> u=i.getParcelableArrayListExtra(Intent.EXTRA_STREAM);if(u!=null)showReview(u);}}
    @Override public void onBackPressed(){ if(content!=null && content.getChildCount()>0 && !(content.getChildAt(0)==title)){buildHome();} else super.onBackPressed(); }
}
