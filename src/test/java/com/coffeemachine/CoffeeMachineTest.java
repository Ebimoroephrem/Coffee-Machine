package com.coffeemachine;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import org.mockito.ArgumentCaptor;

import org.junit.jupiter.api.Test;

@ExtendWith(MockitoExtension.class)
class CoffeeMachineTest {

    @Mock
    DrinkMaker drinkMaker;
    @Mock
    Display display;
    @Mock
    EmailNotifier emailNotifier;

    @Mock
    BeverageQuantityChecker quantityChecker;

    @Test
    void tea_with_one_sugar() {
        CoffeeMachineLogic machine = new CoffeeMachineLogic(drinkMaker, display);

        machine.process(new Order(DrinkType.TEA, 1, 40));

        verify(drinkMaker).execute("T:1:0");
    }

    @Test
    void coffee_with_two_sugars() {
        CoffeeMachineLogic machine = new CoffeeMachineLogic(drinkMaker, display);

        machine.process(new Order(DrinkType.COFFEE, 2, 60));

        verify(drinkMaker).execute("C:2:0");
    }

    @Test
    void chocolate_without_sugar_no_stick() {
        CoffeeMachineLogic machine = new CoffeeMachineLogic(drinkMaker, display);

        machine.process(new Order(DrinkType.HOT_CHOCOLATE, 0, 50));

        verify(drinkMaker).execute("H::");
    }

    @Test
    void coffee_without_sugar_no_stick() {
        CoffeeMachineLogic machine = new CoffeeMachineLogic(drinkMaker, display);

        machine.process(new Order(DrinkType.COFFEE, 0, 60));

        verify(drinkMaker).execute("C::");
    }

    @Test
    void shouldDisplayMessageReceivedFromDrinkMaker() {
        CoffeeMachineLogic machine = new CoffeeMachineLogic(drinkMaker, display);

        machine.onMessage("M:Bonjour !");

        verify(display).showMessage("Bonjour !");
    }

    @Test
    void shouldIgnoreMessageWithoutPrefix() {
        CoffeeMachineLogic machine = new CoffeeMachineLogic(drinkMaker, display);

        machine.onMessage("Bonjour !");

        verifyNoInteractions(display);
    }

    @Test
    void shouldIgnoreNullMessage() {
        CoffeeMachineLogic machine = new CoffeeMachineLogic(drinkMaker, display);

        machine.onMessage(null);

        verifyNoInteractions(display);
    }

    @Test
    void Not_Prepare_DrinkWhen_MoneyIs_Insufficient() {
        CoffeeMachineLogic machine = new CoffeeMachineLogic(drinkMaker, display);

        machine.process(new Order(DrinkType.COFFEE, 0, 40));

        verify(drinkMaker).execute(
                "M:Montant insuffisant. Il manque 20 €");
    }

    @Test
    void Prepare_DrinkWhenMoney_IsMoreThanPrice() {
        CoffeeMachineLogic machine = new CoffeeMachineLogic(drinkMaker, display);

        machine.process(new Order(DrinkType.TEA, 1, 50));

        verify(drinkMaker).execute("T:1:0");
    }

    @Test
    void Prepare_Orange_Juice() {
        CoffeeMachineLogic machine = new CoffeeMachineLogic(drinkMaker, display);

        machine.process(new Order(DrinkType.ORANGE_JUICE, 0, 60));

        verify(drinkMaker).execute("O::");
    }

    @Test
    void Prepare_ExtraHot_CoffeeWithout_Sugar() {
        CoffeeMachineLogic machine = new CoffeeMachineLogic(drinkMaker, display);

        machine.process(new Order(DrinkType.COFFEE, 0, 60, true));

        verify(drinkMaker).execute("Ch::");
    }

    @Test
    void Prepare_ExtraHot_ChocolateWith_OneSugar() {
        CoffeeMachineLogic machine = new CoffeeMachineLogic(drinkMaker, display);

        machine.process(new Order(DrinkType.HOT_CHOCOLATE, 1, 50, true));

        verify(drinkMaker).execute("Hh:1:0");
    }

    @Test
    void Prepare_ExtraHot_TeaWith_TwoSugars() {
        CoffeeMachineLogic machine = new CoffeeMachineLogic(drinkMaker, display);

        machine.process(new Order(DrinkType.TEA, 2, 40, true));

        verify(drinkMaker).execute("Th:2:0");
    }

    @Test
    void shouldRecordSaleWhenDrinkIsOrderedSuccessfully() {
        GenerateRapport report = new GenerateRapport();

        CoffeeMachineLogic machine = new CoffeeMachineLogic(drinkMaker, display, report, emailNotifier, quantityChecker);

        machine.process(new Order(DrinkType.TEA, 1, 40));

        verify(drinkMaker).execute("T:1:0");
        assertEquals(1, report.getSoldCount(DrinkType.TEA));
        assertEquals(40, report.getTotalRevenue());
    }

    @Test
    void NotRecord_SaleWhenMoney_IsInsufficient() {
        GenerateRapport report = new GenerateRapport();

        CoffeeMachineLogic machine = new CoffeeMachineLogic(drinkMaker, display, report, emailNotifier, quantityChecker);

        machine.process(new Order(DrinkType.COFFEE, 0, 40));

        verify(drinkMaker).execute(
                "M:Montant insuffisant. Il manque 20 €");

        assertEquals(0, report.getSoldCount(DrinkType.COFFEE));
        assertEquals(0, report.getTotalRevenue());
    }

    @Test
    void NotifyCompany_AndDisplayMessage_WhenDrinkIsEmpty() {
        GenerateRapport report = new GenerateRapport();

        CoffeeMachineLogic machine = new CoffeeMachineLogic(
                drinkMaker, display, report, emailNotifier, quantityChecker);

        org.mockito.Mockito.when(quantityChecker.isEmpty("coffee"))
                .thenReturn(true);

        machine.process(new Order(DrinkType.COFFEE, 0, 60));

        verify(drinkMaker, never()).execute("C::");
        verify(emailNotifier).notifierBoissonManquante("coffee");
        verify(display).showMessage(
                "Rupture de stock : coffee. Une notification a été envoyée.");
    }

    @Test
    void Prepare_Drink_When_Stock_Is_Available() {
        GenerateRapport report = new GenerateRapport();

        CoffeeMachineLogic machine = new CoffeeMachineLogic(
                drinkMaker, display, report, emailNotifier, quantityChecker);

        org.mockito.Mockito.when(quantityChecker.isEmpty("coffee"))
                .thenReturn(false);

        machine.process(new Order(DrinkType.COFFEE, 0, 60));

        verify(drinkMaker).execute("C::");
        verify(emailNotifier, never())
                .notifierBoissonManquante("coffee");
    }

}