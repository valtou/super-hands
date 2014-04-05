package com.example.markoquiz;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class Kukkanen extends Activity {

	Random rand = new Random();
	ImageView kukka;
	ImageView kannu;
	ImageView kukkasenkuuntelija;
	ProgressBar aPatukka;
	ProgressBar pistePatukka;
	int pisteet, loppupisteet = 0;
	int randomi = 0, aika;
	int kukkalaskuri = 0, kannulaskuri = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kukkanen);

		pisteet = 0;

		kukka = (ImageView) findViewById(R.id.kukka);
		kukkasenkuuntelija = (ImageView) findViewById(R.id.kukkasenkuuntelija);
		kannu = (ImageView) findViewById(R.id.kannu);

		pistePatukka = (ProgressBar) findViewById(R.id.progressPatukka);

		aikaPatukka();

		pistePatukka.setProgress(0);

		randomi = 3 + rand.nextInt(3);

		kukkasenkuuntelija.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				kukkalaskuri++;
				pisteet++;
				pistePatukka.setProgress(pisteet * 2);

				if (kukkalaskuri == randomi) {
					kukkalaskuri = 0;
					randomi = 3 + rand.nextInt(3);
					lisaaKannuun();

				}
				if (pisteet == 10) {
					kukka.setImageResource(R.drawable.pikkukukkajaisostaekatlehdet);
				}
				if (pisteet == 30) {
					kukka.setImageResource(R.drawable.tokakukka);
				}
				if (pisteet == 50) {
					kukka.setImageResource(R.drawable.kukkanenvika);
					kannu.setVisibility(View.GONE);
					kukkasenkuuntelija.setClickable(false);
					kukkaAani();
					loppupisteet += aika * 200;
				}

			}
		});

		kannu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (kannulaskuri == 0) {
					kannu.setImageResource(R.drawable.kannutaynna);
					kukkasenkuuntelija.setClickable(true);
				}
				if (kannulaskuri == 1) {
					kannu.setImageResource(R.drawable.kannuekapuna);
					kannulaskuri--;
				}
				if (kannulaskuri == 2) {
					kannu.setImageResource(R.drawable.kannutokapuna);
					kannulaskuri--;
				}
				if (kannulaskuri == 3) {
					kannu.setImageResource(R.drawable.kannukolmaspuna);
					kannulaskuri--;
				}
				if (kannulaskuri == 4) {
					kannu.setImageResource(R.drawable.kannutyhja);
					kukkasenkuuntelija.setClickable(false);
					kannulaskuri--;
				} else if (kannulaskuri < 0) {
					kannulaskuri = 0;
				}

			}
		});

	}

	public void lisaaKannuun() {

		kannulaskuri++;

		switch (kannulaskuri) {
		case -1:
			kannu.setImageResource(R.drawable.kannutaynna);
			kukkasenkuuntelija.setClickable(true);
			break;
		case 0:
			kannu.setImageResource(R.drawable.kannutaynna);
			kukkasenkuuntelija.setClickable(true);
			break;
		case 1:
			kannu.setImageResource(R.drawable.kannuekapuna);
			kukkasenkuuntelija.setClickable(true);
			break;
		case 2:
			kannu.setImageResource(R.drawable.kannutokapuna);
			kukkasenkuuntelija.setClickable(true);
			break;
		case 3:
			kannu.setImageResource(R.drawable.kannukolmaspuna);
			kukkasenkuuntelija.setClickable(true);
			break;
		case 4:
			kannu.setImageResource(R.drawable.kannutyhja);
			kukkasenkuuntelija.setClickable(false);
		}

	}

	public void TarkastaPisteet() {
		kukkasenkuuntelija.setClickable(false);
		kannu.setVisibility(View.GONE);
		if (pisteet >= 50) {
			loppupisteet += pisteet * 100;
			vikaAika();

		} else {
			Intent g = new Intent(getApplicationContext(), Havisit.class);
			g.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(g);
		}

	}

	public void aikaPatukka() {
		aPatukka = (ProgressBar) findViewById(R.id.aikaPatukka);
		aPatukka.setProgress(100);

		new CountDownTimer(25000, 100) {

			public void onTick(long millisUntilFinished) {
				aika = (int) (4 * (millisUntilFinished / 1000));
				aPatukka.setProgress(aika);
			}

			public void onFinish() {
				TarkastaPisteet();
			}
		}.start();
	}

	public void vikaAika() {
		new CountDownTimer(1000, 500) {

			public void onTick(long millisUntilFinished) {

			}

			public void onFinish() {
				Intent g = new Intent(getApplicationContext(),
						KukkaValiruutu.class);
				g.putExtra("ekatkukkapisteet", loppupisteet);
				startActivity(g);
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

	public void kukkaAani() {
		MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.kukkane);
		mPlayer.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.kukkanen, menu);
		return true;
	}

}
