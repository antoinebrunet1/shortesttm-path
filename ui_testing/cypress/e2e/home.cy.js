describe("Home page tests", () => {
  it("oneTansfer", () => {
    const startingStation = "Laurier";
    const transfers = ["Berri-UQAM"];
    const destinationStation = "Charlevoix";

    cy.visit("http://localhost:4200/");
    cy.clickMatSelect(0);
    cy.clickElementThatContains(startingStation);
    cy.clickMatSelect(1);
    cy.clickElementThatContains(destinationStation);
    cy.clickFromHtmlTag("button");
    cy.nthPHasText(1, ` Start at ${startingStation} station. `);
    cy.nthPHasText(
      2,
      ` Go to ${transfers[0]} station. You will switch lines at that station. `,
    );
    cy.nthPHasText(3, ` Go to ${destinationStation} station. `);
  });
});
