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

package compecon.engine.dashboard.panel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.XYDataset;

import compecon.economy.sectors.financial.Currency;
import compecon.economy.sectors.state.law.bookkeeping.BalanceSheet;
import compecon.engine.Simulation;
import compecon.engine.dashboard.panel.BalanceSheetPanel.BalanceSheetTableModel;
import compecon.engine.statistics.model.NotificationListenerModel.IModelListener;
import compecon.engine.statistics.model.PricesModel;
import compecon.engine.statistics.model.PricesModel.PriceModel;
import compecon.materia.GoodType;
import compecon.materia.InputOutputModel;

public class IndustriesPanel extends AbstractChartsPanel implements
		IModelListener {

	public class IndustriesPanelForCurrency extends JPanel implements
			IModelListener {

		public class IndustriesPanelForGoodTypeInCurrency extends JPanel
				implements IModelListener {

			protected final Currency currency;

			protected final GoodType goodType;

			protected JPanel priceTimeSeriesPanel;

			protected JPanel marketDepthPanel;

			public IndustriesPanelForGoodTypeInCurrency(Currency currency,
					GoodType goodType) {
				this.currency = currency;
				this.goodType = goodType;

				this.setLayout(new GridLayout(0, 2));

				this.add(createProductionPanel(currency, goodType));
				this.add(createFactoryBalanceSheetPanel(currency, goodType));

				Simulation.getInstance().getModelRegistry()
						.getPricesModel(currency).registerListener(this);
				// no registration with the market depth model, as they call
				// listeners synchronously

				notifyListener();
			}

			@Override
			public void notifyListener() {
				if (this.isShowing()) {
					if (this.priceTimeSeriesPanel != null)
						this.remove(this.priceTimeSeriesPanel);
					this.priceTimeSeriesPanel = createPriceTimeSeriesChartPanel(
							currency, goodType);
					this.add(priceTimeSeriesPanel);

					if (this.marketDepthPanel != null)
						this.remove(this.marketDepthPanel);
					this.marketDepthPanel = createMarketDepthPanel(currency,
							goodType);
					this.add(this.marketDepthPanel);

					validate();
					repaint();
				}
			}
		}

		protected final Currency currency;

		protected final JTabbedPane jTabbedPaneGoodType = new JTabbedPane();

		public IndustriesPanelForCurrency(Currency currency) {
			this.setLayout(new BorderLayout());

			this.currency = currency;
			this.add(jTabbedPaneGoodType, BorderLayout.CENTER);
			for (GoodType outputGoodType : GoodType.values()) {
				if (!outputGoodType.equals(GoodType.LABOURHOUR)) {
					IndustriesPanelForGoodTypeInCurrency panelForGoodType = new IndustriesPanelForGoodTypeInCurrency(
							currency, outputGoodType);
					jTabbedPaneGoodType.addTab(outputGoodType.name(),
							panelForGoodType);
				}
			}

			jTabbedPaneGoodType.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (e.getSource() instanceof JTabbedPane) {
						JTabbedPane pane = (JTabbedPane) e.getSource();
						IndustriesPanelForGoodTypeInCurrency selectedComponent = (IndustriesPanelForGoodTypeInCurrency) pane
								.getSelectedComponent();
						selectedComponent.notifyListener();
					}
				}
			});
		}

		@Override
		public void notifyListener() {
			if (this.isShowing()) {
				IndustriesPanelForGoodTypeInCurrency industryPanel = (IndustriesPanelForGoodTypeInCurrency) jTabbedPaneGoodType
						.getSelectedComponent();
				industryPanel.notifyListener();
			}
		}
	}

	protected final JTabbedPane jTabbedPaneCurrency = new JTabbedPane();

	public IndustriesPanel() {
		this.setLayout(new BorderLayout());
		for (Currency currency : Currency.values()) {
			IndustriesPanelForCurrency panelForCurrency = new IndustriesPanelForCurrency(
					currency);
			jTabbedPaneCurrency.addTab(currency.getIso4217Code(),
					panelForCurrency);
		}

		jTabbedPaneCurrency.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (e.getSource() instanceof JTabbedPane) {
					JTabbedPane pane = (JTabbedPane) e.getSource();
					IndustriesPanelForCurrency selectedComponent = (IndustriesPanelForCurrency) pane
							.getSelectedComponent();
					selectedComponent.notifyListener();
				}
			}
		});

		add(jTabbedPaneCurrency, BorderLayout.CENTER);
	}

	protected ChartPanel createProductionPanel(Currency currency,
			GoodType outputGoodType) {
		TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();

		timeSeriesCollection.addSeries(Simulation.getInstance()
				.getModelRegistry()
				.getGoodTypeProductionModel(currency, outputGoodType)
				.getOutputModel().getTimeSeries());
		for (GoodType inputGoodType : Simulation.getInstance()
				.getModelRegistry()
				.getGoodTypeProductionModel(currency, outputGoodType)
				.getInputGoodTypes()) {
			timeSeriesCollection.addSeries(Simulation.getInstance()
					.getModelRegistry()
					.getGoodTypeProductionModel(currency, outputGoodType)
					.getInputModel(inputGoodType).getTimeSeries());
		}

		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				outputGoodType.toString() + " Output", "Date", "Output",
				(XYDataset) timeSeriesCollection, true, true, false);
		configureChart(chart);
		chart.addSubtitle(new TextTitle("Inputs: "
				+ InputOutputModel.getProductionFunction(outputGoodType)
						.getInputGoodTypes().toString()));
		return new ChartPanel(chart);
	}

	protected JPanel createFactoryBalanceSheetPanel(final Currency currency,
			final GoodType goodType) {
		final BalanceSheetTableModel balanceSheetTableModel = new BalanceSheetTableModel(
				currency) {
			@Override
			protected BalanceSheet getModelData() {
				return Simulation.getInstance().getModelRegistry()
						.getBalanceSheetsModel(referenceCurrency)
						.getFactoryNationalAccountsBalanceSheet(goodType);
			}
		};
		return new BalanceSheetPanel(currency, balanceSheetTableModel,
				"Balance Sheet for " + currency.getIso4217Code() + " "
						+ goodType + " Factories");
	}

	protected ChartPanel createPriceTimeSeriesChartPanel(Currency currency,
			GoodType goodType) {
		JFreeChart priceChart = ChartFactory.createCandlestickChart(
				"Price Chart for " + goodType, "Time",
				"Price in " + currency.getIso4217Code(),
				this.getDefaultHighLowDataset(currency, goodType), false);
		ChartPanel chartPanel = new ChartPanel(priceChart);
		chartPanel.setDomainZoomable(true);
		chartPanel.setPreferredSize(new java.awt.Dimension(800, 400));
		return chartPanel;
	}

	protected ChartPanel createMarketDepthPanel(Currency currency,
			GoodType goodType) {
		XYDataset dataset = Simulation.getInstance().getModelRegistry()
				.getMarketDepthModel(currency)
				.getMarketDepthDataset(currency, goodType);
		JFreeChart chart = ChartFactory.createXYStepAreaChart(
				"Market Depth for " + goodType, "Price", "Volume", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		return new ChartPanel(chart);
	}

	protected DefaultHighLowDataset getDefaultHighLowDataset(Currency currency,
			GoodType goodType) {
		PricesModel pricesModel = Simulation.getInstance().getModelRegistry()
				.getPricesModel(currency);
		if (pricesModel.getPriceModelsForGoodTypes().containsKey(currency)) {
			Map<GoodType, PriceModel> priceModelsForGoodType = pricesModel
					.getPriceModelsForGoodTypes().get(currency);
			PriceModel priceModel = priceModelsForGoodType.get(goodType);
			if (priceModel != null)
				return new DefaultHighLowDataset("", priceModel.getDate(),
						priceModel.getHigh(), priceModel.getLow(),
						priceModel.getOpen(), priceModel.getClose(),
						priceModel.getVolume());
		}
		return null;
	}

	@Override
	public void notifyListener() {
		if (this.isShowing()) {
			IndustriesPanelForCurrency industryPanel = (IndustriesPanelForCurrency) jTabbedPaneCurrency
					.getSelectedComponent();
			industryPanel.notifyListener();
		}
	}
}