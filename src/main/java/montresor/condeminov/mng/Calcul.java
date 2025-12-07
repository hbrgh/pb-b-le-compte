package montresor.condeminov.mng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Calcul {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Calcul.class);
	
	private static final double TAUX_10_PCENT = 10.0; // à mettre un jour dans le paramétrage
	
	public Calcul() {
		super();
		
	}

	public OutputDataPbB launch(InputDataPbB pInpuDataPbB) {
		
		OutputDataPbB lOutputDataPbB = new OutputDataPbB();
		
		// calcul du (7)
		double pressionFiscaleEurl = 
				ParamsPbB.getInstance().getBaremeIs().getImpotDu((int)Math.round(pInpuDataPbB.getRevenuFiscalSoumisIs()),1,0);

		
//		// calcul du (3) Résultat Hors Cso
//		// SANS
		double resultatHorsCsoSansPec = pInpuDataPbB.getRevenuFiscalSoumisIs() + pInpuDataPbB.getRti();

		
		// calcul du (8) dans le cas AVEC
		double dividendesAvecPec = pInpuDataPbB.getRevenuFiscalSoumisIs() - pressionFiscaleEurl;
		// calcul du (8) dans le cas SANS
		double dividendesSansPec = resultatHorsCsoSansPec - pInpuDataPbB.getRti() - pressionFiscaleEurl;
		double dividendes = dividendesSansPec;
		if (pInpuDataPbB.isAvecPriseEnCharge() ) {
			dividendes = dividendesAvecPec;
		}

		
		// calcul du (4) la pression sociale
		
		
				
		
		double csoRti = pInpuDataPbB.getRti() * (pInpuDataPbB.getTauxPressionSociale() / 100.0);
		
		double butee = pInpuDataPbB.getResultatAnneePrecedente() * (TAUX_10_PCENT / 100.0) *
				(ParamsPbB.getInstance().getAutresParams().getTauxPfuSocial() / 100.0);
		double csoDiv = dividendes * (ParamsPbB.getInstance().getAutresParams().getTauxPfuSocial() / 100.0);
		if (csoDiv > butee) {
			csoDiv = butee;
		}

		double sousDivDiv2 = (dividendes - ((TAUX_10_PCENT / 100.0) * pInpuDataPbB.getResultatAnneePrecedente()));
		if (sousDivDiv2 < 0) {
			sousDivDiv2 = 0.0;
		}
		double csoDiv2 = sousDivDiv2 * (pInpuDataPbB.getTauxPressionSociale() / 100.0);

		double pressionSociale = csoRti + csoDiv + csoDiv2;
		
		double assiette4 = pInpuDataPbB.getRti() + sousDivDiv2;
		
//		// calcul du (3) Résultat Hors Cso
//		// AVEC
		double resultatHorsCsoAvecPec = resultatHorsCsoSansPec + pressionSociale;

		double resultatHorsCso = resultatHorsCsoSansPec;
		if (pInpuDataPbB.isAvecPriseEnCharge() ) {
			resultatHorsCso = resultatHorsCsoAvecPec;
		}
		
		
		// calcul du (1)
		double recettesEurlExtrapolees = resultatHorsCso + pInpuDataPbB.getDepensesHorsCso();
		
		// calcul du (9)  PFU fiscal sur Dividendes
		double pfuFiscalSurDividendes = dividendes * (ParamsPbB.getInstance().getAutresParams().getTauxPfuFiscal() / 100.0);
		
		
		// calcul du (10) RI catégoriel du dirigeant
		
//		double csoNonDeductible = pInpuDataPbA.getBnc() * (pInpuDataPbA.getRsm() / pInpuDataPbA.getRs()) *
//				(ParamsPbA.getInstance().getAutresParams().getTauxCotisationSociale() / 100.0);
//		LOGGER.info("csNonDeductibles (valeur4b) = {}", csoNonDeductible);
//		lOutputDataPbA.setCsoNonDeductible(csoNonDeductible);
		

		// ANCIEN CODE
//		double valeur4b = assiette4 * (pInpuDataPbB.getRsm() / pInpuDataPbB.getRs()) *
//				(ParamsPbB.getInstance().getAutresParams().getTauxCotisationSociale() / 100.0);
		
		// NOUVEAU 06122025: DEBUT
		double valeur4b = assiette4  *
				(ParamsPbB.getInstance().getAutresParams().getTauxCotisationSociale() / 100.0);
		// NOUVEAU 06122025: FIN 
		
		LOGGER.info("valeur4b = {}", valeur4b);
		double valeur4a = pressionSociale - valeur4b;
		LOGGER.info("valeur4a = {}", valeur4a);
		LOGGER.info("valeur4 (pression sociale) = {}", pressionSociale);
		double riCategorielDirigeant = pInpuDataPbB.getRti();
		
		if (pInpuDataPbB.isAvecPriseEnCharge() ) {
			
		
			riCategorielDirigeant += valeur4b;
			
		} else {
			riCategorielDirigeant -= pressionSociale;
			
		}
		
		//calcul du (11) Pression fiscale du dirigeant
		
		
		
		double riCategorielDirigeantPositif = riCategorielDirigeant;
		if (riCategorielDirigeant < 0.0) {
			riCategorielDirigeantPositif = 0.0; // demande du Compte le 16/02/2023
		}
		double pressionFiscaleRevenuHorsEurl = 
				ParamsPbB.getInstance().getBaremeIrpp().getImpotDu((int)Math.round(pInpuDataPbB.getRevenuFiscalHorsEurl()),pInpuDataPbB.getNbParts(),0);

		double pressionFiscaleTotaleDirigeant = ParamsPbB.getInstance().getBaremeIrpp().getImpotDu((int)Math.round(pInpuDataPbB.getRevenuFiscalHorsEurl()+riCategorielDirigeantPositif),pInpuDataPbB.getNbParts(),0);
		if (pInpuDataPbB.isAvecPriseEnCharge() ) {
			
			double riCategorielAdmisAuBareme = riCategorielDirigeant + valeur4b;
			if (riCategorielAdmisAuBareme > 14455.0) {
				riCategorielAdmisAuBareme = riCategorielAdmisAuBareme - 14455.0;
			} else {
				riCategorielAdmisAuBareme = riCategorielAdmisAuBareme* 0.9;
			}
			pressionFiscaleTotaleDirigeant = ParamsPbB.getInstance().getBaremeIrpp().getImpotDu((int)Math.round(pInpuDataPbB.getRevenuFiscalHorsEurl()+riCategorielAdmisAuBareme),pInpuDataPbB.getNbParts(),0);
			
		}

		double pressionFiscaleDirigeant = pressionFiscaleTotaleDirigeant - pressionFiscaleRevenuHorsEurl;
		
		//calcul du (12) Revenu réel
		double revenuReel = pInpuDataPbB.getRti() + dividendes - pfuFiscalSurDividendes - pressionFiscaleDirigeant;
		// demande du Compte le 16/02/2023
		if (pInpuDataPbB.isAvecPriseEnCharge() == false ) {
			revenuReel -= pressionSociale;
		} 
		
		
		//calcul du (13) Perte de rentabilité liée aux PO
		double perteRentabilitePO = 
				((pressionSociale + pressionFiscaleEurl + pressionFiscaleDirigeant + pfuFiscalSurDividendes) / resultatHorsCso) * 100.0;
		
		//calcul du (14) Rentabilité réelle en div/IS
		double rentabiliteReelleDivIs = (100.0 - perteRentabilitePO);
		

		
		// (1)
		lOutputDataPbB.setRecetteExtrapolees(recettesEurlExtrapolees);
		LOGGER.info("recettesEurlExtrapolees = {}", recettesEurlExtrapolees);
		
		// c'est le (3)
		lOutputDataPbB.setResultatHorsCso(resultatHorsCso);
		LOGGER.info("resultatHorsCso = {}", resultatHorsCso);
		
		// c'est le (4)
		lOutputDataPbB.setPressionSociale(pressionSociale);
		LOGGER.info("pressionSociale = {}", pressionSociale);
	
		LOGGER.info("csNonDeductibles (valeur4b) = {}", valeur4b);
		lOutputDataPbB.setCsoNonDeductible(valeur4b);
		
		
		// c'est le (7)
		lOutputDataPbB.setPressionFiscaleEurl(pressionFiscaleEurl);
		LOGGER.info("pressionFiscaleEurl = {}", pressionFiscaleEurl);
		
		// c'est le (8)
		lOutputDataPbB.setDividendes(dividendes);
		LOGGER.info("dividendes = {}", dividendes);
		
		// (9)
		lOutputDataPbB.setPfuFiscalSurDividendes(pfuFiscalSurDividendes);
		LOGGER.info("pfuFiscalSurDividendes = {}", pfuFiscalSurDividendes);
		
		// (10)
		lOutputDataPbB.setRiCategorielDirigeant(riCategorielDirigeant);
		LOGGER.info("riCategorielDirigeant = {}", riCategorielDirigeant);
		
		// (11) 
		lOutputDataPbB.setPressionFiscaleDirigeant(pressionFiscaleDirigeant);
		LOGGER.info("pressionFiscaleDirigeant = {}", pressionFiscaleDirigeant);
		
		// (12) 
		lOutputDataPbB.setRevenuReel(revenuReel);
		LOGGER.info("revenuReel = {}", revenuReel);
		
		// (13) 
		lOutputDataPbB.setPerteRentabilitePO(perteRentabilitePO);
		LOGGER.info("perteRentabilitePO = {}", perteRentabilitePO);
		
		// (14)
		lOutputDataPbB.setRentabiliteRelle(rentabiliteReelleDivIs);
		LOGGER.info("rentabiliteReelleDivIs = {}", rentabiliteReelleDivIs);

		return(lOutputDataPbB);
	}

}
