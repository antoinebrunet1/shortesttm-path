package com.example.shortesttmpath.repository;

import com.example.shortesttmpath.enums.Line;
import com.example.shortesttmpath.enums.Station;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.stereotype.Repository;

/**
 * Station related repository.
 */
@Repository
@Getter
public class StationRepository {
  private final List<Station> blueLine = List.of(
      Station.SNOWDON,
      Station.COTE_DES_NEIGES,
      Station.UNIVERSITE_DE_MONTREAL,
      Station.EDOUARD_MONTPETIT,
      Station.OUTREMONT,
      Station.ACADIE,
      Station.PARC,
      Station.DE_CASTELNAU,
      Station.JEAN_TALON,
      Station.FABRE,
      Station.D_IBERVILLE,
      Station.SAINT_MICHEL
  );
  private final List<Station> greenLine = List.of(
      Station.ANGRIGNON,
      Station.MONK,
      Station.JOLICOEUR,
      Station.VERDUN,
      Station.DE_L_EGLISE,
      Station.LASALLE,
      Station.CHARLEVOIX,
      Station.LIONEL_GROULX,
      Station.ATWATER,
      Station.GUY_CONCORDIA,
      Station.PEEL,
      Station.MCGILL,
      Station.PLACE_DES_ARTS,
      Station.SAINT_LAURENT,
      Station.BERRI_UQAM,
      Station.BEAUDRY,
      Station.PAPINEAU,
      Station.FRONTENAC,
      Station.PREFONTAINE,
      Station.JOLIETTE,
      Station.PIE_IX,
      Station.VIAU,
      Station.ASSOMPTION,
      Station.CADILLAC,
      Station.LANGELIER,
      Station.RADISSON,
      Station.HONORE_BEAUGRAND
  );
  private final List<Station> orangeLine = List.of(
      Station.COTE_VERTU,
      Station.DU_COLLEGE,
      Station.DE_LA_SAVANE,
      Station.NAMUR,
      Station.PLAMONDON,
      Station.COTE_SAINTE_CATHERINE,
      Station.SNOWDON,
      Station.VILLA_MARIA,
      Station.VENDOME,
      Station.PLACE_SAINT_HENRI,
      Station.LIONEL_GROULX,
      Station.GEORGES_VANIER,
      Station.LUCIEN_L_ALLIER,
      Station.BONAVENTURE,
      Station.SQUARE_VICTORIA_OACI,
      Station.PLACE_D_ARMES,
      Station.CHAMP_DE_MARS,
      Station.BERRI_UQAM,
      Station.SHERBROOKE,
      Station.MONT_ROYAL,
      Station.LAURIER,
      Station.ROSEMONT,
      Station.BEAUBIEN,
      Station.JEAN_TALON,
      Station.JARRY,
      Station.CREMAZIE,
      Station.SAUVE,
      Station.HENRI_BOURASSA,
      Station.CARTIER,
      Station.DE_LA_CONCORDE,
      Station.MONTMORENCY
  );
  private final List<Station> yellowLine = List.of(
      Station.BERRI_UQAM,
      Station.JEAN_DRAPEAU,
      Station.LONGUEUIL_UNIVERSITE_DE_SHERBROOKE
  );
  private final Map<Line, List<Station>> linesToStations = Map.of(
      Line.BLUE, blueLine,
      Line.GREEN, greenLine,
      Line.ORANGE, orangeLine,
      Line.YELLOW, yellowLine
  );
  private final List<Station> allStationsToSwitchLines = List.of(
      Station.BERRI_UQAM,
      Station.LIONEL_GROULX,
      Station.SNOWDON,
      Station.JEAN_TALON
  );

  /**
   * The default constructor.
   */
  public StationRepository() {

  }
}
