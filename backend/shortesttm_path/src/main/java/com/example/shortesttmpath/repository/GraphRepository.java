package com.example.shortesttmpath.repository;

import com.example.shortesttmpath.data.Edge;
import com.example.shortesttmpath.enums.Station;
import java.util.List;
import lombok.Getter;
import org.springframework.stereotype.Repository;

/**
 * Graph related repository.
 */
@Repository
@Getter
public class GraphRepository {
  private final List<List<Edge>> edges = List.<List<Edge>>of(
      List.of(new Edge(Station.OUTREMONT.ordinal(), 729), new Edge(Station.PARC.ordinal(), 728)),
      List.of(new Edge(Station.MONK.ordinal(), 844)),
      List.of(new Edge(Station.CADILLAC.ordinal(), 782), new Edge(Station.VIAU.ordinal(), 896)),
      List.of(new Edge(Station.GUY_CONCORDIA.ordinal(), 682), new Edge(Station.LIONEL_GROULX.ordinal(), 1388)),
      List.of(new Edge(Station.JEAN_TALON.ordinal(), 712), new Edge(Station.ROSEMONT.ordinal(), 541)),
      List.of(new Edge(Station.BERRI_UQAM.ordinal(), 379), new Edge(Station.PAPINEAU.ordinal(), 495)),
      List.of(new Edge(Station.BEAUDRY.ordinal(), 379), new Edge(Station.CHAMP_DE_MARS.ordinal(), 721), new Edge(Station.JEAN_DRAPEAU.ordinal(), 2362), new Edge(Station.SAINT_LAURENT.ordinal(), 337), new Edge(Station.SHERBROOKE.ordinal(), 579)),
      List.of(new Edge(Station.LUCIEN_L_ALLIER.ordinal(), 382), new Edge(Station.SQUARE_VICTORIA_OACI.ordinal(), 393)),
      List.of(new Edge(Station.ASSOMPTION.ordinal(), 782), new Edge(Station.LANGELIER.ordinal(), 519)),
      List.of(new Edge(Station.DE_LA_CONCORDE.ordinal(), 2074), new Edge(Station.HENRI_BOURASSA.ordinal(), 1102)),
      List.of(new Edge(Station.BERRI_UQAM.ordinal(), 721), new Edge(Station.PLACE_D_ARMES.ordinal(), 371)),
      List.of(new Edge(Station.LASALLE.ordinal(), 707), new Edge(Station.LIONEL_GROULX.ordinal(), 1077)),
      List.of(new Edge(Station.SNOWDON.ordinal(), 960), new Edge(Station.UNIVERSITE_DE_MONTREAL.ordinal(), 765)),
      List.of(new Edge(Station.PLAMONDON.ordinal(), 451), new Edge(Station.SNOWDON.ordinal(), 693)),
      List.of(new Edge(Station.DU_COLLEGE.ordinal(), 777)),
      List.of(new Edge(Station.JARRY.ordinal(), 826), new Edge(Station.SAUVE.ordinal(), 128)),
      List.of(new Edge(Station.JEAN_TALON.ordinal(), 472), new Edge(Station.PARC.ordinal(), 491)),
      List.of(new Edge(Station.CARTIER.ordinal(), 2074), new Edge(Station.MONTMORENCY.ordinal(), 848)),
      List.of(new Edge(Station.DU_COLLEGE.ordinal(), 1282), new Edge(Station.NAMUR.ordinal(), 787)),
      List.of(new Edge(Station.LASALLE.ordinal(), 812), new Edge(Station.VERDUN.ordinal(), 564)),
      List.of(new Edge(Station.FABRE.ordinal(), 645), new Edge(Station.SAINT_MICHEL.ordinal(), 608)),
      List.of(new Edge(Station.COTE_VERTU.ordinal(), 777), new Edge(Station.DE_LA_SAVANE.ordinal(), 1282)),
      List.of(new Edge(Station.OUTREMONT.ordinal(), 1091), new Edge(Station.UNIVERSITE_DE_MONTREAL.ordinal(), 668)),
      List.of(new Edge(Station.D_IBERVILLE.ordinal(), 645), new Edge(Station.JEAN_TALON.ordinal(), 840)),
      List.of(new Edge(Station.PAPINEAU.ordinal(), 1158), new Edge(Station.PREFONTAINE.ordinal(), 1004)),
      List.of(new Edge(Station.LIONEL_GROULX.ordinal(), 759), new Edge(Station.LUCIEN_L_ALLIER.ordinal(), 531)),
      List.of(new Edge(Station.ATWATER.ordinal(), 682), new Edge(Station.PEEL.ordinal(), 593)),
      List.of(new Edge(Station.CARTIER.ordinal(), 1102), new Edge(Station.SAUVE.ordinal(), 772)),
      List.of(new Edge(Station.RADISSON.ordinal(), 717)),
      List.of(new Edge(Station.CREMAZIE.ordinal(), 826), new Edge(Station.JEAN_TALON.ordinal(), 977)),
      List.of(new Edge(Station.BERRI_UQAM.ordinal(), 2362), new Edge(Station.LONGUEUIL_UNIVERSITE_DE_SHERBROOKE.ordinal(), 1572)),
      List.of(new Edge(Station.BEAUBIEN.ordinal(), 712), new Edge(Station.DE_CASTELNAU.ordinal(), 472), new Edge(Station.FABRE.ordinal(), 840), new Edge(Station.JARRY.ordinal(), 977)),
      List.of(new Edge(Station.MONK.ordinal(), 1063), new Edge(Station.VERDUN.ordinal(), 761)),
      List.of(new Edge(Station.PIE_IX.ordinal(), 767), new Edge(Station.PREFONTAINE.ordinal(), 383)),
      List.of(new Edge(Station.CADILLAC.ordinal(), 519), new Edge(Station.RADISSON.ordinal(), 622)),
      List.of(new Edge(Station.CHARLEVOIX.ordinal(), 707), new Edge(Station.DE_L_EGLISE.ordinal(), 812)),
      List.of(new Edge(Station.MONT_ROYAL.ordinal(), 500), new Edge(Station.ROSEMONT.ordinal(), 746)),
      List.of(new Edge(Station.ATWATER.ordinal(), 1388), new Edge(Station.CHARLEVOIX.ordinal(), 1077), new Edge(Station.GEORGES_VANIER.ordinal(), 759), new Edge(Station.PLACE_SAINT_HENRI.ordinal(), 580)),
      List.of(new Edge(Station.JEAN_DRAPEAU.ordinal(), 1572)),
      List.of(new Edge(Station.BONAVENTURE.ordinal(), 382), new Edge(Station.GEORGES_VANIER.ordinal(), 531)),
      List.of(new Edge(Station.PEEL.ordinal(), 297), new Edge(Station.PLACE_DES_ARTS.ordinal(), 346)),
      List.of(new Edge(Station.ANGRIGNON.ordinal(), 844), new Edge(Station.JOLICOEUR.ordinal(), 1063)),
      List.of(new Edge(Station.DE_LA_CONCORDE.ordinal(), 848)),
      List.of(new Edge(Station.LAURIER.ordinal(), 500), new Edge(Station.SHERBROOKE.ordinal(), 932)),
      List.of(new Edge(Station.DE_LA_SAVANE.ordinal(), 787), new Edge(Station.PLAMONDON.ordinal(), 988)),
      List.of(new Edge(Station.ACADIE.ordinal(), 729), new Edge(Station.EDOUARD_MONTPETIT.ordinal(), 1091)),
      List.of(new Edge(Station.BEAUDRY.ordinal(), 495), new Edge(Station.FRONTENAC.ordinal(), 1158)),
      List.of(new Edge(Station.ACADIE.ordinal(), 728), new Edge(Station.DE_CASTELNAU.ordinal(), 491)),
      List.of(new Edge(Station.GUY_CONCORDIA.ordinal(), 593), new Edge(Station.MCGILL.ordinal(), 297)),
      List.of(new Edge(Station.JOLIETTE.ordinal(), 767), new Edge(Station.VIAU.ordinal(), 622)),
      List.of(new Edge(Station.CHAMP_DE_MARS.ordinal(), 371), new Edge(Station.SQUARE_VICTORIA_OACI.ordinal(), 357)),
      List.of(new Edge(Station.MCGILL.ordinal(), 346), new Edge(Station.SAINT_LAURENT.ordinal(), 354)),
      List.of(new Edge(Station.LIONEL_GROULX.ordinal(), 580), new Edge(Station.VENDOME.ordinal(), 1451)),
      List.of(new Edge(Station.COTE_SAINTE_CATHERINE.ordinal(), 451), new Edge(Station.NAMUR.ordinal(), 988)),
      List.of(new Edge(Station.FRONTENAC.ordinal(), 1004), new Edge(Station.JOLIETTE.ordinal(), 383)),
      List.of(new Edge(Station.HONORE_BEAUGRAND.ordinal(), 717), new Edge(Station.LANGELIER.ordinal(), 622)),
      List.of(new Edge(Station.BEAUBIEN.ordinal(), 541), new Edge(Station.LAURIER.ordinal(), 746)),
      List.of(new Edge(Station.BERRI_UQAM.ordinal(), 337), new Edge(Station.PLACE_DES_ARTS.ordinal(), 354)),
      List.of(new Edge(Station.D_IBERVILLE.ordinal(), 608)),
      List.of(new Edge(Station.CREMAZIE.ordinal(), 128), new Edge(Station.HENRI_BOURASSA.ordinal(), 772)),
      List.of(new Edge(Station.BERRI_UQAM.ordinal(), 579), new Edge(Station.MONT_ROYAL.ordinal(), 932)),
      List.of(new Edge(Station.COTE_DES_NEIGES.ordinal(), 960), new Edge(Station.COTE_SAINTE_CATHERINE.ordinal(), 693), new Edge(Station.VILLA_MARIA.ordinal(), 884)),
      List.of(new Edge(Station.BONAVENTURE.ordinal(), 393), new Edge(Station.PLACE_D_ARMES.ordinal(), 357)),
      List.of(new Edge(Station.COTE_DES_NEIGES.ordinal(), 765), new Edge(Station.EDOUARD_MONTPETIT.ordinal(), 668)),
      List.of(new Edge(Station.PLACE_SAINT_HENRI.ordinal(), 1451), new Edge(Station.VILLA_MARIA.ordinal(), 1407)),
      List.of(new Edge(Station.DE_L_EGLISE.ordinal(), 564), new Edge(Station.JOLICOEUR.ordinal(), 761)),
      List.of(new Edge(Station.ASSOMPTION.ordinal(), 896), new Edge(Station.PIE_IX.ordinal(), 622)),
      List.of(new Edge(Station.SNOWDON.ordinal(), 884), new Edge(Station.VENDOME.ordinal(), 1407))
  );

  /**
   * The default constructor.
   */
  public GraphRepository() {

  }
}
