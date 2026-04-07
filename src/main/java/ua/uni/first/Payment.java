package ua.uni.first;

public record Payment(int id, String email, PaymentStatus status, double amountCents) {

}
