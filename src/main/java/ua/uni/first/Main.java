package ua.uni.first;

import ua.uni.second.InboxArchiver;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        Path csv = args.length > 0 ? Path.of(args[0]) : Path.of("data/students.csv");
        PaymentLoader.LoadResult result = PaymentLoader.loadWithStats(csv);

        Path reportOut = Path.of("out/payment-report.txt");
        PaymentReportWriter.writeReport(reportOut, result.payments());
        InboxArchiver.archiveTmpFiles(Path.of("practical-data/inbox"), Path.of("practical-data/archive"));

        System.out.println("CSV file: " + csv.toAbsolutePath());
        System.out.println("Valid payments: " + result.payments().size());
        System.out.println("Invalid lines: " + result.invalidLines());
    }
}
