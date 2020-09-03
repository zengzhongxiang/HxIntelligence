package com.adv.hxsoft.util;

import android.content.Context;
import android.util.Log;

public class Display {
	
	private Context context;
	private int density;
	private float scaledDensity;
	private int heightPixels;
	private int widthPixels;
	private int heightdps;
	private int widthdps;
	private int marginbottom = 0;
	private String tag = "Histar_Display";
	
	public Display(int density, float scaledDensity, int heightPixels, int widthPixels) {
		super();
		this.density = density;
		this.scaledDensity = scaledDensity;
		this.heightPixels = heightPixels;
		this.widthPixels = widthPixels;
		this.widthdps = widthPixels*160/density;
		this.heightdps = heightPixels*160/density;
		this.marginbottom = heightPixels/60;
//		Log.e(tag , "this.density"+this.density+
//				" this.heightPixels"+this.heightPixels+
//				" this.widthPixels"+this.widthPixels+
//				" this.widthdps"+this.widthdps+
//				" this.heightdps"+this.heightdps+
//				" this.marginbottom"+this.marginbottom);
	}
	
	public int getWidth(){
		return widthPixels;
	}

	public int getHeight() {
		return heightPixels;
	}
	
	public int getMenuDailogWidth(){
		int result = widthPixels/4;
		Log.d(tag, "result:"+result);
		return result;
	}
	
	public int getMenuDailogHeight() {
//		return 7*(heightPixels-marginbottom)/8;
		return heightPixels;
	}

	public int getMenuItemHeight() {
		return getMenuDailogHeight()*2/26;
	}
	
	public float getscaledDensity(){
		return scaledDensity;
	}
	
	public int getPointX0(){
		return widthPixels/2;
	}
	
	
	public int getPointY0(){
		return heightPixels/12;
	}
	
	public int[] getPointXs(int n){
		int[] result = new int[n];
		int x0= getPointX0();
		int tmp=n/2;
		int step = widthPixels/7;
		if(n%2 == 0){
			for (int i=tmp; i>0; i--){
				result[tmp-i] = x0 - (tmp-1)*step - step/2;
				result[tmp+i] = x0 + (tmp-1)*step - step/2;
			}
		}else{
			for (int i=tmp; i>0; i--){
				result[tmp-i] = x0 - tmp*step;
				result[tmp+i] = x0 + tmp*step;
			}
			result[tmp+1] = x0;
		}
		return result;
	}

	public int getProgressHeight(){
		return heightPixels/8;
	}
	
	public int getProgressHeight2(){
		return heightPixels/2;
	}
	
	
	public int getPlayInfoWidth(){
		return 2*widthPixels/4;
	}
	
	public int getPlayInfoHeight(){
		return (heightPixels-marginbottom)/10;
	}
	
	public int getPlayInfoMarginButtom(){
		return marginbottom;
	}
}
