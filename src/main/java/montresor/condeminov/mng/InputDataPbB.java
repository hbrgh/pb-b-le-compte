package montresor.condeminov.mng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputDataPbB extends DataPbB {
	
	private static Logger LOGGER = LoggerFactory.getLogger(InputDataPbB.class);
	
	private int nbParts;
	private double revenuFiscalHorsEurl;
	private double revenuFiscalSoumisIs;
	private double depensesHorsCso;
	private double tauxPressionSociale;
	private double rti;
	private double rsm;
	private double rs;
	private double resultatAnneePrecedente;
	private boolean avecPriseEnCharge = true;

	

	
	
	public InputDataPbB(int nbParts, double revenuFiscalHorsEurl, double revenuFiscalSoumisIs, double depensesHorsCso,
			double tauxPressionSociale, double rti, double rsm, double rs, double resultatAnneePrecedente) {
		super();
		this.nbParts = nbParts;
		this.revenuFiscalHorsEurl = revenuFiscalHorsEurl;
		this.revenuFiscalSoumisIs = revenuFiscalSoumisIs;
		this.depensesHorsCso = depensesHorsCso;
		this.tauxPressionSociale = tauxPressionSociale;
		this.rti = rti;
		this.rsm = rsm;
		this.rs = rs;
		this.resultatAnneePrecedente = resultatAnneePrecedente;
	}
	
	public void verifDouble(String name, double val, double binf, double bsup) throws InputDataPbBException {
		if ((val < binf) || (val > bsup)) {
			StringBuilder sbZeError = new StringBuilder("Erreur sur ");
			sbZeError.append(name);
			sbZeError.append(": ");
			sbZeError.append(String.format("%.2f", val));
			sbZeError.append(". Valeur attendue: entre ");
			sbZeError.append(String.format("%.2f", binf));
			sbZeError.append(" et ");
			sbZeError.append(String.format("%.2f", bsup));
			sbZeError.append(".");
			String zeError = sbZeError.toString();
			LOGGER.error(zeError);
			throw new InputDataPbBException(zeError);

		}		
	}
	public void verification() throws InputDataPbBException {
		if ((this.nbParts < 1) || (this.nbParts > 10)) {
			String zeErrorNbParts = "Erreur sur nbParts: "+ this.nbParts+". Valeur attendue: entre 1 et 9";
			LOGGER.error(zeErrorNbParts);
			throw new InputDataPbBException(zeErrorNbParts);
		}
		verifDouble("Revenus imposables hors EURL", this.revenuFiscalHorsEurl, 0.0, 1000000000.0);
		verifDouble("Revenu fiscal soumis à l'IS", this.revenuFiscalSoumisIs, 0.0, 1000000000.0);
		verifDouble("Dépenses hors CSO", this.depensesHorsCso, 0.0, 1000000000.0);
		verifDouble("Taux Pression Sociale", this.tauxPressionSociale, 0.0, 100.0);
		verifDouble("RTI", this.rti, 0.0, 1000000000.0);
		verifDouble("RSM", this.rsm, 0.0, 1000000000.0);
		verifDouble("RS", this.rs, 0.0, 1000000000.0);
	}



	public String toStringSpecial() {
		StringBuilder sb = new StringBuilder("DONNEES D'ENTREE:");
		sb.append(System.lineSeparator());
		appendResultIntegerValueSansFinDeLigne(sb, "Nombre de parts", this.nbParts, "");
		sb.append(ESPACE_ENTRE_DATA);
		appendResultDoubleValueSansFinDeLigne(sb, "Revenus imposables hors EURL", this.revenuFiscalHorsEurl, " €");
		sb.append(ESPACE_ENTRE_DATA);
		appendResultDoubleValue(sb, "Résultat fiscal soumis à l'IS", this.revenuFiscalSoumisIs, " €");
		
		appendResultDoubleValueSansFinDeLigne(sb, "Dépenses hors CSO", this.depensesHorsCso, " €");
		sb.append(ESPACE_ENTRE_DATA);
		appendResultDoubleValue(sb, "RTI", this.rti, " €");
		
	
		
		appendResultDoubleValueSansFinDeLigne(sb, "RSM", this.rsm, " €");
		sb.append(ESPACE_ENTRE_DATA);
		appendResultDoubleValueSansFinDeLigne(sb, "RS", this.rs, " €");
		sb.append(ESPACE_ENTRE_DATA);
		appendResultDoubleValue(sb, "R N-1", this.resultatAnneePrecedente, " €");
		
		if (avecPriseEnCharge) {
			sb.append("Prise en charge CSO par EURL: AVEC");
		} else {
			sb.append("Prise en charge CSO par EURL: SANS");
		}
			
		
		
		return (sb.toString());
	}





	@Override
	public String toString() {
		return "InputDataPbB [nbParts=" + nbParts + ", revenuFiscalHorsEurl=" + revenuFiscalHorsEurl
				+ ", revenuFiscalSoumisIs=" + revenuFiscalSoumisIs + ", depensesHorsCso=" + depensesHorsCso
				+ ", tauxPressionSociale=" + tauxPressionSociale + ", rti=" + rti + ", rsm=" + rsm + ", rs=" + rs
				+ ", avecPriseEnCharge=" + avecPriseEnCharge + "]";
	}

	public int getNbParts() {
		return nbParts;
	}


	public void setNbParts(int nbParts) {
		this.nbParts = nbParts;
	}


	public double getDepensesHorsCso() {
		return depensesHorsCso;
	}


	public void setDepensesHorsCso(double depensesHorsCso) {
		this.depensesHorsCso = depensesHorsCso;
	}




	public double getTauxPressionSociale() {
		return tauxPressionSociale;
	}


	public void setTauxPressionSociale(double tauxPressionSociale) {
		this.tauxPressionSociale = tauxPressionSociale;
	}

	public double getRevenuFiscalHorsEurl() {
		return revenuFiscalHorsEurl;
	}

	public void setRevenuFiscalHorsEurl(double revenuFiscalHorsEurl) {
		this.revenuFiscalHorsEurl = revenuFiscalHorsEurl;
	}

	public double getRevenuFiscalSoumisIs() {
		return revenuFiscalSoumisIs;
	}

	public void setRevenuFiscalSoumisIs(double revenuFiscalSoumisIs) {
		this.revenuFiscalSoumisIs = revenuFiscalSoumisIs;
	}

	public double getRti() {
		return rti;
	}

	public void setRti(double rti) {
		this.rti = rti;
	}

	public double getRsm() {
		return rsm;
	}

	public void setRsm(double rsm) {
		this.rsm = rsm;
	}

	public double getRs() {
		return rs;
	}

	public void setRs(double rs) {
		this.rs = rs;
	}

	
	
	public double getResultatAnneePrecedente() {
		return resultatAnneePrecedente;
	}

	public void setResultatAnneePrecedente(double resultatAnneePrecedente) {
		this.resultatAnneePrecedente = resultatAnneePrecedente;
	}

	public boolean isAvecPriseEnCharge() {
		return avecPriseEnCharge;
	}

	public void setAvecPriseEnCharge(boolean avecPriseEnCharge) {
		this.avecPriseEnCharge = avecPriseEnCharge;
	}


	

	
	
	

}
