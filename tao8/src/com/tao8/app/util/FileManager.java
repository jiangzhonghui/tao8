package com.tao8.app.util;


public class FileManager {

	public static String getSaveFilePath() {
		if (CommonUtil.hasSDCard()) {
			return CommonUtil.getRootFilePath() + "com.tao8.app/files/";
		} else {
			return CommonUtil.getRootFilePath() + "com.tao8.app/files/";
		}
	}
}
