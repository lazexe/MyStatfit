package com.lazexe.mystatfit.utils;

public class CustomTime {

	private int hours;
	private int minutes;
	private int seconds;
	
	public CustomTime(int hours, int minutes, int seconds) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (hours  < 10) {
			builder.append("0");
			builder.append(hours);
		} else {
			builder.append(hours);
		}
		builder.append(":");
		if (minutes < 10) {
			builder.append("0");
			builder.append(minutes);
		} else {
			builder.append(minutes);
		}
		builder.append(":");
		if (seconds < 10) {
			builder.append("0");
			builder.append(seconds);
		} else {
			builder.append(seconds);
		}
		return builder.toString();
	}
	
	
}
