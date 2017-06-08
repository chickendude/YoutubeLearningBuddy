package ch.ralena.youtubelearningbuddy.tools;

import android.content.Context;
import android.widget.Toast;

public class Toaster {
	public static void makeToast(Context context, String message) {
		if (context != null) {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}
	}
}
