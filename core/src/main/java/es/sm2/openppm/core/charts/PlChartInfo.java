/*
 * Copyright (C) 2009-2015 SM2 SOFTWARE & SERVICES MANAGEMENT
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program has been created in the hope that it will be useful.
 * It is distributed WITHOUT ANY WARRANTY of any Kind,
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 *
 * For more information, please contact SM2 Software & Services Management.
 * Mail: info@talaia-openppm.com
 * Web: http://www.talaia-openppm.com
 *
 * Module: core
 * File: PlChartInfo.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:58
 */

package es.sm2.openppm.core.charts;

public class PlChartInfo {
	
	private double sumIwos;
	private double sumExpenses;
	private double sumDirectCost;
	private double tcv;

	public double getSumIwos() {
		return sumIwos;
	}

	public void setSumIwos(double sumIwos) {
		this.sumIwos = sumIwos;
	}

	public double getSumExpenses() {
		return sumExpenses;
	}

	public void setSumExpenses(double sumExpenses) {
		this.sumExpenses = sumExpenses;
	}

	public double getSumDirectCost() {
		return sumDirectCost;
	}

	public void setSumDirectCost(double sumDirectCost) {
		this.sumDirectCost = sumDirectCost;
	}

	public void setTcv(double tcv) {
		this.tcv = tcv;
	}
	
	public double getTcv() {
		return tcv;
	}
	
	public double getCalculatedNetIncome() {
		return this.tcv-this.sumExpenses-this.sumIwos;
	}
	
	public double getDirectMargin() {
		return this.getCalculatedNetIncome()-this.sumDirectCost;
	}
	
	public double getDirectMarginPercent() {
		return this.getDirectMargin()/this.tcv*100;
	}
	
	
}
