/*
Copyright (C) 2013 u.wol@wwu.de 
 
This file is part of ComputationalEconomy.

ComputationalEconomy is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

ComputationalEconomy is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with ComputationalEconomy. If not, see <http://www.gnu.org/licenses/>.
 */

package compecon.economy.agent;

import java.util.Set;

import compecon.economy.sectors.financial.Bank;
import compecon.economy.sectors.financial.BankAccount;
import compecon.economy.sectors.financial.Currency;
import compecon.engine.timesystem.ITimeSystemEvent;

public interface Agent {

	public void initialize();

	public void deconstruct();

	public void assureBankAccountTransactions();

	public void assureBankCustomerAccount();

	public BankAccount getBankAccountTransactions();

	public int getId();

	public boolean isDeconstructed();

	public Set<ITimeSystemEvent> getTimeSystemEvents();

	public Bank getPrimaryBank();

	public Currency getPrimaryCurrency();

	public void onBankCloseBankAccount(BankAccount bankAccount);

	public void setId(int id);

	public void setPrimaryBank(final Bank primaryBank);

	public void setPrimaryCurrency(final Currency primaryCurrency);

	public void setReferenceCredit(final double referenceCredit);
}