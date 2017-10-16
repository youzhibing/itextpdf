package com.yzb.lee.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TemplateUtil {
	
	public static String getTemplate() throws IOException {
		InputStream is = TemplateUtil.class.getResourceAsStream("/template.html");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder("");
		String line = null;
		while((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		is.close();
		return sb.toString();
	}
}
