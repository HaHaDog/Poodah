var btns = select(".check")
var selectedColor="#1eb8a3"
var unselectedColor="#4e4f52";
function switchState(d1,d2){
	var temp = d1.style.backgroundColor;
	d1.style.backgroundColor=d2.style.backgroundColor;
	d2.style.backgroundColor=temp;
}

for(var i=0;i<btns.length;i++){
	var d1 = document.createElement("span");
	var d2 = document.createElement("span");
	d1.innerHTML="&nbsp;ON&nbsp;";
	d2.innerHTML="&nbsp;OFF&nbsp;";
	d1.style.backgroundColor=selectedColor;
	d2.style.backgroundColor=unselectedColor;
	btns[i].style.float="right";
	btns[i].appendChild(d1);
	btns[i].appendChild(d2);
	if(btns[i].getAttribute("checked")=="false"){
		switchState(d1,d2);
	}else{
		btns[i].setAttribute("checked","true");
	}
	btns[i].addEventListener("click",(function(_p,d1,d2){
		return function(){
			//var name=_p.getAttribute("name");
			if(_p.getAttribute('checked')=="true"){
				_p.setAttribute("checked","false");
				//android.disable(name);
			}else{
				_p.setAttribute("checked","true");
				//android.enable(name);
			}
			switchState(d1,d2);
		};
	})(btns[i],d1,d2),false);
}