Computational Economy
=====================

Computational Economy is an agent-based computational economics simulator implemented in Java (https://github.com/uwol).

<a href="https://travis-ci.org/uwol/ComputationalEconomy"><img src="https://api.travis-ci.org/uwol/ComputationalEconomy.png"></a>


Agent types
-----------

* Agent type *Household*: Households offer labour hours and consume goods according to CES or Cobb-Douglas utility functions. Intertemporal consumption and retirement saving preferences are modeled by Irving-Fisher and Modigliani intertemporal choice models.
* Agent type *Factory*: Factories produce multiple goods (e. g. iron, coal, ...) according to an input-output-model based on Cobb-Douglas, CES and root production functions. Depending on the configured economic model, factories produce and accumulate capital goods, which induce economic growth and depreciate over time (-> Solow-Swan).
* Agent type *Credit bank*: Credit banks manage bank accounts, create money by credit, trade currencies, follow minimum reserve requirements of central banks and buy bonds for deposits (fractional reserve banking).
* Agent type *Central bank*: Central banks adjust key interest rates based on price indices. Key interest rates induce changes in buying behaviour via a monetary transmission mechanism.
* Agent type *Trader*: Traders import goods from foreign markets for arbitrage.
* Agent type *State*: States emit bonds, which are bought by credit banks for customer savings deposits. Thereby, national debt represents retirement savings of households.


Other entities / features
-------------------------

* General equilibrium: Macroeconomic equilibria reproducibly emerge from non-stochastic microeconomic decision making of agents (-> Arrow-Debreu).
* Markets: Sellers offer goods on markets. Settlement markets transfer ownership of goods and money, automatically.
* Currencies: National currency zones, foreign exchange markets, arbitrage.
* Shares: Joint-stock companies are owned by agents and pay dividends to them every period.
* Bonds: Bonds are given as security for credit in open market operations between central and credit banks.
* Time system: Agents register their actions as events in a global time system (observer pattern). At runtime agents can be instantiated and deconstructed at arbitrary points in time, enabling a dynamic population. Execution of events is not bound to a fixed sequential order of economic phases, e. g. for production and consumption.


Publication
-----------

* Wolffgang, U.: [A Multi-Agent Non-Stochastic Economic Simulator](http://uwol.github.io/docs/2015-06_CEF2015-322.pdf). In Proc. of the 21st Int. Conf. on Computing in Economics and Finance (CEF 2015), June 2015.


How to run
----------

Import into Eclipse:

1. Clone or download the repository. 
2. In Eclipse import the directory as a an `existing Maven project`.
3. Right click file `src/main/java/io.github.uwol.compecon.simulation.impl.DashboardSimulationImpl.java` and `run as Java application`.


Build process
-------------

* The build process is based on Maven (version 3 or higher). Building requires a JDK 8.
* To build, run:

```
$ mvn clean package
```

* You should see output like this:

```
[INFO] Scanning for projects...
...
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running io.github.uwol.compecon.economy.sectors.financial.CreditBankTest
loading configuration file testing.configuration.properties
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.784 sec
Running io.github.uwol.compecon.economy.sectors.household.HouseholdImplTest
...
Results :

Tests run: 44, Failures: 0, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

* To only run the tests:

```
$ mvn clean test
```


Screenshots
-----------

![screenshot1](http://uwol.github.io/img/compecon/compecon1.png)

![screenshot2](http://uwol.github.io/img/compecon/compecon2.png)

![screenshot3](http://uwol.github.io/img/compecon/compecon3.png)

![screenshot4](http://uwol.github.io/img/compecon/compecon4.png)

![screenshot5](http://uwol.github.io/img/compecon/compecon5.png)

![screenshot6](http://uwol.github.io/img/compecon/compecon6.png)

![screenshot7](http://uwol.github.io/img/compecon/compecon7.png)

![screenshot8](http://uwol.github.io/img/compecon/compecon8.png)
