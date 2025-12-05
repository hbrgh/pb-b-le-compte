package montresor.condeminov.mng;

public class AutresParametresPbB {
	
	private double tauxCotisationSociale; // pourcentage
	private double tauxPfuFiscal; // pourcentage
	private double tauxPfuSocial; // pourcentage

	public AutresParametresPbB() {
		super();
		
	}
	
	
	private void appendParam(StringBuilder sb, String nomParam, double valParam) {
		sb.append(System.lineSeparator());
		sb.append(nomParam);
		sb.append("=");
		sb.append( String.format("%.2f", valParam) );
		sb.append("%");		
		
	}

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();

		appendParam(sb, ParamsInputUtils.NOM_PARAM_TAUX_CS_NON_DEDUCTIBLES_DANS_ONGLET_AUTRES,
				this.getTauxCotisationSociale());
		appendParam(sb, ParamsInputUtils.NOM_PARAM_TAUX_PFU_FISCAL_DANS_ONGLET_AUTRES,
				this.getTauxPfuFiscal());
		appendParam(sb, ParamsInputUtils.NOM_PARAM_TAUX_PFU_SOCIAL_DANS_ONGLET_AUTRES,
				this.getTauxPfuSocial());
		return sb.toString();	
	}



	
	public double getTauxCotisationSociale() {
		return tauxCotisationSociale;
	}

	public void setTauxCotisationSociale(double tauxCotisationSociale) {
		this.tauxCotisationSociale = tauxCotisationSociale;
	}



	public double getTauxPfuFiscal() {
		return tauxPfuFiscal;
	}



	public void setTauxPfuFiscal(double tauxPfuFiscal) {
		this.tauxPfuFiscal = tauxPfuFiscal;
	}



	public double getTauxPfuSocial() {
		return tauxPfuSocial;
	}



	public void setTauxPfuSocial(double tauxPfuSocial) {
		this.tauxPfuSocial = tauxPfuSocial;
	}
	
	
	

}
