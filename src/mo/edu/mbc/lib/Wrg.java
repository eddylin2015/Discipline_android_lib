package mo.edu.mbc.lib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Wrg {
	public static void writeStudWrg2File(File file,String wrgid,String wrgtype,String wrg,String classno,String seat,String name,String stud_ref,String note) throws IOException
	{
		FileOutputStream out = new FileOutputStream(file,true);
		writeStudWrg2File(out,wrgid,wrgtype,wrg,classno,seat,name,stud_ref,note);
		out.flush();
   	 	out.close();
	}
	public static void writeStudWrg2File(FileOutputStream  out,String wrgid,String wrgtype,String wrg,String classno,String seat,String name,String stud_ref,String note) throws IOException
	{
   	    SimpleDateFormat formatter_time = new SimpleDateFormat("yyyy-MM-dd_H:mm", new Locale("en", "US"));
   	    Date today = new Date();
   	    String time_str = formatter_time.format(today);
   	    writeStudWrg2File(out,wrgid,wrgtype,wrg,classno,seat,name,stud_ref,note,time_str);
	}
	public static void writeStudWrg2File(FileOutputStream  out,String wrgid,String wrgtype,String wrg,String classno,String seat,String name,String stud_ref,String note,String datetime) throws IOException
	{
		StringBuilder sb=new StringBuilder();
		sb.append(wrgid).append("_").append(wrgtype).append("_").append(wrg).append("_").append(classno).append("_").append(seat).append("_").append(name).append("_").append(stud_ref).append("_");
		sb.append(note.replace("\n", "").replace("_", ""));
   	    SimpleDateFormat formatter_time = new SimpleDateFormat("yyyy-MM-dd_H:mm", new Locale("en", "US"));
   	    Date today = new Date();
   	    String time_str = formatter_time.format(today);
   	    sb.append("_").append(time_str).append("\n");
		out.write(sb.toString().getBytes());
	}	

}
