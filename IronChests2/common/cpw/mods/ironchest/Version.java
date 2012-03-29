package cpw.mods.ironchest;


public class Version {
	public static final String MAJOR="@MAJOR@";
	public static final String MINOR="@MINOR@";
	public static final String REV="@REV@";
	public static final String BUILD="@BUILD@";
	
	public static final String version() {
		return MAJOR+"."+MINOR;
	}
	
	public static final String name() {
		return "Iron Chest ("+MAJOR+"."+MINOR+") rev "+REV+" build "+BUILD;
	}
}
