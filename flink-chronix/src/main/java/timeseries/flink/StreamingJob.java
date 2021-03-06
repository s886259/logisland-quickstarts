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

package timeseries.flink;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

import java.util.Properties;

/**
 * Skeleton for a Flink Streaming Job.
 *
 * <p>For a tutorial how to write a Flink streaming application, check the
 * tutorials and examples on the <a href="http://flink.apache.org/docs/stable/">Flink Website</a>.
 *
 * <p>To package your application into a JAR file for execution, run
 * 'mvn clean package' on the command line.
 *
 * <p>If you change the name of the main class (with the public static void main(String[] args))
 * method, change the respective entry in the POM.xml file (simply search for 'mainClass').
 */
public class StreamingJob {

    public static void main(String[] args) throws Exception {
        // set up the streaming execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // configure event-time characteristics
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        // generate a Watermark every second
        env.getConfig().setAutoWatermarkInterval(1000);

        // configure Kafka consumer (don'tx
        Properties props = new Properties();
        props.setProperty("zookeeper.connect", "zookeeper:2181"); // Zookeeper default host:port
        props.setProperty("bootstrap.servers", "kafka:9092"); // Broker default host:port
        props.setProperty("group.id", "flink-chronix");           // Consumer group ID
        props.setProperty("auto.offset.reset", "earliest");       // Always read topic from start

        // create a Kafka consumer
        FlinkKafkaConsumer010<String> consumer =
                new FlinkKafkaConsumer010<>(
                        "logisland_measures",
                        new SimpleStringSchema(),
                        props);

        // create Kafka consumer data source
        DataStream<String> rides = env.addSource(consumer);

        rides.print();

        // execute program
        env.execute("Flink Streaming Java API Skeleton");
    }
}
