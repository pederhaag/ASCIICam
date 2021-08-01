import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
 * 
 * The {@code AsciiMappings} class is a simple class containing static methods
 * for constructing different mappings used to translate a byte-value to a
 * character. It also controls the default mapping used.
 * 
 * @see getDefault
 * @see getMappings
 * 
 */
class AsciiMappings {
	private static HashMap<String, Set<Entry<Integer, Character>>> mappings = initMappings();
	private static String defaultMapping;

	private static HashMap<String, Set<Entry<Integer, Character>>> initMappings() {
		HashMap<String, Set<Entry<Integer, Character>>> mappings = new HashMap<String, Set<Entry<Integer, Character>>>();
		defaultMapping = "Tail-based fine";
		mappings.put("Uniform coarse", getConfig1());
		mappings.put("Uniform fine", getConfig2());
		mappings.put("Tail-based fine", getConfig3());
		return mappings;
	}

	/**
	 * Return the id of the default mapping
	 * 
	 * @return the {@code String} id of the default mapping
	 */
	static String getDefault() {
		return defaultMapping;
	}

	/**
	 * Gets a HashMap of all defined mappings. A mapping is associated with a
	 * {@code String} id/name and a {@code Set<Entry<Integer, Character>>} mapping.
	 * 
	 * @return a {@code HashMap} with {@code String} keys and mapping as values
	 */
	static HashMap<String, Set<Entry<Integer, Character>>> getMappings() {
		return mappings;
	}

	/**
	 * <b>Uniform coarse</b>
	 * 
	 * This mapping has few points uniformly distributed on [-128, 127]
	 */
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

	/**
	 * <b>Uniform fine</b>
	 * 
	 * This mapping has a few more points than {@link getConfig1} which also are
	 * distributed uniformly
	 */
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

	/**
	 * <b>Tail-based fine</b>
	 * 
	 * This mapping has more points distributed in the tail-end. This mapping seems
	 * to work the best on the tested images.
	 */
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
