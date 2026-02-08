package com.sv.meeting.notify.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BookingNotificationListener {
  @KafkaListener(topics = "${app.kafka.bookingTopic}")
  public void onEvent(BookingResultEvent event) {
    log.info("EMAIL: status={} bookingId={} roomId={} userId={} startUtc={} endUtc={} reason={}",
        event.status(), event.bookingId(), event.roomId(), event.userId(), event.startUtc(), event.endUtc(), event.reason());
  }
}
