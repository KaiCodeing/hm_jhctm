package com.hemaapp.jhctm.getui;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.activity.MainActivity;
import com.hemaapp.jhctm.push.MyPushMessageReceiver;
import com.hemaapp.jhctm.push.PushUtils;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import xtom.frame.util.XtomSharedPreferencesUtil;


public class PushDemoReceiver extends BroadcastReceiver {

	public static final String TAG = MyPushMessageReceiver.class
			.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));

		switch (bundle.getInt(PushConsts.CMD_ACTION)) {
		case PushConsts.GET_MSG_DATA:
			// 获取透传数据
			// String appid = bundle.getString("appid");
			byte[] payload = bundle.getByteArray("payload");

			String taskid = bundle.getString("taskid");
			String messageid = bundle.getString("messageid");

			// smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
			boolean result = PushManager.getInstance().sendFeedbackMessage(
					context, taskid, messageid, 90001);
			System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));

			if (payload != null) {
				String data = new String(payload);

				Log.d("GetuiSdkDemo", "receiver payload : " + data);

				String msg_content = "";
				String msg_keytype = "";
				String msg_keyid = "";
				try {
					JSONObject msgJson = new JSONObject(data);
					msg_content = msgJson.getString("msg");
					msg_keytype = msgJson.getString("keyType");
					msg_keyid = msgJson.getString("keyId");
				} catch (JSONException e) {
					return;
				}

				if (msg_content == null || TextUtils.isEmpty(msg_content)) {
					msg_content = context.getString(R.string.app_name);
				}

				mynotify(context, msg_content, msg_keytype, msg_keyid);
				PushUtils.savemsgreadflag(context, true, msg_keytype);

				Intent msgIntent = new Intent();
				msgIntent.setAction("com.hemaapp.push.msg");
				// 发送 一个无序广播
				context.sendBroadcast(msgIntent);
				if (!msg_keytype.equals("0")) {
					Intent intent2 = new Intent();
					intent2.setAction("com.hemaapp.xxc.message");
					context.sendBroadcast(intent2);

				}
			}
			break;

		case PushConsts.GET_CLIENTID:
			// 获取ClientID(CID)
			// 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
			String userId = bundle.getString("clientid");
			PushUtils.saveUserId(context, userId);
			PushUtils.saveChannelId(context, userId);
			Intent clientIntent = new Intent();
			clientIntent.setAction("com.hemaapp.push.connect");
			// 发送 一个无序广播
			context.sendBroadcast(clientIntent);
			break;

		case PushConsts.THIRDPART_FEEDBACK:
			/*
			 * String appid = bundle.getString("appid"); String taskid =
			 * bundle.getString("taskid"); String actionid =
			 * bundle.getString("actionid"); String result =
			 * bundle.getString("result"); long timestamp =
			 * bundle.getLong("timestamp");
			 * 
			 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo",
			 * "taskid = " + taskid); Log.d("GetuiSdkDemo", "actionid = " +
			 * actionid); Log.d("GetuiSdkDemo", "result = " + result);
			 * Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
			 */
			break;

		default:
			System.out.println(bundle);
			break;
		}
	}

	public void mynotify(Context context, String content, String keytype,
			String keyid) {
		Log.d(TAG, "notify()...");
		Log.d(TAG, "keytype=" + keytype);

		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context);
		Notification notification = null;
		Intent it = null;
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setWhen(System.currentTimeMillis());
		builder.setContentTitle(context.getString(R.string.app_name));
		builder.setContentText(content).setTicker(content);
		it = new Intent(context, MainActivity.class);
		Log.i("++++++++++PUSHgetui","");
		PendingIntent pi = PendingIntent.getActivity(context, 0, it,
				PendingIntent.FLAG_CANCEL_CURRENT);
		builder.setContentIntent(pi);
		notification = builder.build();
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.defaults = Notification.DEFAULT_LIGHTS;
		if ( XtomSharedPreferencesUtil.get(context,"notice")==null ||"".equals(XtomSharedPreferencesUtil.get(context,"notice")))
		{
			return;
		}
			if ("0".equals(keytype)) {
            // notification.defaults |= Notification.DEFAULT_SOUND;
        } else {
            notification.defaults |= Notification.DEFAULT_SOUND;
        }
		// notification.defaults |= Notification.DEFAULT_SOUND;
		nm.notify(0, notification);
	}

	private boolean isForeground(Context context, String className) {
		if (context == null || TextUtils.isEmpty(className)) {
			return false;
		}

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(1);
		if (list != null && list.size() > 0) {
			ComponentName cpn = list.get(0).topActivity;
			if (className.equals(cpn.getClassName())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 需要权限:android.permission.GET_TASKS
	 *
	 * @param context
	 * @return
	 */
	public boolean isApplicationBroughtToBackground(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(10);
		if (tasks != null && !tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			// Debug.i(TAG, "topActivity:" + topActivity.flattenToString());
			// Debug.f(TAG, "topActivity:" + topActivity.flattenToString());
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}
}
