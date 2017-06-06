package ch.ralena.youtubelearningbuddy.tools;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by crater on 06/06/17.
 */

public class Keyboard {

	public static void close(View view) {
		InputMethodManager imeManager =
				(InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imeManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static void open(View view) {
		InputMethodManager imeManager =
				(InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imeManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(),InputMethodManager.SHOW_FORCED, 0);
	}
}
