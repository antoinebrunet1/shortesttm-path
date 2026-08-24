package com.example.shortesttmpath.repository;

import com.example.shortesttmpath.data.Edge;
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
      List.of(new Edge(45, 729), new Edge(47, 728)), List.of(new Edge(41, 844)),
      List.of(new Edge(8, 782), new Edge(66, 896)), List.of(new Edge(26, 682), new Edge(37, 1388)),
      List.of(new Edge(31, 712), new Edge(56, 541)), List.of(new Edge(6, 379), new Edge(46, 495)),
      List.of(new Edge(5, 379), new Edge(10, 721), new Edge(30, 2362), new Edge(57, 337),
          new Edge(60, 579)), List.of(new Edge(39, 382), new Edge(62, 393)),
      List.of(new Edge(2, 782), new Edge(34, 519)), List.of(new Edge(17, 2074), new Edge(27, 1102)),
      List.of(new Edge(6, 721), new Edge(50, 371)), List.of(new Edge(35, 707), new Edge(37, 1077)),
      List.of(new Edge(61, 960), new Edge(63, 765)), List.of(new Edge(53, 451), new Edge(61, 693)),
      List.of(new Edge(21, 777)), List.of(new Edge(29, 826), new Edge(59, 128)),
      List.of(new Edge(31, 472), new Edge(47, 491)), List.of(new Edge(9, 2074), new Edge(42, 848)),
      List.of(new Edge(21, 1282), new Edge(44, 787)), List.of(new Edge(35, 812), new Edge(65, 564)),
      List.of(new Edge(23, 645), new Edge(58, 608)), List.of(new Edge(14, 777), new Edge(18, 1282)),
      List.of(new Edge(45, 1091), new Edge(63, 668)), List.of(new Edge(20, 645), new Edge(31, 840)),
      List.of(new Edge(46, 1158), new Edge(54, 1004)),
      List.of(new Edge(37, 759), new Edge(39, 531)), List.of(new Edge(3, 682), new Edge(48, 593)),
      List.of(new Edge(9, 1102), new Edge(59, 772)), List.of(new Edge(55, 717)),
      List.of(new Edge(15, 826), new Edge(31, 977)), List.of(new Edge(6, 2362), new Edge(38, 1572)),
      List.of(new Edge(4, 712), new Edge(16, 472), new Edge(23, 840), new Edge(29, 977)),
      List.of(new Edge(41, 1063), new Edge(65, 761)), List.of(new Edge(49, 767), new Edge(54, 383)),
      List.of(new Edge(8, 519), new Edge(55, 622)), List.of(new Edge(11, 707), new Edge(19, 812)),
      List.of(new Edge(43, 500), new Edge(56, 746)),
      List.of(new Edge(3, 1388), new Edge(11, 1077), new Edge(25, 759), new Edge(52, 580)),
      List.of(new Edge(30, 1572)), List.of(new Edge(7, 382), new Edge(25, 531)),
      List.of(new Edge(48, 297), new Edge(51, 346)), List.of(new Edge(1, 844), new Edge(32, 1063)),
      List.of(new Edge(17, 848)), List.of(new Edge(36, 500), new Edge(60, 932)),
      List.of(new Edge(18, 787), new Edge(53, 988)), List.of(new Edge(0, 729), new Edge(22, 1091)),
      List.of(new Edge(5, 495), new Edge(24, 1158)), List.of(new Edge(0, 728), new Edge(16, 491)),
      List.of(new Edge(26, 593), new Edge(40, 297)), List.of(new Edge(33, 767), new Edge(66, 622)),
      List.of(new Edge(10, 371), new Edge(62, 357)), List.of(new Edge(40, 346), new Edge(57, 354)),
      List.of(new Edge(37, 580), new Edge(64, 1451)), List.of(new Edge(13, 451), new Edge(44, 988)),
      List.of(new Edge(24, 1004), new Edge(33, 383)), List.of(new Edge(28, 717), new Edge(34, 622)),
      List.of(new Edge(4, 541), new Edge(36, 746)), List.of(new Edge(6, 337), new Edge(51, 354)),
      List.of(new Edge(20, 608)), List.of(new Edge(15, 128), new Edge(27, 772)),
      List.of(new Edge(6, 579), new Edge(43, 932)),
      List.of(new Edge(12, 960), new Edge(13, 693), new Edge(67, 884)),
      List.of(new Edge(7, 393), new Edge(50, 357)), List.of(new Edge(12, 765), new Edge(22, 668)),
      List.of(new Edge(52, 1451), new Edge(67, 1407)),
      List.of(new Edge(19, 564), new Edge(32, 761)), List.of(new Edge(2, 896), new Edge(49, 622)),
      List.of(new Edge(61, 884), new Edge(64, 1407))
  );

  /**
   * The default constructor.
   */
  public GraphRepository() {

  }
}
