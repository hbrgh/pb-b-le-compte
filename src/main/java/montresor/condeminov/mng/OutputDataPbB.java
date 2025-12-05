package montresor.condeminov.mng;

public class OutputDataPbB extends DataPbB {
	
	private double recetteExtrapolees; // (1)
	private double resultatHorsCso; // (3)
	private double pressionSociale; // (4)
	private double pressionFiscaleEurl; // (7)
	private double dividendes; // (8)
	private double pfuFiscalSurDividendes; // (9)	
	private double riCategorielDirigeant; // (10)
	private double pressionFiscaleDirigeant; // (11)
	private double revenuReel; // (12)
	private double perteRentabilitePO; // (13)	
	private double rentabiliteRelle; // (14)
	private double csoNonDeductible;
	
	



	public OutputDataPbB() {
		super();
	}

	
	public String toStringSpecial() {
		StringBuilder sb = new StringBuilder("RESULTATS DU CALCUL:");
		sb.append(System.lineSeparator());
		appendResultDoubleValueSansFinDeLigne(sb, "Recettes extrapolées", this.recetteExtrapolees, " €");
		sb.append(ESPACE_ENTRE_DATA);
		appendResultDoubleValue(sb, "Résultat hors CSO", this.resultatHorsCso, " €");
		appendResultDoubleValueSansFinDeLigne(sb, "CSO sur RTI+dividendes", this.pressionSociale , " €");
		appendResultDoubleValue(sb, "    dont CS non déductibles", this.csoNonDeductible , " €");
		sb.append(ESPACE_ENTRE_DATA);
		
		
		
		
		
		
		
		appendResultDoubleValue(sb, "IS dû", this.pressionFiscaleEurl, " €");
		appendResultDoubleValueSansFinDeLigne(sb, "Dividendes", this.dividendes, " €");
		sb.append(ESPACE_ENTRE_DATA);
		appendResultDoubleValue(sb, "PFU fiscal sur dividendes", this.pfuFiscalSurDividendes, " €");
		
		appendResultDoubleValueSansFinDeLigne(sb, "RI catégoriel admis à l'IRPP", this.riCategorielDirigeant, " €");
		sb.append(ESPACE_ENTRE_DATA);
		appendResultDoubleValue(sb, "Surcroît d'impôt lié aux seuls revenus EURL imposables du dirigeant", this.pressionFiscaleDirigeant, " €");
		
		appendResultDoubleValue(sb, "Revenu réel du dirigeant issu de ses seuls revenus EURL", this.revenuReel, " €");
		appendResultDoubleValue(sb, "Perte de rentabilité liée aux PO", this.perteRentabilitePO, " %");
		appendResultDoubleValue(sb, "Rentabilité réelle en EURL/IS", this.rentabiliteRelle, " %");
		return (sb.toString());
	}

	public double getRecetteExtrapolees() {
		return recetteExtrapolees;
	}

	public void setRecetteExtrapolees(double recetteExtrapolees) {
		this.recetteExtrapolees = recetteExtrapolees;
	}

	
	public double getPressionFiscaleEurl() {
		return pressionFiscaleEurl;
	}


	public void setPressionFiscaleEurl(double pressionFiscaleEurl) {
		this.pressionFiscaleEurl = pressionFiscaleEurl;
	}


	public double getRevenuReel() {
		return revenuReel;
	}

	public void setRevenuReel(double revenuReel) {
		this.revenuReel = revenuReel;
	}

	public double getPerteRentabilitePO() {
		return perteRentabilitePO;
	}

	public void setPerteRentabilitePO(double perteRentabilitePO) {
		this.perteRentabilitePO = perteRentabilitePO;
	}

	public double getRentabiliteRelle() {
		return rentabiliteRelle;
	}

	public void setRentabiliteRelle(double rentabiliteRelle) {
		this.rentabiliteRelle = rentabiliteRelle;
	}

	public double getPressionSociale() {
		return pressionSociale;
	}

	public void setPressionSociale(double pressionSociale) {
		this.pressionSociale = pressionSociale;
	}

	public double getResultatHorsCso() {
		return resultatHorsCso;
	}

	public void setResultatHorsCso(double resultatHorsCso) {
		this.resultatHorsCso = resultatHorsCso;
	}


	public double getDividendes() {
		return dividendes;
	}


	public void setDividendes(double dividendes) {
		this.dividendes = dividendes;
	}


	public double getPfuFiscalSurDividendes() {
		return pfuFiscalSurDividendes;
	}


	public void setPfuFiscalSurDividendes(double pfuFiscalSurDividendes) {
		this.pfuFiscalSurDividendes = pfuFiscalSurDividendes;
	}


	public double getRiCategorielDirigeant() {
		return riCategorielDirigeant;
	}


	public void setRiCategorielDirigeant(double riCategorielDirigeant) {
		this.riCategorielDirigeant = riCategorielDirigeant;
	}


	public double getPressionFiscaleDirigeant() {
		return pressionFiscaleDirigeant;
	}


	public void setPressionFiscaleDirigeant(double pressionFiscaleDirigeant) {
		this.pressionFiscaleDirigeant = pressionFiscaleDirigeant;
	}


	public double getCsoNonDeductible() {
		return csoNonDeductible;
	}


	public void setCsoNonDeductible(double csoNonDeductible) {
		this.csoNonDeductible = csoNonDeductible;
	}
	
	

}
