import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:mytts8/mytts8.dart';

void main() => runApp(MyApp());

class Ss {
  var _value = 0;
  Function fn;
  get value => this._value;
  set value(num s) {
    this._value = s;
    fn();
  }

  setvalue(num tx) {
    this._value = tx;
  }
}

class MyApp extends StatefulWidget {
  final tts = new Mytts8();
  final lsn = Ss();
  MyApp() {
    tts.isLanguageAvailable("zh-CN").then((v) async {
      if (v) {
        print(v);
        await tts.setLanguage("zh-CN");
      }
    });
  }
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  @override
  void initState() {
    super.initState();
    this.widget.lsn.fn = changeText;
    initPlatformState();
  }

  Future<void> initPlatformState() async {
    String platformVersion;

    try {
      platformVersion = await Mytts8.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  changeText() {
    setState(() {
//      print("setstate");
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
            child: ListView(
          children: <Widget>[
            RaisedButton(
              child: Text("Read1"),
              onPressed: () async {
                await this.widget.tts.speak("这是一段中文字汉字");
                this.widget.tts.setCompletionHandler(() {
                  this.widget.lsn.value += 1;
                });
              },
            ),
            Text('Running on: $_platformVersion\n'),
            Text("count ${this.widget.lsn.value} "),
          ],
        )),
      ),
    );
  }
}
