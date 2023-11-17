package com.example.ticketstream;

import java.time.LocalDateTime;

public class TestConstants {
	public static LocalDateTime searchStart = LocalDateTime.now().plusDays(1).withHour(14).withMinute(0);
	public static LocalDateTime searchEnd = searchStart.plusHours(5);
}
