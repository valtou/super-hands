package com.example.markoquiz;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.os.CountDownTimer;

public class SilitaTopia extends Activity {

	Random rand = new Random();

	int n, asd;
	int loppupisteet = 0;

	int pisteet = 0;
	int ekavaihto, tokavaihto, kolmasvaihto, neljasvaihto;
	ImageView topisilitysyks, topisilityskaks, topisilityskolme,
			topisilitysnelja;
	ImageView topisiliyks, topisilikaks, topisilikolme, topisilinelja;
	ProgressBar progressBar;
	ProgressBar aikaBar;

	protected void onCreate(Bundle savedInstanceState) {

		int eka = rand.nextInt(3);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_silita_topia);

		aikaPatukka();

		progressBar = (ProgressBar) findViewById(R.id.progressPatukka);
		progressBar.setProgress(pisteet);

		topisilitysyks = (ImageView) findViewById(R.id.topisilitysyks);
		topisilityskaks = (ImageView) findViewById(R.id.topisilityskaks);
		topisilityskolme = (ImageView) findViewById(R.id.topisilityskolme);
		topisilitysnelja = (ImageView) findViewById(R.id.topisilitysnelja);

		topisiliyks = (ImageView) findViewById(R.id.topisiliyks);
		topisilikaks = (ImageView) findViewById(R.id.topisilikaks);
		topisilikolme = (ImageView) findViewById(R.id.topisilikolme);
		topisilinelja = (ImageView) findViewById(R.id.topisilinelja);

		topisilitysyks.setVisibility(View.GONE);
		topisiliyks.setVisibility(View.GONE);
		topisilityskaks.setVisibility(View.GONE);
		topisilikaks.setVisibility(View.GONE);
		topisilityskolme.setVisibility(View.GONE);
		topisilikolme.setVisibility(View.GONE);
		topisilitysnelja.setVisibility(View.GONE);
		topisilinelja.setVisibility(View.GONE);

