package mo.edu.mbc.lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Scanner;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.widget.ArrayAdapter;
/*
 * 公用程式庫
 */
public class Pub {
	/*獲取screen 大小
	 * int[] int_arr=new int[2];
	 * int_arr[0] : widthPixels;
	 * int_arr[1] : heightPixels;
	 */
	public static void getDislayWidthHeight(Activity act,Integer[] int_arr)
	{
		DisplayMetrics dm=new DisplayMetrics();
		act.getWindowManager().getDefaultDisplay().getMetrics(dm);
		if(int_arr.length==2)
		{
			int_arr[0]=dm.widthPixels;
			int_arr[1]=dm.heightPixels;
		}
	}
	/*SQLite DB File 讀取入hashTable
	 *<key,value> 
	 */
	public static void getSqlTohashTable(Hashtable<String,String> htb,File db_file,String sql,String[] keyfields,String[] fields)
	{
		if(!db_file.exists()) return;
		try
		{
			SQLiteDatabase myDataBase=SQLiteDatabase.openDatabase(db_file.getPath(),null,0);
			Cursor cursor=null;
			try
			{
				cursor = myDataBase.rawQuery(sql,null);
				if(cursor!=null && cursor.getCount()>0 && cursor.moveToFirst())
				{
					int[] iKey=new int[keyfields.length];
					int[] iValue=new int[fields.length];
					for(int i=0;i<iKey.length;i++)
					{
						iKey[i]=cursor.getColumnIndex(keyfields[i]);
					}
					for(int i=0;i<iValue.length;i++)
					{
						iValue[i]=cursor.getColumnIndex(fields[i]);
					}
					do
					{
						StringBuilder k=new StringBuilder();
                   	 	for(int i=0;i<iKey.length;i++)
                   	 	{
                   	 		if(keyfields[i].equals("C_NO")&& cursor.getString(iKey[i]).length()==1)
                   	 		{
                   	 			k.append("0");
                   	 		}
                   	 		k.append(cursor.getString(iKey[i]));
                   	 	}
                   	 	StringBuilder v=new StringBuilder();
                   	 	for(int i=0;i<iValue.length;i++)
                   	 	{
                   	 		v.append(cursor.getString(iValue[i]));
                   	 		if(i<iValue.length-1) v.append("_");
                   	 	}
                   	 	htb.put(k.toString(),v.toString());
					}while(cursor.moveToNext());
				}
			}catch(Exception e)
			{
			}finally
			{
				if(cursor!=null && !cursor.isClosed())  cursor.close();
			}
			myDataBase.close();
		}catch(Exception e){ }
	}
	/*File 獲取 檔案
	 * externalstoragedirectory/prefix_str+current_date.txt
	 */
	public static File getCurrentDateFileFromSDCard(String prefix_str)
	{
		Date today; 
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", new Locale("en", "US"));
		today = new Date();
		String date_str = formatter.format(today);
		String FILE_NAME =prefix_str+date_str+".txt";
		File extDir=Environment.getExternalStorageDirectory();
		File file = new File(extDir , FILE_NAME);
		return file;
	}
	/*讀取db 至 ArrayAdapter<String>
	 * 主要用於ListView,
	 *  String classno="SC1A";
     *	ArrayAdapter<String> adapter =new  ArrayAdapter<String>(
    	        this, android.R.layout.simple_spinner_item);
     *	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     *	spinner.setAdapter(adapter);
     *  File db_file=new File(Environment.getExternalStorageDirectory(),"STUDMAINSQLITE.db");
     *  String[] fields={"CLASSNO","C_NO","NAME_C","STUD_ID"};
     *  StringBuilder sql=new StringBuilder();
     *  sql.append("Select GRADE||CLASS CLASSNO,C_NO,NAME_C,STUD_ID from student where GRADE||CLASS='");
     *  sql.append(classno);
     *  sql.append("';");
     *	mo.edu.mbc.lib.Pub.readDBToArrayAdapter(adapter_classstud_temp, db_file,sql.toString(), fields);
	 */
	public static void readDBToArrayAdapter( ArrayAdapter<String> MyArrayAdapter,File db_file,String sql,String[] fields)
	{
		if(!db_file.exists()) return;
		try
		{
            SQLiteDatabase myDataBase=SQLiteDatabase.openDatabase(db_file.getPath(),null,0);
            Cursor cursor=null;
             try
             {
                 cursor = myDataBase.rawQuery(sql,null);
                 if(cursor!=null && cursor.getCount()>0 && cursor.moveToFirst())
                 {
                	 int[] iValue=new int[fields.length];
                	 for(int i=0;i<iValue.length;i++)
                	 {
                		 iValue[i]=cursor.getColumnIndex(fields[i]);
                	 }
                	 do
                	 {
                		 StringBuilder v=new StringBuilder();
                		 for(int i=0;i<iValue.length;i++)
                		 {
                			 v.append(cursor.getString(iValue[i]));
                			 if(i<iValue.length-1) v.append("_");
                		 }
                		 MyArrayAdapter.add(v.toString());
                	 }while(cursor.moveToNext());
                   }
             	}catch(Exception e)
             	{
             	}finally
             	{
             		if(cursor!=null && !cursor.isClosed())  cursor.close();
             	}
             	myDataBase.close();
		  }catch(Exception e)
		  { 
		  }
		  MyArrayAdapter.notifyDataSetChanged();
	}
	/*讀取TXT 至 ArrayAdapter<String>
	 * 
	 *    File file=mo.edu.mbc.lib.Pub.getCurrentDateFileFromSDCard("LaterRecord");
	 *    MyArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
	 *    listview.setAdapter(MyArrayAdapter);
	 *    mo.edu.mbc.lib.Pub.readFileToArrayAdapter(MyArrayAdapter, file );
	 */
	public static void readFileToArrayAdapter( ArrayAdapter<String> MyArrayAdapter,File file)
	{
		if(!file.exists()) return;
		Scanner scanner=null;
		try{
			scanner=new Scanner(file);
			while(scanner.hasNextLine())
			{
				String line=scanner.nextLine();
				MyArrayAdapter.add(line);
			}
		}catch(FileNotFoundException fnfe)
		{
		}finally
		{
			scanner.close();
		}
		MyArrayAdapter.notifyDataSetChanged();
	}

	
}