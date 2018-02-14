import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class JSONWriterTest {

	/** Prevents tests from running forever. */
	@Rule
	public TestRule globalTimeout = Timeout.seconds(60);

	public static final Path CURRENT_DIR = Paths.get(".");
	public static final Path ACTUAL_DIR = Paths.get("out");
	public static final Path EXPECTED_DIR = Paths.get("test");

	public static class ArrayTests {

		private TreeSet<Integer> test;

		@Before
		public void setup() throws IOException {
			Files.createDirectories(ACTUAL_DIR);
			test = new TreeSet<>();
		}

		@After
		public void teardown() {
			test = null;
		}

		public void runTest(String name) throws IOException {
			Path actualPath = ACTUAL_DIR.resolve(name);
			Path expectPath = EXPECTED_DIR.resolve(name);
			Files.deleteIfExists(actualPath);

			JSONWriter.asArray(test, actualPath);

			List<String> actual = Files.readAllLines(actualPath, Charset.forName("UTF8"));
			List<String> expect = Files.readAllLines(expectPath, Charset.forName("UTF8"));

			String debug = String.format("%nCompare %s and %s for differences.%n", actualPath, expectPath);

			Assert.assertEquals(debug, expect, actual);
		}

		@Test
		public void testEmpty() throws IOException {
			String name = "array-empty.json";
			runTest(name);
		}

		@Test
		public void testSingle() throws IOException {
			String name = "array-single.json";
			test.add(1);

			runTest(name);
		}

		@Test
		public void testSimple() throws IOException {
			String name = "array-simple.json";
			test.add(3);
			test.add(2);
			test.add(1);

			runTest(name);
		}
	}

	public static class ObjectTests {

		private TreeMap<String, Integer> test;

		@Before
		public void setup() throws IOException {
			Files.createDirectories(ACTUAL_DIR);
			test = new TreeMap<>();
		}

		@After
		public void teardown() {
			test = null;
		}

		public void runTest(String name) throws IOException {
			Path actualPath = ACTUAL_DIR.resolve(name);
			Path expectPath = EXPECTED_DIR.resolve(name);
			Files.deleteIfExists(actualPath);

			JSONWriter.asObject(test, actualPath);

			List<String> actual = Files.readAllLines(actualPath, Charset.forName("UTF8"));
			List<String> expect = Files.readAllLines(expectPath, Charset.forName("UTF8"));

			String debug = String.format("%nCompare %s and %s for differences.%n", actualPath, expectPath);

			Assert.assertEquals(debug, expect, actual);
		}

		@Test
		public void testEmpty() throws IOException {
			String name = "object-empty.json";
			runTest(name);
		}

		@Test
		public void testSingle() throws IOException {
			String name = "object-single.json";
			test.put("hello", 1);

			runTest(name);
		}

		@Test
		public void testSimple() throws IOException {
			String name = "object-simple.json";
			test.put("three", 3);
			test.put("two", 2);
			test.put("one", 1);

			runTest(name);
		}
	}

	public static class NestedTests {

		private TreeMap<String, TreeSet<Integer>> test;

		@Before
		public void setup() throws IOException {
			Files.createDirectories(ACTUAL_DIR);
			test = new TreeMap<>();
		}

		@After
		public void teardown() {
			test = null;
		}

		public void runTest(String name) throws IOException {
			Path actualPath = ACTUAL_DIR.resolve(name);
			Path expectPath = EXPECTED_DIR.resolve(name);
			Files.deleteIfExists(actualPath);

			JSONWriter.asNestedObject(test, actualPath);

			List<String> actual = Files.readAllLines(actualPath, Charset.forName("UTF8"));
			List<String> expect = Files.readAllLines(expectPath, Charset.forName("UTF8"));

			String debug = String.format("%nCompare %s and %s for differences.%n", actualPath, expectPath);

			Assert.assertEquals(debug, expect, actual);
		}

		@Test
		public void testEmptyEmpty() throws IOException {
			String name = "nested-empty-empty.json";
			runTest(name);
		}

		@Test
		public void testSingleEmpty() throws IOException {
			String name = "nested-single-empty.json";
			test.put("hello", new TreeSet<>());

			runTest(name);
		}

		@Test
		public void testSingleSingle() throws IOException {
			String name = "nested-single-single.json";
			test.put("hello", new TreeSet<>());
			test.get("hello").add(1);

			runTest(name);
		}

		@Test
		public void testSingleSimple() throws IOException {
			String name = "nested-single-simple.json";
			test.put("hello", new TreeSet<>());
			test.get("hello").add(1);
			test.get("hello").add(2);
			test.get("hello").add(3);

			runTest(name);
		}

		@Test
		public void testSimpleSimple() throws IOException {
			String name = "nested-simple-simple.json";
			test.put("three", new TreeSet<>());
			test.get("three").add(1);
			test.get("three").add(2);
			test.get("three").add(3);

			test.put("two", new TreeSet<>());
			test.get("two").add(1);
			test.get("two").add(2);

			test.put("one", new TreeSet<>());
			test.get("one").add(1);

			runTest(name);
		}
	}
}
