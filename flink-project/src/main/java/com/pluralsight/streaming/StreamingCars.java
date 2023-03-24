/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pluralsight.streaming;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamUtils;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;

import java.util.Arrays;
import java.util.List;

/**
 * Skeleton for a Flink DataStream Job.
 *
 * <p>For a tutorial how to write a Flink application, check the
 * tutorials and examples on the <a href="https://flink.apache.org">Flink Website</a>.
 *
 * <p>To package your application into a JAR file for execution, run
 * 'mvn clean package' on the command line.
 *
 * <p>If you change the name of the main class (with the public static void main(String[] args))
 * method, change the respective entry in the POM.xml file (simply search for 'mainClass').
 */
public class StreamingCars {

	public static void main(String[] args) throws Exception {

		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		DataStream<String> carStream = env.fromElements("acura,door,2017,1000,57792459",
				"yaris,4by4,2017,10000,58732579",
				"maybach,luxury,2021,100000,91284314",
				"jeep,ranger,2011,40000,4556811",
				"acura,door,2017,1000,57792459",
				"yaris,4by4,2017,10000,58732579",
				"maybach,luxury,2021,100000,91284314",
				"jeep,ranger,2011,40000,4556811",
				"acura,door,2017,1000,57792459",
				"yaris,4by4,2017,10000,58732579",
				"maybach,luxury,2021,100000,91284314",
				"jeep,ranger,2011,40000,4556811",
				"acura,door,2017,1000,57792459",
				"yaris,4by4,2017,10000,58732579",
				"maybach,luxury,2021,100000,91284314",
				"jeep,ranger,2011,40000,4556811",
				"acura,door,2017,1000,57792459",
				"yaris,4by4,2017,10000,58732579",
				"maybach,luxury,2021,100000,91284314",
				"jeep,ranger,2011,40000,4556811",
				"acura,door,2017,1000,57792459",
				"yaris,4by4,2017,10000,58732579",
				"maybach,luxury,2021,100000,91284314",
				"jeep,ranger,2011,40000,4556811",
				"acura,door,2017,1000,57792459",
				"yaris,4by4,2017,10000,58732579",
				"maybach,luxury,2021,100000,91284314",
				"jeep,ranger,2011,40000,4556811",
				"acura,door,2017,1000,57792459",
				"yaris,4by4,2017,10000,58732579");

		//DataStream<String> filteredStream = carStream.filter((FilterFunction<String>) line -> !line.contains("brand,model,year,price,mileage"));

		DataStream<CarPojo> carDetails = carStream
				.map(new MapFunction<String, CarPojo>() {
					public CarPojo map(String row) throws Exception {

						String[] fields = row.split(",");

						return new CarPojo(fields[0], fields[1], fields[2], Integer.parseInt(fields[3]), Integer.parseInt(fields[4]));
					}
				});

		final StreamingFileSink<String> sink = StreamingFileSink
				.forRowFormat(new Path("flink-project/src/main/resources/USA_cars_results"),
						new SimpleStringEncoder<String>("UTF8"))
				.build();


		KeyedStream<CarPojo, String> keyedCarStream = carDetails.keyBy(carPojo -> carPojo.brand + carPojo.model);

		DataStream<String> carResults = keyedCarStream.map(new MapFunction<CarPojo, String>() {
			@Override
			public String map(CarPojo value) throws Exception {

				return value.brand + ", " +  value.model + ", " + value.miles + ", ";
			}
		});

		carResults.addSink(sink);
		keyedCarStream.print();

		env.execute("Streaming cars");
	}
}
