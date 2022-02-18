import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

public class Main {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    String mode = args.length > 0 ? args[0] : null;
    System.out.println("Running with mode: " + mode);
    if ("c".equals(mode)) {
      runConsumer();
    } else if ("p".equals(mode)) {
      runProducer();
    } else {
      System.out.printf("Invalid mode: " + mode);
    }
  }

  private static void runConsumer() {
    Consumer<Long, String> consumer = Creators.createConsumer();

    int noMessageFound = 0;
    while (true) {
      ConsumerRecords<Long, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));
      if (consumerRecords.isEmpty()) {
        noMessageFound++;
        if (noMessageFound > Config.MAX_NO_MESSAGE_FOUND_COUNT) {
          break;
        }
        continue;
      }

      consumerRecords.forEach(
          record -> {
            System.out.println("Key: " + record.key());
            System.out.println("Value: " + record.value());
            System.out.println("Partition: " + record.partition());
            System.out.println("Offset: " + record.offset());
          });
      consumer.commitSync();
    }
  }

  private static void runProducer() throws ExecutionException, InterruptedException {
    Producer<Long, String> producer = Creators.createProducer();
    for (long idx = 0; idx < Config.MESSAGE_COUNT; idx++) {
      ProducerRecord<Long, String> record =
          new ProducerRecord<>(Config.TOPIC_NAME, idx, "This is record " + idx);
      RecordMetadata metadata = producer.send(record).get();
      System.out.println(
          "Record sent with key "
              + idx
              + " to partition "
              + metadata.partition()
              + " with offset "
              + metadata.offset());
    }
  }
}
