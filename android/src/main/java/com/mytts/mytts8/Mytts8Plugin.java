package com.mytts.mytts8;

import android.app.Activity;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.os.Handler;
import android.os.Bundle;
import java.util.Locale;
import java.util.UUID;
import java.util.HashMap;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

public class Mytts8Plugin implements MethodCallHandler {
  private Activity mActivity;
  private final Handler handler;
  private final MethodChannel channel;
  private TextToSpeech _mytts;
  private static final String tag = "MyTTS";
  String uuid;
  Bundle bundle;

  private Mytts8Plugin(Activity context, MethodChannel channel) {
    this.mActivity = context;
    this.channel = channel;
    this.channel.setMethodCallHandler(this);
    handler = new Handler(Looper.getMainLooper());
    bundle = new Bundle();
    _mytts = new TextToSpeech(this.mActivity, onInitListener);

  }

  private UtteranceProgressListener utteranceProgressListener = new UtteranceProgressListener() {
    @Override
    public void onStart(String utteranceId) {
      invokeMethod("speak.onStart", true);
    }

    @Override
    public void onDone(String utteranceId) {
      invokeMethod("speak.onComplete", true);
    }

    @Override
    @Deprecated
    public void onError(String utteranceId) {
      invokeMethod("speak.onError", "Error from TextToSpeech");
    }

    @Override
    public void onError(String utteranceId, int errorCode) {
      invokeMethod("speak.onError", "Error from TextToSpeech - " + errorCode);
    }
  };

  private TextToSpeech.OnInitListener onInitListener = new TextToSpeech.OnInitListener() {
    @Override
    public void onInit(int status) {
      if (status == TextToSpeech.SUCCESS) {
        _mytts.setOnUtteranceProgressListener(utteranceProgressListener);
        invokeMethod("tts.init", true);

        try {
          Locale locale = _mytts.getDefaultVoice().getLocale();
          if (isLanguageAvailable(locale)) {
            _mytts.setLanguage(locale);
          }
        } catch (NullPointerException | IllegalArgumentException e) {
        }
      } else {
      }
    }
  };

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    // Log.d(tag,"中文乱码");
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "mytts8");
    channel.setMethodCallHandler(new Mytts8Plugin(registrar.activity(), channel));
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if (call.method.equals("speak")) {
      String text = call.arguments.toString();
      speak(text);
      result.success(1);
    } else if (call.method.equals("stop")) {
      stop();
      result.success(1);
    } else if (call.method.equals("setSpeechRate")) {
      String rate = call.arguments.toString();
      result.success(setSpeechRate(Float.parseFloat(rate)));
    } else if (call.method.equals("setPitch")) {
      String pitch = call.arguments.toString();
      setPitch(Float.parseFloat(pitch), result);
    } else if (call.method.equals("setLanguage")) {
      String language = call.arguments.toString();
      setLanguage(language, result);
    } else if (call.method.equals("isLanguageAvailable")) {
      String language = ((HashMap) call.arguments()).get("language").toString();
      Locale locale = Locale.forLanguageTag(language);
      result.success(isLanguageAvailable(locale));
      // } else if (call.method.equals("getAvailableLanguages")) {
      // result.success(getAvailableLanguages());
    } else if (call.method.equals("isSpeaking")) {
      result.success(isSpeaking());
    } else {
      result.notImplemented();
    }
  }

  Boolean isLanguageAvailable(Locale locale) {

    return _mytts.isLanguageAvailable(locale) > 0;
  }

  void speak(String text) {
    uuid = UUID.randomUUID().toString();
    _mytts.speak(text, TextToSpeech.QUEUE_FLUSH, bundle, uuid);
  }

  void stop() {
    if (_mytts != null) {
      _mytts.stop();
    }
  }

  int setSpeechRate(float rateV) {
    int r = -1;
    if (_mytts != null) {
      r = _mytts.setSpeechRate(rateV * 2);
    }
    return r;
  }

  void setPitch(float pitch, Result result) {
    if (pitch >= 0.5F && pitch <= 2.0F) {
      _mytts.setPitch(pitch);
      result.success(1);
    } else {
      Log.d(tag, "Invalid pitch " + pitch + " value - Range is from 0.5 to 2.0");
      result.success(0);
    }
  }

  void setLanguage(String language, Result result) {
    Locale locale = Locale.forLanguageTag(language);
    if (isLanguageAvailable(locale)) {
      _mytts.setLanguage(locale);
      result.success(1);
    } else {
      result.success(0);
    }
  }

  boolean isSpeaking() {
    return _mytts != null && _mytts.isSpeaking();
  }

  private void invokeMethod(final String method, final Object arguments) {
    handler.post(new Runnable() {
      @Override
      public void run() {
        channel.invokeMethod(method, arguments);
      }
    });
  }
}
