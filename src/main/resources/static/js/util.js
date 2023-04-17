 /**
 * 중복코드 유틸
 * ------------------

 */

/**
 * document.getElementById 축약
 * @param id ID
 * @returns {HTMLElement} document.getElementById 반환
 */
const dId = (id) => (document.getElementById(id))

/**
 * document.getElementByClassName 축약
 * @param className Class
 * @returns {HTMLElement} document.getElementByClassName 반환
 */
const dCN = (className) => (document.getElementsByClassName(className))

/**
 * document.querySelector 축약
 * @param selector selector
 * @returns {HTMLElement} document.querySelector 반환
 */
const dSel = (selector) => (document.querySelector(selector))

/**
 * document.querySelectorAll 축약
 * @param selector selector
 * @returns {HTMLElement} document.querySelectorAll 반환
 */
const dSelA = (selector) => (document.querySelectorAll(selector))

/**
 * document.createElement 축약
 * @param element selector
 * @returns {HTMLElement} document.createElement 반환
 */
const dCE = (element) => (document.createElement(element))


