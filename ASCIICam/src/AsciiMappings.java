import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

class AsciiMappings {
	static HashMap<String, Set<Entry<Integer, Character>>> mappings = initMappings();
	static String defaultMapping;

	private static HashMap<String, Set<Entry<Integer, Character>>> initMappings() {
		HashMap<String, Set<Entry<Integer, Character>>> mappings = new HashMap<String, Set<Entry<Integer, Character>>>();
		defaultMapping = "Config 3 - Tail-based fine";
		mappings.put("Config 1 - Uniform coarse", getConfig1());
		mappings.put("Config 2 - Uniform fin", getConfig2());
		mappings.put("Config 3 - Tail-based fine", getConfig3());
		return mappings;
	}

	static String getDefault() {
		return defaultMapping;
	}

	// Uniform coarse
	private static Set<Entry<Integer, Character>> getConfig1() {
		Map<Integer, Character> map = new TreeMap<Integer, Character>(Collections.reverseOrder());
		map.put(-1000, ' ');
		map.put(-76, '.');
		map.put(-51, ':');
		map.put(-25, '*');
		map.put(1, 'o');
		map.put(26, '&');
		map.put(52, '8');
		map.put(77, '#');
		map.put(103, '@');

		return map.entrySet();
	}

	// Uniform fine
	private static Set<Entry<Integer, Character>> getConfig2() {
		Map<Integer, Character> map = new TreeMap<Integer, Character>(Collections.reverseOrder());
		map.put(-1000, ' ');
		map.put(-91, '.');
		map.put(-72, '-');
		map.put(-54, ':');
		map.put(-36, '*');
		map.put(-18, '/');
		map.put(1, '>');
		map.put(19, 'Y');
		map.put(37, '%');
		map.put(55, 'X');
		map.put(73, '#');
		map.put(92, '&');
		map.put(110, '@');

		return map.entrySet();
	}

	// Tail-based fine
	private static Set<Entry<Integer, Character>> getConfig3() {
		Map<Integer, Character> map = new TreeMap<Integer, Character>(Collections.reverseOrder());
		map.put(-1000, ' ');
		map.put(-108, '.');
		map.put(-97, '-');
		map.put(-80, ':');
		map.put(-50, '*');
		map.put(-20, '/');
		map.put(1, '>');
		map.put(22, 'Y');
		map.put(52, '%');
		map.put(82, 'X');
		map.put(99, '#');
		map.put(110, '&');
		map.put(121, '@');

		return map.entrySet();
	}

}
