describe("Home page tests", () => {
  beforeEach(() => {
    cy.visit("http://localhost:4200/");
  });

  it("sameMetroLine", () => {
    cy.clickFromHtmlTag("button");
    cy.get("mat-error").should(
      "have.text",
      "Provided stations are on the same line",
    );
  });

  it("oneTansfer", () => {
    startingStation = {
      name: "Laurier",
      line: "orange",
      direction: "Côte-Vertu",
    };
    transfers = [
      {
        name: "Lionel-Groulx",
        line: "green",
        direction: "Angrignon",
      },
    ];
    cy.happyPathTest(startingStation, transfers, "Charlevoix");
  });

  it("twoTansfers", () => {
    cy.happyPathTest("Outremont", ["Snowdon", "Lionel-Groulx"], "Angrignon");
  });
});
