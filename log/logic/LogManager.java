package log.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import log.storage.*;

public class LogManager {

	public LogStorage storage;
	private TreeSet<Security> data = new TreeSet<>();

	private Date portfolioChanges;

	public LogManager() throws IOException, ClassNotFoundException {
		storage = new LogStorage();
		loadAll();
		portfolioChanges = new Date();
	}

	public void addSecurityToLog(String stockName, int amount, int price, int orderId, int status, Date date)
			throws IOException {
		Security sc = new Security(stockName, amount, price, orderId, status, date);
		data.add(sc);
	}

	public void loadAll() throws ClassNotFoundException, IOException {
		Set<SecurityInfo> sc = storage.loadAll();
		for (SecurityInfo s : sc) {
			data.add(new Security(s.getStockName(), s.getAmount(), s.getPricePerUnit(), s.getOrderId(),
					s.getStatusOfOrder(), s.getDateOfLastChange()));
		}
	}

	public void saveAll() throws ClassNotFoundException, IOException {
		Set<SecurityInfo> sc = new HashSet<SecurityInfo>();
		for (Security s : data) {
			sc.add(new SecurityInfo(s.getStockName(), s.getAmount(), s.getPricePerUnit(), s.getOrderId(),
					s.getStatusOfOrder(), s.getDateOfLastChange()));
		}
		storage.saveToFile(sc);
	}

	public TreeSet<Security> getData() {
		return data;
	}

	public ArrayList<Object> moveToPortfolio() {
		ArrayList<Object> dataToPortfolio = new ArrayList<>();
		for (Security sc : data) {
			if (sc.getDateOfLastChange().compareTo(portfolioChanges) < 0)
				break;
			else if (sc.getStatusOfOrder() == 0) {
				dataToPortfolio.add(sc.getOrderId());
				dataToPortfolio.add(sc.getStockName());
				dataToPortfolio.add("Buy");
				dataToPortfolio.add(sc.getAmount());
				dataToPortfolio.add(sc.getPricePerUnit());
				dataToPortfolio.add(sc.getPricePerUnit());
			} else if (sc.getStatusOfOrder() == 1) {
				dataToPortfolio.add(sc.getOrderId());
				dataToPortfolio.add(sc.getStockName());
				dataToPortfolio.add("Sell");
				dataToPortfolio.add(sc.getAmount());
				dataToPortfolio.add(sc.getPricePerUnit());
			}

		}
		this.portfolioChanges = new Date();
		return dataToPortfolio;
	}

}
