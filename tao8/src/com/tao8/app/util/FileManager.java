package com.tao8.app.util;


public class FileManager {

	public static String getSaveFilePath() {
		if (CommonUtil.hasSDCard()) {
			return CommonUtil.getRootFilePath() + "com.emar.egou/files/";
		} else {
			return CommonUtil.getRootFilePath() + "com.emar.egou/files";
		}
	}
}
