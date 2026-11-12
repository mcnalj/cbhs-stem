/**
 * CBHS STEM site — minimal JavaScript
 * Students will extend this file for filters and interactivity.
 */
document.addEventListener("DOMContentLoaded", function () {
  var currentPath = window.location.pathname;
  var navLinks = document.querySelectorAll(".main-nav a");

  navLinks.forEach(function (link) {
    var linkPath = link.getAttribute("href");
    var isWebDesignPage = linkPath === "/web"
      && (currentPath === "/html" || currentPath.indexOf("/html/") === 0
        || currentPath === "/css" || currentPath.indexOf("/css/") === 0);
    if (linkPath === currentPath || (currentPath === "/" && linkPath === "/") || isWebDesignPage) {
      link.classList.add("active");
    }
  });
});
