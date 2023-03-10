/*
 * #%L
 * Simmetrics Core
 * %%
 * Copyright (C) 2014 - 2023 Simmetrics Authors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package org.simmetrics.metrics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.simmetrics.simplifiers.Simplifiers.toLowerCase;
import static org.simmetrics.simplifiers.SimplifiersMatcher.chain;
import static org.simmetrics.tokenizers.Tokenizers.whitespace;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.simmetrics.Metric;
import org.simmetrics.StringMetric;
import org.simmetrics.StringMetricTest;
import org.simmetrics.metrics.Identity;
import org.simmetrics.metrics.StringMetrics;
import org.simmetrics.metrics.StringMetrics.ForList;
import org.simmetrics.metrics.StringMetrics.ForListWithSimplifier;
import org.simmetrics.metrics.StringMetrics.ForMultiset;
import org.simmetrics.metrics.StringMetrics.ForMultisetWithSimplifier;
import org.simmetrics.metrics.StringMetrics.ForSet;
import org.simmetrics.metrics.StringMetrics.ForSetWithSimplifier;
import org.simmetrics.metrics.StringMetrics.ForString;
import org.simmetrics.metrics.StringMetrics.ForStringWithSimplifier;
import org.simmetrics.simplifiers.Simplifier;
import org.simmetrics.simplifiers.Simplifiers;
import org.simmetrics.tokenizers.Tokenizer;
import org.simmetrics.tokenizers.Tokenizers;

import com.google.common.collect.Multiset;

@SuppressWarnings({ "javadoc", "deprecation" })
@RunWith(Enclosed.class)
public class StringMetricsTest {

	public static class Create {

		private final Metric<String> metric = new Identity<>();
		private final Metric<List<String>> listMetric = new Identity<>();
		private final Metric<Set<String>> setMetric = new Identity<>();

		private final Simplifier simplifier = Simplifiers.toLowerCase();
		private final Simplifier simplifier2 = Simplifiers.removeNonWord();
		private final Tokenizer tokenizer = Tokenizers.whitespace();

		@Test
		public void shouldReturnSame() {
			StringMetric s = new ForString(metric);
			assertSame(s, StringMetrics.create(s));
		}

		@Test
		public void shouldReturnForString() {
			StringMetric wrapped = StringMetrics.create(metric);
			assertEquals(ForString.class, wrapped.getClass());
			ForString forString = (ForString) wrapped;
			assertSame(metric, forString.getMetric());
		}

		@Test
		public void shouldReturnForStringWithSimplifier() {
			ForString forString = new ForString(metric);
			StringMetric wrapped = StringMetrics.create(forString, simplifier);

			assertEquals(ForStringWithSimplifier.class, wrapped.getClass());
			ForStringWithSimplifier fsws = (ForStringWithSimplifier) wrapped;
			assertSame(metric, fsws.getMetric());
			assertSame(simplifier, fsws.getSimplifier());
		}

		@Test
		public void shouldReturnForStringWithChainedSimplifiers() {
			ForStringWithSimplifier forString = new ForStringWithSimplifier(metric, simplifier);
			StringMetric wrapped = StringMetrics.create(forString, simplifier2);

			assertEquals(ForStringWithSimplifier.class, wrapped.getClass());
			ForStringWithSimplifier fsws = (ForStringWithSimplifier) wrapped;
			assertSame(metric, fsws.getMetric());
		}

		@Test
		public void shouldReturnForListWithSimplifier() {
			ForList forList = new ForList(listMetric, tokenizer);
			StringMetric wrapped = StringMetrics.create(forList, simplifier);

			assertEquals(ForListWithSimplifier.class, wrapped.getClass());
			ForListWithSimplifier flws = (ForListWithSimplifier) wrapped;
			assertSame(listMetric, flws.getMetric());
			assertEquals(simplifier, flws.getSimplifier());
			assertSame(tokenizer, flws.getTokenizer());
		}

		@Test
		public void shouldReturnForListWithChainedSimplifiers() {
			ForListWithSimplifier forList = new ForListWithSimplifier(listMetric, simplifier, tokenizer);
			StringMetric wrapped = StringMetrics.create(forList, simplifier2);

			assertEquals(ForListWithSimplifier.class, wrapped.getClass());
			ForListWithSimplifier flws = (ForListWithSimplifier) wrapped;
			assertSame(listMetric, flws.getMetric());
			assertThat(flws.getSimplifier(), chain(simplifier2, simplifier));
			assertSame(tokenizer, flws.getTokenizer());

		}

		@Test
		public void shouldReturnForSetWithSimplifier() {
			ForSet forSet = new ForSet(setMetric, tokenizer);
			StringMetric wrapped = StringMetrics.create(forSet, simplifier);

			assertEquals(ForSetWithSimplifier.class, wrapped.getClass());
			ForSetWithSimplifier fsws = (ForSetWithSimplifier) wrapped;
			assertSame(setMetric, fsws.getMetric());
			assertSame(simplifier, fsws.getSimplifier());
			assertSame(tokenizer, fsws.getTokenizer());

		}

		@Test
		public void shouldReturnForSetWithChainedSimplifiers() {
			ForSetWithSimplifier forSet = new ForSetWithSimplifier(setMetric, simplifier, tokenizer);
			StringMetric wrapped = StringMetrics.create(forSet, simplifier2);

			assertEquals(ForSetWithSimplifier.class, wrapped.getClass());
			ForSetWithSimplifier fsws = (ForSetWithSimplifier) wrapped;
			assertSame(setMetric, fsws.getMetric());
			assertThat(fsws.getSimplifier(), chain(simplifier2, simplifier));
			assertSame(tokenizer, fsws.getTokenizer());
		}

	}

	public static class CreateForList {

		private Metric<List<String>> metric = new Identity<>();
		private Tokenizer tokenizer = Tokenizers.whitespace();
		private Simplifier simplifier = Simplifiers.toLowerCase();

		@Test
		public void shouldReturnForList() {

			StringMetric wrapped = StringMetrics.createForListMetric(metric, tokenizer);
			assertEquals(ForList.class, wrapped.getClass());
			ForList forList = (ForList) wrapped;
			assertSame(metric, forList.getMetric());
			assertSame(tokenizer, forList.getTokenizer());
		}

		@Test
		public void shouldReturnForListWithSimplifier() {

			StringMetric wrapped = StringMetrics.createForListMetric(metric, simplifier, tokenizer);
			assertEquals(ForListWithSimplifier.class, wrapped.getClass());
			ForListWithSimplifier forList = (ForListWithSimplifier) wrapped;
			assertSame(metric, forList.getMetric());
			assertSame(tokenizer, forList.getTokenizer());
			assertSame(simplifier, forList.getSimplifier());
		}

	}

	public static class CreateForSet {

		private Metric<Set<String>> metric = new Identity<>();
		private Tokenizer tokenizer = Tokenizers.whitespace();
		private Simplifier simplifier = Simplifiers.toLowerCase();

		@Test
		public void shouldReturnForSet() {

			StringMetric wrapped = StringMetrics.createForSetMetric(metric, tokenizer);
			assertEquals(ForSet.class, wrapped.getClass());
			ForSet forSet = (ForSet) wrapped;
			assertSame(metric, forSet.getMetric());
			assertSame(tokenizer, forSet.getTokenizer());
		}

		@Test
		public void shouldReturnForSetWithSimplifier() {

			StringMetric wrapped = StringMetrics.createForSetMetric(metric, simplifier, tokenizer);
			assertEquals(ForSetWithSimplifier.class, wrapped.getClass());
			ForSetWithSimplifier forSet = (ForSetWithSimplifier) wrapped;
			assertSame(metric, forSet.getMetric());
			assertSame(tokenizer, forSet.getTokenizer());
			assertSame(simplifier, forSet.getSimplifier());
		}

	}
	
	public static class CreateForMultiset {

		private Metric<Multiset<String>> metric = new Identity<>();
		private Tokenizer tokenizer = Tokenizers.whitespace();
		private Simplifier simplifier = Simplifiers.toLowerCase();

		@Test
		public void shouldReturnForSet() {

			StringMetric wrapped = StringMetrics.createForMultisetMetric(metric, tokenizer);
			assertEquals(ForMultiset.class, wrapped.getClass());
			ForMultiset forSet = (ForMultiset) wrapped;
			assertSame(metric, forSet.getMetric());
			assertSame(tokenizer, forSet.getTokenizer());
		}

		@Test
		public void shouldReturnForSetWithSimplifier() {

			StringMetric wrapped = StringMetrics.createForMultisetMetric(metric, simplifier, tokenizer);
			assertEquals(ForMultisetWithSimplifier.class, wrapped.getClass());
			ForMultisetWithSimplifier forSet = (ForMultisetWithSimplifier) wrapped;
			assertSame(metric, forSet.getMetric());
			assertSame(tokenizer, forSet.getTokenizer());
			assertSame(simplifier, forSet.getSimplifier());
		}

	}

	public static class CreateIdentity extends StringMetricTest {

		@Override
		protected Metric<String> getMetric() {
			return StringMetrics.identity();
		}

		@Override
		protected T[] getTests() {
			return new T[] { new T(0.0f, "To repeat repeat is to repeat", ""),
					new T(1.0f, "To repeat repeat is to repeat", "To repeat repeat is to repeat") };
		}
	}

	public static class CreateCosineSimilarity extends StringMetricTest {

		@Override
		protected Metric<String> getMetric() {
			return StringMetrics.cosineSimilarity();
		}
		
		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}
		
		@Override
		protected T[] getTests() {
			return new T[] { new T(0.5000f, "test string1", "test string2"),
					new T(0.5000f, "test string1", "test string2"), new T(0.7071f, "test", "test string2"),
					new T(0.0000f, "", "test string2"),

					new T(0.7500f, "aaa bbb ccc ddd", "aaa bbb ccc eee"), new T(0.7500f, "a b c d", "a b c e"), };
		}

	}

	public static class CreateDiceSimlarity extends StringMetricTest {

		@Override
		protected Metric<String> getMetric() {
			return StringMetrics.dice();
		}
		
		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}
		
		@Override
		protected T[] getTests() {
			return new T[] { new T(0.5000f, "test string1", "test string2"), new T(0.6666f, "test", "test string2"),
					new T(0.0000f, "", "test string2"), new T(0.7500f, "aaa bbb ccc ddd", "aaa bbb ccc eee"),
					new T(0.7500f, "a b c d", "a b c e"), };
		}

	}

	public static class CreateEuclideanMetric extends StringMetricTest {

		@Override
		protected Metric<String> getMetric() {
			return StringMetrics.euclideanDistance();
		}
		
		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}
		
		@Override
		protected T[] getTests() {
			return new T[] { new T(0.5000f, "test string1", "test string2"), new T(0.5527f, "test", "test string2"),
					new T(0.2928f, "", "test string2"), new T(0.7500f, "aaa bbb ccc ddd", "aaa bbb ccc eee"),
					new T(0.7500f, "a b c d", "a b c e"), };
		}

	}

	public static class CreateJaccard extends StringMetricTest {

		@Override
		protected Metric<String> getMetric() {
			return StringMetrics.jaccard();
		}
		
		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}
		
		@Override
		protected T[] getTests() {
			return new T[] { new T(0.3333f, "test string1", "test string2"), new T(0.5000f, "test", "test string2"),
					new T(0.0000f, "", "test string2"), new T(0.6000f, "aaa bbb ccc ddd", "aaa bbb ccc eee"),
					new T(0.6000f, "a b c d", "a b c e"), };
		}

	}

	public static class CreateGeneralizedJaccard extends StringMetricTest {

		@Override
		protected Metric<String> getMetric() {
			return StringMetrics.generalizedJaccard();
		}
		
		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}
		
		@Override
		protected T[] getTests() {
			return new T[] { new T(0.3333f, "test string1", "test string2"), new T(0.5000f, "test", "test string2"),
					new T(0.0000f, "", "test string2"), new T(0.6000f, "aaa bbb ccc ddd", "aaa bbb ccc eee"),
					new T(0.6000f, "a b c d", "a b c e"), };
		}

	}

	public static class CreateMongeElkan extends StringMetricTest {

		@Override
		protected Metric<String> getMetric() {
			return StringMetrics.mongeElkan();
		}
		
		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}
		
		@Override
		protected T[] getTests() {
			return new T[] { new T(0.9286f, "test string1", "test string2"), new T(0.8660f, "test", "test string2"),
					new T(0.0000f, "", "test string2"), new T(0.7500f, "aaa bbb ccc ddd", "aaa bbb ccc eee"),
					new T(0.7500f, "a b c d", "a b c e"), };
		}

		@Override
		protected boolean satisfiesSubadditivity() {
			return false;
		}

	}

	public static class CreateOverlapCoefficient extends StringMetricTest {
		@Override
		protected Metric<String> getMetric() {
			return StringMetrics.overlapCoefficient();
		}
		
		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}
		
		@Override
		protected T[] getTests() {
			return new T[] { new T(0.5000f, "test string1", "test string2"), new T(1.0000f, "test", "test string2"),
					new T(0.0000f, "", "test string2"), new T(0.7500f, "aaa bbb ccc ddd", "aaa bbb ccc eee"),
					new T(0.7500f, "a b c d", "a b c e"), };
		}

		@Override
		protected boolean satisfiesCoincidence() {
			return false;
		}

	}

	public static class CreateQGramsMetric extends StringMetricTest {

		@Override
		protected Metric<String> getMetric() {
			return StringMetrics.qGramsDistance();
		}
		
		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}
		
		@Override
		protected T[] getTests() {
			return new T[] { new T(0.7857f, "test string1", "test string2"), new T(0.3999f, "test", "test string2"),
					new T(0.0000f, "", "test string2"), new T(0.7058f, "aaa bbb ccc ddd", "aaa bbb ccc eee"),
					new T(0.6666f, "a b c d", "a b c e"), };
		}

	}

	public static class CreateSimonWhite extends StringMetricTest {

		@Override
		protected Metric<String> getMetric() {
			return StringMetrics.simonWhite();
		}
		
		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}
		
		@Override
		protected T[] getTests() {
			return new T[] { new T(0.8889f, "test string1", "test string2"), new T(0.5000f, "test", "test string2"),
					new T(0.0000f, "", "test string2"), new T(0.7500f, "aaa bbb ccc ddd", "aaa bbb ccc eee") };
		}

	}

	public static class CreateSoundex extends StringMetricTest {

		@Override
		protected Metric<String> getMetric() {
			return StringMetrics.soundex();
		}
		
		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}
		
		@Override
		protected T[] getTests() {
			return new T[] { new T(0.5000f, "Tannhauser", "Ozymandias"), new T(1.0000f, "James", "Jones"),
					new T(0.0000f, "", "Jenkins"), new T(0.8833f, "Travis", "Trevor"),
					new T(0.8666f, "Marcus", "Marinus"), };
		}

		@Override
		protected boolean satisfiesCoincidence() {
			return false;
		}

		@Override
		protected boolean satisfiesSubadditivity() {
			return false;
		}
	}

	public static class Utilities {
		//TODO: Test
		@Test
		public void blockDistance() {
			assertNotNull(StringMetrics.blockDistance());
		}

	}

	public static class CreateStringMetrics {
		@Test
		public void damerauLevenshtein() {
			assertNotNull(StringMetrics.damerauLevenshtein());
		}

		@Test
		public void jaro() {
			assertNotNull(StringMetrics.jaro());
		}

		@Test
		public void jaroWinkler() {
			assertNotNull(StringMetrics.jaroWinkler());
		}

		@Test
		public void levenshtein() {
			assertNotNull(StringMetrics.levenshtein());
		}

		@Test
		public void needlemanWunch() {
			assertNotNull(StringMetrics.needlemanWunch());

		}

		@Test
		public void smithWaterman() {
			assertNotNull(StringMetrics.smithWaterman());

		}

		@Test
		public void smithWatermanGotoh() {
			assertNotNull(StringMetrics.smithWatermanGotoh());
		}
		
		@Test
		public void longestCommonSubsequence() {
			assertNotNull(StringMetrics.longestCommonSubsequence());
		}
		
		@Test
		public void longestCommonSubstring() {
			assertNotNull(StringMetrics.longestCommonSubstring());
		}
	}
	


	public static class ForListTest extends StringMetricTest {
		
		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}
		
		@Override
		protected StringMetric getMetric() {
			Metric<List<String>> identity = new Identity<>();
			return new StringMetrics.ForList(identity, whitespace());
		}
		
		@Override
		protected T[] getTests() {
			return new T[]{
					new T(1.0f, "a b c","a b c"),
					new T(0.0f, "a b c","a b c d"),
					new T(0.0f, "","a b c")
			};
		}
	}
	
	public static class ForListWithSimplifierTest extends StringMetricTest {
		
		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}

		@Override
		protected boolean satisfiesCoincidence() {
			return false;
		}
		
		@Override
		protected StringMetric getMetric() {
			Metric<List<String>> identity = new Identity<>();
			return new StringMetrics.ForListWithSimplifier(identity, toLowerCase(), whitespace());
		}
		
		@Override
		protected T[] getTests() {
			return new T[]{
					new T(1.0f, "A B C","a b c"),
					new T(0.0f, "a b c","a b c d"),
					new T(0.0f, "","a b c")
			};
		}
	}
	
	public static class ForSetTest extends StringMetricTest {
		
		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}
		
		@Override
		protected StringMetric getMetric() {
			Metric<Set<String>> identity = new Identity<>();
			return new StringMetrics.ForSet(identity, whitespace());
		}
		
		@Override
		protected T[] getTests() {
			return new T[]{
					new T(1.0f, "a b c","a b c"),
					new T(0.0f, "a b c","a b c d"),
					new T(0.0f, "","a b c")
			};
		}
	}
	
	public static class ForSetWithSimplifierTest extends StringMetricTest {
		
		@Override
		protected boolean satisfiesCoincidence() {
			return false;
		}
		
		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}
		
		@Override
		protected StringMetric getMetric() {
			Metric<Set<String>> identity = new Identity<>();
			return new StringMetrics.ForSetWithSimplifier(identity, toLowerCase(), whitespace());
		}
		
		@Override
		protected T[] getTests() {
			return new T[]{
					new T(1.0f, "A B C","a b c"),
					new T(0.0f, "a b c","a b c d"),
					new T(0.0f, "","a b c")
			};
		}
	}
	
	public static class ForMultisetTest extends StringMetricTest {
		
		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}
		
		@Override
		protected StringMetric getMetric() {
			Metric<Multiset<String>> identity = new Identity<>();
			return new StringMetrics.ForMultiset(identity, whitespace());
		}
		
		@Override
		protected T[] getTests() {
			return new T[]{
					new T(1.0f, "a b c","a b c"),
					new T(0.0f, "a b c","a b c d"),
					new T(0.0f, "","a b c")
			};
		}
	}
	
	public static class ForMultisetWithSimplifierTest extends StringMetricTest {
		
		@Override
		protected boolean satisfiesCoincidence() {
			return false;
		}
		
		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}
		
		@Override
		protected StringMetric getMetric() {
			Metric<Multiset<String>> identity = new Identity<>();
			return new StringMetrics.ForMultisetWithSimplifier(identity, toLowerCase(), whitespace());
		}
		
		@Override
		protected T[] getTests() {
			return new T[]{
					new T(1.0f, "A B C","a b c"),
					new T(0.0f, "a b c","a b c d"),
					new T(0.0f, "","a b c")
			};
		}
	}
	
	public static class ForStringTest extends StringMetricTest {
		
		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}
		
		@Override
		protected StringMetric getMetric() {
			Metric<String> identity = new Identity<>();
			return new ForString(identity);
		}
		
		@Override
		protected T[] getTests() {
			return new T[]{
					new T(1.0f, "a b c","a b c"),
					new T(0.0f, "a b c","a b c d"),
					new T(0.0f, "","a b c")
			};
		}
	}
	
	public static class ForStringWithSimplifierTest extends StringMetricTest {
		
		@Override
		protected boolean satisfiesCoincidence() {
			return false;
		}
		
		@Override
		protected boolean toStringIncludesSimpleClassName() {
			return false;
		}
		
		@Override
		protected StringMetric getMetric() {
			Metric<String> identity = new Identity<>();
			return new StringMetrics.ForStringWithSimplifier(identity, toLowerCase());
		}
		
		@Override
		protected T[] getTests() {
			return new T[]{
					new T(1.0f, "A B C","a b c"),
					new T(0.0f, "a b c","a b c d"),
					new T(0.0f, "","a b c")
			};
		}
	}
}
