package montresor.condeminov.mng;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import montresor.BaremeFiscal;



public class ParamsPbB {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ParamsPbB.class);
	
	private static ParamsPbB uniqueInstance = null;
	
	private BaremeFiscal baremeIrpp;
	private BaremeFiscal baremeIs;
	private AutresParametresPbB autresParams;
	
	
	private ParamsPbB() {
		
	}

	public static synchronized ParamsPbB getInstance()
	{
          if(uniqueInstance==null)
          {
                  uniqueInstance = new ParamsPbB();
          }
          return uniqueInstance;
	}
	
	public static synchronized void newInstance()
	{
     
          uniqueInstance = new ParamsPbB();
        
         
	}
	
	
	
	public boolean lireFichierParametrage(String fullPath) {
		
		BaremeFiscal lBaremeFiscalIrpp;
		BaremeFiscal lBaremeFiscalIs;
		
		LOGGER.info("Début lecture du fichier : {}", fullPath);
		
		
		
		try (FileInputStream stream = new FileInputStream(new File(fullPath))){
			
			XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(stream);
			
			lBaremeFiscalIrpp = ParamsInputUtils.readBaremeIrppFromFileParam(workbook);
			if (lBaremeFiscalIrpp == null) {
				return(false);
			}
			
			lBaremeFiscalIs =  ParamsInputUtils.readBaremeIsFromFileParam(workbook);
			if (lBaremeFiscalIs == null) {
				return(false);
			}
			workbook.close();
		} catch (IOException e) {
			LOGGER.error(e.toString());
			return(false);
		} catch (InvalidFormatException e1) {
			LOGGER.error(e1.toString());
			return(false);
		}
		LOGGER.info("Fin lecture du fichier : {}", fullPath);
		
		
		
		LOGGER.info(lBaremeFiscalIrpp.toString());
		this.setBaremeIrpp(lBaremeFiscalIrpp);
		
		LOGGER.info(lBaremeFiscalIs.toString());
		this.setBaremeIs(lBaremeFiscalIs);
		
		AutresParametresPbB lAutresParametresPbB = null;
		try {
			lAutresParametresPbB = ParamsInputUtils.readAutresParamsPbBFromFileParam(fullPath);
			if (lAutresParametresPbB == null) {
				return(false);
			}
		} catch (InvalidFormatException e) {
			LOGGER.error(e.toString());
			return(false);
		}
		LOGGER.info(lAutresParametresPbB.toString());
		this.setAutresParams(lAutresParametresPbB);
	
		
		return(true);
	}

	public BaremeFiscal getBaremeIrpp() {
		return baremeIrpp;
	}

	public void setBaremeIrpp(BaremeFiscal baremeIrpp) {
		this.baremeIrpp = baremeIrpp;
	}
	
	
	

	public BaremeFiscal getBaremeIs() {
		return baremeIs;
	}

	public void setBaremeIs(BaremeFiscal baremeIs) {
		this.baremeIs = baremeIs;
	}

	public AutresParametresPbB getAutresParams() {
		return autresParams;
	}

	public void setAutresParams(AutresParametresPbB autresParams) {
		this.autresParams = autresParams;
	}

	


	
}
