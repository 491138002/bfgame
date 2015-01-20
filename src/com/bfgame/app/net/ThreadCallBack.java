package com.bfgame.app.net;

import java.io.Serializable;

/**
 * 线程回调
 * @author admin
 *
 */
public interface ThreadCallBack extends Serializable {
	
	public void onCallbackFromThread(String resultJson);
	public void onCallbackFromThread(String resultJson, int resultCode);
}
