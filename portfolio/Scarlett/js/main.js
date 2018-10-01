$(document).ready(function () {
	"use strict";

	//Slider screenshots
	var swiper = new Swiper('.swiper-container', {
		loop: true,
		centeredSlides: true,
		slidesPerView: 'auto',
		spaceBetween: 30,
		autoHeight: true,
		keyboardControl: true,
		mousewheelControl: false,
		lazyLoading: true,
		preventClicks: false,
		preventClicksPropagation: false,
		lazyLoadingInPrevNext: true
	});

	//Animation appearance when scrolling
	AOS.init();

	//Smooth Scrolling
	$(function () {
		$("a[href^='#']").on("click", function () {
			var _href = $(this).attr("href");
			$("html, body").animate({
				scrollTop: $(_href).offset().top + "px"
			});
			return false;
		});
	});

})