		switch (eka) {
		case 0:
			yks();
			break;
		case 1:
			kaks();
			break;
		case 2:
			kolme();
			break;
		case 3:
			nelja();
			break;
		}

	}

	public void yks() {

		n = 5 + rand.nextInt(10);
		ekavaihto = 0;

		topisilitysyks.setVisibility(View.VISIBLE);
		topisiliyks.setVisibility(View.VISIBLE);
		topisilityskaks.setVisibility(View.GONE);
		topisilikaks.setVisibility(View.GONE);
		topisilityskolme.setVisibility(View.GONE);
		topisilikolme.setVisibility(View.GONE);
		topisilitysnelja.setVisibility(View.GONE);
		topisilinelja.setVisibility(View.GONE);

		topisilitysyks.setOnTouchListener(new OnFlingGestureListener() {

			@Override
			public void onTopToBottom() {
				// Your code here
			}

			@Override
			public void onRightToLeft() {

				if (pisteet == 100) {
					topisilitysyks.setVisibility(View.GONE);
					topisiliyks.setVisibility(View.GONE);

				}

				ekavaihto++;

				if (ekavaihto == n) {
					int vaihto = rand.nextInt(2);
					switch (vaihto) {
					case 0:
						kaks();
						break;
					case 1:
						kolme();
						break;
					case 2:
						nelja();
						break;
					}
				}

				progressBar.setProgress(pisteet);
				pisteet += 4;
			}

			@Override
			public void onLeftToRight() {

				if (pisteet == 100) {
					topisilitysyks.setVisibility(View.GONE);
					topisiliyks.setVisibility(View.GONE);

				}

				ekavaihto++;

				if (ekavaihto == n) {
					int vaihto = rand.nextInt(2);
					switch (vaihto) {
					case 0:
						kaks();
						break;
					case 1:
						kolme();
						break;
					case 2:
						nelja();
						break;
					}
				}

				progressBar.setProgress(pisteet);
				pisteet += 4;
			}

			@Override
			public void onBottomToTop() {
				// Your code here
			}
		});
	}

	public void kaks() {

		n = 5 + rand.nextInt(10);
		tokavaihto = 0;

		topisilitysyks.setVisibility(View.GONE);
		topisiliyks.setVisibility(View.GONE);
		topisilityskaks.setVisibility(View.VISIBLE);
		topisilikaks.setVisibility(View.VISIBLE);
		topisilityskolme.setVisibility(View.GONE);
		topisilikolme.setVisibility(View.GONE);
		topisilitysnelja.setVisibility(View.GONE);
		topisilinelja.setVisibility(View.GONE);

		topisilityskaks.setOnTouchListener(new OnFlingGestureListener() {

			@Override
			public void onTopToBottom() {

				if (pisteet == 100) {
					topisilityskaks.setVisibility(View.GONE);
					topisilikaks.setVisibility(View.GONE);

				}

				tokavaihto++;

				if (tokavaihto == n) {
					int vaihto = rand.nextInt(2);
					switch (vaihto) {
					case 0:
						yks();
						break;
					case 1:
						kolme();
						break;
					case 2:
						nelja();
						break;
					}
				}

				progressBar.setProgress(pisteet);
				pisteet += 4;
			}

			@Override
			public void onRightToLeft() {
				// Your code here

			}

			@Override
			public void onLeftToRight() {
				// Your code here

			}

			@Override
			public void onBottomToTop() {

				if (pisteet == 100) {
					topisilityskaks.setVisibility(View.GONE);
					topisilikaks.setVisibility(View.GONE);

				}

				tokavaihto++;

				if (tokavaihto == n) {
					int vaihto = rand.nextInt(2);
					switch (vaihto) {
					case 0:
						yks();
						break;
					case 1:
						kolme();
						break;
					case 2:
						nelja();
						break;
					}
				}

				progressBar.setProgress(pisteet);
				pisteet += 4;
			}
		});
	}

	public void kolme() {

		n = 5 + rand.nextInt(10);
		kolmasvaihto = 0;

		topisilitysyks.setVisibility(View.GONE);
		topisiliyks.setVisibility(View.GONE);
		topisilityskaks.setVisibility(View.GONE);
		topisilikaks.setVisibility(View.GONE);
		topisilitysnelja.setVisibility(View.GONE);
		topisilinelja.setVisibility(View.GONE);
		topisilityskolme.setVisibility(View.VISIBLE);
		topisilikolme.setVisibility(View.VISIBLE);

		topisilityskolme.setOnTouchListener(new OnFlingGestureListener() {

			@Override
			public void onTopToBottom() {
				// Your code here

			}

			@Override
			public void onRightToLeft() {

				if (pisteet == 100) {
					topisilityskolme.setVisibility(View.GONE);
					topisilikolme.setVisibility(View.GONE);

				}

				kolmasvaihto++;

				if (kolmasvaihto == n) {
					int vaihto = rand.nextInt(2);
					switch (vaihto) {
					case 0:
						yks();
						break;
					case 1:
						kaks();
						break;
					case 2:
						nelja();
						break;
					}
				}

				progressBar.setProgress(pisteet);
				pisteet += 4;
			}

			@Override
			public void onLeftToRight() {

				if (pisteet == 100) {
					topisilityskolme.setVisibility(View.GONE);
					topisilikolme.setVisibility(View.GONE);

				}

				kolmasvaihto++;

				if (kolmasvaihto == n) {
					int vaihto = rand.nextInt(2);
					switch (vaihto) {
					case 0:
						yks();
						break;
					case 1:
						kaks();
						break;
					case 2:
						nelja();
						break;
					}
				}

				progressBar.setProgress(pisteet);
				pisteet += 4;
			}

			@Override
			public void onBottomToTop() {
				// Your code here
			}
		});
	}

	public void nelja() {

		n = 5 + rand.nextInt(10);
		neljasvaihto = 0;

		topisilitysyks.setVisibility(View.GONE);
		topisiliyks.setVisibility(View.GONE);
		topisilityskaks.setVisibility(View.GONE);
		topisilikaks.setVisibility(View.GONE);
		topisilityskolme.setVisibility(View.GONE);
		topisilikolme.setVisibility(View.GONE);
		topisilitysnelja.setVisibility(View.VISIBLE);
		topisilinelja.setVisibility(View.VISIBLE);

		topisilitysnelja.setOnTouchListener(new OnFlingGestureListener() {

			@Override
			public void onTopToBottom() {

				if (pisteet == 100) {
					topisilitysnelja.setVisibility(View.GONE);
					topisilinelja.setVisibility(View.GONE);

				}

				neljasvaihto++;

				if (neljasvaihto == n) {
					int vaihto = rand.nextInt(2);
					switch (vaihto) {
					case 0:
						yks();
						break;
					case 1:
						kaks();
						break;
					case 2:
						kolme();
						break;
					}
				}

				progressBar.setProgress(pisteet);
				pisteet += 4;
			}

			@Override
			public void onRightToLeft() {
				// Your code here

			}

			@Override
			public void onLeftToRight() {
				// Your code here

			}

			@Override
			public void onBottomToTop() {

				if (pisteet == 100) {
					topisilitysnelja.setVisibility(View.GONE);
					topisilinelja.setVisibility(View.GONE);

				}

				neljasvaihto++;

				if (neljasvaihto == n) {
					int vaihto = rand.nextInt(2);
					switch (vaihto) {
					case 0:
						yks();
						break;
					case 1:
						kaks();
						break;
					case 2:
						kolme();
						break;
					}
				}

				progressBar.setProgress(pisteet);
				pisteet += 4;
			}
		});

	}

	public void tarkastaPisteet() {
		if (pisteet > 98) {
			topisilitysyks.setVisibility(View.GONE);
			topisiliyks.setVisibility(View.GONE);
			topisilityskaks.setVisibility(View.GONE);
			topisilikaks.setVisibility(View.GONE);
			topisilitysnelja.setVisibility(View.GONE);
			topisilinelja.setVisibility(View.GONE);
			topisilityskolme.setVisibility(View.GONE);
			topisilikolme.setVisibility(View.GONE);

			Intent g = new Intent(getApplicationContext(), TopiValiruutu.class);
			loppupisteet = pisteet * 30 + asd * 100;
			g.putExtra("ekatpongot", loppupisteet);
			startActivity(g);
		} else {
			Intent g = new Intent(getApplicationContext(), Havisit.class);
			g.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(g);
		}

	}

	public void aikaPatukka() {
		aikaBar = (ProgressBar) findViewById(R.id.aikaPatukka);
		aikaBar.setProgress(100);

		new CountDownTimer(12500, 100) {

			public void onTick(long millisUntilFinished) {
				asd = (int) (8 * (millisUntilFinished / 1000));
				aikaBar.setProgress(asd);
			}

			public void onFinish() {
				tarkastaPisteet();
			}
		}.start();
	}

	@Override
	public void onBackPressed() {

	}

	@Override
	public void onPause() {
		finish();
		super.onPause();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.silita_topia, menu);
		return true;
	}

}
