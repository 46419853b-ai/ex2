package theatricalplays;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class StatementPrinter {

    public String print(Invoice invoice, Map<String, Play> plays) {
        var totalAmount = 0;
        var volumeCredits = 0;
        var result = printHeader(invoice);

        NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

        for (var perf : invoice.performances) {
            var play = playFor(plays, perf);
            var thisAmount = amountFor(perf, play);

            volumeCredits += volumeCreditsFor(perf, play);

            result += formatPerformanceLine(perf, play, thisAmount, frmt);
            totalAmount += thisAmount;
        }
        result += String.format("Amount owed is %s\n", frmt.format(totalAmount / 100));
        result += String.format("You earned %s credits\n", volumeCredits);
        return result;
    }

    private String printHeader(Invoice invoice) {
        return String.format("Statement for %s\n", invoice.customer);
    }

    private String formatPerformanceLine(Performance perf, Play play, int thisAmount, NumberFormat frmt) {
        return String.format("  %s: %s (%s seats)\n", play.name, frmt.format(thisAmount / 100), perf.audience);
    }

    private Play playFor(Map<String, Play> plays, Performance perf) {
        return plays.get(perf.playID);
    }

    private int amountFor(Performance perf, Play play) {
        var thisAmount = 0;

        switch (play.type) {
            case "tragedy":
                thisAmount = 40000;
                if (perf.audience > 30) {
                    thisAmount += 1000 * (perf.audience - 30);
                }
                break;
            case "comedy":
                thisAmount = 30000;
                if (perf.audience > 20) {
                    thisAmount += 10000 + 500 * (perf.audience - 20);
                }
                thisAmount += 300 * perf.audience;
                break;
            default:
                throw new Error("unknown type: ${play.type}");
        }

        return thisAmount;
    }

    private int volumeCreditsFor(Performance perf, Play play) {
        var volumeCredits = Math.max(perf.audience - 30, 0);

        if ("comedy".equals(play.type)) {
            volumeCredits += (int) Math.floor(perf.audience / 5);
        }

        return volumeCredits;
    }
}





