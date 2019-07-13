# mytts8

A new Flutter plugin.
This is a test plugin ,  TextToSpeech to  read chinese, and completionHandler will do something when speaking is over.

## suport android ,and tested on android 8.1.0

## function
  void setStartHandler(VoidCallback callback) 
  void setCompletionHandler(VoidCallback callback) 
  void setErrorHandler(Function handler) 
  void ttsInitHandler(VoidCallback handler) 

  Future<dynamic> speak(String text) 
  Future<dynamic> setLanguage(String language) 
  Future<dynamic> setSpeechRate(double rate)   
  Future<dynamic> setPitch(double pitch)
  Future<dynamic> stop() 
  Future<dynamic> isLanguageAvailable(String language) 
  

## Getting Started

This project is a starting point for a Flutter
[plug-in package](https://flutter.dev/developing-packages/),
a specialized package that includes platform-specific implementation code for
Android and/or iOS.

For help getting started with Flutter, view our 
[online documentation](https://flutter.dev/docs), which offers tutorials, 
samples, guidance on mobile development, and a full API reference.
