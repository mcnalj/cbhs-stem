/**
 * CBHS STEM site — minimal JavaScript
 * Students will extend this file for filters and interactivity.
 */
document.addEventListener("DOMContentLoaded", function () {
  var currentPath = window.location.pathname;
  var navLinks = document.querySelectorAll(".main-nav a");

  navLinks.forEach(function (link) {
    var linkPath = link.getAttribute("href");
    if (linkPath === currentPath || (currentPath === "/" && linkPath === "/")) {
      link.classList.add("active");
    }
  });
});
