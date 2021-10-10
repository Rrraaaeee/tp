package cooper.finance;

import java.util.ArrayList;

/**
 * Handles all actions and operations pertaining to financial assistance functions of the application.
 */
public class FinanceManager {
    private final ArrayList<Integer> balanceSheet;
    private final InvoiceBuilder invoiceBuilder;

    public FinanceManager() {
        balanceSheet = new ArrayList<>();
        invoiceBuilder = new InvoiceBuilder();
    }

    public ArrayList<Integer> getBalanceSheet() {
        return balanceSheet;
    }

    /**
     * Adds specified amount input by user to the balanceSheet, with specified inflow or outflow.
     * @param amount amount inout by user
     * @param isInflow boolean which specifies if {@code amount} is inflow or outflow
     */
    public void addBalance(int amount, boolean isInflow) {
        if (isInflow) {
            balanceSheet.add(amount);
        } else {
            balanceSheet.add(amount * -1);
        }
    }

    public void createInvoice() {
        for (Integer entry : balanceSheet) {
            invoiceBuilder.createInvoiceEntry("general", entry.toString(), "1", "0", entry.toString());
        }
        invoiceBuilder.compileInvoice();
    }
}
