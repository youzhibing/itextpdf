package com.yzb.lee.test;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yzb.lee.entity.User;
import com.yzb.lee.generator.HtmlGenerator;
import com.yzb.lee.generator.PdfGenerator;

public class Test {

	public static void main(String[] args) {
		try {
			String outputFile = "D:\\sample.pdf";
			Map<String, Object> variables = new HashMap<String, Object>();

			List<User> userList = new ArrayList<User>();

			User tom = new User("Tom", 19, 1);
			User amy = new User("Amy", 28, 0);
			User leo = new User("Leo", 23, 1);

			userList.add(tom);
			userList.add(amy);
			userList.add(leo);

			variables.put("title", "用户列表");
			variables.put("userList", userList);

			String htmlStr = HtmlGenerator.generate("template.html", variables);

			OutputStream out = new FileOutputStream(outputFile);
			PdfGenerator.generate1(htmlStr, out);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
