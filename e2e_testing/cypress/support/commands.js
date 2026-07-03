// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add('login', (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add('drag', { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add('dismiss', { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite('visit', (originalFn, url, options) => { ... })

Cypress.Commands.add("getElementByDataTestid", (dataTestid) => {
  cy.get(`[data-test-id="${dataTestid}"]`);
});

Cypress.Commands.add("clickElementByDataTestid", (dataTestid) => {
  const gottenElement = cy.getElementByDataTestid(dataTestid);

  gottenElement.then((element) => {
    cy.wrap(element).click();
  });
});

Cypress.Commands.add("clickElementThatContains", (text) => {
  cy.clickGottenElement(cy.contains(text));
});

Cypress.Commands.add("clickGottenElement", (gottenElement) => {
  gottenElement.then((element) => {
    cy.wrap(element).click();
  });
});

Cypress.Commands.add("clickFromHtmlTag", (htmlTag) => {
  cy.clickGottenElement(cy.get(htmlTag));
});

Cypress.Commands.add("nthPHasText", (index, text) => {
  cy.get("p").eq(index).should("have.text", text);
});

Cypress.Commands.add(
  "happyPathTest",
  (startingStation, transfers, destinationStation) => {
    cy.contains("Acadie").should("be.visible");

    if (startingStation.name !== "Acadie") {
      cy.clickElementByDataTestid("mat-select-starting-station");
      cy.clickElementThatContains(startingStation.name);
    }

    cy.clickElementByDataTestid("mat-select-destination-station");
    cy.clickElementThatContains(destinationStation);
    cy.clickFromHtmlTag("button");
    cy.nthPHasText(
      2,
      ` Start at ${startingStation.name} and go in the ${startingStation.direction} direction on the ${startingStation.line} line. `,
    );

    const indexOfPOfDestinationStation = transfers.length + 3;

    for (let index = 3; index < 3 + transfers.length; index++) {
      cy.nthPHasText(
        index,
        ` At ${transfers[index - 3].name}, switch to the ${transfers[index - 3].line} line and go in the ${transfers[index - 3].direction} direction. `,
      );
    }

    cy.nthPHasText(
      indexOfPOfDestinationStation,
      ` Stop at ${destinationStation}. `,
    );
  },
);
