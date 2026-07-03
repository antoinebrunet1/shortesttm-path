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

Cypress.Commands.add("clickFromDataTestid", (dataTestid) => {
  cy.clickGottenElement(cy.getByDataTestid(dataTestid));
});

Cypress.Commands.add("getByDataTestid", (dataTestid) => {
  cy.get([`data-testid="${dataTestid}"`]);
});

Cypress.Commands.add("elementHasTextByDataTestid", (dataTestid, text) => {
  cy.getByDataTestid(dataTestid).should("have.text", text);
});

Cypress.Commands.add("clickElementThatContains", (text) => {
  cy.clickGottenElement(cy.contains(text));
});

Cypress.Commands.add("clickGottenElement", (gottenElement) => {
  gottenElement.then((element) => {
    cy.wrap(element).click();
  });
});

Cypress.Commands.add(
  "happyPathTest",
  (startingStation, transfers, destinationStation) => {
    cy.contains("Acadie").should("be.visible");

    if (startingStation.name !== "Acadie") {
      cy.clickFromDataTestid("starting-station-mat-select");
      cy.clickElementThatContains(startingStation.name);
    }

    cy.clickFromDataTestid("destination-station-mat-select");
    cy.clickElementThatContains(destinationStation);
    cy.clickFromDataTestid("find-button");
    cy.elementHasTextByDataTestid(
      "starting-station-p",
      ` Start at ${startingStation.name} and go in the ${startingStation.direction} direction on the ${startingStation.line} line `,
    );

    for (let index = 0; index < transfers.length; index++) {
      cy.elementHasTextByDataTestid(
        `transfer-${index}`,
        ` At ${transfers[index].name}, switch to the ${transfers[index].line} line and go in the ${transfers[index].direction} direction `,
      );
    }

    cy.elementHasTextByDataTestid(
      "destination-station-p",
      ` Stop at ${destinationStation} `,
    );
  },
);
