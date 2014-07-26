package com.neojsy.SimplePodcast;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class ProgressView extends SurfaceView implements Callback {
 
     private static int Max = 0;                      // Progress 최대값
     private static int Position = 0;                  // Progress 현재의 position
     private static String Title = "";                  // Progress 아래에 표시할 제목
     private static int BgColor = 0xFF89B5E1;    // View의 배경색
     int Width, Height;                                 // SurfaceView 전체의 크기  
 
     static ProgressThread mThread;               // Thread
     SurfaceHolder mHolder;                          // SurfaceHolder 
     Context mContext;                               // Context
 
     //-------------------------------------
     //  생성자
     //-------------------------------------
     public ProgressView(Context context, AttributeSet attrs) {
          super(context, attrs);
          SurfaceHolder holder = getHolder();
          holder.addCallback(this);
  
          mHolder = holder;              // holder와 Context 보존
          mContext = context;
          mThread = new ProgressThread(holder, context);
     }
 
     //-------------------------------------
     // SetMax - Progress의 최대값 설정 부분
     // 타이틀과 최대값을 받아온다
     //-------------------------------------
     public static void SetMax(String title, int max) {
          Title = title;
          Max = max;
          mThread.Notify();      // 값이 바뀌면 Thread에 통지
     }
 
     //-------------------------------------
     //  SetPosition
     //-------------------------------------
     public static void SetPosition(int position) {
          Position = position;
          mThread.Notify();      // 값이 바뀌면 Thread에 통지 - 이게 싱크를 맞춘다
     }
 
     //-------------------------------------
     //  Set BgColor
     //-------------------------------------
     public static void SetBgColor(int color) {
          BgColor = color;
          mThread.Notify();      // 값이 바뀌면 Thread에 통지
     }
 
     //-------------------------------------
     public void surfaceCreated(SurfaceHolder holder) {
          try {
               mThread.start();            // 일단 스레드를 실행해 보고
          } catch (Exception e) {       // 안되면
               RestartThread();            // 스레드를 다시 만들어서 실행한다
          }
     }
 
     //-------------------------------------
     //  SurfaceView가 바뀔 때 실행되는 부분
     //-------------------------------------
     public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
          Width = width;            // 여기에서 SurfaceView의 크기를 구한다
          Height = height;
          Log.v("SurfaceView", "" + Width + "x" + Height);
     }
 
     //-------------------------------------
     //  SurfaceView가 해제될 때 실행되는 부분
     //-------------------------------------
     public void surfaceDestroyed(SurfaceHolder holder) {
          StopThread();
     }
 
     //-------------------------------------
     //  스레드 완전 정지
     //-------------------------------------
     public void StopThread() {
          mThread.StopThread(); 
     }
 
     //-------------------------------------
     //  Thread 초기화
     //-------------------------------------
     public void RestartThread() {
          mThread.StopThread();  // 스레드 중지
          mThread = null;   
          mThread = new ProgressThread(mHolder, mContext); 
          mThread.start(); 
     }
 
//----------------------------------------------------------------
 
     //-------------------------------------
     //  Thread Class
     //-------------------------------------
     class ProgressThread extends Thread {
          boolean canRun = true;                 // Thread 제어용
          Paint paint = new Paint();               // progress 출력용
          LinearGradient gradient;                  // 그라디언트 처리용
  
          //-------------------------------------
          //  생성자 
          //-------------------------------------
          public ProgressThread(SurfaceHolder holder, Context context) {
               paint.setAntiAlias(true);
               gradient = new LinearGradient(0, 0, 0, 15, Color.LTGRAY, Color.BLACK, TileMode.CLAMP);
               paint.setColor(Color.BLACK);
               paint.setTextSize(16);
          }
  
          //-------------------------------------
          //  DrawAll  - 실제 progress를 그려주는 부분
          //-------------------------------------
          public void DrawAll(Canvas canvas) {
               int h = 18;                                         // 프로그레스의 세로폭
   
               canvas.drawRect(0, 0, Width, h, paint);      // 검은색 테두리 그리기 
               paint.setColor(Color.WHITE);                    // 테두리 안을 흰색으로 채운다 
               canvas.drawRect(1, 1, Width - 1, h - 1, paint);
   
               // 배경 지움
               paint.setColor(BgColor);                   
               canvas.drawRect(0, h + 1, Width, Height, paint);
   
               if (Max == 0) return;
               float w = (float) Width / Max * Position;;
               paint.setShader(gradient);
               canvas.drawRect(1, 1, w, h - 1, paint);
   
               paint.setShader(null);
               paint.setColor(Color.BLACK);
               //canvas.drawText(Title + "  " + Max + " / " + Position, 1, Height - 2, paint);
          }
  
          //-------------------------------------
          //  스레드 본체
          //-------------------------------------
          public void run() {
               Canvas canvas = null;     
               while (canRun) {
                    canvas = mHolder.lockCanvas(); 
                    try {
                         synchronized (mHolder) { 
                              DrawAll(canvas);   // Canvas에 그리기
                         } // sync
                    } catch (Exception e) {
                         // e.printStackTrace();
                    } finally {       
                         if (canvas != null)   
                              mHolder.unlockCanvasAndPost(canvas);
                    } // try
               } // while
          } // run
 
          //-------------------------------------
          //  스레드 완전 정지
          //-------------------------------------
          public void StopThread() {
               canRun = false;
               synchronized (this) {
                      this.notify();
               }
          }
  
          //-------------------------------------
          //  Notify - 스레드에게 통지하는 부분
          //-------------------------------------
          public void Notify() {
                 synchronized (this) {
                      this.notify();
                 }
          }
       } // Thread 끝
} // SurfaceView