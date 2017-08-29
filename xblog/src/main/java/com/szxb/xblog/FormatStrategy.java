package com.szxb.xblog;

public interface FormatStrategy {

  void log(int priority, String tag, String message);
}
