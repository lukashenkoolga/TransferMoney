package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {
    private int amountValid = 1000;
    private int amountInvalid = 25000;


    private DashboardPage shouldOpenDashboardPage() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        return verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyFromCard2toCard1() {
        DashboardPage dashboardPage = shouldOpenDashboardPage();
        dashboardPage.dashboardPageVisible();
        int expected1 = dashboardPage.getBalanceCard1() + amountValid;
        int expected2 = dashboardPage.getBalanceCard2() - amountValid;
        val moneyTransfer = dashboardPage.card1();
        moneyTransfer.moneyTransferVisible();
        moneyTransfer.setTransferAmount(amountValid);
        moneyTransfer.setFrom(DataHelper.getCardNumber2());
        moneyTransfer.doTransfer();
        assertEquals(expected1, dashboardPage.getBalanceCard1());
        assertEquals(expected2, dashboardPage.getBalanceCard2());
    }

    @Test
    void shouldTransferMoneyFromCard1toCard2() {
        DashboardPage dashboardPage = shouldOpenDashboardPage();
        dashboardPage.dashboardPageVisible();
        int expected1 = dashboardPage.getBalanceCard2() + amountValid;
        int expected2 = dashboardPage.getBalanceCard1() - amountValid;
        val moneyTransfer = dashboardPage.card2();
        moneyTransfer.moneyTransferVisible();
        moneyTransfer.setTransferAmount(amountValid);
        moneyTransfer.setFrom(DataHelper.getCardNumber1());
        moneyTransfer.doTransfer();
        assertEquals(expected1, dashboardPage.getBalanceCard2());
        assertEquals(expected2, dashboardPage.getBalanceCard1());
    }

    @Test
    void shouldTransferInvalidAmountFromCard2toCard1() {
        DashboardPage dashboardPage = shouldOpenDashboardPage();
        dashboardPage.dashboardPageVisible();
        val moneyTransfer = dashboardPage.card1();
        moneyTransfer.moneyTransferVisible();
        moneyTransfer.setTransferAmount(amountInvalid);
        moneyTransfer.setFrom(DataHelper.getCardNumber2());
        moneyTransfer.doTransfer();
        moneyTransfer.errorTransfer();
    }

}

