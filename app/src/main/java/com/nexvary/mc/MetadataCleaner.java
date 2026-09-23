package com.nexvary.mc;

import android.content.Context;
import android.net.Uri;
import java.io.*;

public final class MetadataCleaner {
 private MetadataCleaner(){}
 public static File clean(Context c, Uri source) throws IOException {
  File dir=new File(c.getFilesDir(),"cleaned"); if(!dir.exists()&&!dir.mkdirs()) throw new IOException("Cannot create output directory");
  File out=new File(dir,"clean_"+System.currentTimeMillis()+".jpg");
  try(InputStream in=c.getContentResolver().openInputStream(source); OutputStream os=new FileOutputStream(out)){
   if(in==null) throw new IOException("Cannot open image");
   byte[] data=readAll(in); byte[] cleaned=stripJpegMetadata(data); os.write(cleaned);
  }
  return out;
 }
 static byte[] readAll(InputStream in)throws IOException{ByteArrayOutputStream b=new ByteArrayOutputStream();byte[] x=new byte[8192];int n;while((n=in.read(x))!=-1)b.write(x,0,n);return b.toByteArray();}
 static byte[] stripJpegMetadata(byte[] d)throws IOException{
  if(d.length<4||(d[0]&255)!=0xFF||(d[1]&255)!=0xD8) return d;
  ByteArrayOutputStream o=new ByteArrayOutputStream();o.write(d,0,2);int p=2;
  while(p+3<d.length){
   if((d[p]&255)!=0xFF){o.write(d,p,d.length-p);break;}
   int marker=d[p+1]&255;
   if(marker==0xDA){o.write(d,p,d.length-p);break;}
   if(marker==0xD9){o.write(d,p,2);break;}
   int len=((d[p+2]&255)<<8)|(d[p+3]&255); if(len<2||p+2+len>d.length) throw new IOException("Malformed JPEG");
   boolean metadata=(marker>=0xE0&&marker<=0xEF)||marker==0xFE;
   if(!metadata)o.write(d,p,len+2);
   p+=len+2;
  }
  return o.toByteArray();
 }
}
