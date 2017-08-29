package com.szxb.xblog;

public interface LogStrategy {

  void log(int priority, String tag, String message);
}
