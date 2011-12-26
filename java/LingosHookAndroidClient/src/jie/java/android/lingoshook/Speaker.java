package jie.java.android.lingoshook;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

public final class Speaker {

	private static Context _context;
	private static TextToSpeech _speaker;
	private static boolean _isReady = false;
	private static Locale _locale = Locale.ENGLISH;
	
	public static int init(Context context) {
		_context = context;
		_speaker = new TextToSpeech(_context, new TextToSpeech.OnInitListener(){

			@Override
			public void onInit(int status) {
				// TODO Auto-generated method stub
				if(status == TextToSpeech.SUCCESS) {
					int result = _speaker.setLanguage(_locale);
					if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
						Toast.makeText(_context, "TTS initialize failed.", Toast.LENGTH_LONG).show();
					}
					else {
						_isReady = true;
						_speaker.speak("Ready", TextToSpeech.QUEUE_FLUSH, null);
					}
				}
				else {
					Toast.makeText(_context, "TTS initialize failed.", Toast.LENGTH_LONG).show();
				}				
			}			
		});
		return 0;
	}
	
	public static void release() {
		if(_speaker != null) {
			_speaker.stop();
			_speaker.shutdown();
			
			_speaker = null;
		}
	}
	
	public static void speak(final String text) {
		if(_speaker != null && _isReady) {
			_speaker.speak(text, TextToSpeech.QUEUE_FLUSH, null);
		}
	}
}
