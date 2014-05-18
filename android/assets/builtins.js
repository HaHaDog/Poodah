/**
* Author: xupingmao
* Date: 2013-11-24
* Change: 2013-11-26
*/

function select(name){
	if(name[0]=='#')return document.getElementById(name.substr(1));
	else if(name[0]=='.')return document.getElementsByClassName(name.substr(1));
	else return document.getElementsByTagName(name);
}

function len(str){return str.length}

String.prototype.format=function(str){
	var reg = /\{\d+\}/g;
	var rs = this.match(reg)
	var text = this;
	for(var i=0;i<rs.length;i++){
		text = text.replace(rs[i],arguments[i]);
	}
	return text
}

String.prototype.startswith=function(str){
	var _l = str.length;
	if (arguments.length == 1)return this.substr(0,_l)==str;
	var flag = false;
	for(var i=0;i<arguments.length;i++){
		flag=flag || this.startswith(arguments[i])
	}
	return flag;
}

String.prototype.endswith=function(str){
	var _l = str.length;
	var _p = this.length - _l;
	if(arguments.length==1) return this.substr(_p,_l)==str;
	var flag = false;
	for(var i=0;i<arguments.length;i++){
		flag = flag || this.endswith(arguments[i])
	}
}

// insert sort
function sorted(list){
	var i = 0,j=0;
	for(i=1;i<list.length;i++){
		var temp=list[i];
		for(j=i;j>0&&list[j-1]>temp;j--){
			list[j]=list[j-1];
		}
		list[j]=temp;
	}
	return list;
}

function range(a,b,step){
	var _l = arguments.length;
	if(_l==1){
		var list = [];
		for(var i=0;i<a;i++)list.push(i);
		return list;
	}
	else if(_l==2){
		var list=[];
		for(var i=a;i<b;i++)list.push(i);
		return list;
	}
	else if(_l==3){
		var list=[];
		for(var i=a;i<b;i+=step)list.push(i);
		return list;
	}
}

// exports.sorted = sorted