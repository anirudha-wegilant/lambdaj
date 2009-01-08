// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import ch.lambdaj.function.aggregate.*;
import ch.lambdaj.function.convert.*;
import ch.lambdaj.mock.*;

/**
 * @author Mario Fusco
 * @author Luca Marrocco
 */
public class LambdaTest {

	@Test
	public void testSelectStringsThatEndsWithD() {
		List<String> strings = asList("first", "second", "third");

		Collection<String> results = select(strings, endsWith("d"));

		assertThat(results.size(), is(equalTo(2)));
		assertThat(results, hasItems("second", "third"));
	}

	@Test
	public void testSelectUnique() {
		List<CharSequence> strings = new ArrayList<CharSequence>();

		strings.add("first");
		strings.add("second");
		strings.add("third");

		CharSequence result = selectUnique(forEach(strings).subSequence(0, 1), equalTo("t"));

		assertThat(result, is(equalTo((CharSequence) "t")));
	}

	@Test
	public void testSelectFirst() {
		List<CharSequence> strings = new ArrayList<CharSequence>();
		strings.add("first");
		strings.add("third");

		CharSequence result = selectFirst(forEach(strings).subSequence(0, 5), equalTo("first"));

		assertThat(result, is(equalTo((CharSequence) "first")));
	}

	@Test
	public void testSelectDistinct() {
		List<String> strings = new ArrayList<String>();
		strings.add("first");
		strings.add("second");
		strings.add("third");
		strings.add("first");
		strings.add("second");

		Collection<String> results = selectDistinct(strings);
		assertThat(results.size(), is(equalTo(3)));

		results = selectDistinct(strings, new Comparator<String>() {
			public int compare(String s1, String s2) {
				return s1.length() - s2.length();
			}
		});
		assertThat(results.size(), is(equalTo(2)));
	}

	@Test
	public void testAggregate() {
		List<String> strings = asList("first", "second", "third");

		int totalLenght = aggregate(convert(strings, new StringLengthConverter()), new Sum()).intValue();

		assertThat(totalLenght, is(equalTo(16)));
	}

	@Test
	public void testSum() {
		List<String> strings = asList("first", "second", "third");

		int totalLenght = sum(extract(strings, "length")).intValue();

		assertThat(totalLenght, is(equalTo(16)));
	}

	@Test
	public void testTypedSum() {
		List<CharSequence> strings = new ArrayList<CharSequence>();
		strings.add("first");
		strings.add("second");
		strings.add("third");

		int totalLenght = sumFrom(strings).length();

		assertThat(totalLenght, is(equalTo(16)));
	}

	@Test
	public void testConcat() {
		List<String> strings = asList("first", "second", "third");
		String result = join(forEach(strings));
		assertThat(result, is(equalTo("first, second, third")));
	}

	@Test
	public void testConvertAndConcat() {
		List<String> strings = asList("first", "second", "third");
		String result = join(convert(strings, new Converter<String, String>() {
			public String convert(String from) {
				return from.substring(0, 2);
			}
		}));
		assertThat(result, is(equalTo("fi, se, th")));
	}

	@Test
	public void testConcatFrom() {
		List<Exposure> exposures = asList(new Exposure("france", "first"), new Exposure("brazil", "second"));
		String result = joinFrom(exposures).getCountryName();
		assertThat(result, is(equalTo("france, brazil")));
	}

	@Test
	public void testSelectFranceExposures() {
		Exposure franceExposure = new Exposure("france", "first");
		Exposure brazilExposure = new Exposure("brazil", "second");
		Collection<Exposure> exposures = asList(franceExposure, brazilExposure);
		Collection<Exposure> result = select(exposures, hasProperty("countryName", is(equalTo("france"))));

		assertThat(result.size(), is(equalTo(1)));
		assertThat(result, hasItem(franceExposure));
	}

	@Test
	public void testSumFromConcreteClass() {
		List<Text> strings = new ArrayList<Text>();
		strings.add(new Text("first"));
		strings.add(new Text("second"));
		strings.add(new Text("third"));
		int totalLenght = sum(extract(strings, "length")).intValue();

		assertThat(totalLenght, is(equalTo(16)));
	}

	@Test
	public void testConcatFromConcreteClass() {
		List<Text> strings = new ArrayList<Text>();
		strings.add(new Text("first"));
		strings.add(new Text("second"));
		strings.add(new Text("third"));

		String result = join(forEach(strings).subString(1, 3));
		assertThat(result, is(equalTo("ir, ec, hi")));
	}

	@Test
	public void testMinMaxUsingMockedComparable() {
		Comparable lesser = new Long(1);
		Comparable greater = new Long(2);
		List comparables = asList(lesser, greater);

		assertThat((Comparable) min(forEach(comparables)), is(equalTo(lesser)));
		assertThat((Comparable) max(forEach(comparables)), is(equalTo(greater)));
	}

	@Test
	public void testConcatUsingMockedString() {
		Text aText = new Text("a text");
		Text anotherText = new Text("another text");
		List<Text> strings = asList(aText, anotherText);

		assertThat(joinFrom(strings, "; ").toString(), is(equalTo("a text; another text")));
	}

	@Test
	public void testJoinStrings() {
		assertThat(join(forEach(asList("many", "strings"))), is(equalTo("many, strings")));
		assertThat(join(asList("many", "strings")), is(equalTo("many, strings")));
		assertThat(join(emptyList()), is(equalTo("")));
		assertThat(join(null), is(equalTo("")));
		assertThat(join(""), is(equalTo("")));
		assertThat(join(1), is(equalTo("1")));
		assertThat(join(1l), is(equalTo("1")));
		assertThat(join(1f), is(equalTo("1.0")));
		assertThat(join(1d), is(equalTo("1.0")));
	}

	@Test
	public void testJoinEmptyStringWithSeparatorAlwaysProduceEmptyString() {
		assertThat(join("", ";"), is(equalTo("")));
		assertThat(join("", ","), is(equalTo("")));
		assertThat(join("", "%"), is(equalTo("")));
		assertThat(join("", ":"), is(equalTo("")));
		assertThat(join("", "$"), is(equalTo("")));
		assertThat(join("", "."), is(equalTo("")));
	}
}