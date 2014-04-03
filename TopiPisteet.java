package com.example.markoquiz;


import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class TopiPisteet extends Activity {

	ImageView eka, toka, kolmas;
	int pisteet = 0;
	ImageView tulokset;
	Button omienpaalle;
	String nimi = null;
	
	Button tausta;
	TextView huipputulokset;
	TextView omatpisteet;
	
	static String TABLE_NAME = "TOPITULOKSET";
	SQLiteDatabase topiDB = null;
	Map<String, Integer> tulosMap = new HashMap<String, Integer>();
	Cursor kursori = null;

	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_elamat);
		
		int pistelaskuri = 0;
		Intent mIntent = getIntent();
		int pisteete = mIntent.getIntExtra("vikatpongot", pistelaskuri);
		pisteet += pisteete;
		
		 try
	        {
			 topiDB = openOrCreateDatabase("NIMI", MODE_PRIVATE, null);
	         if (topiDB == null){
	        	  createTable();
	        	  
	          }
	          
	        lookupData();
	           
	        }
	        catch (SQLiteException se)
	        {
	            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
	        }
	        finally
	        {
	 
	            if (topiDB != null)
	           
	            	topiDB.close();
	        }
		 
		tulokset = (ImageView)findViewById(R.id.tulokset);
		omienpaalle = (Button)findViewById(R.id.omienpaalle);	
		tausta = (Button)findViewById(R.id.taustapaalle);
		
		huipputulokset = (TextView)findViewById(R.id.tuloslista);
		omatpisteet = (TextView)findViewById(R.id.omatpisteet);
		
		String tulosString ="";
		int i = 0;
		
		for (Entry<String, Integer> entry  : entriesSortedByValues(tulosMap)) {
			i++;
		    tulosString += i + ". " + (entry.getKey() + " " + entry.getValue()) + "\n";
		}
			
		huipputulokset.setText(tulosString);
		 
		omatpisteet.setText("SAIT " + pisteet + " PISTETTÄ");
		 
		omienpaalle.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					lisaaTulos();
					
				}
			});
			
	}
	
    
    private void createTable()
    {
    	topiDB.execSQL("CREATE TABLE IF NOT EXISTS " +
                    TABLE_NAME +
                    " (NIMI STRING, " +
                    "  PISTEET INT(8));");
    }
    
    
    private void insertOmaData(){
    	
    	topiDB.execSQL("INSERT INTO " + TABLE_NAME + " Values ('" + nimi + "','" + pisteet + "');");
    }
 
    
    private void lookupData()
    {
        kursori = topiDB.rawQuery("SELECT NIMI, PISTEET FROM " +
                TABLE_NAME +
                " where PISTEET > 0 ORDER BY PISTEET DESC ", null);
 
        if (kursori != null)
        {
            if (kursori.moveToFirst())
            {
                do
                {
                    String pelaajanNimi = kursori.getString(kursori.getColumnIndex("NIMI"));
                    int piste = kursori.getInt(kursori.getColumnIndex("PISTEET"));
                    tulosMap.put(pelaajanNimi, piste);   
                   
                } while (kursori.moveToNext());
            }
            kursori.close();
        }
    }
    
    
	@Override
	public void onBackPressed() {
		Intent i = new Intent(getApplicationContext(),Main.class);
		i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);   
        startActivity(i);
		
	}
	
	
    @Override
    public void onPause() {
        finish();
        super.onPause();

    }

    
    public void lisaaTulos(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(TopiPisteet.this);
		alertDialog.setTitle("SILITTÄMISTULOKSET");
		
		alertDialog.setMessage("SYÖTÄ NIMI");
		final EditText input = new EditText(TopiPisteet.this);  
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
		                        LinearLayout.LayoutParams.MATCH_PARENT,
		                        LinearLayout.LayoutParams.MATCH_PARENT);
		input.setLayoutParams(lp);
		alertDialog.setView(input);
		  
            
              alertDialog.setPositiveButton("OK",
                      new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog,int which) {

                              nimi = input.getText().toString();
                              paivitaPisteet();
                                                  
                          }
                          
                      });
              
              alertDialog.setNegativeButton("NO",
                      new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int which) {
                           
                              dialog.cancel();
                          }
                      });
              
              alertDialog.show();
    }
    
    
    public void paivitaPisteet(){
    	 try
	        {
    		 topiDB = openOrCreateDatabase("NIMI", MODE_PRIVATE, null);
	            createTable();
	            insertOmaData();
	            lookupData();
	           
	        }
	        catch (SQLiteException se)
	        {
	            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
	        }
	        finally
	        {
	 
	            if (topiDB != null)
	            	
	            	topiDB.close();
	        }
    	 
    	 paivitaKentta();
        
    }
    
    
    public void paivitaKentta(){
        String tokaString ="";
        
        huipputulokset.setText(tokaString);
        
		int j = 0;
		
		for (Entry<String, Integer> entry  : entriesSortedByValues(tulosMap)) {
			j++;
		    tokaString += j + ". " + (entry.getKey() + " " + entry.getValue()) + "\n";
		}
			
		huipputulokset.setText(tokaString);
		huipputulokset.invalidate();
		omienpaalle.setClickable(false);
    }
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.elamat, menu);
		return true;
	}
	
	static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
            new Comparator<Map.Entry<K,V>>() {
                @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                	int res= e2.getValue().compareTo(e1.getValue());
                    return res != 0 ? res : 1;
                }
            }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

	
}
