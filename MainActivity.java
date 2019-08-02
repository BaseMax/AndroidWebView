package com.aterd.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
	private WebView browser;
	private Button restart, exit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.activity_main);
		restart= (Button) findViewById(R.id.restart);
		exit= (Button) findViewById(R.id.exit);

		restart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					finishAffinity();
					Intent intent = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(intent);
				} else {
					ActivityCompat.finishAffinity(MainActivity.this);
					Intent intent = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(intent);
				}

			}
		});

		exit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
				moveTaskToBack(true);
			}
		});

		browser = (WebView) findViewById(R.id.web_view);
		browser.getSettings().setLoadsImagesAutomatically(true);
		browser.getSettings().setJavaScriptEnabled(true);
		// browser.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		browser.getSettings().setBuiltInZoomControls(true);
		browser.getSettings().setGeolocationEnabled(true);
		browser.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 8.0.0; TA-1053 Build/OPR1.170623.026) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3368.0 Mobile Safari/537.36");
		browser.setWebChromeClient(new WebChromeClient() {
		 public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
			callback.invoke(origin, true, false);
		   }
		});
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			browser.setWebChromeClient(new WebChromeClient() {
				@Override
				public void onReceivedTitle(WebView view, String title) {
					getWindow().setTitle(title);
				}
			});
			browser.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					view.loadUrl(url);
					return false;
				}
			});
		}
		browser.loadUrl("https://github.com/");
//        createWebPagePrint(browser);
	}

	public  void createWebPagePrint(WebView webView) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
			PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();
			String jobName = "Web" + " Document";
			PrintAttributes.Builder builder = new PrintAttributes.Builder();
			builder.setMediaSize(PrintAttributes.MediaSize.ISO_A5);
			PrintJob printJob = printManager.print(jobName, printAdapter, builder.build());
			if(printJob.isCompleted()){
				Toast.makeText(getApplicationContext(), "print_complete", Toast.LENGTH_LONG).show();
			}
			else if(printJob.isFailed()){
				Toast.makeText(getApplicationContext(), "print_failed", Toast.LENGTH_LONG).show();
			}
		}
	}

}
