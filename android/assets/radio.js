﻿var radios=select(".radio")var radioList = select(".radio-list");var _w = parseInt(select(".list")[0].clientHeight)/2;function hideAllRadio(){	var t = select(".radio-img");	for(var i=0;i<t.length;i++){		t[i].style.display="none";	}}for(var i=0;i<btns.length;i++){	var d1 = document.createElement("img");	for(var i=0;i<radios.length;i++){		var img = new Image();		img.src="radio.png";		img.width=_w;		img.height=_w;		img.setAttribute("class","radio-img");		if(radios[i].getAttribute("selected")=="false"){			img.style.display="none"		}		radios[i].appendChild(img);		radioList[i].addEventListener("click",(function(img){			return function(){				hideAllRadio();				img.style.display="";			};		})(img),false);	}}