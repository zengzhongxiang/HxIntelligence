//package com.jmgo.middleware.ui;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.util.Log;
//import com.jmgo.middleware.utils.JmGOSystemProperties;
//import java.util.HashMap;
//
//public class JmGOUIManager
//{
//    private static final boolean DEBUG = JmGOSystemProperties.getBoolean("persist.jmgo.debug", false);
//    public static final String TAG = JmGOUIManager.class.getSimpleName();
//    private static JmGOUIManager mInstance;
//    private Context mContext;
//    private Handler mHandler = new Handler(Looper.getMainLooper())
//    {
//        public void handleMessage(Message paramAnonymousMessage)
//        {
//            int i = paramAnonymousMessage.what;
//            int j = paramAnonymousMessage.arg1;
//            JmGOUIManager.this.mainShow(i, j);
//        }
//    };
//    private HashMap<Integer, UIController> mHashMap = new HashMap(2);
//    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver()
//    {
//        public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
//        {
//            paramAnonymousContext = paramAnonymousIntent.getAction();
//        }
//    };
//    private boolean mIsLauncherError = false;
//    private boolean mSystemShutDown = false;
//
//    public static JmGOUIManager getInstance(Context paramContext)
//    {
//        if (mInstance == null) {}
//        try
//        {
//            if (mInstance == null)
//            {
//                mInstance = new JmGOUIManager();
//                mInstance.setContext(paramContext);
//            }
//            return mInstance;
//        }
//        finally
//        {
//            paramContext = finally;
//            throw paramContext;
//        }
//    }
//
//    private void registBroadcast()
//    {
//        IntentFilter localIntentFilter = new IntentFilter();
//        localIntentFilter.addAction("android.intent.action.update.error");
//        localIntentFilter.addAction("android.intent.action.HOLATEK_SHOW_FACTORY_AUTOFOCUS_IMG");
//        localIntentFilter.addAction("android.intent.action.HOLATEK_LAUNCHER_STATUS");
//        this.mContext.registerReceiver(this.mIntentReceiver, localIntentFilter);
//    }
//
//    public void dismiss(int paramInt)
//    {
//        try
//        {
//            dismiss(paramInt, -1);
//            return;
//        }
//        finally
//        {
//            localObject = finally;
//            throw ((Throwable)localObject);
//        }
//    }
//
//    public void dismiss(int paramInt1, int paramInt2)
//    {
//        try
//        {
//            UIController localUIController = (UIController)this.mHashMap.get(Integer.valueOf(paramInt1));
//            localUIController.cleanTag(paramInt2);
//            if (localUIController.isShowing())
//            {
//                if (DEBUG) {
//                    Log.d(TAG, " dismiss showLevel " + paramInt2 + " mUIController.getShowLevel()" + localUIController.getShowLevel());
//                }
//                if ((localUIController.getShowLevel() == paramInt2) || (paramInt2 == -1)) {
//                    localUIController.dismiss(paramInt2);
//                }
//            }
//            return;
//        }
//        finally {}
//    }
//
//    public void mainShow(int paramInt1, int paramInt2)
//    {
//        try
//        {
//            if (DEBUG) {
//                Log.d(TAG, " mainShow ----type:" + paramInt2 + " cmd:" + paramInt1);
//            }
//            UIController localUIController = (UIController)this.mHashMap.get(Integer.valueOf(paramInt2));
//            if (localUIController.isShowing()) {}
//            localUIController.showWindow(paramInt1);
//            return;
//        }
//        finally {}
//    }
//
//    public void setContext(Context paramContext)
//    {
//        this.mContext = paramContext;
//        this.mHashMap.put(Integer.valueOf(1), new AlarmUIController(this.mContext));
//        this.mHashMap.put(Integer.valueOf(0), new FocusUIController(this.mContext));
//        registBroadcast();
//    }
//
//    public void show(int paramInt1, int paramInt2)
//    {
//        Message localMessage = this.mHandler.obtainMessage(paramInt2, paramInt1, 0);
//        this.mHandler.sendMessage(localMessage);
//    }
//}
