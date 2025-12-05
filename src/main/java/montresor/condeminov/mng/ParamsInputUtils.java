package montresor.condeminov.mng;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import montresor.BaremeFiscal;
import montresor.TrancheFiscale;

public class ParamsInputUtils {
	private static Logger LOGGER = LoggerFactory.getLogger(ParamsInputUtils.class);

	private static final String NOM_ONGLET_BAREME_IRPP = "bareme_irpp";
	private static final String NOM_ONGLET_BAREME_IS = "bareme_is";
	private static final String NOM_ONGLET_AUTRES_PARAMS = "autres";

	private static final int IND_COL_NOM_TRANCHE_DANS_ONGLET_BAREME = 0;
	private static final int IND_COL_TAUX_DANS_ONGLET_BAREME = 1;
	private static final int IND_COL_BINF_DANS_ONGLET_BAREME = 2;
	private static final int IND_COL_BSUP_DANS_ONGLET_BAREME = 3;

	private static final int IND_COL_NOM_PARAM_DANS_ONGLET_AUTRES = 0;
	public static final String NOM_PARAM_TAUX_CS_NON_DEDUCTIBLES_DANS_ONGLET_AUTRES = "TauxDesCSnonDeductibles";
	private static final int IND_LIG_TAUX_COTISATION_SOCIALE_DANS_ONGLET_AUTRES = 0;
	public static final String NOM_PARAM_TAUX_PFU_FISCAL_DANS_ONGLET_AUTRES = "TauxPfuFiscal";
	private static final int IND_LIG_TAUX_TAUX_PFU_FISCAL_DANS_ONGLET_AUTRES = 1;
	public static final String NOM_PARAM_TAUX_PFU_SOCIAL_DANS_ONGLET_AUTRES = "TauxPfuSocial";
	private static final int IND_LIG_TAUX_TAUX_PFU_SOCIAL_DANS_ONGLET_AUTRES = 2;
	private static final int IND_COL_VALEUR_PARAM_DANS_ONGLET_AUTRES = 1;

	public static void lireOngletBaremeIrpp(XSSFSheet sheet, BaremeFiscal bareme) {
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();

		// on saute la ligne de headers
		for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {

			XSSFRow row = sheet.getRow(rowNum);
			XSSFCell cellCode = row.getCell(IND_COL_NOM_TRANCHE_DANS_ONGLET_BAREME);
			String nomTranche = cellCode.getStringCellValue();

			cellCode = row.getCell(IND_COL_TAUX_DANS_ONGLET_BAREME);
			double taux = cellCode.getNumericCellValue();

			cellCode = row.getCell(IND_COL_BINF_DANS_ONGLET_BAREME);
			int binf = (int) cellCode.getNumericCellValue();

			cellCode = row.getCell(IND_COL_BSUP_DANS_ONGLET_BAREME);
			int bsup = (int) cellCode.getNumericCellValue();
			TrancheFiscale lTrancheFiscale = new TrancheFiscale(nomTranche, taux, binf, bsup);
			bareme.addTranche(lTrancheFiscale);

		}
	}

	public static void lireOngletBaremeIs(XSSFSheet sheet, BaremeFiscal bareme) {
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();

		// on saute la ligne de headers
		for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {

			XSSFRow row = sheet.getRow(rowNum);
			XSSFCell cellCode = row.getCell(IND_COL_NOM_TRANCHE_DANS_ONGLET_BAREME);
			String nomTranche = cellCode.getStringCellValue();

			cellCode = row.getCell(IND_COL_TAUX_DANS_ONGLET_BAREME);
			double taux = cellCode.getNumericCellValue();

			cellCode = row.getCell(IND_COL_BINF_DANS_ONGLET_BAREME);
			int binf = (int) cellCode.getNumericCellValue();

			cellCode = row.getCell(IND_COL_BSUP_DANS_ONGLET_BAREME);
			int bsup = (int) cellCode.getNumericCellValue();
			TrancheFiscale lTrancheFiscale = new TrancheFiscale(nomTranche, taux, binf, bsup);
			bareme.addTranche(lTrancheFiscale);

		}
	}

	private static XSSFRow verifPresenceParamDansOngletAutres(XSSFSheet sheet, String nomParam, int ligParam) throws MissingOrIncorrectParamPbBException {
		XSSFRow row = sheet.getRow(ligParam);
		if (row == null) {
			String fatalError = "Paramètre absent: " + nomParam;
			LOGGER.error(fatalError);
			throw new MissingOrIncorrectParamPbBException(fatalError);
		}
		XSSFCell cellCode = row.getCell(IND_COL_NOM_PARAM_DANS_ONGLET_AUTRES);
		String nomParamCell = cellCode.getStringCellValue();
		if (nomParamCell.equalsIgnoreCase(nomParam) == false) {

			String fatalError = "Paramètre absent: " + nomParam;
			LOGGER.error(fatalError);
			throw new MissingOrIncorrectParamPbBException(fatalError);
		}

		return(row);
	}
	
