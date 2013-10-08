package cn.im47.commons.utils;

import cn.im47.commons.utils.date.LunarCalendar;
import org.dtools.javaini.BasicIniFile;
import org.dtools.javaini.IniFile;
import org.dtools.javaini.IniFileReader;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Calendar;

/**
 * 节假日
 * <p/>
 * User: baitao.jibt@gmail.com
 * Date: 12-9-24
 * Time: 下午5:45
 */
public class HolidayUtils {

	private static IniFile Holidays = null;
	private static long lastModified = 0L;
	private final static NumberFormat NumFmt = NumberFormat.getInstance();
	private static String webroot = null;

	static {
		NumFmt.setMaximumFractionDigits(0);
		NumFmt.setMinimumIntegerDigits(2);
	}

	public HolidayUtils(String webroot2) {
		webroot = webroot2;
	}

	public static String get(Calendar cal) {
		IniFile iniFile = _ReloadIniFile();

		//周节日
		String week = NumFmt.format(cal.get(Calendar.MONTH) + 1) +
				cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) +
				(cal.get(Calendar.DAY_OF_WEEK) - 1);
		String holiday = iniFile.getItemValue("WEEK", week);
		if (holiday != null) {
			return holiday;
		}

		long[] ds = LunarCalendar.get(cal);
		String nongli = NumFmt.format(ds[1]) + NumFmt.format(ds[2]);
		String yangli = NumFmt.format(cal.get(Calendar.MONTH) + 1) +
				NumFmt.format(cal.get(Calendar.DATE));

		//阳历
		holiday = iniFile.getItemValue("SOLAR", yangli);
		if (holiday != null) {
			return holiday;
		}

		//阴历
		holiday = iniFile.getItemValue("LUNAR", nongli);
		if (holiday != null) {
			return holiday;
		}

		return "";
	}

	private static IniFile _ReloadIniFile() {
		File iniFile = new File(webroot + File.separator + "holiday.dat");
		if (Holidays == null || lastModified != iniFile.lastModified()) {
			synchronized (HolidayUtils.class) {
				if (Holidays == null || lastModified != iniFile.lastModified()) {
					if (Holidays != null)
						Holidays.removeAll();
					else
						Holidays = new BasicIniFile(false);//不使用大小写敏感
					IniFileReader reader = new IniFileReader(Holidays, iniFile);
					try {
						reader.read();
						lastModified = iniFile.lastModified();
					} catch (IOException e) {
						e.printStackTrace(System.err);
						return null;
					}
				}
			}
		}
		return Holidays;
	}

}
