import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

public class JSONWriter {

	/**
	 * Returns a String with the specified number of tab characters.
	 *
	 * @param times
	 *            number of tab characters to include
	 * @return tab characters repeated the specified number of times
	 */
	public static String indent(int times) {
		char[] tabs = new char[times];
		Arrays.fill(tabs, '\t');
		return String.valueOf(tabs);
	}

	/**
	 * Returns a quoted version of the provided text.
	 *
	 * @param text
	 *            text to surround in quotes
	 * @return text surrounded by quotes
	 */
	public static String quote(String text) {
		return String.format("\"%s\"", text);
	}

	/**
	 * Writes the set of elements as a JSON array at the specified indent level.
	 *
	 * @param writer
	 *            writer to use for output
	 * @param elements
	 *            elements to write as JSON array
	 * @param level
	 *            number of times to indent the array itself
	 * @throws IOException
	 */
	private static void asArray(Writer writer, TreeSet<Integer> elements, int level) throws IOException {
		/*
		 * TODO This is optional, but if you implement this method you can reuse
		 * it in several places in this class.
		 */

		/*
		 * TODO This is optional, but if you implement this method you can reuse
		 * it in several places in this class.
		 */
		int count = 0;
		writer.write("[\n");
		for (Iterator<Integer> iterator = elements.iterator(); iterator.hasNext();) {
			Integer integer = (Integer) iterator.next();
			writer.write(indent(level + 1));
			writer.write(integer.toString());
			count++;
			if (count < elements.size()) {
				writer.write(",");
			}
			writer.write("\n");
		}
		writer.write(indent(level));
		writer.write("]");
		writer.flush();

	}

	/**
	 * Writes the set of elements as a JSON array to the path using UTF8.
	 *
	 * @param elements
	 *            elements to write as a JSON array
	 * @param path
	 *            path to write file
	 * @throws IOException
	 */
	public static void asArray(TreeSet<Integer> elements, Path path) throws IOException {
		// TODO Use try-with-resources (no catch block needed)
		try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asArray(bw, elements, 0);
		}
	}

	/**
	 * Writes the map of elements as a JSON object to the path using UTF8.
	 *
	 * @param elements
	 *            elements to write as a JSON object
	 * @param path
	 *            path to write file
	 * @throws IOException
	 */
	public static void asObject(TreeMap<String, Integer> elements, Path path) throws IOException {
		// TODO Use try-with-resources (no catch block needed)
		try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			int level = 0, index = 0;
			bw.write(indent(level));
			bw.write("{\n");
			for (Iterator<String> iterator = elements.keySet().iterator(); iterator.hasNext();) {
				String str = (String) iterator.next();
				Integer value = elements.get(str);
				bw.write(indent(level + 1));
				bw.write(String.format("%s: %d", quote(str), value.intValue()));
				index++;
				if (index < elements.keySet().size()) {
					bw.write(",\n");
				} else {
					bw.write("\n");
				}

			}
			bw.write(indent(level));
			bw.write("}\n");
			bw.flush();
		}
	}

	/**
	 * Writes the set of elements as a JSON object with a nested array to the
	 * path using UTF8.
	 *
	 * @param elements
	 *            elements to write as a JSON object with a nested array
	 * @param path
	 *            path to write file
	 * @throws IOException
	 */
	public static void asNestedObject(TreeMap<String, TreeSet<Integer>> elements, Path path) throws IOException {
		// TODO Use try-with-resources (no catch block needed)
		try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

			int level = 0, index = 0;
			bw.write(indent(level));
			bw.write("{\n");
			for (Iterator<String> iterator = elements.keySet().iterator(); iterator.hasNext();) {
				index++;
				String key = (String) iterator.next();
				TreeSet<Integer> values = elements.get(key);
				bw.write(indent(level + 1));
				bw.write(String.format("%s: ", quote(key)));
				asArray(bw, values, level + 1);
				if (index < elements.keySet().size()) {
					bw.write(",\n");
				} else {
					bw.write("\n");
				}
			}
			bw.write(indent(level));
			bw.write("}\n");
			bw.flush();

		}
	}
}
