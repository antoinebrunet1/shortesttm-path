describe("Home page tests", () => {
  beforeEach(() => {
    cy.visit("http://localhost:4200/");
  });

  it("sameMetroLine", () => {
    cy.clickFromDataTestid("find-button");
    cy.elementHasTextByDataTestid(
      "error-message",
      "The stations are on the same metro line",
    );
  });

  it("oneTansfer", () => {
    const startingStation = {
      name: "Laurier",
      line: "orange",
      direction: "Côte-Vertu",
    };
    const transfers = [
      {
        name: "Lionel-Groulx",
        line: "green",
        direction: "Angrignon",
      },
    ];
    cy.happyPathTest(startingStation, transfers, "Charlevoix");
  });

  it("twoTansfers", () => {
    const startingStation = {
      name: "Acadie",
      line: "blue",
      direction: "Snowdon",
    };
    const transfers = [
      {
        name: "Snowdon",
        line: "orange",
        direction: "Montmorency",
      },
      {
        name: "Lionel-Groulx",
        line: "green",
        direction: "Angrignon",
      },
    ];
    cy.happyPathTest(startingStation, transfers, "Angrignon");
  });
});
