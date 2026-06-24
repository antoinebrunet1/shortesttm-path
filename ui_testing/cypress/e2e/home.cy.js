describe("Home page tests", () => {
  it("oneTansfer", () => {
    cy.happyPathTest("Laurier", ["Berri-UQAM"], "Charlevoix");
  });

  it("twoTansfers", () => {
    cy.happyPathTest("Outremont", ["Snowdon", "Lionel-Groulx"], "Angrignon");
  });
});
