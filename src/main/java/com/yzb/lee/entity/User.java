package com.yzb.lee.entity;

public class User {

	private String name;
	private int age;
	private int sex;

	/**
	 * Constructor with all fields
	 * 
	 * @param name
	 * @param age
	 * @param sex
	 */
	public User(String name, int age, int sex) {
		super();
		this.name = name;
		this.age = age;
		this.sex = sex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

}
