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
    cy.happyPathTest("Laurier", ["Berri-UQAM"], "Charlevoix");
  });

  it("twoTansfers", () => {
    cy.happyPathTest("Outremont", ["Snowdon", "Lionel-Groulx"], "Angrignon");
  });
});
