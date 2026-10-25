package com.coffeemachine;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenerateRapportTest {
    @Test
    void shouldStartWithNoSalesAndZeroRevenue() {
        GenerateRapport report = new GenerateRapport();

        assertEquals(0, report.getSoldCount(DrinkType.TEA));
        assertEquals(0, report.getSoldCount(DrinkType.COFFEE));
        assertEquals(0, report.getSoldCount(DrinkType.HOT_CHOCOLATE));
        assertEquals(0, report.getSoldCount(DrinkType.ORANGE_JUICE));
        assertEquals(0, report.getTotalRevenue());
    }

    @Test
    void shouldRecordSaleAndUpdateRevenue() {
        GenerateRapport report = new GenerateRapport();

        report.recordSale(DrinkType.TEA, 40);
        report.recordSale(DrinkType.COFFEE, 60);
        report.recordSale(DrinkType.TEA, 40);

        assertEquals(2, report.getSoldCount(DrinkType.TEA));
        assertEquals(1, report.getSoldCount(DrinkType.COFFEE));
        assertEquals(0, report.getSoldCount(DrinkType.HOT_CHOCOLATE));
        assertEquals(0, report.getSoldCount(DrinkType.ORANGE_JUICE));
        assertEquals(140, report.getTotalRevenue());
    }
    
@Test
void shouldPrintSalesReport() {
    GenerateRapport report = new GenerateRapport();
    report.recordSale(DrinkType.TEA, 40);
    report.recordSale(DrinkType.COFFEE, 60);
    report.recordSale(DrinkType.TEA, 40);

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;

    try {
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));

        report.printReport();

    } finally {
        System.setOut(originalOut);
    }

    String result = output.toString(StandardCharsets.UTF_8);

    assertEquals(true, result.contains("Thé : 2"));
    assertEquals(true, result.contains("Café : 1"));
    assertEquals(true, result.contains("Recettes totales : 1.40 EUR"));
}


}