	private static boolean verifValParamTaux(String nomParam, double taux) {
		if ((taux < 0.0) || (taux > 100.0)) {
			LOGGER.error("Paramètre dont la valeur est inacceptable: {}. Valeur=", nomParam,
					taux);
			return false;
		}
		return(true);
	}
	public static boolean lireOngletAutres(XSSFSheet sheet, AutresParametresPbB otrParams) {


		XSSFRow row;
		// Taux cotisation sociale
		try {
			row = verifPresenceParamDansOngletAutres
							(
								sheet,
								NOM_PARAM_TAUX_CS_NON_DEDUCTIBLES_DANS_ONGLET_AUTRES,
								IND_LIG_TAUX_COTISATION_SOCIALE_DANS_ONGLET_AUTRES
							);
		} catch (MissingOrIncorrectParamPbBException e) {
			return(false);
		}
		XSSFCell cellCode = row.getCell(IND_COL_VALEUR_PARAM_DANS_ONGLET_AUTRES);
		double tauxCs = cellCode.getNumericCellValue();
		if (verifValParamTaux(NOM_PARAM_TAUX_CS_NON_DEDUCTIBLES_DANS_ONGLET_AUTRES, tauxCs) == false) {
			return(false);
		}		
		otrParams.setTauxCotisationSociale(tauxCs);
		
		
		// Taux PFU fiscal
		try {
			row = verifPresenceParamDansOngletAutres
							(
								sheet,
								NOM_PARAM_TAUX_PFU_FISCAL_DANS_ONGLET_AUTRES,
								IND_LIG_TAUX_TAUX_PFU_FISCAL_DANS_ONGLET_AUTRES
							);
		} catch (MissingOrIncorrectParamPbBException e) {
			return(false);
		}
		cellCode = row.getCell(IND_COL_VALEUR_PARAM_DANS_ONGLET_AUTRES);
		double tauxPfuFiscal = cellCode.getNumericCellValue();
		if (verifValParamTaux(NOM_PARAM_TAUX_PFU_FISCAL_DANS_ONGLET_AUTRES, tauxPfuFiscal) == false) {
			return(false);
		}
		otrParams.setTauxPfuFiscal(tauxPfuFiscal);
		
		// Taux PFU social
		try {
			row = verifPresenceParamDansOngletAutres
							(
								sheet,
								NOM_PARAM_TAUX_PFU_SOCIAL_DANS_ONGLET_AUTRES,
								IND_LIG_TAUX_TAUX_PFU_SOCIAL_DANS_ONGLET_AUTRES
							);
		} catch (MissingOrIncorrectParamPbBException e) {
			return(false);
		}
		cellCode = row.getCell(IND_COL_VALEUR_PARAM_DANS_ONGLET_AUTRES);
		double tauxPfuSocial = cellCode.getNumericCellValue();
		if (verifValParamTaux(NOM_PARAM_TAUX_PFU_SOCIAL_DANS_ONGLET_AUTRES, tauxPfuSocial) == false) {
			return(false);
		}
		otrParams.setTauxPfuSocial(tauxPfuSocial);
		


		return (true);
	}

	public static BaremeFiscal readBaremeIrppFromFileParam(XSSFWorkbook workbook) throws InvalidFormatException {

		BaremeFiscal lBaremeFiscal = new BaremeFiscal(NOM_ONGLET_BAREME_IRPP);

		XSSFSheet sheet = workbook.getSheet(NOM_ONGLET_BAREME_IRPP);
		lireOngletBaremeIrpp(sheet, lBaremeFiscal);

		if (lBaremeFiscal.verifCompletEtCoherent()) {
			return (lBaremeFiscal);
		}
		return (null);

	}

	public static BaremeFiscal readBaremeIsFromFileParam(XSSFWorkbook workbook) throws InvalidFormatException {

		BaremeFiscal lBaremeFiscal = new BaremeFiscal(NOM_ONGLET_BAREME_IS);

		XSSFSheet sheet = workbook.getSheet(NOM_ONGLET_BAREME_IS);
		lireOngletBaremeIs(sheet, lBaremeFiscal);

		if (lBaremeFiscal.verifCompletEtCoherent()) {
			return (lBaremeFiscal);
		}
		return (null);

	}

	public static AutresParametresPbB readAutresParamsPbBFromFileParam(String filePathName)
			throws InvalidFormatException {

		AutresParametresPbB lAutresParametresPbB = new AutresParametresPbB();
		boolean autresParamsOK = false;
		LOGGER.info("Début lecture du fichier : {}", filePathName);

		try (FileInputStream stream = new FileInputStream(new File(filePathName))) {

			XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(stream);
			XSSFSheet sheet = workbook.getSheet(NOM_ONGLET_AUTRES_PARAMS);
			autresParamsOK = lireOngletAutres(sheet, lAutresParametresPbB);

			workbook.close();
		} catch (IOException e) {
			LOGGER.error(e.toString());

		}
		LOGGER.info("Fin lecture du fichier : {}", filePathName);
		if (autresParamsOK == true) {
			return (lAutresParametresPbB);
		}
		return (null);

	}

}
