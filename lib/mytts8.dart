import 'dart:async';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

class Mytts8 {
  static const MethodChannel _channel = const MethodChannel('mytts8');
  VoidCallback initHandler;
  VoidCallback startHandler;
  VoidCallback completionHandler;
  Function errorHandler;

  Mytts8() {
    _channel.setMethodCallHandler(platformCallHandler);
  }

  Future<dynamic> speak(String text) => _channel.invokeMethod('speak', text);

  Future<dynamic> setLanguage(String language) => _channel.invokeMethod('setLanguage', language);

  /// Allowed values are in the range from 0.0 (silent) to 1.0 (loudest)
  Future<dynamic> setSpeechRate(double rate) => _channel.invokeMethod('setSpeechRate', rate);

  /// 1.0 is default and ranges from .5 to 2.0
  Future<dynamic> setPitch(double pitch) => _channel.invokeMethod('setPitch', pitch);

  Future<dynamic> stop() => _channel.invokeMethod('stop');

  Future<dynamic> isLanguageAvailable(String language) =>
      _channel.invokeMethod('isLanguageAvailable', <String, Object>{'language': language});

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  void setStartHandler(VoidCallback callback) {
    startHandler = callback;
  }

  void setCompletionHandler(VoidCallback callback) {
    completionHandler = callback;
  }

  void setErrorHandler(Function handler) {
    errorHandler = handler;
  }

  void ttsInitHandler(VoidCallback handler) {
    initHandler = handler;
  }

  Future platformCallHandler(MethodCall call) async {
    switch (call.method) {
      case "tts.init":
        if (initHandler != null) {
          initHandler();
        }
        break;
      case "speak.onStart":
        if (startHandler != null) {
          startHandler();
        }
        break;
      case "speak.onComplete":
        if (completionHandler != null) {
          completionHandler();
        }
        break;
      case "speak.onError":
        if (errorHandler != null) {
          errorHandler(call.arguments);
        }
        break;
      default:
        print('Unknowm method ${call.method}');
    }
  }
}
