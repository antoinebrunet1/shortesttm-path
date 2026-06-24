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

Cypress.Commands.add("clickMatSelect", (index) => {
  cy.clickGottenElement(cy.get("mat-select").eq(index));
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
