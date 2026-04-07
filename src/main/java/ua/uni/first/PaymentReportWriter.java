package ua.uni.first;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class PaymentReportWriter {
    public static void writeReport(Path out, List<Payment> payments) throws IOException {
        Objects.requireNonNull(out, "out must not be null");
        Objects.requireNonNull(payments, "payments must not be null");

        int newCount = 0;
        int paidCount = 0;
        int failedCount = 0;
        double paidTotalCents = 0.0;
        for (Payment payment : payments) {
            switch (payment.status()) {
                case NEW -> newCount++;
                case PAID -> {
                    paidCount++;
                    paidTotalCents += payment.amountCents();
                }
                case FAILED -> failedCount++;
            }
        }

        Path target = out.toAbsolutePath();
        Path dir = target.getParent();
        if (dir == null) {
            dir = Path.of(".").toAbsolutePath().normalize();
            target = dir.resolve(out.getFileName());
        }
        Files.createDirectories(dir);

        Path tmp = Files.createTempFile(dir, target.getFileName().toString(), ".tmp");
        try {
            try (BufferedWriter writer = Files.newBufferedWriter(tmp)) {
                writer.write("invalidLines=0");
                writer.newLine();
                writer.write("paidTotalCents=" + paidTotalCents);
                writer.newLine();
                writer.write("NEW=" + newCount + ", PAID=" + paidCount + ", FAILED=" + failedCount);
                writer.newLine();
            }

            try {
                Files.move(tmp, target,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING,
                        java.nio.file.StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(tmp, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(tmp);
        }
    }
}
