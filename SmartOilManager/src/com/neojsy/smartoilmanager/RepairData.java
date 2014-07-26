package com.neojsy.smartoilmanager;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class RepairData  implements Serializable {
	String name;
	int period;
	int change;
	GregorianCalendar time;

	RepairData(String mName, int mperiod, int mChange, GregorianCalendar mTime) {
		name = mName;
		period = mperiod;
		change = mChange;
		time = mTime;
	}
}
